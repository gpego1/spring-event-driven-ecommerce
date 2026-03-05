package com.ecommerce.application.usecase;
import com.ecommerce.application.dto.LoginRequest;
import com.ecommerce.application.dto.LoginResponse;
import com.ecommerce.domain.entity.User;
import com.ecommerce.domain.repository.UserRepository;
import com.ecommerce.infrastructure.config.TokenConfig;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginUserUseCase {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    public LoginUserUseCase(UserRepository userRepository,  AuthenticationManager authenticationManager, TokenConfig tokenConfig) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
    }

    public LoginResponse login(LoginRequest request) {
        var exists = userRepository.existsByEmail(request.email());
        if (exists) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthToken );
            User user = (User) authentication.getPrincipal();
            String token = tokenConfig.generateToken(user);
            return new LoginResponse(token);
        } else {
            throw new UsernameNotFoundException("Invalid email or password");
        }
    }

}
