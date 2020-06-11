package it.unicam.cs.pa.jbudget105129.model;

import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A collection of {@link Account}, {@link Transaction} and {@link ScheduledTransaction} representing
 * a family ledger. Provides method to manage this three type of object which are stored in three separate
 * lists.
 *
 * This class has the responsibility to guarantee that every information it contains make sense with the others,
 * for instance it makes sure that if you add a {@link Transaction} each {@link Account} considered by the transaction receives
 * all its {@link Movement}.
 *
 * This is a model object so it is possible to listen it for changes, this kind of responsibility is delegated
 * to a field of type {@link PropertyChangeSupport} that can be obtained by its getter.
 *
 * It is listener responsibility to add itself to the listener list by getting the {@link PropertyChangeSupport}
 * and calling the method addListener.
 */
public class FamilyLedger implements Ledger {

    private final List<Transaction> transactions;
    private final List<ScheduledTransaction> scheduledTransactions;
    private final List<Account> accounts;
    private final PropertyChangeSupport pcs;

    /**
     * Creates a new instance of this class, each list is initialized with an empty {@link LinkedList}.
     */
    public FamilyLedger(){
        transactions= new LinkedList<>();
        scheduledTransactions= new LinkedList<>();
        accounts= new LinkedList<>();
        pcs = new PropertyChangeSupport(this);
    }

    /**
     * Returns the list of {@link Transaction} contained by this ledger
     * @return the list of {@link Transaction}
     */
    @Override
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Return the list of {@link Transaction} contained by this ledger filtered using the specified {@link Predicate<Transaction>}
     * @param predicate the predicate used to filter the list
     * @return the list of all {@link Transaction} that matches the predicate
     */
    @Override
    public List<Transaction> getTransactions(Predicate<Transaction> predicate) {
        return transactions.stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * Returns all the {@link ScheduledTransaction} this ledger contains.
     * @return the list of scheduled transactions
     */
    @Override
    public List<ScheduledTransaction> getScheduledTransactions() {
        return scheduledTransactions;
    }

    /**
     * Returns all the {@link Account} this ledger contains.
     * @return the list of accounts.
     */
    @Override
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * Adds an {@link Account} to this ledger and fires an event to all the listeners
     * on the {@link PropertyChangeSupport}.
     * @param account the account to add
     */
    @Override
    public void addAccount(Account account) {
        accounts.add(account);
        pcs.firePropertyChange("accounts",accounts,account);
    }

    /**
     * Adds a {@link Transaction} to this ledger and fires an event to all the listeners
     * on the {@link PropertyChangeSupport}. If one of the transaction's movement has an
     * {@link Account} that is not present inside the ledger the account is added to the
     * ledger. Each transaction's {@link Movement} is also added to the linked account to
     * update accounts balance.
     * @param transaction the transaction to add
     * @throws AccountException if one of the accounts refuses the transaction
     */
    @Override
    public void addTransaction(Transaction transaction) throws AccountException{
        transactions.add(transaction);
        updateAccountAdding(transaction);
        pcs.firePropertyChange("transactions",transactions,transaction);
    }

    /**
     * Adds a {@link ScheduledTransaction} to this ledger and fires an event to all the listeners
     * on the {@link PropertyChangeSupport}.
     * @param transaction the scheduled transaction to add
     */
    @Override
    public void addScheduledTransaction(ScheduledTransaction transaction) {
        scheduledTransactions.add(transaction);
        pcs.firePropertyChange("scheduledTransactions",scheduledTransactions,transaction);
    }

    /**
     * Removes a {@link Transaction} from this ledger and fires an event to all the listeners
     * on the {@link PropertyChangeSupport}. It also removes all transaction's movements
     * from the linked account to maintain a consistent state of the ledger.
     * @param transaction the transaction to be removed.
     * @throws AccountException if it is not possible to remove the transaction
     */
    @Override
    public void removeTransaction(Transaction transaction) throws AccountException{
        transactions.remove(transaction);
        updateAccountRemoving(transaction);
        pcs.firePropertyChange("transactions",transaction,transactions);
    }

    /**
     * Removes a {@link ScheduledTransaction} from this ledger and fires an event to all the listeners
     * on the {@link PropertyChangeSupport}.
     * @param st the scheduled st to remove
     */
    @Override
    public void removeScheduledTransaction(ScheduledTransaction st) throws AccountException {
        List<Transaction> alreadyCompleted = st.getTransactions().stream().filter(st::isCompleted).collect(Collectors.toList());
        for (Transaction transaction : alreadyCompleted){
            updateAccountRemoving(transaction);
        }
        scheduledTransactions.remove(st);
        pcs.firePropertyChange("scheduledTransactions",st,scheduledTransactions);
    }

    /**
     * Removes a {@link Transaction} from this ledger and fires an event to all the listeners
     * on the {@link PropertyChangeSupport}.
     * @param account the account to remove
     */
    @Override
    public void removeAccount(Account account) {
        accounts.remove(account);
        pcs.firePropertyChange("accounts",account,accounts);
    }

    /**
     * Schedules every {@link ScheduledTransaction} to a given date.
     * This means that every {@link Transaction} before this date is marked as completed and its {@link Movement}
     * are added to the linked account to update the balance.
     * @param date the date to be scheduled.
     * @throws AccountException if one of the account refuses the movement
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

    /**
     * Returns the inner {@link PropertyChangeSupport} used to notify listener objects.
     * To listen for this ledger an object needs to implement {@link java.beans.PropertyChangeListener}
     * interface and than add itself to the listener of this return object.
     * @return the {@link PropertyChangeSupport} object
     */
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
