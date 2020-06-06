package it.unicam.cs.pa.jbudget105129.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import it.unicam.cs.pa.jbudget105129.Dependency.AppLedger;
import it.unicam.cs.pa.jbudget105129.Dependency.AppPersistence;
import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import it.unicam.cs.pa.jbudget105129.model.*;
import it.unicam.cs.pa.jbudget105129.persistence.PersistenceManager;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

//TODO javadoc
@Singleton
public class FamilyLedgerManager implements LedgerManager {

    private Ledger ledger;
    private final PersistenceManager persistenceManager;

    @Inject
    public FamilyLedgerManager(@AppLedger Ledger ledger, @AppPersistence PersistenceManager persistenceManager){
        this.ledger=ledger;
        this.persistenceManager =persistenceManager;
    }

    @Override
    public Ledger getLedger() {
        return ledger;
    }

    /**
     * This method creates a new instance of RoundedTransaction and adds it to the ledger. It also takes
     * as parameter a list of movements that will be added to the transaction. Before adding the transaction
     * to the ledger every movement is checked to verify that it has an account, if this condition is not
     * true an IllegalArgumentException will be thrown. Finally the transaction is added to the ledger
     * which is responsible for adding each movement to the linked account. If ledger throws an AccountException,
     * meaning that one or more account refused the transaction, the entire transaction will be removed
     * from the ledger and the exception is re-thrown to be fully managed by the view.
     * @param description a description for the transaction
     * @param date a date for the transaction
     * @param movements a list of movements for the transaction
     * @throws IllegalArgumentException if one or more movements does not have an account or if the
     * list of movements is empty because a transaction with no
     * movements wont have any effect.
     * @throws NullPointerException if any of the parameter is null.
     * @throws AccountException if thrown by the ledger.
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
            throw e;
        }
    }

    @Override
    public void removeTransaction(Transaction transaction) throws AccountException {
        if(transaction==null) throw new NullPointerException();
        ledger.removeTransaction(transaction);
    }

    @Override
    public void addAccount(String name, String description, double opening, AccountType type) {
        Account account=new RoundedAccount(name,description,opening,type);
        if(type.equals(AccountType.LIABILITY))
            account.setMinAmount(0);
        ledger.addAccount(account);
    }

    @Override
    public void addAccount(String name, String description, double opening, AccountType type, double min, double max) {
        Account account=new RoundedAccount(name,description,opening,type);
        if(type.equals(AccountType.LIABILITY)&&min<0)
            throw new IllegalArgumentException("Account of type liability should always have min amount >=0");
        account.setMinAmount(min);
        account.setMaxAmount(max);
        ledger.addAccount(account);
    }

    @Override
    public void removeAccount(Account account) throws AccountException {
        //controllo che il conto non sia utilizzato
        boolean isUsed = ledger.getTransactions().stream().map(Transaction::getMovements).flatMap(List::stream)
                .anyMatch(m->m.getAccount().equals(account)) ||
                ledger.getScheduledTransactions().stream().map(ScheduledTransaction::getTransactions)
                        .flatMap(List::stream).map(Transaction::getMovements).flatMap(List::stream)
                        .anyMatch(m->m.getAccount().equals(account));
        if(isUsed) throw new AccountException("Tried to remove used account");
        ledger.removeAccount(account);
    }

    @Override
    public void addScheduledTransaction(ScheduledTransaction scheduledTransaction) {
        if(scheduledTransaction.isCompleted()) throw new IllegalArgumentException("tried to add a completed sheduled transaction");
        ledger.addScheduledTransaction(scheduledTransaction);
    }

    @Override
    public void removeScheduledTransaction(ScheduledTransaction scheduledTransaction) {
        ledger.removeScheduledTransaction(scheduledTransaction);
    }

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

    @Override
    public List<Account> getAccounts(String expression) {
        Predicate<Account> predicate = account -> account.getName().equals(expression);
        predicate=predicate.or(account -> account.getDescription().equals(expression));
        predicate=predicate.or(account -> account.getReferent().equals(expression));
        predicate=predicate.or(account -> account.getMovements().stream()
                .allMatch(movement -> movement.getDescription().equals(expression)));
        return ledger.getAccounts().stream().filter(predicate).collect(Collectors.toList());
    }

    @Override
    public void schedule(Date date) throws AccountException {
        ledger.schedule(date);
    }

    @Override
    public void schedule() throws AccountException {
        ledger.schedule(Calendar.getInstance().getTime());
    }

    @Override
    public void loadLedger(String file) throws IOException {
        ledger= persistenceManager.load(file);
    }

    @Override
    public void saveLedger(String file) throws IOException {
        persistenceManager.save(ledger,file);
    }

    @Override
    public void setLogLevel() {
        // TODO: 04/06/20 implementare
    }
}
