package webproject.factoryvision.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

//    @Bean
//    CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true); // 내 서버가 응답을 할때 json을 자바스크립트에서 처리할 수 있게 할지를 설정하는
//        // config.addAllowedOrigin(""); // 모든 ip에 응답 허용
//        // config.addAllowedOrigin(frontUrl);
//        config.setAllowedOrigins(Arrays.asList("http://localhost:3002", "http://localhost:5002", "http://localhost:8080"));
////        config.addAllowedOrigin("*");
////        config.addAllowedHeader(""); // 모든 header에 응답 허용
////        config.addAllowedMethod("*"); // 모든 post,get,put,delete,patch 요청 허용
//        config.setAllowedMethods(Arrays.asList("OPTIONS", "GET", "POST", "PUT", "DELETE"));
//        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }


}