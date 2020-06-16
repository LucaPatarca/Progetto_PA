package it.unicam.cs.pa.jbudget105129;

import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import it.unicam.cs.pa.jbudget105129.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

public class RoundedTransactionTest {

    private static Transaction transaction;
    private static Movement m1;
    private static Movement m2;
    private static Movement m3;
    private static Movement m4;

    @BeforeAll
    static void init(){
        transaction = new RoundedTransaction("transazione", LocalDate.now());
        Account account1=RoundedAccount.getInstance("account1","account1",0, AccountType.ASSET);
        Account account2=RoundedAccount.getInstance("account2","account2",0,AccountType.LIABILITY);
        m1=RoundedMovement.getInstance("m1",0.78,MovementType.INCOME,account1);
        m2=RoundedMovement.getInstance("m2",10.66,MovementType.OUTFLOW,account1);
        m3=RoundedMovement.getInstance("m3",42.8,MovementType.INCOME,account2);
        m4=RoundedMovement.getInstance("m4",133.5,MovementType.OUTFLOW,account2);
    }

    @Test
    void testTotalAmount(){
        assertEquals(0,transaction.getTotalAmount());
        transaction.addMovement(m1);
        assertEquals(0.78,transaction.getTotalAmount());
        transaction.addMovement(m2);
        assertEquals(-9.88,transaction.getTotalAmount());
        transaction.addMovement(m3);
        assertEquals(32.92,transaction.getTotalAmount());
        transaction.addMovement(m4);
        assertEquals(-100.58,transaction.getTotalAmount());

        transaction.removeMovement(m4);
        assertEquals(32.92,transaction.getTotalAmount());
        transaction.removeMovement(m3);
        assertEquals(-9.88,transaction.getTotalAmount());
        transaction.removeMovement(m2);
        assertEquals(0.78,transaction.getTotalAmount());
        transaction.removeMovement(m1);
        assertTrue(transaction.getMovements().isEmpty());
        assertEquals(0,transaction.getTotalAmount());
    }

    @Test
    void shouldMirrorTagsToMovements(){
        Tag tag1 = SingleTag.getInstance("tag1","");
        Tag tag2 = SingleTag.getInstance("tag2","");

        m1.addTag(tag1);
        m2.addTag(tag1);

        transaction.addMovement(m1);
        transaction.addMovement(m2);
        transaction.addMovement(m3);
        transaction.addMovement(m4);

        transaction.addTag(tag2);

        assertTrue(m1.getTags().contains(tag2));
        assertTrue(m2.getTags().contains(tag2));
        assertTrue(m3.getTags().contains(tag2));
        assertTrue(m4.getTags().contains(tag2));
        assertTrue(m1.getTags().contains(tag1));
        assertTrue(m2.getTags().contains(tag1));

        transaction.removeTag(tag1);

        assertFalse(m1.getTags().contains(tag1));
        assertFalse(m2.getTags().contains(tag1));
        assertFalse(m3.getTags().contains(tag1));
        assertFalse(m4.getTags().contains(tag1));
    }
}
