package com.ecommerce.infrastructure.filters;
import com.ecommerce.application.usecase.AuthUseCase;
import com.ecommerce.domain.entity.User;
import com.ecommerce.infrastructure.config.TokenConfig;
import com.ecommerce.shared.exception.InvalidTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenConfig tokenConfig;
    private final AuthUseCase authService;

    public JwtAuthenticationFilter(TokenConfig tokenConfig, AuthUseCase authService) {
        this.tokenConfig = tokenConfig;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = this.extractTokenFromAuthorizationHeader(request.getHeader("Authorization"));
        try {
            if (token != null) {
                String username = tokenConfig.validateToken(token);
                UserDetails user = authService.loadUserByUsername(username);
                var authentication = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (InvalidTokenException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new InvalidTokenException(e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
    private String extractTokenFromAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        return authorizationHeader.substring(7);
    }
}
