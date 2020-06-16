package it.unicam.cs.pa.jbudget105129;

import it.unicam.cs.pa.jbudget105129.controller.BudgetManager;
import it.unicam.cs.pa.jbudget105129.controller.SimpleBudgetManager;
import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import it.unicam.cs.pa.jbudget105129.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleBudgetReportTest {

    private static Ledger ledger;
    private static Budget budget;
    private static BudgetManager bm;
    private static Tag tag0;
    private static Tag tag1;

    @BeforeAll
    static void init() throws AccountException {
        ledger = new FamilyLedger();
        budget = new SimpleBudget();
        bm = new SimpleBudgetManager();
        Account account = RoundedAccount.getInstance("account", "", 0, AccountType.ASSET);

        Transaction transaction0 = new RoundedTransaction("transazione 0", LocalDate.now());
        Transaction transaction1 = new RoundedTransaction("transazione 1", LocalDate.now());
        Transaction transaction2 = new RoundedTransaction("transazione 2", LocalDate.now());
        Transaction transaction3 = new RoundedTransaction("transazione 3", LocalDate.now());

        Movement movement1 = RoundedMovement.getInstance("",2.5,MovementType.INCOME, account);
        Movement movement2 = RoundedMovement.getInstance("",4.64,MovementType.OUTFLOW, account);
        Movement movement3 = RoundedMovement.getInstance("",7.48,MovementType.INCOME, account);
        Movement movement4 = RoundedMovement.getInstance("",4.8,MovementType.OUTFLOW, account);
        Movement movement5 = RoundedMovement.getInstance("",9.86,MovementType.INCOME, account);
        Movement movement6 = RoundedMovement.getInstance("",7.74,MovementType.OUTFLOW, account);
        Movement movement7 = RoundedMovement.getInstance("",3.4,MovementType.INCOME, account);
        Movement movement8 = RoundedMovement.getInstance("",6.5,MovementType.OUTFLOW, account);

        transaction0.addMovement(movement1);
        transaction0.addMovement(movement2);
        transaction1.addMovement(movement3);
        transaction1.addMovement(movement4);
        transaction2.addMovement(movement5);
        transaction2.addMovement(movement6);
        transaction3.addMovement(movement7);
        transaction3.addMovement(movement8);

        tag0 = SingleTag.getInstance("tag 0","");
        tag1 = SingleTag.getInstance("tag 1","");

        transaction0.addTag(tag0);
        transaction1.addTag(tag0);
        transaction2.addTag(tag1);
        transaction3.addTag(tag1);

        ledger.addTransaction(transaction0);
        ledger.addTransaction(transaction1);
        ledger.addTransaction(transaction2);
        ledger.addTransaction(transaction3);
    }

    @Test
    void testReport(){
        budget.set(tag0,20);
        budget.set(tag1,10);

        BudgetReport br=bm.generateReport(ledger,budget);

        Map<Tag,Double> expectedReport = new HashMap<>();
        expectedReport.put(tag0,19.46);
        expectedReport.put(tag1,10.98);

        assertEquals(expectedReport,br.report());
    }
}
