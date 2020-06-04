package it.unicam.cs.pa.jbudget105129;

import it.unicam.cs.pa.jbudget105129.controller.FamilyLedgerManager;
import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import it.unicam.cs.pa.jbudget105129.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
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
        manager.addTransaction("test",Calendar.getInstance().getTime(),List.of(movement));
        assertTrue(ledger.getTransactions().contains(transaction));
        assertTrue(ledger.getAccounts().contains(account));
        assertEquals(10,account.getBalance());
    }

    @Test
    void shouldRemoveTransaction(){
        manager.addTransaction(transaction);
        assertTrue(ledger.getTransactions().contains(transaction));
        manager.removeTransaction(transaction);
        assertTrue(ledger.getTransactions().isEmpty());
        assertEquals(0,account.getBalance());
    }

    @Test
    void shouldRefuseToRemoveUsedAccount(){
        manager.addTransaction(transaction);
        assertThrows(AccountException.class,()->manager.removeAccount(account));
        manager.removeTransaction(transaction);
        manager.addScheduledTransaction(new MapScheduledTransaction("prova", List.of(transaction)));
        assertThrows(AccountException.class,()->manager.removeAccount(account));
    }

    @Test
    void shouldFindTransactions(){
        Tag tag1= SingleTag.getInstance("yes1","");
        Tag tag2= SingleTag.getInstance("no","yes2");
        Tag tag3= SingleTag.getInstance("no","no");
        Transaction transaction1=new RoundedTransaction("yes3",Calendar.getInstance().getTime());
        Transaction transaction2=new RoundedTransaction("no1",Calendar.getInstance().getTime());
        Transaction transaction3=new RoundedTransaction("no2",Calendar.getInstance().getTime());
        Transaction transaction4=new RoundedTransaction("no3",Calendar.getInstance().getTime());
        Transaction transaction5=new RoundedTransaction("no4",Calendar.getInstance().getTime());
        Transaction transaction6=new RoundedTransaction("no5",Calendar.getInstance().getTime());
        Movement movement1=RoundedMovement.getInstance()
                .setAccount(account)
                .setDescription("fake1")
                .setAmount(0)
                .setType(MovementType.INCOME);
        Movement movement2=RoundedMovement.getInstance()
                .setAccount(account)
                .setDescription("fake2")
                .setAmount(0)
                .setType(MovementType.INCOME);
        Movement movement3=RoundedMovement.getInstance()
                .setAccount(account)
                .setDescription("fake3")
                .setAmount(0)
                .setType(MovementType.INCOME);
        transaction2.addMovement(movement1);
        transaction3.addMovement(movement2);
        transaction4.addMovement(movement3);
        transaction2.addTag(tag3);
        transaction3.addTag(tag1);
        transaction4.addTag(tag2);
        Movement movement4=RoundedMovement.getInstance()
                .setAccount(account)
                .setDescription("yes4")
                .setAmount(10)
                .setType(MovementType.INCOME);
        Movement movement5=RoundedMovement.getInstance()
                .setAccount(account)
                .setDescription("no")
                .setAmount(10)
                .setType(MovementType.INCOME);
        transaction5.addMovement(movement4);
        transaction6.addMovement(movement5);
        manager.addTransaction(transaction1);
        manager.addTransaction(transaction2);
        manager.addTransaction(transaction3);
        manager.addTransaction(transaction4);
        manager.addTransaction(transaction5);
        manager.addTransaction(transaction6);
        List<Transaction> result=manager.getTransactions("yes");
        assertTrue(result.contains(transaction1));
        assertTrue(result.contains(transaction3));
        assertTrue(result.contains(transaction4));
        assertTrue(result.contains(transaction5));
        assertFalse(result.contains(transaction2));
        assertFalse(result.contains(transaction6));
    }
}
