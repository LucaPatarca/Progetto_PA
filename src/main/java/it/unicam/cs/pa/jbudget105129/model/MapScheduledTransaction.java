package it.unicam.cs.pa.jbudget105129.model;

import java.util.*;
import java.util.stream.Collectors;
//TODO javadoc
public class MapScheduledTransaction implements ScheduledTransaction {

    private String description;
    private final Map<Transaction,Boolean> transactions;

    public MapScheduledTransaction(String description, List<Transaction> transactions){
        this.description=description;
        this.transactions=new IdentityHashMap<>();
        transactions.forEach(transaction -> this.transactions.put(transaction,false));
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<Transaction> getTransactions() {
        return List.copyOf(transactions.keySet());
    }

    @Override
    public List<Transaction> getTransactions(Date date,boolean includeCompleted) {
        if (includeCompleted)
            return transactions.keySet().stream()
                .filter(t->t.getDate().before(date))
                .collect(Collectors.toList());
        else
            return transactions.keySet().stream()
                    .filter(t->t.getDate().before(date))
                    .filter(t -> !isCompleted(t))
                    .collect(Collectors.toList());
    }

    @Override
    public void markTransactionAsCompleted(Transaction transaction) {
        transactions.put(transaction,true);
    }

    @Override
    public boolean isCompleted(Transaction transaction) {
        Boolean toReturn=transactions.get(transaction);
        if(toReturn==null){
            return false;
        }
        return toReturn;
    }

    @Override
    public boolean isCompleted() {
        return transactions.values().stream().allMatch(b->b);
    }

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
