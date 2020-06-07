package it.unicam.cs.pa.jbudget105129.model;

import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import it.unicam.cs.pa.jbudget105129.enums.AccountType;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Represents a sum of money and all its information.
 */

public interface Account {
    /**
     * Returns the current balance of the account as a double value, this should be equal to the
     * sum of all movements applied to the account's opening balance.
     * @return The current balance
     */
    double getBalance();

    /**
     * Returns the balance the account had at the moment of its creation.
     * @return The opening balance
     */
    double getOpeningBalance();

    /**
     * Returns the description of the account.
     * @return The description
     */
    String getDescription();

    /**
     * Returns the name of the account.
     * @return The name
     */
    String getName();

    /**
     * Returns the name and surname of the owner of the account.
     * @return The referent name and surname
     */
    String getReferent();
    /**
     * Returns the maximum value the balance of the account can reach, in case of an asset
     * this represents the maximum amount of money the account can contain, in case of a
     * liability this represents the maximum amount the debt can reach. The return value
     * is an {@link Optional} so if an account does not have a maximum value should return an
     * empty Optional.
     * @return The max amount.
     */
    Optional<Double>  getMaxAmount();

    /**
     * Returns the minimum value the balance of the account can reach, in case of an asset
     * this represents the minimum amount of money the account can contain (usually zero but
     * some bank account can handle relatively small negative values), in case of a
     * liability this should always return zero. The return value is an {@link Optional} so if
     * an account does not have a minimum value should return an empty Optional.
     * @return The minimum amount.
     */
    Optional<Double> getMinAmount();

    /**
     * Returns the type of the account (ASSET OR LIABILITY).
     * @return The type of the account
     */
    AccountType getType();

    /**
     * Returns the list of movements associated with the account.
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
     * @throws UnsupportedOperationException if the account does not support a maximum amount
     */
    void setMaxAmount(double maxAmount) throws UnsupportedOperationException;

    /**
     * Sets a new value as the minimum value.
     * @param minAmount The new minimum value
     * @throws UnsupportedOperationException if the account does not support a minimum amount
     */
    void setMinAmount(double minAmount) throws UnsupportedOperationException;
}