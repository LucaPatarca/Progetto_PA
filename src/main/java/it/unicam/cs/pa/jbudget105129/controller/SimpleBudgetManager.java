package it.unicam.cs.pa.jbudget105129.controller;

import it.unicam.cs.pa.jbudget105129.model.SimpleBudgetReport;
import it.unicam.cs.pa.jbudget105129.model.Budget;
import it.unicam.cs.pa.jbudget105129.model.BudgetReport;
import it.unicam.cs.pa.jbudget105129.model.Ledger;
// TODO: 09/06/20 trovare il modo di implementare.
/**
 * An object used to generate a {@link BudgetReport} from a {@link Ledger} and a {@link Budget}
 */
public class SimpleBudgetManager implements BudgetManager {
    @Override
    public BudgetReport generateReport(Ledger ledger, Budget budget) {
        return new SimpleBudgetReport(budget,ledger.getTransactions(budget.getPredicate()));
    }
}
