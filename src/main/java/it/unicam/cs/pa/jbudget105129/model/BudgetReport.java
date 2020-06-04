package it.unicam.cs.pa.jbudget105129.model;

import java.util.List;
import java.util.Map;

//TODO javadoc

/**
 * Represents an object responsible for showing the progress of each category relative to a budget.
 * Instances of objects implementing this interface should be created only by a BudgetManager.
 */
public interface BudgetReport {
    /**
     * Returns the list of tags the inner budget contains.
     * @return the list of tags
     */
    List<Tag> tags();

    /**
     * Returns a map object representing the report,
     * @return
     */
    Map<Tag,Double> report();
    Budget getBudget();
    double get(Tag tag);
}
