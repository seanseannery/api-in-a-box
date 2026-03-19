package com.apiinabox.account.model;

import com.apiinabox.account.api.dto.AccountProto;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

/** Maps between Account domain model and AccountProto protobuf representation. */
@Component
public class AccountMapper {
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

  /** Converts an Account domain model to its protobuf representation. */
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

  /** Converts an AccountProto protobuf message to an Account domain model. */
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
