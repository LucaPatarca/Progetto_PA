package it.unicam.cs.pa.jbudget105129.Dependency;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import it.unicam.cs.pa.jbudget105129.controller.FamilyLedgerManager;
import it.unicam.cs.pa.jbudget105129.controller.LedgerManager;
import it.unicam.cs.pa.jbudget105129.model.FamilyLedger;
import it.unicam.cs.pa.jbudget105129.model.Ledger;
import it.unicam.cs.pa.jbudget105129.persistence.JsonPersistenceManager;
import it.unicam.cs.pa.jbudget105129.persistence.PersistenceManager;

public class LedgerManagerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(LedgerManager.class).to(FamilyLedgerManager.class);
    }

    @Provides
    @AppLedger
    static Ledger provideLedger(){
        return new FamilyLedger();
    }

    @Provides
    @AppPersistence
    static PersistenceManager providePersistence(){
        return new JsonPersistenceManager();
    }
}
