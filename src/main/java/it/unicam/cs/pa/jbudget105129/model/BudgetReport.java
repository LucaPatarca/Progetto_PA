package it.unicam.cs.pa.jbudget105129.model;

import java.util.List;
import java.util.Map;

//TODO javadoc
public interface BudgetReport {
    List<Tag> tags();
    Map<Tag,Double> report();
    Budget getBudget();
    double get(Tag tag);
}
