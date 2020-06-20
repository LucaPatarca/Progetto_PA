package it.unicam.cs.pa.jbudget105129.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a set of {@link Movement}s considered as an atomic operation.
 */
public interface Transaction extends Tagged{
    /**
     * Returns the description of this transaction.
     * @return the description
     */
    String getDescription();

    /**
     * Returns the list of inner {@link Movement} contained in this transaction.
     * @return the list of movements
     */
    List<Movement> getMovements();

    /**
     * Returns the {@link LocalDate} object representing the time on which this transaction was (or will be) applied.
     * @return the transaction's date
     */
    LocalDate getDate();

    /**
     * Returns the total resulting amount of this transaction. It is calculated as the sum of all
     * {@link Movement} amount considering as positive the one of type INCOME and negative the one of
     * type OUTFLOW.
     * @return the total amount of this transaction
     */
    double getTotalAmount();

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
    void setDate(LocalDate date);
}
