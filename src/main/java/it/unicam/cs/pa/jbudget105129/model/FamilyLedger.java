package it.unicam.cs.pa.jbudget105129.model;

import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
//TODO javadoc
public class FamilyLedger implements Ledger {

    private final List<Transaction> transactions;
    private final List<ScheduledTransaction> scheduledTransactions;
    private final List<Account> accounts;
    private final PropertyChangeSupport pcs;

    public FamilyLedger(){
        transactions= new LinkedList<>();
        scheduledTransactions= new LinkedList<>();
        accounts= new LinkedList<>();
        pcs = new PropertyChangeSupport(this);
    }

    @Override
    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public List<Transaction> getTransactions(Predicate<Transaction> predicate) {
        return transactions.stream().filter(predicate).collect(Collectors.toList());
    }

    @Override
    public List<ScheduledTransaction> getScheduledTransactions() {
        return scheduledTransactions;
    }

    @Override
    public List<Account> getAccounts() {
        return accounts;
    }

    @Override
    public void addAccount(Account account) {
        accounts.add(account);
        pcs.firePropertyChange("accounts",accounts,account);
    }

    /**
     * This adds a new transaction to the list, it also adds all transaction's movements
     * to the linked account to maintain a consistent state of the ledger.
     * @param transaction the transaction to be added.
     * @throws AccountException if the same exception is thrown by one of the updated account.
     */
    @Override
    public void addTransaction(Transaction transaction) throws AccountException{
        transactions.add(transaction);
        updateAccountAdding(transaction);
        pcs.firePropertyChange("transactions",transactions,transaction);
    }

    @Override
    public void addScheduledTransaction(ScheduledTransaction transaction) {
        scheduledTransactions.add(transaction);
    }

    /**
     * This removes a transaction from the list, it also removes all transaction's movements
     * from the linked account to maintain a consistent state of the ledger.
     * @param transaction the transaction to be removed.
     * @throws AccountException if the same exception is thrown by one of the updated account.
     */
    @Override
    public void removeTransaction(Transaction transaction) throws AccountException{
        transactions.remove(transaction);
        updateAccountRemoving(transaction);
    }

    @Override
    public void removeScheduledTransaction(ScheduledTransaction transaction) {
        scheduledTransactions.remove(transaction);
    }

    @Override
    public void removeAccount(Account account) {
        accounts.remove(account);
    }

    /**
     * mark completed transactions as completed, adds each movement to the linked account.
     * @param date the date to be scheduled.
     */
    @Override
    public void schedule(Date date) throws AccountException {
        for (ScheduledTransaction st : scheduledTransactions) {
            if (st.isCompleted())
                return;
            List<Transaction> list = st.getTransactions(date, false);
            list = list.parallelStream()
                    .filter(t -> !transactions.contains(t))
                    .collect(Collectors.toList());
            for (Transaction transaction : list) {
                updateAccountAdding(transaction);
                st.markTransactionAsCompleted(transaction);
            }
        }
    }

    @Override
    public PropertyChangeSupport getPropertyChangeSupport() {
        return pcs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FamilyLedger)) return false;
        FamilyLedger that = (FamilyLedger) o;
        return getTransactions().equals(that.getTransactions()) &&
                getScheduledTransactions().equals(that.getScheduledTransactions()) &&
                getAccounts().equals(that.getAccounts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTransactions(), getScheduledTransactions(), getAccounts());
    }

    @Override
    public String toString() {
        return "FamilyLedger{" +
                "transactions=" + transactions +
                ", scheduledTransactions=" + scheduledTransactions +
                ", accounts=" + accounts +
                '}';
    }

    private void updateAccountAdding(Transaction transaction) throws AccountException {
        for (Movement movement : transaction.getMovements()) {
            if(!accounts.contains(movement.getAccount()))
                accounts.add(movement.getAccount());
            movement.getAccount().addMovement(movement);
        }
    }

    private void updateAccountRemoving(Transaction transaction) throws AccountException {
        for (Movement movement : transaction.getMovements()) {
            movement.getAccount().removeMovement(movement);
        }
    }
}
