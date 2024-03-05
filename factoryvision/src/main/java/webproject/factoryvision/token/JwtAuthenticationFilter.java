package webproject.factoryvision.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import webproject.factoryvision.redis.RedisDao;
import webproject.factoryvision.security.SecurityExceptionDto;
import webproject.factoryvision.token.TokenProvider;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

//@Order(0)
//@Component

@RequiredArgsConstructor
@Slf4j
@Component
// 클라이언트의 인증 및 권한 부여
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // [feat] Spring security + JWT 사용한 로그인 회원가입
//    private final TokenProvider tokenProvider;
//
//    @Override
//    // 인증 정보 설정
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//        String token = parseBearerToken(request);
//        User user = parseUserSpecification(token);
//        AbstractAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(user, token, user.getAuthorities());
//        authenticated.setDetails(new WebAuthenticationDetails(request));    // 인증 토큰의 details에는 요청을 날린 클라(or 프록시의 IP주소, 세션 ID)저장 --> 필요없으면 삭제가능.
//        SecurityContextHolder.getContext().setAuthentication(authenticated);
//
//        filterChain.doFilter(request, response);
//    }
//
//    // 헤더에서 Bearer(접두어)를 제외한 토큰 값 추출
//    private String parseBearerToken(HttpServletRequest request) {
//        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
//                .filter(token -> token.substring(0, 7).equalsIgnoreCase("Bearer "))
//                .map(token -> token.substring(7))
//                .orElse(null);
//    }
//
//    // 토큰값을 기반으로, 토큰에 담긴 회원ID를 토대로 스프링 시큐리티에서 사용할 User 객체 반환함.
//    private User parseUserSpecification(String token) {
//        String username = Optional.ofNullable(token)
//                .filter(subject -> subject.length() >= 10)
//                .map(tokenProvider::validateTokenAndGetSubject)
//                .orElse("anonymous:anonymous")
//                .split(":")[0];
//
//        return new User(username, "", List.of(new SimpleGrantedAuthority("ROLE_USER")));
//    }

    private final RedisDao redisDao;
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        String token = tokenProvider.resolveToken(request);
        if (token != null) {
            String blackList = redisDao.getBlackList(token);
            if (blackList != null) {
                if (blackList.equals("logout")) {
                    throw new IllegalArgumentException("Please Login again.");
                }
            }
            if(!tokenProvider.validateToken(token)){
                response.sendError(401, "만료되었습니다.");
                jwtExceptionHandler(response,"401", HttpStatus.BAD_REQUEST.value() );
                return;
            }
            // 검증 후 인증 객체 생성하여 securityContextHolder에서 관리
            Claims userInfo = tokenProvider.getUserInfoFromToken(token);
            setAuthentication(userInfo.getSubject());//subject = userId
        }
        filterChain.doFilter(request,response);
    }

    private void setAuthentication(String userId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = tokenProvider.createUserAuthentication(userId);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    public void jwtExceptionHandler(HttpServletResponse response, String message, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(statusCode, message));
            // ObjectMapper를 사용하여 SecurityExceptionDto 객체를 JSON 문자열로 변환
            response.getWriter().write(json); //JSON 문자열을 응답으로 작성
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
