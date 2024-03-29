package webproject.factoryvision.domain.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import webproject.factoryvision.domain.user.entity.Role;
import webproject.factoryvision.domain.user.service.UserDetailsServiceImpl;
import webproject.factoryvision.redis.RedisDao;
import webproject.factoryvision.domain.token.dto.TokenResponse;

import java.security.Key;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    private static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    private static final String BEARER_PREFIX = "Bearer ";

    private static final long ACCESS_TOKEN_TIME = 1000 * 60 * 30L; // 30분
//    private static final long ACCESS_TOKEN_TIME = 1000 * 1000 * 30L;
    private static final long REFRESH_TOKEN_TIME = 1000 * 60 * 60 * 24 * 7L;// 7일

    @Value("${jwt.secretKey}")
    private String secretKey = "c2lsdmVybmluZS10ZWNoLXNwcmluZy1ib290LWp3dC10dXRvcmlhbC1zZWNyZXQtc2lsdmVybmluZS10ZWNoLXNwcmluZy1ib290LWp3dC10dXRvcmlhbC1zZWNyZXQK";

    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

//    private final UserDetailsService userDetailsService;
    private final UserDetailsServiceImpl userDetailsServiceimpl;
    private final RedisDao redisDao;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
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
                .claim(AUTHORIZATION_KEY, role)
                .setSubject(userId)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + tokenExpireTime))
                .signWith(key, signatureAlgorithm)
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
//        log.info("reissuedAtk의 매개변수 값인 userId: {} ", userId);
//        log.info("매개변수 reToken {} ", reToken);

        String refreshTokenWithQuotes = redisDao.getRefreshToken(userId);
        String RedisRefreshToken = refreshTokenWithQuotes.replaceAll("^\"|\"$", "");

        if (!RedisRefreshToken.equals(reToken)) {
//            log.info("레디스에 저장된 reToken {} ", redisDao.getRefreshToken(userId));
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
        UserDetails userDetails = userDetailsServiceimpl.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
    
}
