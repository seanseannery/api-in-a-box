package com.apiinabox.account.controller;

import com.apiinabox.account.api.AccountAPI;
import com.apiinabox.account.api.dto.*;
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
    public ResponseEntity<AccountDTO> createAccount(AccountDTO accountProto) {
        Account account = accountMapper.toModel(accountProto);
        Account savedAccount = accountRepository.save(account);
        return ResponseEntity.ok(accountMapper.toDTO(savedAccount));
    }

    @Override
    public ResponseEntity<AccountDTO> getAccount(String id) {
        Account account = accountRepository.findById(id);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(accountMapper.toDTO(account));
    }

    @Override
    public ResponseEntity< List<AccountDTO> > getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        List<AccountDTO> accountDTOs = accounts.stream()
                .map(accountMapper::toDTO)
                .toList();
        return ResponseEntity.ok(accountDTOs);
    }

    @Override
    public ResponseEntity<AccountDTO> updateAccount(String id, AccountDTO accountProto) {
        Account account = accountMapper.toModel(accountProto);
        account.setId(id);
        Account updatedAccount = accountRepository.update(account);
        if (updatedAccount == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(accountMapper.toDTO(updatedAccount));
    }

    @Override
    public ResponseEntity<Void> deleteAccount(String id) {
        accountRepository.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> setPassword(String id, SetPasswordRequestDTO request) {
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
    public ResponseEntity<AuthenticationResponseDTO> authenticate(AuthenticationRequestDTO request) {
        List<Account> accounts = accountRepository.findAll();
        Account account = accounts.stream()
                .filter(a -> a.getUsername().equals(request.getUserName()))
                .findFirst()
                .orElse(null);

        if (account == null) {
            return ResponseEntity.ok(AuthenticationResponseDTO.builder()
                    .authenticated(false)
                    .build());
        }

        boolean authenticated = passwordService.verifyPassword(request.getPassword(), account.getPasswordHash());
        
        return ResponseEntity.ok(AuthenticationResponseDTO.builder()
                .authenticated(authenticated)
                .accountId(authenticated ? account.getId() : "")
                .build());
    }
} 