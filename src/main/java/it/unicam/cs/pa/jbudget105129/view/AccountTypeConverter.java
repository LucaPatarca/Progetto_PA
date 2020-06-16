package it.unicam.cs.pa.jbudget105129.view;

import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import javafx.util.StringConverter;

public class AccountTypeConverter extends StringConverter<AccountType> {
    @Override
    public String toString(AccountType accountType) {
        return accountType.toString().toLowerCase();
    }

    @Override
    public AccountType fromString(String s) {
        return null;
    }
}