package com.apiinabox.account.api;

import com.apiinabox.account.api.dto.AccountProto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/accounts")
public interface AccountAPI {
    @PostMapping
    ResponseEntity<AccountProto.Account> createAccount(@RequestBody AccountProto.Account accountProto);

    @GetMapping("/{id}")
    ResponseEntity<AccountProto.Account> getAccount(@PathVariable String id);

    @GetMapping
    ResponseEntity<AccountProto.AccountList> getAllAccounts();

    @PutMapping("/{id}")
    ResponseEntity<AccountProto.Account> updateAccount(
            @PathVariable String id,
            @RequestBody AccountProto.Account accountProto);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteAccount(@PathVariable String id);

    @PostMapping("/{id}/password")
    ResponseEntity<Void> setPassword(
            @PathVariable String id,
            @RequestBody AccountProto.SetPasswordRequest request);

    @PostMapping("/authenticate")
    ResponseEntity<AccountProto.AuthenticateResponse> authenticate(
            @RequestBody AccountProto.AuthenticateRequest request);
} 