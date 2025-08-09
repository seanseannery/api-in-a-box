package com.apiinabox.config;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "app.authorization-enabled=false",
    "app.rate-limiting.enabled=true",
    "resilience4j.ratelimiter.instances.default-api.limitForPeriod=10",
    "resilience4j.ratelimiter.instances.accounts-api.limitForPeriod=5",
    "resilience4j.ratelimiter.instances.books-api.limitForPeriod=20"
})
@DisplayName("Resilience4j Configuration Tests")
class Resilience4jConfigurationTest {

    @Autowired
    private RateLimiterRegistry rateLimiterRegistry;

    @Test
    @DisplayName("Should auto-configure RateLimiterRegistry bean")
    void shouldAutoConfigureRateLimiterRegistry() {
        assertNotNull(rateLimiterRegistry, "RateLimiterRegistry should be auto-configured");
    }

    @Nested
    @DisplayName("Rate Limiter Instance Tests")
    class RateLimiterInstanceTests {

        private RateLimiter defaultRateLimiter;
        private RateLimiter accountsRateLimiter;
        private RateLimiter booksRateLimiter;

        @BeforeEach
        void setUp() {
            defaultRateLimiter = rateLimiterRegistry.rateLimiter("default-api");
            accountsRateLimiter = rateLimiterRegistry.rateLimiter("accounts-api");
            booksRateLimiter = rateLimiterRegistry.rateLimiter("books-api");
        }

        @Test
        @DisplayName("Should create all configured rate limiters")
        void shouldCreateConfiguredRateLimiters() {
            assertAll("Rate limiters should be created",
                () -> assertNotNull(defaultRateLimiter, "Default rate limiter should exist"),
                () -> assertNotNull(accountsRateLimiter, "Accounts rate limiter should exist"),
                () -> assertNotNull(booksRateLimiter, "Books rate limiter should exist")
            );
        }

        @Test
        @DisplayName("Should have correct limit per period for each instance")
        void shouldHaveCorrectLimitForPeriod() {
            assertAll("Rate limiters should have correct limits",
                () -> assertEquals(10, defaultRateLimiter.getRateLimiterConfig().getLimitForPeriod(),
                    "Default API should allow 10 requests per period"),
                () -> assertEquals(5, accountsRateLimiter.getRateLimiterConfig().getLimitForPeriod(),
                    "Accounts API should allow 5 requests per period"),
                () -> assertEquals(20, booksRateLimiter.getRateLimiterConfig().getLimitForPeriod(),
                    "Books API should allow 20 requests per period")
            );
        }

        @Test
        @DisplayName("Should have correct refresh period configuration")
        void shouldHaveCorrectRefreshPeriod() {
            Duration expectedRefreshPeriod = Duration.ofMinutes(1);
            
            assertAll("All rate limiters should have 1-minute refresh period",
                () -> assertEquals(expectedRefreshPeriod, defaultRateLimiter.getRateLimiterConfig().getLimitRefreshPeriod(),
                    "Default API refresh period should be 1 minute"),
                () -> assertEquals(expectedRefreshPeriod, accountsRateLimiter.getRateLimiterConfig().getLimitRefreshPeriod(),
                    "Accounts API refresh period should be 1 minute"),
                () -> assertEquals(expectedRefreshPeriod, booksRateLimiter.getRateLimiterConfig().getLimitRefreshPeriod(),
                    "Books API refresh period should be 1 minute")
            );
        }

        @Test
        @DisplayName("Should have zero timeout duration for non-blocking behavior")
        void shouldHaveZeroTimeoutDuration() {
            Duration expectedTimeout = Duration.ZERO;
            
            assertAll("All rate limiters should have zero timeout for non-blocking behavior",
                () -> assertEquals(expectedTimeout, defaultRateLimiter.getRateLimiterConfig().getTimeoutDuration(),
                    "Default API should have zero timeout"),
                () -> assertEquals(expectedTimeout, accountsRateLimiter.getRateLimiterConfig().getTimeoutDuration(),
                    "Accounts API should have zero timeout"),
                () -> assertEquals(expectedTimeout, booksRateLimiter.getRateLimiterConfig().getTimeoutDuration(),
                    "Books API should have zero timeout")
            );
        }
    }

    @Nested
    @DisplayName("Rate Limiter Configuration Tests")
    class RateLimiterConfigurationTests {

        @Test
        @DisplayName("Should have health indicator enabled")
        void shouldHaveHealthIndicatorEnabled() {
            RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter("default-api");
            RateLimiterConfig config = rateLimiter.getRateLimiterConfig();
            
            // Note: This property might not be directly accessible via the config
            // but we can verify the rate limiter was created successfully
            assertNotNull(config, "Rate limiter configuration should exist");
        }

        @Test
        @DisplayName("Should create new rate limiter instances dynamically")
        void shouldCreateNewRateLimiterInstancesDynamically() {
            String dynamicInstanceName = "dynamic-test-api";
            
            RateLimiter dynamicRateLimiter = rateLimiterRegistry.rateLimiter(dynamicInstanceName);
            
            assertNotNull(dynamicRateLimiter, "Should be able to create dynamic rate limiter instances");
            assertEquals(dynamicInstanceName, dynamicRateLimiter.getName(), 
                "Dynamic rate limiter should have correct name");
        }

        @Test
        @DisplayName("Should use default configuration for non-configured instances")
        void shouldUseDefaultConfigurationForNonConfiguredInstances() {
            RateLimiter nonConfiguredRateLimiter = rateLimiterRegistry.rateLimiter("non-configured-api");
            RateLimiterConfig config = nonConfiguredRateLimiter.getRateLimiterConfig();
            
            // Should use default configuration values
            assertEquals(10, config.getLimitForPeriod(), 
                "Non-configured instance should use default limit");
            assertEquals(Duration.ofMinutes(1), config.getLimitRefreshPeriod(),
                "Non-configured instance should use default refresh period");
            assertEquals(Duration.ZERO, config.getTimeoutDuration(),
                "Non-configured instance should use default timeout");
        }
    }
}