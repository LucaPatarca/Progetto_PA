package it.unicam.cs.pa.jbudget105129.enums;

/**
 * Represents the type of an {@link it.unicam.cs.pa.jbudget105129.model.Account}. Specific operation regarding an
 * account behaves differently depending on the type of the Account.
 */
public enum AccountType {

    /**
     * This type of account represents any amount of money that grows with an income and
     * decrease with an outflow.
     */
    ASSET,

    /**
     * This type of Account represents any amount of money that grows with an outflow and
     * decrease with an income.
     */
    LIABILITY,
}
