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
                .build());
    }

    @Override
    public Account save(Account account) {
        if (account.getId() == null) {
            account.setId(UUID.randomUUID().toString());
            account.setCreatedAt(LocalDateTime.now());
            accounts.add(account);
        } else {
            update(account);
        }
        return account;
    }

    @Override
    public Account findById(String id) {
        return accounts.stream()
                .filter(account -> account.getId().equals(id))
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
            if (accounts.get(i).getId().equals(account.getId())) {
                accounts.set(i, account);
                return account;
            }
        }
        return null;
    }

    @Override
    public void delete(String id) {
        accounts.removeIf(account -> account.getId().equals(id));
    }
} 