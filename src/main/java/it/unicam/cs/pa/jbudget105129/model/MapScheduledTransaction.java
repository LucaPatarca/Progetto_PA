package it.unicam.cs.pa.jbudget105129.model;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represent a set of programmed {@link Transaction}. This object is based on a {@link Map} that is
 * used to keep track of completed transactions.
 */
public class MapScheduledTransaction implements ScheduledTransaction {

    private String description;
    private final Map<Transaction,Boolean> transactions;

    /**
     * Creates a new {@link MapScheduledTransaction} with the specified parameters.
     * @param description the new scheduled transaction's description
     * @param transactions the new scheduled transaction's transaction list
     */
    public MapScheduledTransaction(String description, List<Transaction> transactions){
        this.description=description;
        this.transactions=new IdentityHashMap<>();
        transactions.forEach(transaction -> this.transactions.put(transaction,false));
    }

    /**
     * Returns the description of this scheduled transaction
     * @return the description
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Returns all the transactions contained in this scheduled transaction object
     * @return the list of all transactions
     */
    @Override
    public List<Transaction> getTransactions() {
        return List.copyOf(transactions.keySet());
    }

    /**
     * Returns all the transactions before the given date. If the includeCompleted parameter is true
     * all completed transactions are included otherwise this method returns only not completed transactions.
     * @param date the date
     * @param includeCompleted if this value is true all completed transactions are included in the result
     * @return the list of {@link Transaction}
     */
    @Override
    public List<Transaction> getTransactions(LocalDate date, boolean includeCompleted) {
        if (includeCompleted)
            return transactions.keySet().stream()
                .filter(t->t.getDate().isBefore(date))
                .collect(Collectors.toList());
        else
            return transactions.keySet().stream()
                    .filter(t->t.getDate().isBefore(date))
                    .filter(t -> !isCompleted(t))
                    .collect(Collectors.toList());
    }

    /**
     * Set the transaction as completed. This can't be undone.
     * @param transaction the transaction to mark as completed
     */
    @Override
    public void markTransactionAsCompleted(Transaction transaction) {
        transactions.put(transaction,true);
    }

    /**
     * Tests if a given transaction is marked as completed.
     * @param transaction the transaction to test
     * @return true if the transaction is completed, false instead.
     */
    @Override
    public boolean isCompleted(Transaction transaction) {
        Boolean toReturn=transactions.get(transaction);
        if(toReturn==null){
            return false;
        }
        return toReturn;
    }

    /**
     * Tests if the entire set of {@link Transaction} this object contains is completed.
     * @return true if all the transactions inside this object are completed, false instead.
     */
    @Override
    public boolean isCompleted() {
        return transactions.values().stream().allMatch(b->b);
    }

    /**
     * Changes the description of this scheduled transaction.
     * @param description the new description
     */
    @Override
    public void setDescription(String description) {
        this.description=description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapScheduledTransaction)) return false;
        MapScheduledTransaction that = (MapScheduledTransaction) o;
        return getDescription().equals(that.getDescription()) &&
                transactions.equals(that.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription(), transactions);
    }

    @Override
    public String toString() {
        return "MapScheduledTransaction{" +
                "description='" + description + '\'' +
                ", transactions=" + transactions +
                '}';
    }
}
