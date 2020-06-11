package it.unicam.cs.pa.jbudget105129.model;

import it.unicam.cs.pa.jbudget105129.enums.AccountType;

@FunctionalInterface
public interface RoundedAccountConstructor {
    RoundedAccount create(int ID,String name, String description, double openingBalance, AccountType type);
}
