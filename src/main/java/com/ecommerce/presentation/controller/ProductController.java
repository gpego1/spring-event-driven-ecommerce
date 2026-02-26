package com.ecommerce.presentation.controller;

import com.ecommerce.application.dto.CreateProductRequest;
import com.ecommerce.application.dto.ProductResponse;
import com.ecommerce.application.usecase.CreateProductUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Products")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;

    @PostMapping
    @Operation(summary = "Cria um novo produto")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createProductUseCase.create(request));
    }

    @GetMapping
    @Operation(summary = "Lista todos os produtos")
    public ResponseEntity<List<ProductResponse>> findAll() {
        return ResponseEntity.ok(createProductUseCase.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca produto por ID")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(createProductUseCase.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um produto")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id,
                                                   @Valid @RequestBody CreateProductRequest request) {
        return ResponseEntity.ok(createProductUseCase.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um produto")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        createProductUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
