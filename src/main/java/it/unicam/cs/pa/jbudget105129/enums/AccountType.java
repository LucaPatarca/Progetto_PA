package it.unicam.cs.pa.jbudget105129.enums;

/**
 * Specifies the type of an account. Specific operation regarding an Account behaves differently depending
 * on the type of the Account.
 */
public enum AccountType {
    /**
     * This type of Account represents any amount of money (physical or not) that grows with an income and
     * shrinks with an outflow.
     */
    ASSET,
    /**
     * This type of Account represents any amount of money (physical or not) that grows with an outflow and
     * shrinks with an income.
     */
    LIABILITY,
}
