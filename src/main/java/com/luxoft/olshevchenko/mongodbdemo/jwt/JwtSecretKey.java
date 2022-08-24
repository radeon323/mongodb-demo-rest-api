package com.luxoft.olshevchenko.mongodbdemo.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

/**
 * @author Oleksandr Shevchenko
 */
@Configuration
@AllArgsConstructor
public class JwtSecretKey {

    private final JwtConfig jwtConfig;

    @Bean
    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes());
    }
}
