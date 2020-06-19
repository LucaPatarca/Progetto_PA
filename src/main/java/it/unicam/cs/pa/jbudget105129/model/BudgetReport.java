package it.unicam.cs.pa.jbudget105129.model;

import it.unicam.cs.pa.jbudget105129.controller.BudgetManager;

import java.util.List;
import java.util.Map;

/**
 * Represents an object responsible for showing the progress of each category in relation to a budget.
 * Instances of objects implementing this interface should only be created by a {@link BudgetManager}.
 */
public interface BudgetReport {
    /**
     * Returns the list of tags the inner budget contains.
     * @return the list of tags
     */
    List<Tag> tags();

    /**
     * Returns a map object representing the report, each {@link Tag} is linked to a double value representing
     * the difference between the expected expense and the current expense.
     * @return the map object representing the report.
     */
    Map<Tag,Double> report();

    /**
     * Returns the {@link Budget} this report contains.
     * @return the {@link Budget}
     */
    Budget getBudget();

    /**
     * Returns the report of a {@link Tag}, the report is represented by a double value calculated as
     * the difference between the expected expense and the current expense.
     * @param tag the tag to report
     * @return the report value
     */
    double get(Tag tag);
}
