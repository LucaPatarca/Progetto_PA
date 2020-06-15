package it.unicam.cs.pa.jbudget105129.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import it.unicam.cs.pa.jbudget105129.annotations.AppLedger;
import it.unicam.cs.pa.jbudget105129.annotations.AppPersistence;
import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import it.unicam.cs.pa.jbudget105129.model.*;
import it.unicam.cs.pa.jbudget105129.persistence.PersistenceManager;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A singleton {@link LedgerManager}.
 * @see Ledger
 */
@Singleton
public class FamilyLedgerManager implements LedgerManager {
    private Ledger ledger;
    private final PersistenceManager persistenceManager;

    /**
     * Creates a new {@link FamilyLedgerManager} with the specified parameters. This constructor
     * can be injected using {@link LedgerManagerModule}.
     * @param ledger the ledger this will manage
     * @param persistenceManager the inner manager responsible for persistence
     * @see Ledger
     * @see PersistenceManager
     */
    @Inject
    protected FamilyLedgerManager(@AppLedger Ledger ledger, @AppPersistence PersistenceManager persistenceManager){
        this.ledger=ledger;
        this.persistenceManager=persistenceManager;
    }

    /**
     * Returns the inner {@link Ledger} this manager contains. Can be used to retrieve information on the current
     * state of the ledger.
     * @return the inner {@link Ledger}.
     */
    @Override
    public Ledger getLedger() {
        return ledger;
    }

    /**
     * Creates a new instance of {@link RoundedTransaction} and adds it to the ledger. Before adding the transaction
     * to the ledger every movement is checked to verify that it has an account, if this condition is not
     * true an {@link IllegalArgumentException} will be thrown. Finally the transaction is added to the ledger
     * which is responsible for adding each movement to the linked account. If ledger throws an AccountException,
     * meaning that one or more account refused the transaction, the entire transaction will be removed
     * from the ledger and the exception is re-thrown to be fully managed by the view.
     * @param description the description of the transaction
     * @param date the date of the transaction
     * @param movements the list of movements of the transaction
     * @throws IllegalArgumentException if one or more movements does not have an account or if the
     * list of movements is empty because a transaction with no
     * movements wont have any effect
     * @throws NullPointerException if any of the parameter is null
     * @throws AccountException if it is not possible to add the transaction to the ledger
     * @see Ledger
     */
    @Override
    public void addTransaction(String description, Date date, List<Movement> movements, List<Tag> tags) throws AccountException {
        if (description==null||date==null||movements==null) throw new NullPointerException();
        if (movements.isEmpty()) throw new IllegalArgumentException();
        if(movements.parallelStream().anyMatch(m->m.getAccount()==null))
            throw new IllegalArgumentException();
        Transaction transaction = new RoundedTransaction(description,date);
        movements.forEach(transaction::addMovement);
        tags.forEach(transaction::addTag);
        try {
            ledger.addTransaction(transaction);
        } catch (AccountException e){
            this.removeTransaction(transaction);
            // TODO: 07/06/20 testare questa cosa
            throw e;
        }
    }

    /**
     * Removes a given {@link Transaction} from the {@link Ledger} list.
     * @param transaction the transaction to be removed
     * @throws NullPointerException if the transaction is null
     * @throws AccountException if is not possible to remove the transaction from the ledger
     */
    @Override
    public void removeTransaction(Transaction transaction) throws AccountException {
        if(transaction==null) throw new NullPointerException();
        ledger.removeTransaction(transaction);
    }

    /**
     * Adds a new instance of {@link RoundedAccount} to the inner {@link Ledger} created with the given parameters.
     * If the {@link AccountType} is LIABILITY it also set the minimum amount to zero.
     * @param name the name of the account.
     * @param description the description of the account
     * @param referent the referent of the account (can be null)
     * @param opening the opening balance of the account
     * @param type the type of the account
     * @see Account
     */
    @Override
    public void addAccount(String name, String description, String referent, double opening, AccountType type) {
        Account account=RoundedAccount.getInstance(Objects.requireNonNull(name),
                Objects.requireNonNull(description),
                opening,
                Objects.requireNonNull(type)
        );
        account.setReferent(referent);
        if(type.equals(AccountType.LIABILITY))
            account.setMinAmount(0.0);
        ledger.addAccount(account);
    }

    /**
     * Adds a new instance of {@link RoundedAccount} to the inner {@link Ledger} created with the given parameters.
     * If the {@link AccountType} is LIABILITY the minimum amount must be zero.
     * @param name the name of the account
     * @param description the description of the account
     * @param referent the referent of the account (can be null)
     * @param opening the opening of the account
     * @param type the type of the account
     * @param min the minimum value of the account
     * @param max the maximum value of the account
     * @throws IllegalArgumentException if the type is LIABILITY and the minimum value is < 0
     * @see Account
     */
    @Override
    public void addAccount(String name, String description, String referent, double opening, AccountType type, Double min, Double max) {
        Account account=RoundedAccount.getInstance(name,description,opening,type);
        account.setMinAmount(min);
        if(type.equals(AccountType.LIABILITY))
            if(min==null)
                account.setMinAmount(0.0);
            else if(min<0)
                throw new IllegalArgumentException("Account of type liability should always have min amount >=0");
        account.setMaxAmount(max);
        account.setReferent(referent);
        ledger.addAccount(account);
    }

