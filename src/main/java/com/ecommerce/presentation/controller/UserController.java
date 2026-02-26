package com.ecommerce.presentation.controller;

import com.ecommerce.application.dto.CreateUserRequest;
import com.ecommerce.application.dto.UserResponse;
import com.ecommerce.application.usecase.CreateUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;

    @PostMapping
    @Operation(summary = "Cria um novo usuário")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createUserUseCase.create(request));
    }

    @GetMapping
    @Operation(summary = "Lista todos os usuários")
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(createUserUseCase.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca usuário por ID")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(createUserUseCase.findById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um usuário")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        createUserUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
