package it.unicam.cs.pa.jbudget105129.model;

import java.util.Date;
import java.util.List;
//TODO javadoc
public interface Transaction {
    String getDescription();
    List<Tag> getTags();
    List<Movement> getMovements();
    Date getDate();
    double getTotalAmount();

    void addTag(Tag tag);
    void removeTag(Tag tag);
    void addMovement(Movement movement);
    void removeMovement(Movement movement);

    void setDate(Date date);
}
