package it.unicam.cs.pa.jbudget105129.model;

import java.util.List;

/**
 * Represents an object that can be classified with a list of {@link Tag}s.
 */
public interface Tagged {
    /**
     * Returns the list of {@link Tag}s this object contains
     * @return the list of {@link Tag}s
     */
    List<Tag> getTags();

    /**
     * Adds a {@link Tag} to the list.
     * @param tag the {@link Tag} to add
     */
    void addTag(Tag tag);

    /**
     * Removes a {@link Tag} from the list.
     * @param tag the {@link Tag} to remove
     */
    void removeTag(Tag tag);
}
