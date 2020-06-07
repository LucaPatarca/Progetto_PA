package it.unicam.cs.pa.jbudget105129.model;

/**
 * Represent a category that can be added to various object to categorize them.
 */
public interface Tag {
    /**
     * Returns the unique identifier of this {@link Tag}.
     * @return the tag's ID
     */
    int getID();

    /**
     * Returns the name of the tag.
     * @return the name
     */
    String getName();

    /**
     * Returns the description of the tag
     * @return the description
     */
    String getDescription();
}
