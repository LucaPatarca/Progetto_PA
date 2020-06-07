package it.unicam.cs.pa.jbudget105129.persistence;

import it.unicam.cs.pa.jbudget105129.model.Ledger;

import java.io.File;
import java.io.IOException;

public interface PersistenceManager {
    default void save(Ledger ledger, String file) throws IOException {
        save(ledger,new File(file));
    }
    default Ledger load(String file) throws IOException {
        return load(new File(file));
    }
    void save(Ledger ledger, File file) throws IOException;
    Ledger load(File file) throws IOException;
}
