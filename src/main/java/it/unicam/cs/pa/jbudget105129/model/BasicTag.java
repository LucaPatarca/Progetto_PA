package it.unicam.cs.pa.jbudget105129.model;
//TODO javadoc

import java.util.Objects;

/**
 * A basic implementation of a Tag.
 */
public class BasicTag implements Tag {

    private final String name;
    private final String description;
    private static BasicTagRegistry registry;

    private BasicTag(String name, String description){
        this.name=name;
        this.description=description;
    }

    public static BasicTagRegistry getRegistry(){
        if(registry==null) registry=new BasicTagRegistry(BasicTag::new);
        return registry;
    }

    public static BasicTag getInstance(String name, String description){
        return getRegistry().getInstance(name,description);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasicTag)) return false;
        BasicTag basicTag = (BasicTag) o;
        return getName().equals(basicTag.getName()) &&
                getDescription().equals(basicTag.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription());
    }

    @Override
    public String toString() {
        return "BasicTag{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
