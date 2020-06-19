package it.unicam.cs.pa.jbudget105129.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
/**
 * Represents an object having the responsibility to link a tag (which represents a category)
 * to a double value (which represents the amount expected to be spend on that category).
 */
public class SimpleBudget implements Budget {

    private final Map<Tag,Double> budgets;

    /**
     * Creates a new empty {@link SimpleBudget}.
     */
    public SimpleBudget(){
        budgets=new HashMap<>();
    }

    /**
     * Returns the list of {@link Tag} this budget contains.
     * @return The list of tags
     */
    @Override
    public List<Tag> tags() {
        return List.copyOf(budgets.keySet());
    }

    /**
     * Sets a new amount to be linked to a {@link Tag}, if the tag is not present it
     * is added to the list.
     * @param tag The tag for the new value
     * @param expected The new value
     */
    @Override
    public void set(Tag tag, double expected) {
        budgets.put(Objects.requireNonNull(tag),expected);
    }

    /**
     * Returns the expected value linked to a  {@link Tag}.
     * @param tag The tag
     * @return The expected value
     */
    @Override
    public double get(Tag tag) {
        return budgets.get(tag);
    }

    /**
     * Returns the predicate used to select transactions related
     * to this budget.
     * @return The predicate
     */
    @Override
    public Predicate<Transaction> getPredicate() {
        return t -> t.getTags().stream().anyMatch(budgets::containsKey);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleBudget)) return false;
        SimpleBudget that = (SimpleBudget) o;
        return budgets.equals(that.budgets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(budgets);
    }

    @Override
    public String toString() {
        return "SimpleBudget{" +
                "budgets=" + budgets +
                '}';
    }
}
