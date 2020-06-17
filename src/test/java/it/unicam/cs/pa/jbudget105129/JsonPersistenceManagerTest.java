package it.unicam.cs.pa.jbudget105129;

import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import it.unicam.cs.pa.jbudget105129.model.*;
import it.unicam.cs.pa.jbudget105129.persistence.JsonPersistenceManager;
import it.unicam.cs.pa.jbudget105129.persistence.PersistenceManager;
import javafx.scene.Scene;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonPersistenceManagerTest {
    private static FamilyLedger ledger;
    private static RoundedAccount account1;
    private static RoundedAccount account2;

    @BeforeAll
    static void init(){
        ledger = new FamilyLedger();
        account1=RoundedAccount.getInstance("banca","mia banca",0, AccountType.ASSET);
        account2=RoundedAccount.getInstance("carta","mia carta di debito",0, AccountType.LIABILITY);
        List<Movement> movements = new LinkedList<>();
        List<Movement> movements2 = new LinkedList<>();
        movements.add(RoundedMovement.getInstance("movimento1",10.94,MovementType.INCOME,account1));
        movements.add(RoundedMovement.getInstance("movimento2", 5.63, MovementType.OUTFLOW,account2));
        movements.add(RoundedMovement.getInstance("movimento3",6.65,MovementType.OUTFLOW,account1));
        movements.add(RoundedMovement.getInstance("movimento4",9.84,MovementType.INCOME,account2));
        movements2.add(RoundedMovement.getInstance("movimento1",10.94,MovementType.INCOME,account1));
        movements2.add(RoundedMovement.getInstance("movimento2", 5.63, MovementType.OUTFLOW,account2));
        movements2.add(RoundedMovement.getInstance("movimento3",6.65,MovementType.OUTFLOW,account1));
        movements2.add(RoundedMovement.getInstance("movimento4",9.84,MovementType.INCOME,account2));
        List<Tag> tags1 = new LinkedList<>();
        List<Tag> tags2 = new LinkedList<>();
        tags1.add(SingleTag.getInstance("prova1","description1"));
        tags1.add(SingleTag.getInstance("prova2","description2"));
        tags2.add(SingleTag.getInstance("prova3","description3"));
        tags2.add(SingleTag.getInstance("prova4","description4"));
        Transaction transaction1 = new RoundedTransaction("transazione1", LocalDate.now());
        Transaction transaction2 = new RoundedTransaction("transazione2",LocalDate.now());
        movements.forEach(transaction1::addMovement);
        movements2.forEach(transaction2::addMovement);
        tags1.forEach(transaction1::addTag);
        tags2.forEach(transaction2::addTag);
        try {
            ledger.addTransaction(transaction1);
            ledger.addTransaction(transaction2);
        } catch (AccountException e) {
            fail(e);
        }
        addScheduledTransactions();
    }

    @Test
    void testSaveAndLoad(){
        PersistenceManager manager = new JsonPersistenceManager();
        File file = new File("build/tmp/PersistenceTest.txt");
        Ledger loaded = null;
        try {
            manager.save(ledger,"build/tmp/PersistenceTest.txt");
            RoundedMovement.getRegistry().reset();
            SingleTag.getRegistry().reset();
            RoundedAccount.getRegistry().reset();
            loaded = manager.load("build/tmp/PersistenceTest.txt");
        } catch (IOException e) {
            fail(e);
        }
        assertTrue(loaded.getTransactions().containsAll(ledger.getTransactions()));
        assertTrue(loaded.getAccounts().containsAll(ledger.getAccounts()));
        assertEquals(loaded.getScheduledTransactions().get(0).getDescription(),ledger.getScheduledTransactions().get(0).getDescription());
        assertTrue(loaded.getScheduledTransactions().get(0).getTransactions().containsAll(ledger.getScheduledTransactions().get(0).getTransactions()));
        assertFalse(loaded.getScheduledTransactions().get(0).isCompleted());
        ScheduledTransaction st = loaded.getScheduledTransactions().get(0);
        assertTrue(st.isCompleted(st.getTransactions().get(1))||st.isCompleted(st.getTransactions().get(0)));
        file.delete();
    }

    private static void addScheduledTransactions(){
        RoundedMovement movement1 = RoundedMovement.getInstance("movement1",10.2,MovementType.INCOME,account1);
        RoundedMovement movement2 = RoundedMovement.getInstance("movement1",15.68,MovementType.OUTFLOW,account2);
        RoundedMovement movement3 = RoundedMovement.getInstance("movement1",8.68,MovementType.INCOME,account2);
        RoundedMovement movement4 = RoundedMovement.getInstance("movement1",5.9,MovementType.OUTFLOW,account1);
        RoundedTransaction transaction1 = new RoundedTransaction("past",LocalDate.now().minusDays(1));
        RoundedTransaction transaction2 = new RoundedTransaction("future",LocalDate.now().plusDays(1));
        transaction1.addMovement(movement1);
        transaction1.addMovement(movement2);
        transaction2.addMovement(movement3);
        transaction2.addMovement(movement4);
        ScheduledTransaction st = new MapScheduledTransaction("scheduledTransaction",List.of(transaction1,transaction2));
        ledger.addScheduledTransaction(st);
        try {
            ledger.schedule(LocalDate.now());
        } catch (AccountException e) {
            fail(e);
        }
    }
}
