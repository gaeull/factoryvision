//package webproject.factoryvision.config;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//import webproject.factoryvision.domain.user.service.TokenProvider;
//
//import java.io.IOException;
//
//// 엑세스 토큰값이 담긴 Authorization헤더값을 가져온 뒤, 엑세스 토큰이 유효 --> 인증 정보 설정
//@RequiredArgsConstructor
//public class TokenAuthenticationFilter extends OncePerRequestFilter {
//    private TokenProvider tokenProvider;
//    private final static String HEADER_AUTHORIZATION = "Authorization";
//    private final static String TOKEN_PREFIX = "Bearer";
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
//        String token = getAccessToken(authorizationHeader);
//        if (tokenProvider.validToken(token)) {
//            Authentication authentication = tokenProvider.getAuthentication(token);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private String getAccessToken(String authorizationHeader) {
//        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
//            return authorizationHeader.substring(TOKEN_PREFIX.length());
//        }
//        return null;
//    }
//}
