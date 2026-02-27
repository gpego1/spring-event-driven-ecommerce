package com.ecommerce.domain.event;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class OrderCreatedEvent implements DomainEvent {

    @Builder.Default
    private final String eventId = UUID.randomUUID().toString();

    private final Long orderId;
    private final Long userId;
    private final String userEmail;
    private final List<OrderItemData> items;
    private final BigDecimal totalAmount;

    @Builder.Default
    private final LocalDateTime occurredAt = LocalDateTime.now();

    @Override
    public String getEventId() {
        return eventId;
    }

    @Override
    public String getEventType() {
        return "order.created";
    }

    @Override
    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }


    @Getter
    @Builder
    public static class OrderItemData {
        private final Long productId;
        private final Integer quantity;
        private final BigDecimal price;
    }
}
