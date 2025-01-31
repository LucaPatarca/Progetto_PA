package it.unicam.cs.pa.jbudget105129.controller;

import it.unicam.cs.pa.jbudget105129.model.SimpleBudgetReport;
import it.unicam.cs.pa.jbudget105129.model.Budget;
import it.unicam.cs.pa.jbudget105129.model.BudgetReport;
import it.unicam.cs.pa.jbudget105129.model.Ledger;
/**
 * An object used to generate a {@link SimpleBudgetReport} from a {@link Ledger} and a {@link Budget}
 */
public class SimpleBudgetManager implements BudgetManager {
    @Override
    public BudgetReport generateReport(Ledger ledger, Budget budget) {
        return new SimpleBudgetReport(budget,ledger.getTransactions(budget.getPredicate()));
    }
}
