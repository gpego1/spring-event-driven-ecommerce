package com.ecommerce.infrastructure.persistence;

import com.ecommerce.domain.entity.User;
import com.ecommerce.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Implementação do repositório de User.
 * Estende JpaRepository (Spring Data) E UserRepository (domínio) — ponte entre as camadas.
 */
@Repository
public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {
    boolean existsByEmail(String email);
}
