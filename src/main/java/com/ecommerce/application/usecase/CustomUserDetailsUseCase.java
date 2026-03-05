package com.ecommerce.application.usecase;
import com.ecommerce.application.dto.UserResponse;
import com.ecommerce.domain.entity.User;
import com.ecommerce.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CustomUserDetailsUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsUseCase(UserRepository userRepository,  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerFailedLogin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setLoginAttempts(user.getLoginAttempts() + 1);

        if (user.getLoginAttempts() >= 5) {
            user.setLockUntil(LocalDateTime.now().plusMinutes(15));
            user.setEnabled(false);
        }

        userRepository.save(user);
    }
    public void registerSuccessfulLogin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setLoginAttempts(0);
        user.setLockUntil(null);
        userRepository.save(user);
    }
    public void updatePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setAccountExpiresAt(LocalDateTime.now().plusDays(90));
        userRepository.save(user);
    }
    public void disableUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setEnabled(false);
        userRepository.save(user);
    }
}
