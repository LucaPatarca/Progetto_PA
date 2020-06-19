package it.unicam.cs.pa.jbudget105129.view;

import com.google.inject.Inject;
import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import javafx.util.StringConverter;

public class AccountTypeConverter extends StringConverter<AccountType> {

    private final LedgerPrinter printer;

    @Inject
    public AccountTypeConverter(LedgerPrinter printer){
        this.printer=printer;
    }

    @Override
    public String toString(AccountType accountType) {
        return printer.stringOf(accountType);
    }

    @Override
    public AccountType fromString(String s) {
        return null;
    }
}