package it.unicam.cs.pa.jbudget105129;

import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import it.unicam.cs.pa.jbudget105129.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
        transaction = new RoundedTransaction("transazione", Calendar.getInstance().getTime());
        Account account1=new RoundedAccount("account1","account1",0, AccountType.ASSET);
        Account account2=new RoundedAccount("account2","account2",0,AccountType.LIABILITY);
        m1=RoundedMovement.getInstance()
                .setDescription("m1")
                .setAmount(0.78)
                .setType(MovementType.INCOME)
                .setAccount(account1);
        m2=RoundedMovement.getInstance()
                .setDescription("m2")
                .setAmount(10.66)
                .setType(MovementType.OUTFLOW)
                .setAccount(account1);
        m3=RoundedMovement.getInstance()
                .setDescription("m3")
                .setAmount(42.8)
                .setType(MovementType.INCOME)
                .setAccount(account2);
        m4=RoundedMovement.getInstance()
                .setDescription("m4")
                .setAmount(133.5)
                .setType(MovementType.OUTFLOW)
                .setAccount(account2);
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
