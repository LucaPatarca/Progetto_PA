package it.unicam.cs.pa.jbudget105129.model;

/**
 * Represents an object that has the responsibility to implement a registry for a class T.
 * @param <T> the type of class this registry can handle
 */
public interface ClassRegistry<T> {

    /**
     * If the registry already contains an entry with the specified ID returns that object,
     * otherwise it creates a new object with the specified ID.
     */
    T getInstance(int ID);

    /**
     * Creates a new instance of type T using the method getInstance(int ID) with a new unique ID
     */
    T getInstance();
}
