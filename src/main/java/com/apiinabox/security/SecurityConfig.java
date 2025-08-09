package com.apiinabox.security;

import com.apiinabox.config.AuthorizationConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthorizationConfig authorizationConfig;

    public SecurityConfig(AuthorizationConfig authorizationConfig) {
        this.authorizationConfig = authorizationConfig;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        if (authorizationConfig.isAuthorizationEnabled()) {
            http
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/books/**").hasRole("USER")
                    .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt());
        } else {
            http
                .authorizeHttpRequests(auth -> auth
                    .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable()); // Add this for tests
        }
        return http.build();
    }

    @Bean
    @ConditionalOnProperty(name = "app.authorization-enabled", havingValue = "true")
    public JwtDecoder jwtDecoder() {
        // Replace with your OAuth2 provider's JWK Set URI
        String jwkSetUri = "https://your-oauth2-provider/.well-known/jwks.json";
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
} 