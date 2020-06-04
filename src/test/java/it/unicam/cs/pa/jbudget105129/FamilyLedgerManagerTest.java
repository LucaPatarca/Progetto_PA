package it.unicam.cs.pa.jbudget105129;

import it.unicam.cs.pa.jbudget105129.controller.FamilyLedgerManager;
import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import it.unicam.cs.pa.jbudget105129.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FamilyLedgerManagerTest {
    private FamilyLedger ledger;
    private FamilyLedgerManager manager;
    private Account account;
    private Movement movement;

    @BeforeEach
    void init(){
        ledger = new FamilyLedger();
        manager =new FamilyLedgerManager(ledger);
        account = new RoundedAccount("prova","",0, AccountType.ASSET);
        movement = RoundedMovement.getInstance("movement1",10,MovementType.INCOME,account);
    }

    @Test
    void shouldAddTransaction() throws AccountException {
        assertTrue(ledger.getTransactions().isEmpty());
        assertTrue(ledger.getAccounts().isEmpty());
        manager.addTransaction("test",Calendar.getInstance().getTime(),List.of(movement),new LinkedList<>());
        assertEquals(1, ledger.getTransactions().size());
        assertEquals("test",ledger.getTransactions().get(0).getDescription());
        assertTrue(ledger.getAccounts().contains(account));
        assertEquals(10,account.getBalance());
    }

    @Test
    void shouldRemoveTransaction() throws AccountException {
        manager.addTransaction("test",Calendar.getInstance().getTime(),List.of(movement),new LinkedList<>());
        assertEquals(1, ledger.getTransactions().size());
        manager.removeTransaction(ledger.getTransactions().get(0));
        assertTrue(ledger.getTransactions().isEmpty());
        assertEquals(0,account.getBalance());
    }

    @Test
    void shouldRefuseToRemoveUsedAccount() throws AccountException {
        manager.addTransaction("test",Calendar.getInstance().getTime(),List.of(movement),new LinkedList<>());
        assertThrows(AccountException.class,()->manager.removeAccount(account));
        Transaction transaction = ledger.getTransactions().get(0);
        manager.removeTransaction(transaction);
        manager.addScheduledTransaction(new MapScheduledTransaction("prova", List.of(transaction)));
        assertThrows(AccountException.class,()->manager.removeAccount(account));
    }

    @Test
    void shouldFindTransactions() throws AccountException {
        List<Tag> tags1= List.of(SingleTag.getInstance("yes1",""));
        List<Tag> tags2= List.of(SingleTag.getInstance("no","yes2"));
        List<Tag> tags3= List.of(SingleTag.getInstance("no","no"));
        List<Movement> movements1 = List.of(RoundedMovement.getInstance("fake1",0,MovementType.INCOME,account));
        List<Movement> movements2 = List.of(RoundedMovement.getInstance("fake2",0,MovementType.INCOME,account));
        List<Movement> movements3 = List.of(RoundedMovement.getInstance("fake3",0,MovementType.INCOME,account));
        List<Movement> movements4 = List.of(RoundedMovement.getInstance("yes4",10,MovementType.INCOME,account));
        List<Movement> movements5 = List.of(RoundedMovement.getInstance("no",10,MovementType.INCOME,account));
        manager.addTransaction("yes3",Calendar.getInstance().getTime(),movements5,new LinkedList<>());
        manager.addTransaction("no1",Calendar.getInstance().getTime(),movements1,tags3);
        manager.addTransaction("no2",Calendar.getInstance().getTime(),movements2,tags1);
        manager.addTransaction("no3",Calendar.getInstance().getTime(),movements3,tags2);
        manager.addTransaction("no4",Calendar.getInstance().getTime(),movements4,new LinkedList<>());
        manager.addTransaction("no5",Calendar.getInstance().getTime(),movements5,new LinkedList<>());
        List<Transaction> result=manager.getTransactions("yes");
        List<Transaction> expected = ledger.getTransactions();
        expected.remove(1);
        expected.remove(4);
        assertEquals(expected,result);
    }
}
