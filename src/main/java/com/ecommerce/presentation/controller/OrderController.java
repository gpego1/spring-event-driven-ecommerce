package com.ecommerce.presentation.controller;

import com.ecommerce.application.dto.CreateOrderRequest;
import com.ecommerce.application.dto.OrderResponse;
import com.ecommerce.application.usecase.CreateOrderUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    @Operation(summary = "Cria um pedido — status COMPLETED, dispara OrderCreatedEvent")
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createOrderUseCase.create(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Lista todos os pedidos")
    public ResponseEntity<List<OrderResponse>> findAll() {
        return ResponseEntity.ok(createOrderUseCase.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Busca pedido por ID")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(createOrderUseCase.findById(id));
    }
}
