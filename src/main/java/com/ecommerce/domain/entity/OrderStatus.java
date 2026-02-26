package com.ecommerce.domain.entity;

/**
 * Status possíveis de um pedido.
 * Preparado para novos estados conforme o negócio evolui (ex: CANCELLED, SHIPPED).
 */
public enum OrderStatus {
    CREATED,
    COMPLETED
}
