package it.unicam.cs.pa.jbudget105129.model;

import java.util.Date;
import java.util.List;
//TODO javadoc
public interface ScheduledTransaction {
    String getDescription();
    List<Transaction> getTransactions();
    List<Transaction> getTransactions(Date date, boolean includeCompleted);
    void markTransactionAsCompleted(Transaction transaction);
    boolean isCompleted(Transaction transaction);
    boolean isCompleted();
    void setDescription(String description);
}
