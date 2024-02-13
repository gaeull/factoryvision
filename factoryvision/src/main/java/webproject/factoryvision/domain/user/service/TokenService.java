//package webproject.factoryvision.domain.user.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import webproject.factoryvision.domain.user.entity.User;
//import webproject.factoryvision.domain.user.repository.RefreshTokenRepository;
//
//import java.time.Duration;
//
//@RequiredArgsConstructor
//@Service
//public class TokenService {
//    private final RefreshTokenService refreshTokenService;
//    private final TokenProvider tokenProvider;
//    private final UserService userService;
//
//    public String createNewAccessToken(String refreshToken) {
//        if (!tokenProvider.validToken(refreshToken)) {
//            throw new IllegalArgumentException("UnExpected token");
//        }
//
//        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
//        User user = userService.findById(userId);
//
//        return tokenProvider.generateToken(user, Duration.ofHours(2));
//    }
//}
