package com.apiinabox.account.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private String id;
    private String username;
    private String email;
    private String fullName;
    private LocalDateTime createdAt;
    private String passwordHash;
} 