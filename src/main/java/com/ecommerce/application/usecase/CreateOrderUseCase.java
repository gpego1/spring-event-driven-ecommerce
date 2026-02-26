package com.ecommerce.application.usecase;

import com.ecommerce.application.dto.CreateOrderRequest;
import com.ecommerce.application.dto.OrderResponse;
import com.ecommerce.domain.entity.Order;
import com.ecommerce.domain.entity.OrderItem;
import com.ecommerce.domain.entity.OrderStatus;
import com.ecommerce.domain.entity.Product;
import com.ecommerce.domain.event.EventPublisher;
import com.ecommerce.domain.event.OrderCreatedEvent;
import com.ecommerce.domain.repository.OrderRepository;
import com.ecommerce.domain.repository.UserRepository;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Caso de uso para criação de pedidos.
 *
 * Fluxo principal:
 *   1. Valida usuário e itens
 *   2. Cria o pedido com status COMPLETED
 *   3. Persiste
 *   4. Gera OrderCreatedEvent
 *   5. Publica via EventPublisher (hoje: log / futuro: Kafka)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CreateProductUseCase createProductUseCase;

    /**
     * EventPublisher é injetado via interface — o use case não sabe se é Kafka, log ou outra coisa.
     * Para integrar Kafka: crie KafkaEventPublisher implements EventPublisher e troque a implementação.
     */
    private final EventPublisher eventPublisher;

    public OrderResponse create(CreateOrderRequest request) {
        log.info("Iniciando criação de pedido para userId: {}", request.userId());

        userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", request.userId()));

        if (request.items() == null || request.items().isEmpty()) {
            throw new BusinessException("Pedido deve conter ao menos um item.");
        }

        Order order = Order.builder()
                .userId(request.userId())
                .status(OrderStatus.COMPLETED)
                .totalAmount(BigDecimal.ZERO)
                .build();

        for (CreateOrderRequest.OrderItemRequest itemReq : request.items()) {
            Product product = createProductUseCase.findOrThrow(itemReq.productId());
            OrderItem item = OrderItem.builder()
                    .productId(product.getId())
                    .quantity(itemReq.quantity())
                    .price(product.getPrice())
                    .build();
            order.addItem(item);
        }

        Order saved = orderRepository.save(order);
        log.info("Pedido {} criado com status {} e total {}", saved.getId(), saved.getStatus(), saved.getTotalAmount());

        // Gera e publica o evento de domínio
        OrderCreatedEvent event = buildEvent(saved);
        eventPublisher.publish(event);

        return OrderResponse.from(saved);
    }

    public OrderResponse findById(Long id) {
        return OrderResponse.from(
                orderRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Pedido", id))
        );
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream().map(OrderResponse::from).toList();
    }

    /**
     * Constrói o evento com dados imutáveis — snapshot do pedido no momento da criação.
     */
    private OrderCreatedEvent buildEvent(Order order) {
        List<OrderCreatedEvent.OrderItemData> itemData = order.getItems().stream()
                .map(item -> OrderCreatedEvent.OrderItemData.builder()
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .toList();

        return OrderCreatedEvent.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .items(itemData)
                .totalAmount(order.getTotalAmount())
                .build();
    }
}
