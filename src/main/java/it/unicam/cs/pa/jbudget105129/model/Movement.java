package it.unicam.cs.pa.jbudget105129.model;

import it.unicam.cs.pa.jbudget105129.enums.MovementType;

import java.util.Date;
import java.util.List;
//TODO javadoc
public interface Movement {
    int getID();
    double getAmount();
    String getDescription();
    MovementType getType();
    Date getDate();
    Transaction getTransaction();
    List<Tag> getTags();
    Account getAccount();
    Movement setTransaction(Transaction transaction);
    Movement setAmount(double amount);
    Movement setType(MovementType type);
    Movement setDescription(String description);
    Movement setAccount(Account account);
    void addTag(Tag tag);
    void removeTag(Tag tag);
}
