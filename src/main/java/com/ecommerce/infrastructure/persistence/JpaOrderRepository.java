package com.ecommerce.infrastructure.persistence;

import com.ecommerce.domain.entity.Order;
import com.ecommerce.domain.repository.OrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Usa JOIN FETCH para evitar problema N+1 ao carregar os itens do pedido.
 */
@Repository
public interface JpaOrderRepository extends JpaRepository<Order, Long>, OrderRepository {

    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.items WHERE o.id = :id")
    Optional<Order> findById(Long id);

    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.items")
    List<Order> findAll();
}
