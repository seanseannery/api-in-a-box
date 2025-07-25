# Implementation Plan

- [ ] 1. Set up Resilience4j dependencies and basic configuration

  - Add Resilience4j Spring Boot starter dependency to pom.xml
  - Create basic application.yml configuration structure for rate limiting
  - Verify Resilience4j auto-configuration is working
  - _Requirements: 4.1, 4.2, 4.4_

- [ ] 2. Implement rate limiting configuration classes

  - Create EndpointRateLimit data class with limitForPeriod (requests per minute)
  - Create RateLimitConfig component to read and parse application properties
  - Implement methods to get endpoint-specific and default configurations with fixed 1-minute window
  - Write unit tests for configuration parsing and validation
  - _Requirements: 4.1, 4.2, 4.3, 4.4_

- [ ] 3. Create client identification utilities

  - Implement extractClientId method to identify clients by API key
  - Implement extractClientSubject to identify clients by JWT subject
  - Implement extractClientIP method to identify clients by IP address
  - Add support for trusted proxy headers (X-Forwarded-For, X-Real-IP)
  - Create priority-based client identification logic
  - Write unit tests for various client identification scenarios
  - _Requirements: 3.1, 3.2, 3.3_

- [ ] 4. Implement core RateLimitInterceptor class

  - Create RateLimitInterceptor implementing HandlerInterceptor
  - Implement preHandle method with rate limiting logic
  - Add Resilience4j RateLimiter creation and management
  - Integrate client identification and endpoint configuration
  - _Requirements: 1.1, 1.4, 3.1, 3.2_

- [ ] 5. Add rate limiting decision logic

  - Implement checkRateLimit method using Resilience4j acquirePermission
  - Create RateLimitResult class to encapsulate rate limiting decisions
  - Add logic to handle permission granted and denied scenarios
  - Write unit tests for rate limiting decision logic
  - _Requirements: 1.1, 1.4, 2.1_

- [ ] 6. Implement HTTP response headers and error handling

  - Add addRateLimitHeaders method to set X-RateLimit-\* headers
  - Implement proper HTTP 429 response with Retry-After header
  - Create structured error response JSON format
  - Write unit tests for header setting and error response formatting
  - _Requirements: 2.1, 2.2, 2.3_

- [ ] 7. Configure Spring interceptor registration

  - Create WebMvcConfigurer to register RateLimitInterceptor
  - Add conditional registration based on rate limiting enabled flag
  - Configure interceptor order and path patterns
  - Write integration tests for interceptor registration
  - _Requirements: 1.1, 4.4_

- [ ] 8. Add comprehensive logging and metrics integration

  - Implement logging for rate limiting events (allowed, denied, errors)
  - Integrate with Resilience4j built-in metrics
  - Add structured logging with client ID, endpoint, and decision details
  - Write tests to verify logging and metrics collection
  - _Requirements: 5.1, 5.2, 5.4_

- [ ] 9. Create integration tests for end-to-end functionality

  - Write tests for actual HTTP requests against rate-limited endpoints
  - Test different client identification methods (API key, JWT, IP)
  - Verify proper HTTP headers and status codes in responses
  - Test configuration changes and endpoint-specific limits
  - _Requirements: 1.1, 1.2, 1.3, 2.1, 2.2, 3.1, 3.2_

- [ ] 10. Add fallback and error handling mechanisms

  - Implement fallback behavior when Resilience4j fails
  - Add graceful degradation options (allow all vs reject all)
  - Create proper exception handling for configuration errors
  - Write tests for error scenarios and fallback behavior
  - _Requirements: 1.4, 4.4, 5.4_

- [ ] 11. Wire rate limiting into existing API endpoints

  - Apply rate limiting to Account API endpoints (/api/accounts/\*\*)
  - Apply rate limiting to Book API endpoints (/api/books/\*\*)
  - Configure different limits for different endpoint patterns
  - Test rate limiting integration with existing authentication and authorization
  - _Requirements: 1.1, 1.2, 1.3, 3.1, 4.1_

- [ ] 12. Create comprehensive documentation and examples
  - Document configuration options and examples in application.yml
  - Create usage examples for different client identification methods
  - Add troubleshooting guide for common rate limiting issues
  - Write developer documentation for extending rate limiting functionality
  - _Requirements: 4.1, 4.2, 4.3, 4.4_
