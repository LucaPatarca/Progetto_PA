package it.unicam.cs.pa.jbudget105129.controller;

import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import it.unicam.cs.pa.jbudget105129.model.*;
import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;

import java.util.Date;
import java.util.List;
//TODO javadoc
public interface LedgerManager {

    Ledger getLedger();

    Transaction addTransaction(String description, Date date, List<Movement> movements) throws AccountException;

    void removeTransaction(Transaction transaction) throws AccountException;

    Account addAccount(String name, String description, double opening, AccountType type);

    Account addAccount(String name, String description, double opening, AccountType type, double min, double max);

    void removeAccount(Account account) throws AccountException;

    void addScheduledTransaction(ScheduledTransaction scheduledTransaction);
    void removeScheduledTransaction(ScheduledTransaction scheduledTransaction);

    List<Transaction> getTransactions(String expression);
    void schedule(Date date) throws AccountException;
    void schedule() throws AccountException;
}
