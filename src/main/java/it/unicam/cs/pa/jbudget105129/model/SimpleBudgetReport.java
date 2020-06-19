package it.unicam.cs.pa.jbudget105129.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * An object responsible for showing the progress of each category in relation to a budget.
 * this should only be created by a BudgetManager.
 */
public class SimpleBudgetReport implements BudgetReport {

    private final Budget budget;
    private final List<Transaction> transactions;

    /**
     * Creates a new {@link SimpleBudgetReport} given a {@link Budget} and a list of transactions, the transactions
     * are used to get information about current expenses.
     * @param budget the {@link Budget}
     * @param transactions the {@link Transaction}s list
     */
    public SimpleBudgetReport(Budget budget, List<Transaction> transactions){
        this.budget=budget;
        this.transactions=transactions;
    }

    /**
     * Returns the list of tags the inner budget contains.
     * @return the list of tags
     */
    @Override
    public List<Tag> tags() {
        return budget.tags();
    }

    /**
     * Returns a map object representing the report, each {@link Tag} is linked to a double value representing
     * the difference between the expected expense and the current expense.
     * @return the map object representing the report.
     */
    @Override
    public Map<Tag, Double> report() {
        Map<Tag,Double> report=new HashMap<>();
        tags().forEach(tag -> report.put(tag,get(tag)));
        return report;
    }

    /**
     * Returns the {@link Budget} this report contains.
     * @return the {@link Budget}
     */
    @Override
    public Budget getBudget() {
        return budget;
    }

    /**
     * Returns the report of a {@link Tag}, the report is represented by a double value calculated as
     * the difference between the expected expense and the current expense.
     * @param tag the tag to report
     * @return the report value
     */
    @Override
    public double get(Tag tag) {
        return budget.get(tag)-transactions.stream()
                .filter(t->t.getTags().contains(tag))
                .mapToDouble(Transaction::getTotalAmount)
                .sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleBudgetReport)) return false;
        SimpleBudgetReport that = (SimpleBudgetReport) o;
        return getBudget().equals(that.getBudget()) &&
                transactions.equals(that.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBudget(), transactions);
    }

    @Override
    public String toString() {
        return "SimpleBudgetReport{" +
                "budget=" + budget +
                ", transactions=" + transactions +
                '}';
    }
}
