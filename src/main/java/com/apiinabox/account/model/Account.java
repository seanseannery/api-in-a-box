package com.apiinabox.account.model;

import java.time.LocalDateTime;
import lombok.Builder;

/** Domain model representing a user account. */
@Builder
public record Account(
    String id,
    String username,
    String email,
    String fullName,
    LocalDateTime createdAt,
    String passwordHash) {}
