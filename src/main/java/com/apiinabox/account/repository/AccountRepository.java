package com.apiinabox.account.repository;

import com.apiinabox.account.model.Account;
import java.util.List;

/** Data access interface for Account persistence operations. */
public interface AccountRepository {
  Account save(Account account);

  Account findById(String id);

  List<Account> findAll();

  Account update(Account account);

  void delete(String id);
}
