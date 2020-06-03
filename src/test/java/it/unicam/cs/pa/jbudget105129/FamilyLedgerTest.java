package it.unicam.cs.pa.jbudget105129;

import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import it.unicam.cs.pa.jbudget105129.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FamilyLedgerTest {

    private static Transaction transaction0;
    private static Transaction transaction1;
    private static Transaction transaction2;
    private static ScheduledTransaction st;
    private static FamilyLedger ledger;
    private static Account asset;
    private static Account liability;

    @BeforeAll
    static void init(){
        //calendar init
        Calendar oneMonthAgo = Calendar.getInstance();
        oneMonthAgo.roll(Calendar.MONTH,false);
        Calendar oneMonthHence = Calendar.getInstance();
        oneMonthHence.roll(Calendar.MONTH,true);
        Calendar oneDayAgo = Calendar.getInstance();
        oneDayAgo.roll(Calendar.DAY_OF_YEAR,false);

        //transactions init
        transaction0 = new RoundedTransaction("transazione base",oneMonthAgo.getTime());
        transaction1 = new RoundedTransaction("transazione futura",oneMonthHence.getTime());
        transaction2 = new RoundedTransaction("transazione passata",oneDayAgo.getTime());
        st = new MapScheduledTransaction("scheduled transaction",
                List.of(transaction1,transaction2));

        //ledger init
        ledger = new FamilyLedger();
        ledger.addTransaction(transaction0);
        ledger.addScheduledTransaction(st);

        asset=new RoundedAccount("asset","",0, AccountType.ASSET);
        liability=new RoundedAccount("liability","",100,AccountType.LIABILITY);

        Movement movement1=RoundedMovement.getInstance()
            .setAmount(87.66)
            .setType(MovementType.INCOME)
            .setAccount(liability);
        Movement movement2=RoundedMovement.getInstance()
            .setAmount(91.44)
            .setType(MovementType.OUTFLOW)
            .setAccount(asset);
        Movement movement3=RoundedMovement.getInstance()
            .setAmount(3.97)
            .setType(MovementType.INCOME)
            .setAccount(asset);
        Movement movement4=RoundedMovement.getInstance()
            .setAmount(4.65)
            .setType(MovementType.OUTFLOW)
            .setAccount(liability);
        transaction1.addMovement(movement1);
        transaction1.addMovement(movement3);
        transaction2.addMovement(movement2);
        transaction2.addMovement(movement4);
    }

    @Test
    void shouldSchedule() throws AccountException {
        ledger.schedule(Calendar.getInstance().getTime());

        assertTrue(st.isCompleted(transaction2));
        assertFalse(st.isCompleted(transaction1));
        assertTrue(ledger.getTransactions().contains(transaction2));
        assertFalse(ledger.getTransactions().contains(transaction1));
        assertTrue(ledger.getTransactions().contains(transaction0));

        assertEquals(-91.44,asset.getBalance());
        assertEquals(104.65,liability.getBalance());
    }
}
