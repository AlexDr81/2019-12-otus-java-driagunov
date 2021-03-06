package ru.otus.core.dao;

import ru.otus.core.model.Account;
import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface AccountDao {
  Optional<Account> findById(long id);

  long saveAccount(Account account);

  void updateAccount(Account account);

  SessionManager getSessionManager();
}
