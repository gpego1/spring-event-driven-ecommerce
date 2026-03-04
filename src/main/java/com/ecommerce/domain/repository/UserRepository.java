package com.ecommerce.domain.repository;

import com.ecommerce.domain.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
    void deleteById(Long id);
    boolean existsByEmail(String email);

    Optional<User> loadByUsername(String email);
}
