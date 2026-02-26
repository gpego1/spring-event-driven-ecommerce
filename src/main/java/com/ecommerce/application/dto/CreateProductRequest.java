package com.ecommerce.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank(message = "Nome é obrigatório") String name,
        @NotNull @Positive(message = "Preço deve ser maior que zero") BigDecimal price,
        @NotNull @PositiveOrZero(message = "Estoque não pode ser negativo") Integer stock
) {}
