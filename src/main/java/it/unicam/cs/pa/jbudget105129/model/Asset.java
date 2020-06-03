package it.unicam.cs.pa.jbudget105129.model;

import it.unicam.cs.pa.jbudget105129.enums.AccountType;

/**
 * A simple instance of RoundedAccount with the "type" field fixed as an asset,
 * it's the same as RoundedAccount(name,description,openingBalance,AccountType.ASSET).
 * Only used to make the code more readable.
 */
public class Asset extends RoundedAccount {

    public Asset(String name, String description, double openingBalance) {
        super(name, description, openingBalance, AccountType.ASSET);
    }
}
