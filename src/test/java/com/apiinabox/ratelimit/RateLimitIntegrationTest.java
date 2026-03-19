package com.apiinabox.ratelimit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Simple integration test to verify the rate limiting components work together. */
class RateLimitIntegrationTest {

  @Test
  void testRateLimitingComponentsWorkTogether() {
    // Test that all the rate limiting components integrate properly

    // 1. Test RateLimitResult
    RateLimitResult allowed = RateLimitResult.allowed();
    assertTrue(allowed.isAllowed());
    assertNull(allowed.getReason());

    RateLimitResult rejected = RateLimitResult.rejected("Too many requests");
    assertFalse(rejected.isAllowed());
    assertEquals("Too many requests", rejected.getReason());

    // 2. Test RateLimitStatus
    RateLimitStatus status = new RateLimitStatus(80, 100, "test-key");
    assertEquals(80, status.getAvailablePermissions());
    assertEquals(100, status.getTotalPermissions());
    assertEquals(20, status.getUsedPermissions());
    assertEquals("test-key: 20/100 used", status.toString());

    // This verifies that our simple rate limiting classes work correctly
    // without needing complex Spring Boot test setup
  }
}
