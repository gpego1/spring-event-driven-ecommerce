package com.ecommerce.infrastructure.config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ecommerce.application.dto.RefreshTokenRequest;
import com.ecommerce.domain.entity.User;
import com.ecommerce.shared.exception.InvalidTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class TokenConfig {
    private final String secret;
    private final long expiration;
    private final long refreshExpiration;

    public TokenConfig(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration,
            @Value("${jwt.refresh-expiration}") long refreshExpiration
    )    {
        this.secret = secret;
        this.expiration = expiration;
        this.refreshExpiration = refreshExpiration;
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret);
    }

    public String generateToken(User user) {
        return JWT.create()
                .withClaim("type", "access")
                .withClaim("role", user.getRole().name())
                .withSubject(user.getUsername())
                .withExpiresAt(Date.from(Instant.now().plusSeconds(expiration)))
                .withIssuedAt(Instant.now())
                .sign(getAlgorithm());
    }
    public String generateRefreshToken(User user) {
        return JWT.create()
                .withClaim("type", "refresh")
                .withSubject(user.getUsername())
                .withExpiresAt(Date.from(Instant.now().plusSeconds(refreshExpiration)))
                .withIssuedAt(Instant.now())
                .sign(getAlgorithm());
    }
    public String validateToken(String token) {
        try {
            JWTVerifier verifier =  JWT.require(getAlgorithm()).build();
            DecodedJWT decoded = verifier.verify(token);
            return decoded.getSubject();
        } catch (InvalidTokenException e) {
            throw new InvalidTokenException("Invalid token");
        }
    }

    public String validateRefreshToken(String token) {
        DecodedJWT decodedJWT = JWT.require(getAlgorithm())
                .withClaim("type", "refresh")
                .build()
                .verify(token);
        return decodedJWT.getSubject();
    }
}
