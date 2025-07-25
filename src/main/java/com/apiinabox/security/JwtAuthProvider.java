package com.apiinabox.security;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

public class JwtAuthProvider implements OAuth2TokenValidator<Jwt> {
    private final String issuer;

    public JwtAuthProvider(String issuer) {
        this.issuer = issuer;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        if (!jwt.getIssuer().equals(issuer)) {
            return OAuth2TokenValidatorResult.failure(
                new OAuth2Error("invalid_token", "The issuer is invalid", null)
            );
        }
        return OAuth2TokenValidatorResult.success();
    }
} 