package com.ecommerce.domain.event;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Evento gerado quando um pedido é criado com sucesso.
 *
 * Este evento carrega todos os dados necessários para que consumidores
 * downstream possam reagir sem precisar chamar de volta a este serviço.
 * (Padrão: evento auto-suficiente / fat event)
 *
 * Quando Kafka for integrado, este objeto será serializado para JSON
 * e enviado ao tópico "orders.created".
 */
@Getter
@Builder
public class OrderCreatedEvent implements DomainEvent {

    /**
     * ID único do evento — gerado automaticamente para garantir idempotência.
     */
    @Builder.Default
    private final String eventId = UUID.randomUUID().toString();

    private final Long orderId;
    private final Long userId;
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

    /**
     * Snapshot dos itens no momento do evento.
     * Dados imutáveis — não referenciam entidades JPA.
     */
    @Getter
    @Builder
    public static class OrderItemData {
        private final Long productId;
        private final Integer quantity;
        private final BigDecimal price;
    }
}
