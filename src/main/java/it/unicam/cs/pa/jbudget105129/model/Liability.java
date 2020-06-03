package it.unicam.cs.pa.jbudget105129.model;

import it.unicam.cs.pa.jbudget105129.enums.AccountType;

import java.util.Optional;

//TODO javadoc
public class Liability extends RoundedAccount {
    public Liability(String name, String description, double openingBalance) {
        super(name, description, openingBalance, AccountType.LIABILITY);
    }

    @Override
    public Optional<Double> getMinAmount() {
        return Optional.of(0.0);
    }

    @Override
    public void setMinAmount(double minAmount) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("this type of account does not support a minimum amount other than zero");
    }
}
