package it.unicam.cs.pa.jbudget105129.model;

import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
//TODO javadoc
public class FamilyLedger implements Ledger {

    private final List<Transaction> transactions;
    private final List<ScheduledTransaction> scheduledTransactions;
    private final List<Account> accounts;

    public FamilyLedger(){
        transactions=new ArrayList<>();
        scheduledTransactions=new ArrayList<>();
        accounts=new ArrayList<>();
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
    }

    /**
     * This adds a new transaction to the list, it also adds all transaction's movements
     * to the linked account to maintain a consistent state of the ledger.
     * @param transaction the transaction to be added.
     * @throws IllegalStateException if the same exception is thrown by one of the updated account.
     */
    @Override
    public void addTransaction(Transaction transaction) throws IllegalStateException{
        transactions.add(transaction);
        updateAccount(transaction,m->m.getAccount().addMovement(m));
    }

    @Override
    public void addScheduledTransaction(ScheduledTransaction transaction) {
        scheduledTransactions.add(transaction);
    }

    /**
     * This removes a new transaction from the list, it also removes all transaction's movements
     * from the linked account to maintain a consistent state of the ledger.
     * @param transaction the transaction to be removed.
     * @throws IllegalStateException if the same exception is thrown by one of the updated account.
     */
    @Override
    public void removeTransaction(Transaction transaction) throws IllegalStateException{
        transactions.remove(transaction);
        updateAccount(transaction,m->m.getAccount().addMovement(m));
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
     * mark completed transactions as completed,
     * adds each movement to the linked account.
     * @param date the date at which this method stops scheduling
     */
    @Override
    public void schedule(Date date) throws IllegalStateException {
        for (ScheduledTransaction st : scheduledTransactions) {
            if (st.isCompleted())
                return;
            List<Transaction> list = st.getTransactions(date, false);
            list = list.parallelStream()
                    .filter(t -> !transactions.contains(t))
                    .collect(Collectors.toList());
            for (Transaction transaction : list) {
                updateAccount(transaction,m->m.getAccount().addMovement(m));
                st.markTransactionAsCompleted(transaction);
            }
        }
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

    private void updateAccount(Transaction transaction, Consumer<Movement> action) {
        for (Movement movement : transaction.getMovements()) {
            action.accept(movement);
        }
    }
}
