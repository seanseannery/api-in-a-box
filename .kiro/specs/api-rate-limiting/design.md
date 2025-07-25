# API Rate Limiting Design Document

## Overview

The API Rate Limiting system will be implemented using Resilience4j's RateLimiter as a Spring Boot interceptor that evaluates incoming requests against configured rate limits before they reach the controller layer. Resilience4j provides a robust, thread-safe, and high-performance rate limiting solution with excellent Spring Boot integration and built-in monitoring capabilities.

This design leverages Resilience4j's proven sliding window algorithm and provides seamless integration with the existing Spring Security and configuration infrastructure. The solution requires no external dependencies and is ideal for single-instance deployments with excellent performance characteristics.

## Architecture

### System Architecture

```mermaid
graph TB
    Client[API Client] --> API[API Instance]
    API --> Interceptor[Rate Limit Interceptor]
    Interceptor --> R4J[(Resilience4j RateLimiter Registry)]
    Interceptor --> Controller[Controllers]
    R4J --> Metrics[Built-in Metrics]
    Interceptor --> Config[Rate Limit Configuration]
```

**Key Benefits:**
- Purpose-built for rate limiting with proven algorithms
- Excellent Spring Boot integration and auto-configuration
- Built-in metrics and monitoring capabilities
- Thread-safe and high performance
- No external dependencies required
- Supports sliding window and fixed window algorithms

### Request Flow

```mermaid
sequenceDiagram
    participant Client
    participant Interceptor as Rate Limit Interceptor
    participant R4J as Resilience4j RateLimiter
    participant Controller
    participant Metrics
    
    Client->>Interceptor: HTTP Request
    Interceptor->>Interceptor: Extract Client ID
    Interceptor->>R4J: Acquire Permission
    
    alt Permission Granted
        R4J-->>Interceptor: Permission Acquired
        Interceptor->>Controller: Forward Request
        Controller-->>Interceptor: Response
        Interceptor->>Metrics: Record Success
        Interceptor-->>Client: HTTP Response + Rate Headers
    else Permission Denied
        R4J-->>Interceptor: Permission Denied
        Interceptor->>Metrics: Record Rate Limit Hit
        Interceptor-->>Client: HTTP 429 + Rate Headers
    end
```

## Components and Interfaces

### Core Components

```mermaid
classDiagram
    class RateLimitInterceptor {
        +preHandle(request, response, handler) boolean
        +afterCompletion(request, response, handler, ex)
        -extractClientId(request) String
        -checkRateLimit(clientId, endpoint) RateLimitResult
        -addRateLimitHeaders(response, result)
        -getRateLimiter(key) RateLimiter
        -createRateLimiterConfig(endpoint) RateLimiterConfig
    }
    
    class RateLimitConfig {
        +getEndpointConfig(endpoint) EndpointRateLimit
        +getDefaultConfig() EndpointRateLimit
        +isRateLimitingEnabled() boolean
    }
    
    class RateLimiterRegistry {
        <<Resilience4j>>
        +rateLimiter(name, config) RateLimiter
        +getAllRateLimiters() Set~RateLimiter~
        +getMetrics() RateLimiterMetrics
    }
    
    class RateLimiter {
        <<Resilience4j>>
        +acquirePermission() boolean
        +acquirePermission(timeout) boolean
        +getMetrics() Metrics
        +getName() String
    }
    
    class RateLimitResult {
        +allowed boolean
        +remainingRequests int
        +resetTimeMillis long
        +limitPerWindow int
    }
    
    class EndpointRateLimit {
        +limitForPeriod int
        +limitRefreshPeriod Duration
        +timeoutDuration Duration
        +enabled boolean
    }
    
    class RateLimiterConfig {
        <<Resilience4j>>
        +limitForPeriod int
        +limitRefreshPeriod Duration
        +timeoutDuration Duration
    }
    
    RateLimitInterceptor --> RateLimitConfig
    RateLimitInterceptor --> RateLimiterRegistry
    RateLimiterRegistry --> RateLimiter
    RateLimitInterceptor --> RateLimitResult
    RateLimitConfig --> EndpointRateLimit
    RateLimitInterceptor --> RateLimiterConfig
```

### Client Identification Strategy

