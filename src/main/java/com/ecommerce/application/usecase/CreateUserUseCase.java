package com.ecommerce.application.usecase;

import com.ecommerce.application.dto.CreateUserRequest;
import com.ecommerce.application.dto.UserResponse;
import com.ecommerce.domain.entity.User;
import com.ecommerce.domain.repository.UserRepository;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Caso de uso para operações de usuário.
 * Orquestra a lógica sem depender de detalhes de infraestrutura.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;

    public UserResponse create(CreateUserRequest request) {
        log.info("Criando usuário com email: {}", request.email());

        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Já existe usuário com email: " + request.email());
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
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
