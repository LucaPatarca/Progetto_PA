package it.unicam.cs.pa.jbudget105129.persistence;

import it.unicam.cs.pa.jbudget105129.model.Ledger;

import java.io.IOException;

/**
 * Represent an object that is responsible for managing the persistence of a {@link Ledger}.
 */
public interface PersistenceManager {

    /**
     * Saves the {@link Ledger} to the path represented by the {@link String} passed as argument.
     * @param ledger the ledger to save
     * @param path the {@link String} representing the path to save to
     * @throws IOException if it is not possible to create and write to the path
     */
    void save(Ledger ledger, String path) throws IOException;

    /**
     * Loads a {@link Ledger} from the path represented by the {@link String} passed as argument.
     * @param path the path to load from
     * @return the new {@link Ledger} created from the path
     * @throws IOException if it is not possible to read from the path
     */
    Ledger load(String path) throws IOException;
}
