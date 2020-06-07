package it.unicam.cs.pa.jbudget105129.controller;

import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import it.unicam.cs.pa.jbudget105129.model.*;
import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import it.unicam.cs.pa.jbudget105129.persistence.PersistenceManager;

import java.io.IOException;
import java.util.Date;
import java.util.List;

//TODO javadoc

/**
 * Represents an object to manage operations upon a {@link Ledger}.
 */
public interface LedgerManager {

    /**
     * Returns the inner {@link Ledger} to retrieve information on the current state of the ledger.
     * @return the inner {@link Ledger}.
     */
    Ledger getLedger();

    /**
     * Adds a new {@link Transaction} to the ledger's list. The transaction instance is created inside the method
     * using the given parameter and than added to the ledger. A check on the parameters is also
     * performed to make sure they match the requirement of the new instance of {@link Transaction}.
     * @param description the transaction's description
     * @param date the transaction's date
     * @param movements the transaction's {@link Movement} list
     * @param tags the transaction's {@link Tag} list
     * @throws AccountException if is not possible to add the new transaction to the ledger
     * @see Ledger
     */
    void addTransaction(String description, Date date, List<Movement> movements, List<Tag> tags) throws AccountException;

    /**
     * Removes a given {@link Transaction} from the {@link Ledger} list.
     * @param transaction the transaction to be removed
     * @throws AccountException if it is not possible to remove the transaction from the ledger
     */
    void removeTransaction(Transaction transaction) throws AccountException;

    /**
     * Adds a new {@link Account} to the ledger's list. The account instance is created inside the method
     * using the given parameter and than added to the ledger. A check on the parameters is also
     * performed to make sure they match the requirement of the new instance of {@link Account}.
     * @param name the account's name
     * @param description the account's description
     * @param referent the account's referent (can be null)
     * @param opening the account's opening balance
     * @param type the account's type
     */
    void addAccount(String name, String description,String referent, double opening, AccountType type);

    /**
     * Adds a new {@link Account} to the ledger's list. The account instance is created inside the method
     * using the given parameter and than added to the ledger. A check on the parameters is also
     * performed to make sure they match the requirement of the new instance of {@link Account}.
     * @param name the account's name
     * @param description the account's description
     * @param referent the account's referent (can be null)
     * @param opening the account's opening balance
     * @param type the account's type
     * @param min the account's minimum value
     * @param max the account's maximum value
     */
    void addAccount(String name, String description,String referent, double opening, AccountType type, double min, double max);

    /**
     * Removes an {@link Account} from the ledger's list. Before removing it checks that the account is not
     * used by any {@link Transaction} or {@link ScheduledTransaction}.
     * @param account the account to be removed
     * @throws AccountException if the account is used in the ledger.
     */
    void removeAccount(Account account) throws AccountException;

    /**
     * Adds a new {@link ScheduledTransaction} to the ledger's list. It checks that the {@link ScheduledTransaction}
     * is not already completed before adding it.
     * @param scheduledTransaction the scheduled transaction to be added
     */
    void addScheduledTransaction(ScheduledTransaction scheduledTransaction);

    /**
     * Removes a {@link ScheduledTransaction} from the ledger.
     * @param scheduledTransaction the scheduled transaction to be removed
     */
    void removeScheduledTransaction(ScheduledTransaction scheduledTransaction);

    /**
     * Returns a list of {@link Transaction} matching a given expression from the ledger's list.
     * @param expression the string used to find matching transactions
     * @return the resulting list of {@link Transaction}
     */
    List<Transaction> getTransactions(String expression);

    /**
     * Returns a list of {@link Account} matching a given expression from the ledger's list.
     * @param expression the string used to find matching accounts
     * @return the resulting list of {@link Account}
     */
    List<Account> getAccounts(String expression);

    /**
     * Schedules the ledger to a date
     * @param date the date to be scheduled
     * @see Ledger
     */
    void schedule(Date date) throws AccountException;

    /**
     * Schedules the ledger to the current date
     * @see Ledger
     */
    void schedule() throws AccountException;

    /**
     * Loads a {@link Ledger} from a file using the inner {@link PersistenceManager}. the loaded ledger is set as the
     * current ledger, every data inside the old ledger will be lost.
     * @param file the string representing the source of the new ledger
     * @see PersistenceManager
     */
    void loadLedger(String file) throws IOException;

    /**
     * Saves the current {@link Ledger} to a file using the inner {@link PersistenceManager}.
     * @param file the string representing the output to save the ledger
     * @see PersistenceManager
     */
    void saveLedger(String file) throws IOException;

    void setLogLevel();
}
