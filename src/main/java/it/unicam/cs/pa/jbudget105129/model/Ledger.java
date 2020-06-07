package it.unicam.cs.pa.jbudget105129.model;

import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import java.beans.PropertyChangeSupport;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
//TODO javadoc

/**
 * Represents
 */
public interface Ledger {
    List<Transaction> getTransactions();
    List<Transaction> getTransactions(Predicate<Transaction> predicate);
    List<ScheduledTransaction> getScheduledTransactions();
    List<Account> getAccounts();

    void addAccount(Account account);
    void addTransaction(Transaction transaction) throws AccountException;
    void addScheduledTransaction(ScheduledTransaction transaction);

    void removeTransaction(Transaction transaction) throws AccountException;
    void removeScheduledTransaction(ScheduledTransaction transaction);
    void removeAccount(Account account);

    void schedule(Date date) throws AccountException;
    PropertyChangeSupport getPropertyChangeSupport();
}
