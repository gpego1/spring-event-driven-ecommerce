package com.ecommerce.application.dto;

import com.ecommerce.domain.entity.Order;
import com.ecommerce.domain.entity.OrderItem;
import com.ecommerce.domain.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        Long userId,
        List<OrderItemResponse> items,
        BigDecimal totalAmount,
        LocalDateTime createdAt,
        OrderStatus status
) {
    public static OrderResponse from(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(OrderItemResponse::from)
                .toList();
        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                itemResponses,
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getStatus()
        );
    }

    public record OrderItemResponse(Long productId, Integer quantity, BigDecimal price, BigDecimal subtotal) {
        public static OrderItemResponse from(OrderItem item) {
            return new OrderItemResponse(item.getProductId(), item.getQuantity(), item.getPrice(), item.getSubtotal());
        }
    }
}
