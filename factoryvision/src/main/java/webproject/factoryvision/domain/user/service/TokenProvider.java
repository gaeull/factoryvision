//package webproject.factoryvision.domain.user.service;
//
//import io.jsonwebtoken.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.stereotype.Service;
//import webproject.factoryvision.domain.user.entity.JwtProperties;
//import webproject.factoryvision.domain.user.entity.User;
//
//import java.time.Duration;
//import java.util.Collections;
//import java.util.Date;
//import java.util.Set;
//
//@RequiredArgsConstructor
//@Service
//public class TokenProvider {
//
//    private final JwtProperties jwtProperties;
//
//    public String generateToken(User user, Duration expiredAt) {
//        Date now = new Date();
//        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
//    }
//
//    // jwt 토큰 생성
//    private String makeToken(Date expiry, User user) {
//        Date now = new Date();
//
//        return Jwts.builder()
//                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
//                .setIssuer(jwtProperties.getIssuer())
//                .setIssuedAt(now)
//                .setExpiration(expiry)
//                .setSubject(user.getEmail())
//                .claim("id", user.getId())
//                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
//                .compact();
//    }
//
//    // jwt 토큰 유효성 검증 메서드
//    public boolean validToken(String token) {
//        try{
//            Jwts.parser()
//                    // 비밀값으로 복호화
//                    .setSigningKey(jwtProperties.getSecretKey())
//                    .parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    // 토큰 기반으로 인증 정보 가져오는 메서드
//    public Authentication getAuthentication(String token) {
//        Claims claims = getClaims(token);
//        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
//
//        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities), token, authorities);
//    }
//
//    // 토큰 기반으로 사용자 인증 정보 가져오는 메서드
////    public  UserAuthentication getAuthentication(String token) {
////        Claims claims = parseToken(token);
////        if (claims == null || claims.getSubject() == null) {
////            return null;
////        }
////        // 사용자 권한 설정
////        Set<String> authorities = Collections.singleton("ROLE_USER");
////
////        // 사용자 인증 정보 반환
////        return new UserAuthentication(claims.getSubject(), authorities);
////    }
//
//    // 토큰 기반으로 유저 ID 가져오는 메서드
//    public Long getUserId(String token) {
////        Claims claims = parseToken(token);
//        Claims claims = getClaims(token);
//        return claims.get("id", Long.class);
//    }
//
//    // 클레임 조회
//    private Claims getClaims(String token) {
//        return Jwts.parser()
//                .setSigningKey(jwtProperties.getSecretKey())
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    // 클레임 조회
////    private Claims parseToken(String token) {
////        try {
////            return Jwts.parser()
////                    .setSigningKey(jwtProperties.getSecretKey())
////                    .parseClaimsJws(token)
////                    .getBody();
////        } catch (JwtException | IllegalArgumentException e) {
////            // 토큰이 유효하지 않은 경우 등의 예외 처리
////            return null;
////        }
////    }
//
//    // 사용자 인증 정보 클래스
////    class UserAuthentication {
////        private String username;
////        private Set<String> authorities;
////
////        public UserAuthentication(String username, Set<String> authorities) {
////            this.username = username;
////            this.authorities = authorities;
////        }
////
////        public String getUsername() {
////            return username;
////        }
////
////        public Set<String> getAuthorities() {
////            return authorities;
////        }
////    }
//
//}
