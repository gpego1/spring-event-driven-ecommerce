package com.ecommerce.domain.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidade de domínio User.
 * Representação pura do usuário — sem dependência de frameworks de aplicação.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;
}
