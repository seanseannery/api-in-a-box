package com.apiinabox.ratelimit;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Configuration to register the rate limiting interceptor
 */
@Configuration
public class RateLimitInterceptor implements WebMvcConfigurer, HandlerInterceptor {
    

    private final RateLimitService rateLimitService;


    public RateLimitInterceptor(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this)
                .addPathPatterns("/api/**") // Only apply to API endpoints
                .excludePathPatterns("/api/health", "/api/status"); // Exclude health checks
    }

    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String apiPath = request.getRequestURI();
        String ipAddress = getClientIpAddress(request);
        String clientId = getClientId(request);
        
        RateLimitResult result = rateLimitService.checkRateLimit(apiPath, ipAddress, clientId);
        
        if (!result.isAllowed()) {
            response.setStatus(429); // Too Many Requests
            response.setContentType("application/json");
            response.getWriter().write(String.format(
                "{\"error\":\"Rate limit exceeded\",\"message\":\"%s\"}", 
                result.getReason()
            ));
            return false;
        }
        
        return true;
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        // Check common proxy headers first
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            // Take the first IP in the chain
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        // Fallback to remote address
        return request.getRemoteAddr();
    }
    
    private String getClientId(HttpServletRequest request) {
        // Try API key header first
        String apiKey = request.getHeader("X-API-Key");
        if (apiKey != null && !apiKey.isEmpty()) {
            return "api-key:" + apiKey;
        }
        
        // Try Authorization header (for JWT or other tokens)
        String auth = request.getHeader("Authorization");
        if (auth != null && !auth.isEmpty()) {
            // For JWT, you might want to extract the subject
            // For now, just use the token itself as identifier
            return "auth:" + auth.hashCode(); // Use hashCode to avoid storing full token
        }
        
        // No client identification available
        return null;
    }
}