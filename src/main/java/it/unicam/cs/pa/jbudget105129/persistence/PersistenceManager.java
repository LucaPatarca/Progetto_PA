package it.unicam.cs.pa.jbudget105129.persistence;

import it.unicam.cs.pa.jbudget105129.model.Ledger;

import java.io.File;
import java.io.IOException;

/**
 * Represent an object that is responsible for managing the persistence of a {@link Ledger}.
 */
public interface PersistenceManager {
    /**
     * Saves the {@link Ledger} to a new file created by the given string.
     * @param ledger the ledger to save
     * @param file the file path of the file to save to
     * @throws IOException if it is not possible to create and write the file
     */
    default void save(Ledger ledger, String file) throws IOException {
        save(ledger,new File(file));
    }

    /**
     * Loads a {@link Ledger} from an existing file represented by a string
     * @param file the file path of the file to load from
     * @return the new {@link Ledger} created from the file
     * @throws IOException if it is not possible to read the file
     */
    default Ledger load(String file) throws IOException {
        return load(new File(file));
    }

    /**
     * Saves the {@link Ledger} to the {@link File} passed as argument.
     * @param ledger the ledger to save
     * @param file the {@link File} object to save to
     * @throws IOException if it is not possible to create and write the file
     */
    void save(Ledger ledger, File file) throws IOException;

    /**
     * Loads a {@link Ledger} from an existing {@link File} passed as argument.
     * @param file the file object to load from
     * @return the new {@link Ledger} created from the file
     * @throws IOException if it is not possible to read the file
     */
    Ledger load(File file) throws IOException;
}
