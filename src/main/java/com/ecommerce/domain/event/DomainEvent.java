package com.ecommerce.domain.event;

import java.time.LocalDateTime;

/**
 * Contrato base para todos os eventos de domínio.
 *
 * Qualquer evento gerado pelo sistema deve implementar esta interface.
 * Isso permite que o EventPublisher aceite qualquer tipo de evento
 * sem conhecer os detalhes de cada um.
 *
 * Quando Kafka for adicionado, o publisher usará este contrato para
 * serializar e enviar para os tópicos correspondentes.
 */
public interface DomainEvent {

    /**
     * Identificador único do evento (para idempotência e rastreabilidade).
     */
    String getEventId();

    /**
     * Tipo do evento (ex: "order.created", "user.registered").
     * Usado como discriminador no futuro roteamento para tópicos Kafka.
     */
    String getEventType();

    /**
     * Momento em que o evento foi gerado.
     */
    LocalDateTime getOccurredAt();
}
