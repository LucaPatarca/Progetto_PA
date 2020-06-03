package it.unicam.cs.pa.jbudget105129;

import it.unicam.cs.pa.jbudget105129.controller.BudgetManager;
import it.unicam.cs.pa.jbudget105129.controller.SimpleBudgetManager;
import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import it.unicam.cs.pa.jbudget105129.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleBudgetReportTest {

    private static Ledger ledger;
    private static Budget budget;
    private static BudgetManager bm;
    private static Account account;
    private static Tag tag0;
    private static Tag tag1;

    @BeforeAll
    static void init(){
        ledger = new FamilyLedger();
        budget = new SimpleBudget();
        bm = new SimpleBudgetManager();
        account = new RoundedAccount("account","",0, AccountType.ASSET);

        Transaction transaction0 = new RoundedTransaction("transazione 0", Calendar.getInstance().getTime());
        Transaction transaction1 = new RoundedTransaction("transazione 1", Calendar.getInstance().getTime());
        Transaction transaction2 = new RoundedTransaction("transazione 2", Calendar.getInstance().getTime());
        Transaction transaction3 = new RoundedTransaction("transazione 3", Calendar.getInstance().getTime());

        Movement movement1 = RoundedMovement.getInstance()
                .setAmount(2.5)
                .setType(MovementType.INCOME)
                .setAccount(account);
        Movement movement2 = RoundedMovement.getInstance()
                .setAmount(4.64)
                .setType(MovementType.OUTFLOW)
                .setAccount(account);
        Movement movement3 = RoundedMovement.getInstance()
                .setAmount(7.48)
                .setType(MovementType.INCOME)
                .setAccount(account);
        Movement movement4 = RoundedMovement.getInstance()
                .setAmount(4.8)
                .setType(MovementType.OUTFLOW)
                .setAccount(account);
        Movement movement5 = RoundedMovement.getInstance()
                .setAmount(9.86)
                .setType(MovementType.INCOME)
                .setAccount(account);
        Movement movement6 = RoundedMovement.getInstance()
                .setAmount(7.74)
                .setType(MovementType.OUTFLOW)
                .setAccount(account);
        Movement movement7 = RoundedMovement.getInstance()
                .setAmount(3.4)
                .setType(MovementType.INCOME)
                .setAccount(account);
        Movement movement8 = RoundedMovement.getInstance()
                .setAmount(6.5)
                .setType(MovementType.OUTFLOW)
                .setAccount(account);

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
