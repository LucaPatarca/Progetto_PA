package it.unicam.cs.pa.jbudget105129;

import it.unicam.cs.pa.jbudget105129.controller.FamilyLedgerManager;
import it.unicam.cs.pa.jbudget105129.controller.LedgerManager;
import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import it.unicam.cs.pa.jbudget105129.model.*;
import it.unicam.cs.pa.jbudget105129.persistence.JsonPersistenceManager;
import it.unicam.cs.pa.jbudget105129.persistence.PersistenceManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

public class JsonPersistenceManagerTest {
    private static Ledger ledger;

    @BeforeAll
    static void init(){
        // TODO: 02/06/20 mettere pure i tag e sperare che funziona
        ledger=new FamilyLedger();
        LedgerManager manager = new FamilyLedgerManager(ledger);
        Account account1=new RoundedAccount("banca","mia banca",0, AccountType.ASSET);
        Account account2=new RoundedAccount("carta","mia carta di debito",0, AccountType.LIABILITY);
        Transaction transaction1 = new RoundedTransaction("transazione1", Calendar.getInstance().getTime());
        Transaction transaction2 = new RoundedTransaction("transazione2", Calendar.getInstance().getTime());
        Movement movement1=RoundedMovement.getInstance()
                .setDescription("movimento1")
                .setAmount(10.94)
                .setType(MovementType.INCOME)
                .setAccount(account1);
        Movement movement2=RoundedMovement.getInstance()
                .setDescription("movimento2")
                .setAmount(5.63)
                .setType(MovementType.OUTFLOW)
                .setAccount(account2);
        transaction1.addMovement(movement1);
        transaction1.addMovement(movement2);
        Movement movement3=RoundedMovement.getInstance()
                .setDescription("movimento3")
                .setAmount(6.65)
                .setType(MovementType.OUTFLOW)
                .setAccount(account1);
        Movement movement4=RoundedMovement.getInstance()
                .setDescription("movimento4")
                .setAmount(9.84)
                .setType(MovementType.INCOME)
                .setAccount(account2);
        transaction2.addMovement(movement3);
        transaction2.addMovement(movement4);
        try {
            manager.addTransaction(transaction1);
            manager.addTransaction(transaction2);
        } catch (AccountException e) {
            fail(e);
        }
    }

    @Test
    void testSaveAndLoad(){
        PersistenceManager manager = new JsonPersistenceManager();
        File file = new File("build/tmp/PersistenceTest.txt");
        Ledger loaded = null;
        try {
            manager.save(ledger,file);
            loaded = manager.load(file);
        } catch (IOException e) {
            fail(e);
        }
        assertEquals(loaded.toString(),ledger.toString());
        file.delete();
    }
}
