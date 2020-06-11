package it.unicam.cs.pa.jbudget105129.model;

import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import it.unicam.cs.pa.jbudget105129.enums.AccountType;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Represents an amount of money and all its information, it keeps track of the balance and all
 * the {@link Movement} associated with it.
 */

public interface Account {
    int getID();

    /**
     * Returns the current balance of the account as a double value, this is calculated as the
     * sum of all movements applied to this account's opening balance.
     * @return The current balance
     */
    double getBalance();

    /**
     * Returns the balance the account had at the moment of its creation.
     * @return The opening balance
     */
    double getOpeningBalance();

    /**
     * The literal description of this account.
     * @return a {@link String} representing the description
     */
    String getDescription();

    /**
     * The name of this account.
     * @return a {@link String} representing the name
     */
    String getName();

    /**
     * Returns the name and surname of the owner of this account.
     * @return The referent's name and surname
     */
    String getReferent();

    /**
     * Returns the maximum value the balance of this account can reach, in case of an ASSET
     * this represents the maximum amount of money this account can contain, in case of a
     * LIABILITY this represents the maximum amount the debt can reach. The return value
     * is an {@link Optional} so if an account does not have a maximum value this returns an
     * empty {@link Optional}.
     * @return The max amount.
     */
    Optional<Double>  getMaxAmount();

    /**
     * Returns the minimum value the balance of this account can reach, in case of an ASSET
     * this represents the minimum amount of money this account can contain (usually zero but
     * some bank account can handle relatively small negative values), in case of a
     * LIABILITY this should always return zero. The return value is an {@link Optional} so if
     * an account does not have a minimum value should return an empty {@link Optional}.
     * @return The minimum amount.
     */
    Optional<Double> getMinAmount();

    /**
     * Returns the type of this account (ASSET OR LIABILITY).
     * @return The type of this account
     */
    AccountType getType();

    /**
     * Returns the list of {@link Movement} associated with this account.
     * @return The list of movements
     */
    List<Movement> getMovements();

    /**
     * Returns the list of {@link Movement} associated with the account that satisfy a {@link Predicate}
     * @param predicate The predicate used to filter the movements
     * @return The list of movements
     */
    List<Movement> getMovements(Predicate<Movement> predicate);

    /**
     * Adds a {@link Movement} to the account, the balance should be recalculated considering the
     * new movement.
     * @param movement The new movement to add
     * @throws AccountException If the new movement introduces a problem to the account balance
     * (for example if the balance goes below the minimum or above the maximum value).
     */
    void addMovement(Movement movement) throws AccountException;

    /**
     * Removes a {@link Movement} from the account, the balance should be recalculated considering the
     * missing movement.
     * @param movement The movement to be removed
     * @throws AccountException If the missing movement introduces a problem to the account balance
     * (for example if the balance goes below the minimum or above the maximum value).
     */
    void removeMovement(Movement movement) throws AccountException;

    /**
     * Changes the referent (the owner of the account).
     * @param referent The name and surname of the new referent
     */
    void setReferent(String referent);

    /**
     * Changes the description of the account.
     * @param description The new description
     */
    void setDescription(String description);

    /**
     * Changes the name of the account.
     * @param name The new name
     */
    void setName(String name);

    /**
     * Sets a new value as the maximum value.
     * @param maxAmount The new maximum value
     * @throws UnsupportedOperationException if this account does not support a maximum amount
     */
    void setMaxAmount(Double maxAmount) throws UnsupportedOperationException;

    /**
     * Sets a new value as the minimum value.
     * @param minAmount The new minimum value
     * @throws UnsupportedOperationException if this account does not support a minimum amount
     */
    void setMinAmount(Double minAmount) throws UnsupportedOperationException;
}