package com.apiinabox.account.repository;

import com.apiinabox.account.model.Account;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class MockAccountRepository implements AccountRepository {
    private final List<Account> accounts = new ArrayList<>();

    public MockAccountRepository() {
        // Initialize with some mock data
        accounts.add(Account.builder()
                .id(UUID.randomUUID().toString())
                .username("john_doe")
                .email("john@example.com")
                .fullName("John Doe")
                .createdAt(LocalDateTime.now())
                .passwordHash("hashed_password1")
                .build());
        accounts.add(Account.builder()
                .id(UUID.randomUUID().toString())
                .username("jane_doe")
                .email("jane.doe@example.com")
                .fullName("Jane Doe")
                .createdAt(LocalDateTime.now())
                .passwordHash("hashed_password2")
                .build());
    }

    @Override
    public Account save(Account account) {
        if (account.id() == null) {
            // Create a new account with generated ID and timestamp since records are immutable
            Account newAccount = new Account(
                UUID.randomUUID().toString(),
                account.username(),
                account.email(),
                account.fullName(),
                LocalDateTime.now(),
                account.passwordHash()
            );
            accounts.add(newAccount);
            return newAccount;
        } else {
            return update(account);
        }
    }

    @Override
    public Account findById(String id) {
        return accounts.stream()
                .filter(account -> account.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(accounts);
    }

    @Override
    public Account update(Account account) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).id().equals(account.id())) {
                accounts.set(i, account);
                return account;
            }
        }
        return null;
    }

    @Override
    public void delete(String id) {
        accounts.removeIf(account -> account.id().equals(id));
    }
} 