package com.apiinabox.account.model;

import com.apiinabox.account.api.dto.AccountProto;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class AccountMapper {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    public AccountProto.Account toProto(Account account) {
        return AccountProto.Account.newBuilder()
                .setId(account.getId())
                .setUsername(account.getUsername())
                .setEmail(account.getEmail())
                .setFullName(account.getFullName())
                .setCreatedAt(account.getCreatedAt().format(DATE_TIME_FORMATTER))
                .build();
    }

    public Account toModel(AccountProto.Account accountProto) {
        return Account.builder()
                .id(accountProto.getId())
                .username(accountProto.getUsername())
                .email(accountProto.getEmail())
                .fullName(accountProto.getFullName())
                .createdAt(LocalDateTime.parse(accountProto.getCreatedAt(), DATE_TIME_FORMATTER))
                .build();
    }
} 