package it.unicam.cs.pa.jbudget105129;

import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import it.unicam.cs.pa.jbudget105129.model.Account;
import it.unicam.cs.pa.jbudget105129.model.Movement;
import it.unicam.cs.pa.jbudget105129.model.RoundedAccount;
import it.unicam.cs.pa.jbudget105129.model.RoundedMovement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class RoundedAccountTest {
    static Account asset;
    static Account liability;

    @BeforeAll
    static void init(){
        asset = new RoundedAccount("asset","",0, AccountType.ASSET);
        liability = new RoundedAccount("liability","", 100,AccountType.LIABILITY);
    }

    @Test
    void testMinMax(){
        assertEquals(Optional.empty(),asset.getMaxAmount());
        assertEquals(Optional.empty(),asset.getMinAmount());
        assertEquals(Optional.empty(),liability.getMaxAmount());
        assertEquals(Optional.empty(),liability.getMinAmount());
        asset.setMinAmount(-100);
        asset.setMaxAmount(200);
        liability.setMinAmount(0);
        liability.setMaxAmount(300);
        assertTrue(asset.getMaxAmount().isPresent());
        assertTrue(asset.getMinAmount().isPresent());
        assertTrue(liability.getMaxAmount().isPresent());
        assertTrue(liability.getMinAmount().isPresent());
        assertEquals(-100,asset.getMinAmount().get());
        assertEquals(200,asset.getMaxAmount().get());
        assertEquals(0,liability.getMinAmount().get());
        assertEquals(300,liability.getMaxAmount().get());
    }

    @Test
    void shouldUpdateBalance() {
        assertEquals(asset.getBalance(),asset.getOpeningBalance());
        Movement movement1 = RoundedMovement.getInstance()
            .setDescription("movement+10")
            .setAmount(10)
            .setType(MovementType.INCOME)
            .setAccount(asset);
        asset.addMovement(movement1);
        assertEquals(asset.getBalance(),10);
        Movement movement2= RoundedMovement.getInstance()
                .setDescription("movement-15")
                .setAmount(15)
                .setType(MovementType.OUTFLOW)
                .setAccount(asset);
        asset.addMovement(movement2);
        assertEquals(asset.getBalance(),-5);

        assertEquals(liability.getBalance(),liability.getOpeningBalance());
        liability.addMovement(movement1);
        assertEquals(liability.getBalance(),90);
        liability.addMovement(movement2);
        assertEquals(liability.getBalance(),105);
    }

    @Test
    void shouldNotThrowAccountException() {
        Movement movement1 = RoundedMovement.getInstance()
                .setDescription("on the min edge")
                .setAmount(95)
                .setType(MovementType.OUTFLOW)
                .setAccount(asset);
        asset.addMovement(movement1);
        assertEquals(asset.getBalance(),-100);
        Movement movement2=RoundedMovement.getInstance()
                .setDescription("on the max edge")
                .setAmount(300)
                .setType(MovementType.INCOME)
                .setAccount(asset);
        asset.addMovement(movement2);
        assertEquals(asset.getBalance(), 200);

        Movement movement3=RoundedMovement.getInstance()
                .setDescription("on the min edge")
                .setAmount(105)
                .setType(MovementType.INCOME)
                .setAccount(liability);
        liability.addMovement(movement3);
        assertEquals(liability.getBalance(),0);
        Movement movement4=RoundedMovement.getInstance()
                .setDescription("on the max edge")
                .setAmount(300)
                .setType(MovementType.OUTFLOW)
                .setAccount(liability);
        liability.addMovement(movement4);
        assertEquals(liability.getBalance(),300);
    }

    @Test
    void shouldTrowAccountException(){
        Movement movement1 = RoundedMovement.getInstance()
                .setDescription("above maximum")
                .setAmount(10)
                .setType(MovementType.INCOME)
                .setAccount(asset);
        assertThrows(AccountException.class,()->asset.addMovement(movement1));
        assertEquals(asset.getBalance(),200);
        Movement movement2 = RoundedMovement.getInstance()
                .setDescription("below minimum")
                .setAmount(310)
                .setType(MovementType.OUTFLOW)
                .setAccount(asset);
        assertThrows(AccountException.class, ()->asset.addMovement(movement2));
        assertEquals(asset.getBalance(), 200);

        Movement movement3 = RoundedMovement.getInstance()
                .setDescription("above maximum")
                .setAmount(10)
                .setType(MovementType.OUTFLOW)
                .setAccount(liability);
        assertThrows(AccountException.class,()->liability.addMovement(movement3));
        assertEquals(liability.getBalance(),300);
        Movement movement4 = RoundedMovement.getInstance()
                .setDescription("below minimum")
                .setAmount(310)
                .setType(MovementType.INCOME)
                .setAccount(liability);
        assertThrows(AccountException.class, ()->liability.addMovement(movement4));
        assertEquals(liability.getBalance(), 300);
    }
}
