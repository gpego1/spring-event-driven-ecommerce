package com.ecommerce.shared.exception;

import org.springframework.security.core.AuthenticationException;

public class LoginUserException extends AuthenticationException {
    public LoginUserException(String message) {
        super(message);
    }
}
