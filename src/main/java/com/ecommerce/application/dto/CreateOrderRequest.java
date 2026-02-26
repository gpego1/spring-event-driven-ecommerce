package com.ecommerce.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record CreateOrderRequest(
        @NotNull(message = "userId é obrigatório") Long userId,
        @NotNull(message = "items é obrigatório") List<OrderItemRequest> items
) {
    public record OrderItemRequest(
            @NotNull Long productId,
            @NotNull @Positive(message = "Quantidade deve ser maior que zero") Integer quantity
    ) {}
}
