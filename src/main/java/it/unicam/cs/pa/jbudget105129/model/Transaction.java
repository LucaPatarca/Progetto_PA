package it.unicam.cs.pa.jbudget105129.model;

import java.util.Date;
import java.util.List;

/**
 * Represents a set of {@link Movement} that is considered as an atomic operation.
 */
public interface Transaction extends Tagged{
    /**
     * Returns the description of this transaction.
     * @return the description
     */
    String getDescription();

    /**
     * Returns a list of distinct (no repeated elements) {@link Tag} that are present on at least one
     * of the inner {@link Movement}.
     * @return the list of tags
     */
    List<Tag> getTags();

    /**
     * Returns the list of inner {@link Movement} contained in this transaction.
     * @return the list of movements
     */
    List<Movement> getMovements();

    /**
     * Returns the {@link Date} object representing the time on which this transaction was (or will be) applied.
     * @return the transaction's date
     */
    Date getDate();

    /**
     * Returns the total resulting amount of this transaction. It is calculated as the sum of all
     * {@link Movement} amount considering as positive the one of type INCOME and negative the one of
     * type OUTFLOW.
     * @return the total amount of this transaction
     */
    double getTotalAmount();

    /**
     * Adds a tag to all the inner movements and, consequently, to this transaction.
     * @param tag the tag to add
     */
    void addTag(Tag tag);

    /**
     * Removes a tag from all the inner movements and, consequently, from this transaction.
     * @param tag the tag to remove
     */
    void removeTag(Tag tag);

    /**
     * Adds a {@link Movement} to this transaction's list.
     * @param movement the {@link Movement} to add
     */
    void addMovement(Movement movement);

    /**
     * Removes a {@link Movement} from this transaction's list.
     * @param movement the {@link Movement} to remove
     */
    void removeMovement(Movement movement);

    /**
     * Changes the date of this transaction.
     * @param date the new date
     */
    void setDate(Date date);
}
