package it.unicam.cs.pa.jbudget105129.view;

import com.google.inject.Inject;
import it.unicam.cs.pa.jbudget105129.model.Account;
import javafx.util.StringConverter;

public class AccountConverter extends StringConverter<Account> {

    private final LedgerPrinter printer;

    @Inject
    public AccountConverter(LedgerPrinter printer){
        this.printer=printer;
    }
    @Override
    public String toString(Account account) {
        return printer.stringOf(account);
    }

    @Override
    public Account fromString(String s) {
        return null;
    }
}
