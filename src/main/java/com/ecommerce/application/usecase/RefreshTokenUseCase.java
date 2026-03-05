package com.ecommerce.application.usecase;
import com.ecommerce.application.dto.RefreshTokenRequest;
import com.ecommerce.application.dto.RefreshTokenResponse;
import com.ecommerce.domain.entity.User;
import com.ecommerce.domain.repository.UserRepository;
import com.ecommerce.infrastructure.config.TokenConfig;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenUseCase {
    private final TokenConfig  tokenConfig;
    private final UserRepository userRepository;

    public RefreshTokenUseCase(TokenConfig tokenConfig, UserRepository userRepository) {
        this.tokenConfig = tokenConfig;
        this.userRepository = userRepository;
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String username = tokenConfig.validateRefreshToken(refreshTokenRequest.refreshToken());
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Could not resolve the username: " + username));
        String newToken = tokenConfig.generateRefreshToken(user);
        return new RefreshTokenResponse(newToken);
    }
}
