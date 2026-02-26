package com.ecommerce.infrastructure.persistence;

import com.ecommerce.domain.entity.Product;
import com.ecommerce.domain.repository.ProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProductRepository extends JpaRepository<Product, Long>, ProductRepository {
}
