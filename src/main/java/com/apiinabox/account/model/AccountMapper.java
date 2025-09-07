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
                .setId(account.id())
                .setUsername(account.username())
                .setEmail(account.email())
                .setFullName(account.fullName())
                .setCreatedAt(account.createdAt().format(DATE_TIME_FORMATTER))
                .setPasswordHash(account.passwordHash())
                .build();
    }

    public Account toModel(AccountProto.Account accountProto) {
        if (accountProto == null) {
            return null;
        }
        return new Account(
                accountProto.getId(),
                accountProto.getUsername(),
                accountProto.getEmail(),
                accountProto.getFullName(),
                LocalDateTime.parse(accountProto.getCreatedAt(), DATE_TIME_FORMATTER),
                accountProto.getPasswordHash());
    }
} 