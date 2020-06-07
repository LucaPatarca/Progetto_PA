package it.unicam.cs.pa.jbudget105129.enums;

/**
 * Represents a type of {@link it.unicam.cs.pa.jbudget105129.model.Movement}. Specific operations regarding
 * a movement behaves differently depending on the type of movement
 */
public enum MovementType {

    /**
     * A movement that is always considered as a positive value inside a transaction.
     */
    INCOME,

    /**
     * A movement that is always considered as a negative value inside a transaction.
     */
    OUTFLOW
}
