package com.apiinabox.account.controller;

import com.apiinabox.account.api.AccountAPI;
import com.apiinabox.account.api.dto.AccountProto;
import com.apiinabox.account.model.Account;
import com.apiinabox.account.model.AccountMapper;
import com.apiinabox.account.repository.AccountRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountController implements AccountAPI {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordService passwordService;

    public AccountController(AccountRepository accountRepository, 
                           AccountMapper accountMapper,
                           PasswordService passwordService) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.passwordService = passwordService;
    }

    @Override
    public ResponseEntity<AccountProto.Account> createAccount(AccountProto.Account accountProto) {
        Account account = accountMapper.toModel(accountProto);
        Account savedAccount = accountRepository.save(account);
        return ResponseEntity.ok(accountMapper.toProto(savedAccount));
    }

    @Override
    public ResponseEntity<AccountProto.Account> getAccount(String id) {
        Account account = accountRepository.findById(id);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(accountMapper.toProto(account));
    }

    @Override
    public ResponseEntity<AccountProto.AccountList> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        AccountProto.AccountList.Builder builder = AccountProto.AccountList.newBuilder();
        accounts.stream()
                .map(accountMapper::toProto)
                .forEach(builder::addAccounts);
        return ResponseEntity.ok(builder.build());
    }

    @Override
    public ResponseEntity<AccountProto.Account> updateAccount(String id, AccountProto.Account accountProto) {
        Account account = accountMapper.toModel(accountProto);
        account.setId(id);
        Account updatedAccount = accountRepository.update(account);
        if (updatedAccount == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(accountMapper.toProto(updatedAccount));
    }

    @Override
    public ResponseEntity<Void> deleteAccount(String id) {
        accountRepository.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> setPassword(String id, AccountProto.SetPasswordRequest request) {
        Account account = accountRepository.findById(id);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }

        String hashedPassword = passwordService.hashPassword(request.getPassword());
        account.setPasswordHash(hashedPassword);
        accountRepository.update(account);
        
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<AccountProto.AuthenticateResponse> authenticate(AccountProto.AuthenticateRequest request) {
        List<Account> accounts = accountRepository.findAll();
        Account account = accounts.stream()
                .filter(a -> a.getUsername().equals(request.getUsername()))
                .findFirst()
                .orElse(null);

        if (account == null) {
            return ResponseEntity.ok(AccountProto.AuthenticateResponse.newBuilder()
                    .setAuthenticated(false)
                    .build());
        }

        boolean authenticated = passwordService.verifyPassword(request.getPassword(), account.getPasswordHash());
        
        return ResponseEntity.ok(AccountProto.AuthenticateResponse.newBuilder()
                .setAuthenticated(authenticated)
                .setAccountId(authenticated ? account.getId() : "")
                .build());
    }
} 