package it.unicam.cs.pa.jbudget105129.view;

import com.google.inject.AbstractModule;

public class ViewModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(LedgerPrinter.class).to(SimpleLedgerPrinter.class);
    }
}
