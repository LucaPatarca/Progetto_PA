package it.unicam.cs.pa.jbudget105129;

import com.google.inject.Guice;
import com.google.inject.Injector;
import it.unicam.cs.pa.jbudget105129.Dependency.LedgerManagerModule;
import it.unicam.cs.pa.jbudget105129.controller.FamilyLedgerManager;
import it.unicam.cs.pa.jbudget105129.controller.LedgerManager;
import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import it.unicam.cs.pa.jbudget105129.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.Transient;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FamilyLedgerManagerTest {
    private FamilyLedgerManager manager;
    private Account account;
    private Movement movement;
    private Injector injector;

    @BeforeEach
    void init(){
        injector = Guice.createInjector(new LedgerManagerModule());
        manager = injector.getInstance(FamilyLedgerManager.class);
        account = new RoundedAccount("prova","",0, AccountType.ASSET);
        movement = RoundedMovement.getInstance("movement1",10,MovementType.INCOME,account);
    }

    @Test
    void shouldAddTransaction() throws AccountException {
        assertTrue(manager.getLedger().getTransactions().isEmpty());
        assertTrue(manager.getLedger().getAccounts().isEmpty());
        manager.addTransaction("test",Calendar.getInstance().getTime(),List.of(movement),new LinkedList<>());
        assertEquals(1, manager.getLedger().getTransactions().size());
        assertEquals("test",manager.getLedger().getTransactions().get(0).getDescription());
        assertTrue(manager.getLedger().getAccounts().contains(account));
        assertEquals(10,account.getBalance());
    }

    @Test
    void shouldRemoveTransaction() throws AccountException {
        manager.addTransaction("test",Calendar.getInstance().getTime(),List.of(movement),new LinkedList<>());
        assertEquals(1, manager.getLedger().getTransactions().size());
        manager.removeTransaction(manager.getLedger().getTransactions().get(0));
        assertTrue(manager.getLedger().getTransactions().isEmpty());
        assertEquals(0,account.getBalance());
    }

    @Test
    void shouldRefuseToRemoveUsedAccount() throws AccountException {
        manager.addTransaction("test",Calendar.getInstance().getTime(),List.of(movement),new LinkedList<>());
        assertThrows(AccountException.class,()->manager.removeAccount(account));
        Transaction transaction = manager.getLedger().getTransactions().get(0);
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
        List<Transaction> expected = manager.getLedger().getTransactions();
        expected.remove(1);
        expected.remove(4);
        assertEquals(expected,result);
    }

    @Test
    void shouldBeSingleton(){
        manager.addAccount("prova","description",0,AccountType.ASSET);
        assertEquals(1,manager.getLedger().getAccounts().size());
        LedgerManager manager2 = injector.getInstance(FamilyLedgerManager.class);
        assertEquals(1,manager2.getLedger().getAccounts().size());
    }
}
