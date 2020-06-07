package it.unicam.cs.pa.jbudget105129.model;

/**
 * A functional interface representing the {@link SingleTag} constructor.
 */
@FunctionalInterface
public interface SingleTagConstructor {
    SingleTag create(int ID,String name, String description);
}
