package it.unicam.cs.pa.jbudget105129.model;

import it.unicam.cs.pa.jbudget105129.enums.MovementType;

import java.util.Date;
import java.util.List;

/**
 * Represents a change in the balance of an {@link Account}.
 */
public interface Movement extends Tagged{
    /**
     * Returns the unique number identifier of this movement.
     * @return the ID of this account
     */
    int getID();

    /**
     * Returns the double value representing the amount of this movement. The amount is never negative,
     * to know if the value should be considered positive or negative just look at the amount type.
     * @return the amount of this movement
     */
    double getAmount();

    /**
     * Returns the string that describes this movement
     * @return the description
     */
    String getDescription();

    /**
     * Returns the type of this movement. Depending on the type, a movement is treated differently by
     * operations that use it. For instance, the type could be used to know if the amount of the movement
     * should be treated as positive or negative.
     * @return the type of this movement
     */
    MovementType getType();

    /**
     * Returns the date on which this movement was (or will be) made. This information should not belong to
     * this movement, instead it should be obtained from the movement's transaction.
     * @return the date of this movement
     */
    Date getDate();

    /**
     * Returns a reference to the transaction this movement is part of.
     * @return the movement's transaction
     */
    Transaction getTransaction();

    /**
     * Returns all the tags of this movement
     * @return the list of {@link Tag}
     */
    List<Tag> getTags();

    /**
     * Returns the {@link Account} to which this movement applies
     * @return the movement's account.
     */
    Account getAccount();

    /**
     * Changes the {@link Transaction} reference of this movement, null values are not allowed.
     * @param transaction the new transaction reference
     */
    void setTransaction(Transaction transaction);

    /**
     * Changes the amount of this movement.
     * @param amount the new amount
     */
    void setAmount(double amount);

    /**
     * Changes the description of this movement, null values are not allowed.
     * @param description the new description
     */
    void setDescription(String description);

    /**
     * Changes the {@link Account} reference of this movement, null values are not allowed.
     * @param account the new account reference
     */
    void setAccount(Account account);

    /**
     * Adds a new {@link Tag} to this movement, null values are not allowed.
     * @param tag the new {@link Tag}
     */
    void addTag(Tag tag);

    /**
     * Removes a {@link Tag} from this movement.
     * @param tag the {@link Tag} to remove
     */
    void removeTag(Tag tag);
}
