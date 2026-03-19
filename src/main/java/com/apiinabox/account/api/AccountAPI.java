package com.apiinabox.account.api;

import com.apiinabox.account.api.dto.AccountProto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/** REST API contract for account CRUD and authentication operations. */
@RequestMapping("/api/accounts")
public interface AccountApi {
  @PostMapping
  ResponseEntity<AccountProto.Account> createAccount(
      @RequestBody AccountProto.Account accountProto);

  @GetMapping("/{id}")
  ResponseEntity<AccountProto.Account> getAccount(@PathVariable String id);

  @GetMapping
  ResponseEntity<AccountProto.AccountList> getAllAccounts();

  @PutMapping("/{id}")
  ResponseEntity<AccountProto.Account> updateAccount(
      @PathVariable String id, @RequestBody AccountProto.Account accountProto);

  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteAccount(@PathVariable String id);

  @PostMapping("/{id}/password")
  ResponseEntity<Void> setPassword(
      @PathVariable String id, @RequestBody AccountProto.SetPasswordRequest request);

  @PostMapping("/authenticate")
  ResponseEntity<AccountProto.AuthenticateResponse> authenticate(
      @RequestBody AccountProto.AuthenticateRequest request);
}
