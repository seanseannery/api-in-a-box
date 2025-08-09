package com.apiinabox.ratelimit;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class RateLimitServiceTest {
    
    private RateLimitService rateLimitService;
    
    @BeforeEach
    void setUp() {
        RateLimiterRegistry registry = RateLimiterRegistry.ofDefaults();
        rateLimitService = new RateLimitService(registry);
    }
    
    @Test
    void testApiRateLimit() {
        String apiPath = "/api/books";
        
        // Should allow requests initially
        assertTrue(rateLimitService.isAllowedForApi(apiPath));
        
        // Exhaust the rate limit (default is 100 per minute)
        for (int i = 0; i < 99; i++) {
            assertTrue(rateLimitService.isAllowedForApi(apiPath));
        }
        
        // Should reject the 101st request
        assertFalse(rateLimitService.isAllowedForApi(apiPath));
    }
    
    @Test
    void testIpRateLimit() {
        String ipAddress = "192.168.1.1";
        
        // Should allow requests initially
        assertTrue(rateLimitService.isAllowedForIp(ipAddress));
        
        // Exhaust the rate limit (default is 200 per minute)
        for (int i = 0; i < 199; i++) {
            assertTrue(rateLimitService.isAllowedForIp(ipAddress));
        }
        
        // Should reject the 201st request
        assertFalse(rateLimitService.isAllowedForIp(ipAddress));
    }
    
    @Test
    void testClientRateLimit() {
        String clientId = "client123";
        
        // Should allow requests initially
        assertTrue(rateLimitService.isAllowedForClient(clientId));
        
        // Exhaust the rate limit (default is 500 per minute)
        for (int i = 0; i < 499; i++) {
            assertTrue(rateLimitService.isAllowedForClient(clientId));
        }
        
        // Should reject the 501st request
        assertFalse(rateLimitService.isAllowedForClient(clientId));
    }
    
    @Test
    void testCombinedRateLimit() {
        String apiPath = "/api/accounts";
        String ipAddress = "10.0.0.1";
        String clientId = "test-client";
        
        // Should allow initially
        RateLimitResult result = rateLimitService.checkRateLimit(apiPath, ipAddress, clientId);
        assertTrue(result.isAllowed());
        
        // Exhaust API limit
        for (int i = 0; i < 99; i++) {
            rateLimitService.isAllowedForApi(apiPath);
        }
        
        // Should be rejected due to API limit
        result = rateLimitService.checkRateLimit(apiPath, ipAddress, clientId);
        assertFalse(result.isAllowed());
        assertTrue(result.getReason().contains("API rate limit exceeded"));
    }
    
    @Test
    void testDifferentApisHaveSeparateLimits() {
        String api1 = "/api/books";
        String api2 = "/api/accounts";
        
        // Exhaust limit for api1
        for (int i = 0; i < 100; i++) {
            rateLimitService.isAllowedForApi(api1);
        }
        
        // api1 should be blocked
        assertFalse(rateLimitService.isAllowedForApi(api1));
        
        // api2 should still be allowed
        assertTrue(rateLimitService.isAllowedForApi(api2));
    }
    
    @Test
    void testRateLimitStatus() {
        String apiPath = "/api/test";
        
        // Make some requests
        for (int i = 0; i < 10; i++) {
            rateLimitService.isAllowedForApi(apiPath);
        }
        
        RateLimitStatus status = rateLimitService.getStatus("api:" + apiPath);
        assertNotNull(status);
        assertEquals(90, status.getAvailablePermissions()); // 100 - 10 = 90
        assertEquals(100, status.getTotalPermissions());
        assertEquals(10, status.getUsedPermissions());
    }
    
    @Test
    void testRateLimitRejectionLogging() {
        // Set up log capture
        Logger logger = (Logger) LoggerFactory.getLogger(RateLimitService.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
        
        String apiPath = "/api/test-logging";
        String ipAddress = "192.168.1.100";
        String clientId = "test-client-123";
        
        // Exhaust API rate limit
        for (int i = 0; i < 100; i++) {
            rateLimitService.checkRateLimit(apiPath, ipAddress, clientId);
        }
        
        // This should trigger rate limit rejection and logging
        RateLimitResult result = rateLimitService.checkRateLimit(apiPath, ipAddress, clientId);
        
        // Verify the request was rejected
        assertFalse(result.isAllowed());
        assertTrue(result.getReason().contains("API rate limit exceeded"));
        
        // Verify logging occurred
        assertEquals(1, listAppender.list.size());
        ILoggingEvent logEvent = listAppender.list.get(0);
        
        assertEquals(Level.WARN, logEvent.getLevel());
        assertTrue(logEvent.getFormattedMessage().contains("Rate limit rejection"));
        assertTrue(logEvent.getFormattedMessage().contains("Type: API"));
        assertTrue(logEvent.getFormattedMessage().contains("API: " + apiPath));
        assertTrue(logEvent.getFormattedMessage().contains("IP: " + ipAddress));
        assertTrue(logEvent.getFormattedMessage().contains("Client: " + clientId));
        assertTrue(logEvent.getFormattedMessage().contains("API rate limit exceeded"));
        
        // Clean up
        logger.detachAppender(listAppender);
    }
    
    @Test
    void testIpRateLimitRejectionLogging() {
        // Set up log capture
        Logger logger = (Logger) LoggerFactory.getLogger(RateLimitService.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
        
        String ipAddress = "10.0.0.50";
        
        // Exhaust IP rate limit directly (not through checkRateLimit to avoid API limit interference)
        for (int i = 0; i < 200; i++) {
            rateLimitService.isAllowedForIp(ipAddress);
        }
        
        // Now test with checkRateLimit - this should trigger IP rate limit rejection and logging
        String apiPath = "/api/test-ip-logging";
        RateLimitResult result = rateLimitService.checkRateLimit(apiPath, ipAddress, null);
        
        // Verify the request was rejected
        assertFalse(result.isAllowed());
        assertTrue(result.getReason().contains("IP rate limit exceeded"));
        
        // Verify logging occurred
        assertEquals(1, listAppender.list.size());
        ILoggingEvent logEvent = listAppender.list.get(0);
        
        assertEquals(Level.WARN, logEvent.getLevel());
        assertTrue(logEvent.getFormattedMessage().contains("Rate limit rejection"));
        assertTrue(logEvent.getFormattedMessage().contains("Type: IP"));
        assertTrue(logEvent.getFormattedMessage().contains("API: " + apiPath));
        assertTrue(logEvent.getFormattedMessage().contains("IP: " + ipAddress));
        assertTrue(logEvent.getFormattedMessage().contains("Client: N/A"));
        assertTrue(logEvent.getFormattedMessage().contains("IP rate limit exceeded"));
        
        // Clean up
        logger.detachAppender(listAppender);
    }
    
    @Test
    void testClientRateLimitRejectionLogging() {
        // Set up log capture
        Logger logger = (Logger) LoggerFactory.getLogger(RateLimitService.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
        
        String clientId = "heavy-user-client";
        
        // Exhaust client rate limit directly (not through checkRateLimit to avoid API/IP limit interference)
        for (int i = 0; i < 500; i++) {
            rateLimitService.isAllowedForClient(clientId);
        }
        
        // Now test with checkRateLimit - this should trigger client rate limit rejection and logging
        String apiPath = "/api/test-client-logging";
        String ipAddress = "172.16.0.10";
        RateLimitResult result = rateLimitService.checkRateLimit(apiPath, ipAddress, clientId);
        
        // Verify the request was rejected
        assertFalse(result.isAllowed());
        assertTrue(result.getReason().contains("Client rate limit exceeded"));
        
        // Verify logging occurred
        assertEquals(1, listAppender.list.size());
        ILoggingEvent logEvent = listAppender.list.get(0);
        
        assertEquals(Level.WARN, logEvent.getLevel());
        assertTrue(logEvent.getFormattedMessage().contains("Rate limit rejection"));
        assertTrue(logEvent.getFormattedMessage().contains("Type: CLIENT"));
        assertTrue(logEvent.getFormattedMessage().contains("API: " + apiPath));
        assertTrue(logEvent.getFormattedMessage().contains("IP: " + ipAddress));
        assertTrue(logEvent.getFormattedMessage().contains("Client: " + clientId));
        assertTrue(logEvent.getFormattedMessage().contains("Client rate limit exceeded"));
        
        // Clean up
        logger.detachAppender(listAppender);
    }
}