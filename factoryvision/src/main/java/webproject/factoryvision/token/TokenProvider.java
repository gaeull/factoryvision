package webproject.factoryvision.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import webproject.factoryvision.domain.user.entity.Role;
import webproject.factoryvision.redis.RedisDao;
import webproject.factoryvision.token.dto.TokenResponse;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@PropertySource("classpath:jwt.yml")
//@Service
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    // [feat] Spring security + JWT 사용한 로그인 회원가입
//    private final String secretKey;
//    private final long expirationHours;
//    private final String issuer;
//
//    public TokenProvider(
//            @Value("${secret-key}") String secretKey,
//            @Value("${expiration-hours}") long expirationHours,
//            @Value("${issuer}") String issuer
//    ) {
//        this.secretKey = secretKey;
//        this.expirationHours = expirationHours;
//        this.issuer = issuer;
//    }
//
//    public String createToken(String userSpecification) {
//        return Jwts.builder()
//                .signWith(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS256.getJcaName()))   // HS512 알고리즘을 사용하여 secretKey를 이용해 서명
//                .setSubject(userSpecification)  // JWT 토큰 제목
//                .setIssuer(issuer)  // JWT 토큰 발급자
//                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))    // JWT 토큰 발급 시간
//                .setExpiration(Date.from(Instant.now().plus(expirationHours, ChronoUnit.HOURS)))    // JWT 토큰 만료 시간
//                .compact(); // JWT 토큰 생성
//    }
//
//    // 헤더로 받은 토큰에 담긴 정보를 가져오는 메소드
//    public String validateTokenAndGetSubject(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(secretKey.getBytes())
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    private static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    private static final String BEARER_PREFIX = "Bearer ";

    private static final long ACCESS_TOKEN_TIME = 1000 * 60 * 30L; // 30분
    private static final long REFRESH_TOKEN_TIME = 1000 * 60 * 60 * 24 * 7L;// 7일

    @Value("${secret-key}")
    private String secretKey;

    //HMAC-SHA 키를 생성
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;


    private final UserDetailsService userDetailsService;
    private final RedisDao redisDao;


    // HMAC-SHA 키를 생성하는 데 사용되는 Base64 인코딩된 문자열을 디코딩하여 키를 초기화하는 용도로 사용
    // 의존성 주입이 이루어진 후 초기화를 수행하는 어노테이션
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);// Base64로 인코딩된 값을 시크릿키 변수에 저장한 값을 디코딩하여 바이트 배열로 변환
        key = Keys.hmacShaKeyFor(bytes); //디코팅된 바이트 배열을 기반으로 HMAC-SHA 알고리즘을 사용해서 Key객체로 반환, 이를 key 변수에 대입
    }

    // Header 에서 토큰 가져오기
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

   // 토큰 생성 메서드(userId)
    private String createToken(String userId, Role role, Long tokenExpireTime) {
        Date date = new Date();
        return BEARER_PREFIX + Jwts.builder()
                .claim(AUTHORIZATION_KEY, role)// JWT에 사용자 역할 정보를 클레임(claim)으로 추가
                .setSubject(userId) //JWT의 주제(subject)를 userId로 설정
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + tokenExpireTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 유저 로그인 후 토큰 발행
    public TokenResponse createTokenByLogin(String userId, Role role) {
        String accessToken = createToken(userId, role, ACCESS_TOKEN_TIME);
        String refreshToken = createToken(userId, role, REFRESH_TOKEN_TIME);
        redisDao.setRefreshToken(userId, refreshToken, REFRESH_TOKEN_TIME);
        return new TokenResponse(accessToken, refreshToken);
    }

    //AccessToken 재발행 + refreshToken 함께 발행
    public TokenResponse reissueAtk(String userId, Role role, String reToken) {
        log.info("reissuedAtk의 매개변수 값인 userId: {} ", userId);
        log.info("매개변수 reToken {} ", reToken);

        String refreshTokenWithQuotes = redisDao.getRefreshToken(userId);
        String RedisRefreshToken = refreshTokenWithQuotes.replaceAll("^\"|\"$", "");

        if (!RedisRefreshToken.equals(reToken)) {
            log.info("레디스에 저장된 reToken {} ", redisDao.getRefreshToken(userId));
            throw new IllegalArgumentException("Invalid refresh token");
        }
        String accessToken = createToken(userId, role, ACCESS_TOKEN_TIME);
        String refreshToken = createToken(userId, role, REFRESH_TOKEN_TIME);
        redisDao.setRefreshToken(userId, refreshToken, REFRESH_TOKEN_TIME);
        return new TokenResponse(accessToken, refreshToken);
    }


    // 토큰으로 유저정보 가져오기
    public Claims getUserInfoFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // Header에서 가져온 토큰 검증하는 메소드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {// 전: 권한 없다면 발생 , 후: JWT가 올바르게 구성되지 않았다면 발생
            log.info("Invalid JWT token, 만료된 jwt 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // accessToken의 만료시간 조회
    public Long getExpiration(String accessToken){
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
        long now = new Date().getTime();
        return (expiration.getTime()-now);
    }

    // 일반 유저 인증 객체 생성
    public Authentication createUserAuthentication(String userId) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
    
}
