package it.unicam.cs.pa.jbudget105129.controller;

import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import it.unicam.cs.pa.jbudget105129.model.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
//TODO javadoc
public class FamilyLedgerManager implements LedgerManager {

    private final Ledger ledger;

    public FamilyLedgerManager(Ledger ledger){
        this.ledger=ledger;
    }

    @Override
    public Ledger getLedger() {
        return ledger;
    }

    @Override
    public void addTransaction(Transaction transaction) {
        if (transaction==null) throw new NullPointerException();
        ledger.addTransaction(transaction);
    }

    @Override
    public void removeTransaction(Transaction transaction) {
        if(transaction==null) throw new NullPointerException();
        ledger.removeTransaction(transaction);
    }

    @Override
    public void addAccount(Account account) {
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
    public void schedule(Date date) throws AccountException {
        ledger.schedule(date);
    }

    @Override
    public void scheduleNow() throws AccountException {
        ledger.schedule(Calendar.getInstance().getTime());
    }
}
