package com.ecommerce.application.dto;

import com.ecommerce.domain.entity.User;

public record UserResponse(Long id, String name, String email, int loginAttempts) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(),  user.getLoginAttempts());
    }
}
