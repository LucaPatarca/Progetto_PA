package it.unicam.cs.pa.jbudget105129.model;

import java.util.List;
import java.util.function.Predicate;

/**
 * Represents an object having the responsibility to link a tag (which represents a category)
 * to a double value (which represents the amount expected to be spend on that category).
 */
public interface Budget {
    /**
     * Returns the list of {@link Tag} this budget contains.
     * @return The list of tags
     */
    List<Tag> tags();

    /**
     * Sets a new amount to be linked to a {@link Tag}, if the tag is not present it
     * is added to the list.
     * @param tag The tag for the new value
     * @param expected The new value
     */
    void set(Tag tag, double expected);

    /**
     * Returns the expected value linked to a {@link Tag}.
     * @param tag The tag
     * @return The expected value
     */
    double get(Tag tag);

    /**
     * Returns the predicate used to select transactions related
     * to this budget.
     * @return The predicate
     */
    Predicate<Transaction> getPredicate();
}
