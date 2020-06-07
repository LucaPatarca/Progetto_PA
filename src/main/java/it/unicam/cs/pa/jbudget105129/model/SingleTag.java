package it.unicam.cs.pa.jbudget105129.model;
//TODO javadoc

import java.util.Objects;

/**
 * A basic implementation of a Tag. This particular implementation allow only one instance at a time
 * of a tag with the same ID. The creation of a new instance of SingleTag is only possible with the
 * static method getInstance().
 */
public class SingleTag implements Tag {

    private final String name;
    private final String description;
    private final int ID;
    private static SingleTagRegistry registry;

    private SingleTag(int ID, String name, String description){
        this.ID=ID;
        this.name=name;
        this.description=description;
    }

    public static SingleTagRegistry getRegistry(){
        if(registry==null) registry=new SingleTagRegistry(SingleTag::new);
        return registry;
    }

    public static SingleTag getInstance(int ID){
        return getRegistry().getInstance(ID);
    }

    public static SingleTag getInstance(String name, String description){
        // TODO: 04/06/20 scrivere da qualche parte che il metodo con tutti e tre i parametri non lo metto perche serve solo alla persistenza.
        return getRegistry().getInstance(name,description);
    }

    @Override
    public int getID() {
        return ID;
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
        if (!(o instanceof SingleTag)) return false;
        SingleTag singleTag = (SingleTag) o;
        return getID() == singleTag.getID() &&
                getName().equals(singleTag.getName()) &&
                getDescription().equals(singleTag.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getID());
    }

    @Override
    public String toString() {
        return "SingleTag{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ID=" + ID +
                '}';
    }
}
