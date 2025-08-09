package com.apiinabox.ratelimit;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple rate limiting service using Resilience4j.
 * Handles three use cases:
 * 1. Per-API endpoint limits
 * 2. Per-IP address limits across all APIs
 * 3. Per-client ID limits across all APIs
 */
@Service
public class RateLimitService {
    
    private static final Logger logger = LoggerFactory.getLogger(RateLimitService.class);
    
    private final RateLimiterRegistry rateLimiterRegistry;
    private final ConcurrentHashMap<String, RateLimiter> rateLimiters = new ConcurrentHashMap<>();
    
    // Default configurations
    private static final int DEFAULT_API_LIMIT = 100;  // requests per minute per API
    private static final int DEFAULT_IP_LIMIT = 200;   // requests per minute per IP
    private static final int DEFAULT_CLIENT_LIMIT = 500; // requests per minute per client
    
    public RateLimitService(RateLimiterRegistry rateLimiterRegistry) {
        this.rateLimiterRegistry = rateLimiterRegistry;
    }
    
    /**
     * Check rate limit for a specific API endpoint
     */
    public boolean isAllowedForApi(String apiPath) {
        String key = "api:" + apiPath;
        RateLimiter rateLimiter = getRateLimiter(key, DEFAULT_API_LIMIT);
        return rateLimiter.acquirePermission();
    }
    
    /**
     * Check rate limit for an IP address across all APIs
     */
    public boolean isAllowedForIp(String ipAddress) {
        String key = "ip:" + ipAddress;
        RateLimiter rateLimiter = getRateLimiter(key, DEFAULT_IP_LIMIT);
        return rateLimiter.acquirePermission();
    }
    
    /**
     * Check rate limit for a client ID across all APIs
     */
    public boolean isAllowedForClient(String clientId) {
        String key = "client:" + clientId;
        RateLimiter rateLimiter = getRateLimiter(key, DEFAULT_CLIENT_LIMIT);
        return rateLimiter.acquirePermission();
    }
    
    /**
     * Check all applicable rate limits for a request
     */
    public RateLimitResult checkRateLimit(String apiPath, String ipAddress, String clientId) {
        // Check API-specific limit
        if (!isAllowedForApi(apiPath)) {
            String reason = "API rate limit exceeded for " + apiPath;
            logRateLimitRejection("API", apiPath, ipAddress, clientId, reason);
            return RateLimitResult.rejected(reason);
        }
        
        // Check IP limit
        if (ipAddress != null && !isAllowedForIp(ipAddress)) {
            String reason = "IP rate limit exceeded for " + ipAddress;
            logRateLimitRejection("IP", apiPath, ipAddress, clientId, reason);
            return RateLimitResult.rejected(reason);
        }
        
        // Check client limit
        if (clientId != null && !isAllowedForClient(clientId)) {
            String reason = "Client rate limit exceeded for " + clientId;
            logRateLimitRejection("CLIENT", apiPath, ipAddress, clientId, reason);
            return RateLimitResult.rejected(reason);
        }
        
        return RateLimitResult.allowed();
    }
    
    private RateLimiter getRateLimiter(String key, int limit) {
        return rateLimiters.computeIfAbsent(key, k -> {
            RateLimiterConfig config = RateLimiterConfig.custom()
                    .limitForPeriod(limit)
                    .limitRefreshPeriod(Duration.ofMinutes(1))
                    .timeoutDuration(Duration.ZERO)
                    .build();
            
            return rateLimiterRegistry.rateLimiter(key, config);
        });
    }
    
    /**
     * Get current rate limit status for debugging
     */
    public RateLimitStatus getStatus(String key) {
        RateLimiter rateLimiter = rateLimiters.get(key);
        if (rateLimiter == null) {
            return null;
        }
        
        var metrics = rateLimiter.getMetrics();
        return new RateLimitStatus(
                metrics.getAvailablePermissions(),
                rateLimiter.getRateLimiterConfig().getLimitForPeriod(),
                key
        );
    }
    
    /**
     * Log rate limit rejection events for monitoring and analysis
     */
    private void logRateLimitRejection(String limitType, String apiPath, String ipAddress, String clientId, String reason) {
        logger.warn("Rate limit rejection - Type: {}, API: {}, IP: {}, Client: {}, Reason: {}", 
                limitType, 
                apiPath != null ? apiPath : "N/A", 
                ipAddress != null ? ipAddress : "N/A", 
                clientId != null ? clientId : "N/A", 
                reason);
    }
}