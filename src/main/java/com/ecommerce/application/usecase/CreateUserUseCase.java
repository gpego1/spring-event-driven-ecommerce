package com.ecommerce.application.usecase;

import com.ecommerce.application.dto.CreateUserRequest;
import com.ecommerce.application.dto.UserResponse;
import com.ecommerce.domain.entity.User;
import com.ecommerce.domain.repository.UserRepository;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse create(CreateUserRequest request) {
        log.info("Criando usuário com email: {}", request.email());

        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Já existe usuário com email: " + request.email());
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())

                .enabled(true)
                .loginAttempts(0)
                .lockUntil(null)

                .passwordExpiresAt(LocalDateTime.now().plusMonths(6))
                .accountExpiresAt(LocalDateTime.now().plusYears(5))

                .build();

        User saved = userRepository.save(user);
        log.info("Usuário criado com id: {}", saved.getId());
        return UserResponse.from(saved);
    }

    public UserResponse findById(Long id) {
        return UserResponse.from(findOrThrow(id));
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(UserResponse::from).toList();
    }

    public void delete(Long id) {
        findOrThrow(id);
        userRepository.deleteById(id);
        log.info("Usuário {} removido", id);
    }

    private User findOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", id));
    }
}
