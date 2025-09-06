package com.apiinabox.account.model;

import com.apiinabox.account.api.dto.AccountDTO;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class AccountMapper {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    public AccountDTO toDTO(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .username(account.getUsername())
                .email(account.getEmail())
                .fullName(account.getFullName())
                .createdAt(account.getCreatedAt().format(DATE_TIME_FORMATTER))
                .passwordHash(account.getPasswordHash())
                .build();
    }

    public Account toModel(AccountDTO accountDTO) {
        return Account.builder()
                .id(accountDTO.getId())
                .username(accountDTO.getUsername())
                .email(accountDTO.getEmail())
                .fullName(accountDTO.getFullName())
                .createdAt(LocalDateTime.parse(accountDTO.getCreatedAt(), DATE_TIME_FORMATTER))
                .build();
    }
} 