package it.unicam.cs.pa.jbudget105129.controller;

import it.unicam.cs.pa.jbudget105129.model.ScheduledTransaction;
import it.unicam.cs.pa.jbudget105129.model.Transaction;
import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import it.unicam.cs.pa.jbudget105129.model.Account;
import it.unicam.cs.pa.jbudget105129.model.Ledger;

import java.util.Date;
import java.util.List;
//TODO javadoc
public interface LedgerManager {

    Ledger getLedger();

    void addTransaction(Transaction transaction) throws AccountException;
    void removeTransaction(Transaction transaction) throws AccountException;

    void addAccount(Account account);
    void removeAccount(Account account) throws AccountException;

    void addScheduledTransaction(ScheduledTransaction scheduledTransaction);
    void removeScheduledTransaction(ScheduledTransaction scheduledTransaction);

    List<Transaction> getTransactions(String expression);
    void schedule(Date date) throws AccountException;
    void scheduleNow() throws AccountException;
}
