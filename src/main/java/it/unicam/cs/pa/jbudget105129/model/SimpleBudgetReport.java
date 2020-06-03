package it.unicam.cs.pa.jbudget105129.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

//TODO javadoc
public class SimpleBudgetReport implements BudgetReport {

    Budget budget;
    List<Transaction> transactions;

    public SimpleBudgetReport(Budget budget, List<Transaction> transactions){
        this.budget=budget;
        this.transactions=transactions;
    }

    @Override
    public List<Tag> tags() {
        return budget.tags();
    }

    @Override
    public Map<Tag, Double> report() {
        Map<Tag,Double> report=new HashMap<>();
        tags().forEach(tag -> report.put(tag,get(tag)));
        return report;
    }

    @Override
    public Budget getBudget() {
        return budget;
    }

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
