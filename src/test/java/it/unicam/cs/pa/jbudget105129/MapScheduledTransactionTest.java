package it.unicam.cs.pa.jbudget105129;

import it.unicam.cs.pa.jbudget105129.model.MapScheduledTransaction;
import it.unicam.cs.pa.jbudget105129.model.RoundedTransaction;
import it.unicam.cs.pa.jbudget105129.model.ScheduledTransaction;
import it.unicam.cs.pa.jbudget105129.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MapScheduledTransactionTest {
    private ScheduledTransaction st;
    private List<Transaction> list;

    @BeforeEach
    void init(){
        list = new LinkedList<>();
        list.add(new RoundedTransaction("transaction0", LocalDate.of(2020,1,24)));
        list.add(new RoundedTransaction("transaction1", LocalDate.of(2020,1,24)));
        list.add(new RoundedTransaction("transaction2", LocalDate.of(2020,1,30)));
        list.add(new RoundedTransaction("transaction3", LocalDate.of(2020,2,10)));
        list.add(new RoundedTransaction("transaction4", LocalDate.of(2020,3,5)));
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
        List<Transaction> result = st.getTransactions(
                LocalDate.of(2020,2,1),
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
        List<Transaction> result = st.getTransactions(
                LocalDate.of(2020,2,1),
                true);
        assertTrue(result.contains(list.get(0)));
        assertTrue(result.contains(list.get(1)));
        assertTrue(result.contains(list.get(2)));
        assertFalse(result.contains(list.get(3)));
        assertFalse(result.contains(list.get(4)));
    }
}
