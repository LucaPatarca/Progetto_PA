package it.unicam.cs.pa.jbudget105129.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
//TODO javadoc
public class SimpleBudget implements Budget {

    Map<Tag,Double> budgets;

    public SimpleBudget(){
        budgets=new HashMap<>();
    }

    @Override
    public List<Tag> tags() {
        return List.copyOf(budgets.keySet());
    }

    @Override
    public void set(Tag tag, double expected) {
        budgets.put(Objects.requireNonNull(tag),expected);
    }

    @Override
    public double get(Tag tag) {
        return budgets.get(tag);
    }

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
