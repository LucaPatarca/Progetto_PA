package it.unicam.cs.pa.jbudget105129.model;

import java.util.List;

public interface Tagged {
    List<Tag> getTags();
    void addTag(Tag tag);
    void removeTag(Tag tag);
}
