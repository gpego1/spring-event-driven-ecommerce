package com.ecommerce.infrastructure.kafka;
import com.ecommerce.domain.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    private static final String TOPIC = "order.created";

    public void publish(OrderCreatedEvent event) {
        kafkaTemplate.send(TOPIC, event.getEventId(), event);
        System.out.println("Event sent: " + event.getOrderId());
    }
}
