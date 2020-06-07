package it.unicam.cs.pa.jbudget105129.exceptions;

/**
 * Thrown any time an operation introduces an inconsistent state regarding an account (for instance when a there is
 * not enough money on the account balance to complete a transaction).
 */
public class AccountException extends Exception{

    /**
     * Creates a new {@link AccountException} with the given string as a message.
     * @param message the message of the exception
     */
    public AccountException(String message) {
        super(message);
    }
}
