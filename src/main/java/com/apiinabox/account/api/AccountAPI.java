package com.apiinabox.account.api;

import com.apiinabox.account.api.dto.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/accounts")
public interface AccountAPI {

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO account);

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable String id);

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts();

    @PutMapping("/{id}")
    public ResponseEntity<AccountDTO> updateAccount(
            @PathVariable String id,
            @RequestBody AccountDTO accountProto);

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String id);

    @PostMapping("/{id}/password")
    public ResponseEntity<Void> setPassword(
            @PathVariable String id,
            @RequestBody SetPasswordRequestDTO request);

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(
            @RequestBody AuthenticationRequestDTO request);
} 