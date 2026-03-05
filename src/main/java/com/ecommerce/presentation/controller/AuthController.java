package com.ecommerce.presentation.controller;
import com.ecommerce.application.dto.*;
import com.ecommerce.application.usecase.CreateUserUseCase;
import com.ecommerce.application.usecase.CustomUserDetailsUseCase;
import com.ecommerce.application.usecase.LoginUserUseCase;
import com.ecommerce.application.usecase.RefreshTokenUseCase;
import com.ecommerce.domain.entity.User;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.shared.exception.InvalidTokenException;
import com.ecommerce.shared.exception.LoginUserException;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final CreateUserUseCase createUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final CustomUserDetailsUseCase  customUserDetailsUseCase;


    public AuthController(
            CreateUserUseCase createUserUseCase,
            LoginUserUseCase loginUserUseCase,
            RefreshTokenUseCase refreshTokenUseCase,
            CustomUserDetailsUseCase customUserDetailsUseCase
    ) {
        this.createUserUseCase = createUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
        this.customUserDetailsUseCase = customUserDetailsUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid  CreateUserRequest request) {
        UserResponse createdUser = createUserUseCase.create(request);

        logger.info("User: {} registered: {}", createdUser.name());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid LoginRequest request) {
        try {
            var login = loginUserUseCase.login(request);
            logger.info("Login: {}", login);
            return ResponseEntity.ok(login);

        } catch (LoginUserException e) {
            logger.error("AuthenticationException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        try {
            RefreshTokenResponse refreshTokenResponse = refreshTokenUseCase.refreshToken(request);
            return ResponseEntity.ok(refreshTokenResponse);
        } catch (InvalidTokenException e) {
            logger.error("InvalidTokenException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PostMapping("/{id}/failed-login")
    public ResponseEntity<Void> failedLogin(@PathVariable Long id) {
        try {
            customUserDetailsUseCase.registerFailedLogin(id);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (LoginUserException e) {
            logger.error("LoginUserException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PostMapping("/{id}/successful-login")
    public ResponseEntity<Void> successfulLogin(@PathVariable Long id) {
        try {
            customUserDetailsUseCase.registerSuccessfulLogin(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ResourceNotFoundException e) {
            throw new BusinessException(e.getMessage());
        }
    }
    @PutMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(
            @PathVariable Long id,
            @RequestBody String newPassword
    ) {
        try {
            customUserDetailsUseCase.updatePassword(id, newPassword);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ResourceNotFoundException e) {
            throw new BusinessException(e.getMessage());
        }
    }
    @PutMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> disableUser(@PathVariable Long id) {
        try {
            customUserDetailsUseCase.disableUser(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ResourceNotFoundException e) {
            throw new BusinessException(e.getMessage());
        }
    }
}
