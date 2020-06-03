package it.unicam.cs.pa.jbudget105129;

import it.unicam.cs.pa.jbudget105129.model.MapScheduledTransaction;
import it.unicam.cs.pa.jbudget105129.model.RoundedTransaction;
import it.unicam.cs.pa.jbudget105129.model.ScheduledTransaction;
import it.unicam.cs.pa.jbudget105129.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MapScheduledTransactionTest {
    private ScheduledTransaction st;
    private List<Transaction> list;

    @BeforeEach
    void init(){
        list = new LinkedList<>();
        list.add(new RoundedTransaction("transaction0", new GregorianCalendar(
                2020,Calendar.JANUARY,24,11,0).getTime()));
        list.add(new RoundedTransaction("transaction1", new GregorianCalendar(
                2020,Calendar.JANUARY,24,13,0).getTime()));
        list.add(new RoundedTransaction("transaction2", new GregorianCalendar(
                2020,Calendar.JANUARY,30,8,0).getTime()));
        list.add(new RoundedTransaction("transaction3", new GregorianCalendar(
                2020,Calendar.FEBRUARY,10,8,0).getTime()));
        list.add(new RoundedTransaction("transaction4", new GregorianCalendar(
                2020,Calendar.MARCH,5,8,0).getTime()));
        st=new MapScheduledTransaction("test",list);
    }

    @Test
    void testIsCompleted(){
        assertFalse(st.isCompleted());
        list.forEach(transaction -> assertFalse(st.isCompleted(transaction)));
        list.forEach(st::markTransactionAsCompleted);
        list.forEach(transaction -> assertTrue(st.isCompleted(transaction)));
        assertTrue(st.isCompleted());
    }

    @Test
    void shouldNotReturnCompletedTransactions(){
        st.markTransactionAsCompleted(list.get(1));
        List<Transaction> result = st.getTransactions(new GregorianCalendar(
                2020,Calendar.FEBRUARY,1,8,0).getTime(),
                false);
        assertFalse(result.contains(list.get(1)));
        assertFalse(result.contains(list.get(3)));
        assertFalse(result.contains(list.get(4)));
        assertTrue(result.contains(list.get(0)));
        assertTrue(result.contains(list.get(2)));
    }

    @Test
    void shouldReturnCompletedTransactions(){
        st.markTransactionAsCompleted(list.get(1));
        List<Transaction> result = st.getTransactions(new GregorianCalendar(
                        2020,Calendar.FEBRUARY,1,8,0).getTime(),
                true);
        assertTrue(result.contains(list.get(0)));
        assertTrue(result.contains(list.get(1)));
        assertTrue(result.contains(list.get(2)));
        assertFalse(result.contains(list.get(3)));
        assertFalse(result.contains(list.get(4)));
    }
}
