package com.ecommerce.infrastructure.config;

import com.ecommerce.domain.event.DomainEvent;
import com.ecommerce.domain.event.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Implementação de EventPublisher que apenas loga o evento.
 *
 * Esta é a implementação atual (sem broker).
 * Quando Kafka for integrado, crie KafkaEventPublisher implements EventPublisher,
 * anote com @Primary e esta classe pode ser mantida para testes/fallback.
 *
 * Troca sem modificar nenhuma linha do domínio ou dos use cases.
 *
 * Exemplo futuro:
 * ┌─────────────────────────────────────────────────────────────┐
 * │  @Primary                                                   │
 * │  @Component                                                 │
 * │  public class KafkaEventPublisher implements EventPublisher │
 * │      @Autowired KafkaTemplate<String, Object> kafka;        │
 * │      public void publish(DomainEvent event) {               │
 * │          kafka.send(event.getEventType(), event);           │
 * │      }                                                      │
 * │  }                                                          │
 * └─────────────────────────────────────────────────────────────┘
 */
@Slf4j
@Component
public class LogEventPublisher implements EventPublisher {

    @Override
    public void publish(DomainEvent event) {
        log.info(
            "[EVENT PUBLISHED] type={} | eventId={} | occurredAt={}",
            event.getEventType(),
            event.getEventId(),
            event.getOccurredAt()
        );
        // TODO: substituir por KafkaTemplate.send() quando Kafka for integrado
    }
}
