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
    void setTransaction(Transaction transaction);
    void setAmount(double amount);
    void setDescription(String description);
    void setAccount(Account account);
    void addTag(Tag tag);
    void removeTag(Tag tag);
}
