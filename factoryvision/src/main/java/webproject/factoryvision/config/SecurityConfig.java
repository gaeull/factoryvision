//package webproject.factoryvision.config;
//
//import lombok.Builder;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import webproject.factoryvision.domain.user.service.UserService;
//
//
//
////@RequiredArgsConstructor
////@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    // 패스워드 암호화
//    @Bean
//    public BCryptPasswordEncoder PasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//
//    // 시큐리티 필터 설정
////    @Bean
////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////
////    }
//
//
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//}