```mermaid
flowchart TD
    Start[Incoming Request] --> CheckAPI{API Key Present?}
    CheckAPI -->|Yes| UseAPI[Use API Key as Client ID]
    CheckAPI -->|No| CheckJWT{JWT Token Present?}
    CheckJWT -->|Yes| ExtractSub[Extract Subject from JWT]
    CheckJWT -->|No| UseIP[Use IP Address as Client ID]
    
    UseAPI --> Validate[Validate and Apply Rate Limit]
    ExtractSub --> Validate
    UseIP --> CheckProxy{Behind Proxy?}
    CheckProxy -->|Yes| ExtractReal[Extract Real IP from Headers]
    CheckProxy -->|No| UseDirectIP[Use Direct IP]
    ExtractReal --> Validate
    UseDirectIP --> Validate
    
    Validate --> Decision{Within Limit?}
    Decision -->|Yes| Allow[Allow Request]
    Decision -->|No| Block[Return 429]
```

## Data Models

### Configuration Model

```yaml
# application.yml
app:
  rate-limiting:
    enabled: true
    client-identification:
      priority: ["api-key", "jwt-subject", "ip-address"]
      api-key-header: "X-API-Key"
      trusted-proxies: ["10.0.0.0/8", "172.16.0.0/12"]
    endpoints:
      "/api/accounts/**": "accounts-api"
      "/api/books/**": "books-api"
      "default": "default-api"

# Standard Resilience4j configuration
resilience4j:
  ratelimiter:
    configs:
      default:
        limitForPeriod: 10
        limitRefreshPeriod: PT1S
        timeoutDuration: PT0S
        registerHealthIndicator: true
        eventConsumerBufferSize: 100
        writableStackTraceEnabled: true
    instances:
      default-api:
        baseConfig: default
      accounts-api:
        baseConfig: default
        limitForPeriod: 5
        limitRefreshPeriod: PT1S
      books-api:
        baseConfig: default
        limitForPeriod: 20
        limitRefreshPeriod: PT1S
```

### Resilience4j Rate Limiter Mechanics

Resilience4j uses a **sliding window** algorithm that:
- Maintains a configurable number of permissions per time window
- Refreshes permissions at regular intervals
- Provides thread-safe access without external dependencies
- Supports both blocking and non-blocking permission acquisition

```java
// Rate limiter key structure: "clientId:endpoint"
// Examples:
// - "user123:/api/accounts"
// - "192.168.1.100:/api/books"
// - "api-key-abc:/api/accounts"
```

## Error Handling

### Rate Limit Response Format

```json
{
  "error": "rate_limit_exceeded",
  "message": "Rate limit exceeded for endpoint /api/accounts. Try again in 45 seconds.",
  "details": {
    "endpoint": "/api/accounts",
    "limit": 50,
    "window": "minute",
    "retry_after": 45
  }
}
```

### HTTP Headers

```
X-RateLimit-Limit: 50
X-RateLimit-Remaining: 0
X-RateLimit-Reset: 1642694400
X-RateLimit-Window: minute
Retry-After: 45
```

### Fallback Strategy

```mermaid
flowchart TD
    Request[Incoming Request] --> R4J{Resilience4j Available?}
    R4J -->|Yes| Normal[Normal Rate Limiting]
    R4J -->|No| Fallback{Fallback Enabled?}
    Fallback -->|Yes| Allow[Allow All Requests]
    Fallback -->|No| Reject[Reject All Requests]
    
    Normal --> Result[Rate Limit Decision]
    Allow --> Pass[Pass Through]
    Reject --> Block[Return 503]
    
    Result --> Log[Log Decision]
    Pass --> Log
    Block --> Log
```

## Testing Strategy

### Unit Tests
- **RateLimitInterceptor**: Test request interception, rate limiting logic, and header setting
- **RateLimitConfig**: Test configuration parsing and validation
- **Client ID Extraction**: Test various client identification scenarios
- **Resilience4j Integration**: Test RateLimiter creation and permission acquisition

### Integration Tests
- **End-to-End Rate Limiting**: Test actual HTTP requests against rate limits
- **Configuration Loading**: Test different configuration scenarios
- **Error Handling**: Test Resilience4j failures and fallback behavior
- **Spring Integration**: Test interceptor registration and Spring context integration