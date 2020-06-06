package it.unicam.cs.pa.jbudget105129.controller;

import com.google.inject.Singleton;
import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import it.unicam.cs.pa.jbudget105129.model.*;
import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.Date;
import java.util.List;

//TODO javadoc
public interface LedgerManager {

    Ledger getLedger();

    void addTransaction(String description, Date date, List<Movement> movements, List<Tag> tags) throws AccountException;
    void removeTransaction(Transaction transaction) throws AccountException;

    void addAccount(String name, String description, double opening, AccountType type);
    void addAccount(String name, String description, double opening, AccountType type, double min, double max);
    void removeAccount(Account account) throws AccountException;

    void addScheduledTransaction(ScheduledTransaction scheduledTransaction);
    void removeScheduledTransaction(ScheduledTransaction scheduledTransaction);

    List<Transaction> getTransactions(String expression);
    List<Account> getAccounts(String expression);

    void schedule(Date date) throws AccountException;
    void schedule() throws AccountException;

    void loadLedger(String file) throws IOException;
    void saveLedger(String file) throws IOException;

    void setLogLevel();
}
