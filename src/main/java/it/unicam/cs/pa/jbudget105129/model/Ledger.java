package it.unicam.cs.pa.jbudget105129.model;

import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

/**
 * Represents a collection of {@link Account}, {@link Transaction} and {@link ScheduledTransaction},
 * it is responsible for managing basic operation between this 3 types of object.
 *
 * This is a model object so it is possible to listen it for changes, this kind of responsibility is delegated
 * to a field of type {@link PropertyChangeSupport} that can be obtained by its getter.
 *
 * It is listener responsibility to add itself to the listener list by getting the {@link PropertyChangeSupport}
 * and calling the method addListener.
 */
public interface Ledger {
    /**
     * Returns the list of {@link Transaction} this ledger contains.
     * @return the list of transactions.
     */
    List<Transaction> getTransactions();

    /**
     * Returns all the {@link Transaction} this ledger contains, filtered by a {@link Predicate<Transaction>}.
     * @return the filtered list of transactions.
     */
    List<Transaction> getTransactions(Predicate<Transaction> predicate);

    /**
     * Returns all the {@link ScheduledTransaction} this ledger contains.
     * @return the list of scheduled transactions
     */
    List<ScheduledTransaction> getScheduledTransactions();

    /**
     * Returns all the {@link Account} this ledger contains.
     * @return the list of accounts.
     */
    List<Account> getAccounts();

    /**
     * Adds an {@link Account} to this ledger and fires an event to all the listeners
     * on the {@link PropertyChangeSupport}.
     * @param account the account to add
     */
    void addAccount(Account account);

    /**
     * Adds a {@link Transaction} to this ledger and fires an event to all the listeners
     * on the {@link PropertyChangeSupport}. If one of the transaction's movement has an
     * {@link Account} that is not present inside the ledger the account is added to the
     * ledger. Each transaction's {@link Movement} is also added to the linked account to
     * update accounts balance.
     * @param transaction the transaction to add
     * @throws AccountException if one of the accounts refuses the transaction
     */
    void addTransaction(Transaction transaction) throws AccountException;

    /**
     * Adds a {@link ScheduledTransaction} to this ledger and fires an event to all the listeners
     * on the {@link PropertyChangeSupport}.
     * @param transaction the scheduled transaction to add
     */
    void addScheduledTransaction(ScheduledTransaction transaction);

    /**
     * Removes a {@link Transaction} from this ledger and fires an event to all the listeners
     * on the {@link PropertyChangeSupport}.
     * @param transaction the transaction to remove
     * @throws AccountException if it is not possible to remove the transaction
     */
    void removeTransaction(Transaction transaction) throws AccountException;

    /**
     * Removes a {@link ScheduledTransaction} from this ledger and fires an event to all the listeners
     * on the {@link PropertyChangeSupport}.
     * @param transaction the scheduled transaction to remove
     */
    void removeScheduledTransaction(ScheduledTransaction transaction) throws AccountException;

    /**
     * Removes a {@link Transaction} from this ledger and fires an event to all the listeners
     * on the {@link PropertyChangeSupport}.
     * @param account the account to remove
     */
    void removeAccount(Account account);

    /**
     * Schedules every {@link ScheduledTransaction} to a given date.
     * This means that every {@link Transaction} before this date is marked as completed and its {@link Movement}
     * are added to the linked account to update the balance.
     * @param date the date to be scheduled.
     * @throws AccountException if one of the account refuses the movement
     */
    void schedule(LocalDate date) throws AccountException;

    /**
     * Adds a new listener to the Ledger's list, each implementing class is responsible for choosing how
     * to manage the listener list. However this should be delegated to an external class
     * like {@link PropertyChangeSupport}
     * @param listener the new listener to add
     */
    void addListener(PropertyChangeListener listener);
}
