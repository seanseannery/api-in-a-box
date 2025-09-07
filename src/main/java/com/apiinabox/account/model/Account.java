package com.apiinabox.account.model;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record Account (String id, String username, String email, String fullName, LocalDateTime createdAt, String passwordHash) { }
