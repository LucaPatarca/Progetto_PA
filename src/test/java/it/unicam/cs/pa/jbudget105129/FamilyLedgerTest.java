package it.unicam.cs.pa.jbudget105129;

import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import it.unicam.cs.pa.jbudget105129.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
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
    static void init() throws AccountException {
        //calendar init
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        LocalDate oneMonthHence = LocalDate.now().plusMonths(1);
        LocalDate oneDayAgo = LocalDate.now().minusDays(1);

        //transactions init
        transaction0 = new RoundedTransaction("transazione base",oneMonthAgo);
        transaction1 = new RoundedTransaction("transazione futura",oneMonthHence);
        transaction2 = new RoundedTransaction("transazione passata",oneDayAgo);
        st = new MapScheduledTransaction("scheduled transaction",
                List.of(transaction1,transaction2));

        asset=RoundedAccount.getInstance("asset","",0, AccountType.ASSET);
        liability=RoundedAccount.getInstance("liability","",100,AccountType.LIABILITY);

        Movement movement1=RoundedMovement.getInstance("",87.66,MovementType.INCOME,liability);
        Movement movement2=RoundedMovement.getInstance("",91.44,MovementType.OUTFLOW,asset);
        Movement movement3=RoundedMovement.getInstance("",3.97,MovementType.INCOME,asset);
        Movement movement4=RoundedMovement.getInstance("",4.65,MovementType.OUTFLOW,liability);
        transaction1.addMovement(movement1);
        transaction1.addMovement(movement3);
        transaction2.addMovement(movement2);
        transaction2.addMovement(movement4);

        //ledger init
        ledger = new FamilyLedger();
        ledger.addTransaction(transaction0);
        ledger.addScheduledTransaction(st);
    }

    @Test
    void shouldSchedule() throws AccountException {
        ledger.schedule(LocalDate.now());

        assertTrue(st.isCompleted(transaction2));
        assertFalse(st.isCompleted(transaction1));

        assertEquals(-91.44,asset.getBalance());
        assertEquals(104.65,liability.getBalance());
    }

    @Test
    void shouldUpdateAccountsRemovingScheduledTransactions(){
        try {
            ledger.removeScheduledTransaction(st);
        } catch (AccountException e) {
            fail(e);
        }
        assertEquals(100,liability.getBalance());
        assertEquals(0,asset.getBalance());
    }
}
