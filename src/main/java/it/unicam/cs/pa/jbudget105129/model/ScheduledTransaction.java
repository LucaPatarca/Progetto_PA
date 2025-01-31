package it.unicam.cs.pa.jbudget105129.model;

import java.time.LocalDate;
import java.util.List;
/**
 * Represent a set of programmed {@link Transaction}. This object must have a way to distinguish completed
 * transactions from not completed ones.
 */
public interface ScheduledTransaction {
    /**
     * Returns the description of this scheduled transaction.
     * @return the description
     */
    String getDescription();

    /**
     * Returns all the transactions contained in this scheduled transaction object
     * @return the list of all transactions
     */
    List<Transaction> getTransactions();

    /**
     * Returns all the transactions before the given date.  If the includeCompleted parameter is true
     * all completed transactions are included otherwise this method returns only not completed transactions.
     * @param date the date
     * @param includeCompleted if this value is true all completed transactions are included in the result
     * @return the list of {@link Transaction}
     */
    List<Transaction> getTransactions(LocalDate date, boolean includeCompleted);

    /**
     * Set the transaction as completed. This can't be undone.
     * @param transaction the transaction to mark as completed
     */
    void markTransactionAsCompleted(Transaction transaction);

    /**
     * Tests if a given transaction is marked as completed.
     * @param transaction the transaction to test
     * @return true if the transaction is completed, false instead.
     */
    boolean isCompleted(Transaction transaction);

    /**
     * Tests if the entire set of {@link Transaction} this object contains is completed.
     * @return true if all the transactions inside this object are completed, false instead.
     */
    boolean isCompleted();

    /**
     * Changes the description of this scheduled transaction.
     * @param description the new description
     */
    void setDescription(String description);
}
