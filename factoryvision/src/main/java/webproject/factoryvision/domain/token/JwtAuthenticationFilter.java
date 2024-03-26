package webproject.factoryvision.domain.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import webproject.factoryvision.redis.RedisDao;
import webproject.factoryvision.security.SecurityExceptionDto;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

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

            Claims userInfo = tokenProvider.getUserInfoFromToken(token);
            setAuthentication(userInfo.getSubject());
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
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
