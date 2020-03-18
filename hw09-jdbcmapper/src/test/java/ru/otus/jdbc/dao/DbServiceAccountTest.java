package ru.otus.jdbc.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.DbServiceDemo;
import ru.otus.core.dao.AccountDao;
import ru.otus.core.model.Account;
import ru.otus.core.service.DBServiceAccount;
import ru.otus.core.service.DbServiceAccountImpl;
import ru.otus.h2.DataSourceH2;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DbServiceAccountTest {
  DataSource dataSource = new DataSourceH2();
  SessionManagerJdbc sessionManager = new SessionManagerJdbc(dataSource);
  DbServiceDemo demo = new DbServiceDemo();


  DbExecutor<Account> dbExecutor2 = new DbExecutor<>();
  AccountDao accountDao = new AccountDaoJdbc(sessionManager, dbExecutor2);
  DBServiceAccount dbServiceAccount = new DbServiceAccountImpl(accountDao);



  @BeforeEach
  void setUp() throws SQLException {
    demo.createTableAccount(dataSource);
  }

  @Test
  void testAccount(){
    Account account = new Account(1, "AccountAlex",new BigDecimal("4643643438"));
    long id = dbServiceAccount.saveAccount(account);
    Optional<Account> account1 = dbServiceAccount.getAccount(id);
    assertEquals(account1.get(), account);
  }

}