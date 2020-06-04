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
import java.util.LinkedList;
import java.util.List;

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
        List<Movement> movements = new LinkedList<>();
        movements.add(RoundedMovement.getInstance("movimento1",10.94,MovementType.INCOME,account1));
        movements.add(RoundedMovement.getInstance("movimento2", 5.63, MovementType.OUTFLOW,account2));
        movements.add(RoundedMovement.getInstance("movimento3",6.65,MovementType.OUTFLOW,account1));
        movements.add(RoundedMovement.getInstance("movimento4",9.84,MovementType.INCOME,account2));
        List<Tag> tags1 = new LinkedList<>();
        List<Tag> tags2 = new LinkedList<>();
        try {
            manager.addTransaction("transazione1",Calendar.getInstance().getTime(),movements,tags1);
            manager.addTransaction("transazione2",Calendar.getInstance().getTime(),movements,tags2);
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
