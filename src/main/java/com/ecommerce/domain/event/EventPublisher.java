package com.ecommerce.domain.event;

/**
 * Contrato para publicação de eventos de domínio.
 *
 * O domínio depende apenas desta interface — não sabe nada sobre
 * Kafka, RabbitMQ ou qualquer outro broker de mensageria.
 *
 * Implementações possíveis (plug-and-play):
 *   - LogEventPublisher (atual)  — loga o evento, sem broker
 *   - KafkaEventPublisher        — envia para tópico Kafka (implementar futuramente)
 *   - RabbitEventPublisher       — envia para exchange RabbitMQ
 *
 * Para trocar a implementação, basta criar a nova classe e anotar com @Primary
 * ou ajustar o @Qualifier no injetor — zero alteração no domínio.
 */
public interface EventPublisher {

    /**
     * Publica um evento de domínio para o canal de saída configurado.
     *
     * @param event qualquer implementação de DomainEvent
     */
    void publish(DomainEvent event);
}