    /**
     * Removes an {@link Account} from the ledger's list. Before removing it checks that the account is not
     * used by any {@link Transaction} or {@link ScheduledTransaction}.
     * @param account the account to be removed
     * @throws AccountException if the account is used in the ledger.
     */
    @Override
    public void removeAccount(Account account) throws AccountException {
        boolean isUsed = ledger.getTransactions().stream().map(Transaction::getMovements).flatMap(List::stream)
                .anyMatch(m->m.getAccount().equals(account)) ||
                ledger.getScheduledTransactions().stream().map(ScheduledTransaction::getTransactions)
                        .flatMap(List::stream).map(Transaction::getMovements).flatMap(List::stream)
                        .anyMatch(m->m.getAccount().equals(account));
        if(isUsed) throw new AccountException("Tried to remove used account");
        ledger.removeAccount(account);
    }

    /**
     * Adds a new {@link ScheduledTransaction} to the ledger's list from a description and a list
     * of {@link Transaction}s. The list cannot be null.
     * @param transactions the list of transaction
     * @param description the description
     */
    @Override
    public void addScheduledTransaction(String description, List<Transaction> transactions) {
        if(Objects.requireNonNull(transactions).isEmpty()) throw new IllegalArgumentException("tried to add a completed scheduled transaction");
        ledger.addScheduledTransaction(new MapScheduledTransaction(description,transactions));
    }

    /**
     * Removes a {@link ScheduledTransaction} from the ledger.
     * @param scheduledTransaction the scheduled transaction to be removed
     */
    @Override
    public void removeScheduledTransaction(ScheduledTransaction scheduledTransaction) throws AccountException {
        ledger.removeScheduledTransaction(scheduledTransaction);
    }

    /**
     * Returns a list of {@link Transaction} matching a given expression from the ledger's list. The expression is compared with:
     * the tag's name, the tag's description, the transaction's description and the transaction's movements description.
     * @param expression the string used to find matching transactions
     * @return the resulting list of {@link Transaction}
     */
    @Override
    public List<Transaction> getTransactions(String expression) {
        Predicate<Transaction> predicate= transaction -> transaction.getTags().stream()
                .anyMatch(tag -> tag.getName().contains(expression));
        predicate = predicate.or(transaction -> transaction.getTags().stream()
                .anyMatch(tag -> tag.getDescription().contains(expression)));
        predicate = predicate.or(transaction -> transaction.getDescription().contains(expression));
        predicate = predicate.or(transaction->transaction.getMovements().stream()
                .anyMatch(movement -> movement.getDescription().contains(expression)));
        return ledger.getTransactions(predicate);
    }

    /**
     * Returns a list of {@link Account} matching a given expression from the ledger's list. The expression is compared with:
     * the account name, the account description, the account referent, the account's movements description
     * @param expression the string used to find matching accounts
     * @return the resulting list of {@link Account}
     */
    @Override
    public List<Account> getAccounts(String expression) {
        Predicate<Account> predicate = account -> account.getName().equals(expression);
        predicate=predicate.or(account -> account.getDescription().equals(expression));
        predicate=predicate.or(account -> account.getReferent().equals(expression));
        predicate=predicate.or(account -> account.getMovements().stream()
                .allMatch(movement -> movement.getDescription().equals(expression)));
        return ledger.getAccounts().stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * Schedules the ledger to a date
     * @param date the date to be scheduled
     * @see Ledger
     */
    @Override
    public void schedule(Date date) throws AccountException {
        ledger.schedule(date);
    }

    /**
     * Schedules the ledger to the current date
     * @see Ledger
     */
    @Override
    public void schedule() throws AccountException {
        ledger.schedule(Calendar.getInstance().getTime());
    }

    /**
     * Loads a {@link Ledger} from a file using the inner {@link PersistenceManager}. the loaded ledger is set as the
     * current ledger, every data inside the old ledger will be lost.
     * @param file the string representing the source of the new ledger
     * @see PersistenceManager
     */
    @Override
    public void loadLedger(String file) throws IOException {
        SingleTag.getRegistry().reset();
        RoundedMovement.getRegistry().reset();
        RoundedAccount.getRegistry().reset();
        ledger= persistenceManager.load(file);
    }

    /**
     * Saves the current {@link Ledger} to a file using the inner {@link PersistenceManager}.
     * @param file the string representing the output to save the ledger
     * @see PersistenceManager
     */
    @Override
    public void saveLedger(String file) throws IOException {
        persistenceManager.save(ledger,file);
    }

    @Override
    public void setLogLevel() {
        // TODO: 04/06/20 implementare
    }
}
