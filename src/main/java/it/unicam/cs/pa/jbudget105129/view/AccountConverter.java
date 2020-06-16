package it.unicam.cs.pa.jbudget105129.view;

import it.unicam.cs.pa.jbudget105129.model.Account;
import javafx.util.StringConverter;

public class AccountConverter extends StringConverter<Account> {
    @Override
    public String toString(Account account) {
        return account.getName()+", "+account.getDescription();
    }

    @Override
    public Account fromString(String s) {
        return null;
    }
}
