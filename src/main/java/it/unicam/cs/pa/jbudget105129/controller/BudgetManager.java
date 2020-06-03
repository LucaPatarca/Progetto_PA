package it.unicam.cs.pa.jbudget105129.controller;

import it.unicam.cs.pa.jbudget105129.model.BudgetReport;
import it.unicam.cs.pa.jbudget105129.model.Ledger;
import it.unicam.cs.pa.jbudget105129.model.Budget;

/**
 * Represents an object having the responsibility to generate a BudgetReport from
 * a Budget and a Ledger
 */
public interface BudgetManager {
    BudgetReport generateReport(Ledger ledger, Budget budget);
}
