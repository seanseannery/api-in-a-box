package com.apiinabox.ratelimit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

/**
 * Simple result class for rate limit checks
 */

 @Data
 @AllArgsConstructor
 @ToString
public class RateLimitResult {

    @Getter
    private final boolean allowed;
    @Getter
    private final String reason;
    
    public static RateLimitResult allowed() {
        return new RateLimitResult(true, null);
    }
    
    public static RateLimitResult rejected(String reason) {
        return new RateLimitResult(false, reason);
    }
    
}