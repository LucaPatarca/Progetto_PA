package it.unicam.cs.pa.jbudget105129.model;

import it.unicam.cs.pa.jbudget105129.enums.MovementType;

public interface RoundedMovementConstructor {
    RoundedMovement create(int ID,String description, double amount, MovementType type,Account account);
}
