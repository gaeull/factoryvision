package webproject.factoryvision.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import webproject.factoryvision.token.JwtAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableWebMvc
public class SecurityConfig implements WebMvcConfigurer {

    @Bean
    public BCryptPasswordEncoder PasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//    private final String[] allowedUrls = {"/", "/swagger-ui/**", "/v3/**", "/factoryvision/signin", "/factoryvision/signup"};
    private final String[] allowedUrls = {"/", "/swagger-ui/**", "/v3/**", "/factoryvision/login", "/factoryvision/signup", "/factoryvision/upload"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers(allowedUrls).permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
                .build();
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3002")
                .allowedMethods("OPTIONS","GET","POST","PUT","DELETE");
    }
}
