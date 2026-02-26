package com.ecommerce.application.usecase;

import com.ecommerce.application.dto.CreateProductRequest;
import com.ecommerce.application.dto.ProductResponse;
import com.ecommerce.domain.entity.Product;
import com.ecommerce.domain.repository.ProductRepository;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Caso de uso para operações de produto.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CreateProductUseCase {

    private final ProductRepository productRepository;

    public ProductResponse create(CreateProductRequest request) {
        log.info("Criando produto: {}", request.name());
        Product product = Product.builder()
                .name(request.name())
                .price(request.price())
                .stock(request.stock())
                .build();
        Product saved = productRepository.save(product);
        log.info("Produto criado com id: {}", saved.getId());
        return ProductResponse.from(saved);
    }

    public ProductResponse update(Long id, CreateProductRequest request) {
        Product product = findOrThrow(id);
        product.setName(request.name());
        product.setPrice(request.price());
        product.setStock(request.stock());
        return ProductResponse.from(productRepository.save(product));
    }

    public ProductResponse findById(Long id) {
        return ProductResponse.from(findOrThrow(id));
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream().map(ProductResponse::from).toList();
    }

    public void delete(Long id) {
        findOrThrow(id);
        productRepository.deleteById(id);
        log.info("Produto {} removido", id);
    }

    public Product findOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", id));
    }
}
