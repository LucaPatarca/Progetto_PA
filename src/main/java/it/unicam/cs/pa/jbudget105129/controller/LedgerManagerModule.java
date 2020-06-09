package it.unicam.cs.pa.jbudget105129.controller;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import it.unicam.cs.pa.jbudget105129.annotations.AppLedger;
import it.unicam.cs.pa.jbudget105129.annotations.AppPersistence;
import it.unicam.cs.pa.jbudget105129.model.FamilyLedger;
import it.unicam.cs.pa.jbudget105129.model.Ledger;
import it.unicam.cs.pa.jbudget105129.persistence.JsonPersistenceManager;
import it.unicam.cs.pa.jbudget105129.persistence.PersistenceManager;

/**
 * The {@link com.google.inject.Guice} module for injecting dependencies to {@link FamilyLedgerManager}
 */
public class LedgerManagerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(LedgerManager.class).to(FamilyLedgerManager.class);
    }

    @Provides
    @AppLedger
    protected static Ledger provideLedger(){
        return new FamilyLedger();
    }

    @Provides
    @AppPersistence
    protected static PersistenceManager providePersistence(){
        return new JsonPersistenceManager();
    }
}
