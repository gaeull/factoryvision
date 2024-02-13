package webproject.factoryvision.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import webproject.factoryvision.domain.user.entity.RefreshToken;
import webproject.factoryvision.domain.user.repository.RefreshTokenRepository;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}
