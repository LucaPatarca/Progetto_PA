package it.unicam.cs.pa.jbudget105129.model;

import java.util.Objects;

/**
 * Represent a category that can be added to various object to categorize them.
 * The creation of a new instance of SingleTag is only possible with the
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

    /**
     * Returns the class registry of this class ({@link SingleTagRegistry}). This object is a singleton.
     * @return the class registry
     */
    public static SingleTagRegistry getRegistry(){
        if(registry==null) registry=new SingleTagRegistry(SingleTag::new);
        return registry;
    }

    /**
     * Returns the instance of {@link SingleTag} corresponding to the specified id,
     * this operation is done using the class registry. If a {@link SingleTag} with this id
     * does not exist this method returns null.
     * @param ID the id of the requested {@link SingleTag}
     * @return the tag of id ID or null if it doesn't exist
     * @see SingleTagRegistry
     */
    public static SingleTag getInstance(int ID){
        return getRegistry().getInstance(ID);
    }

    /**
     * Returns a new instance of {@link SingleTag} created with the specified parameters.
     * this operation is done using the class registry. The new tag will have the specified name
     * and description and a unique generated ID, this is guaranteed by the registry.
     * @param name the new Tag's name
     * @param description the new Tag's description
     * @return a new instance of {@link SingleTag}
     * @see SingleTagRegistry
     */
    public static SingleTag getInstance(String name, String description){
        return getRegistry().getInstance(name,description);
    }

    /**
     * Returns the unique identifier of this {@link Tag}.
     * @return the tag's ID
     */
    @Override
    public int getID() {
        return ID;
    }

    /**
     * Returns the name of the tag.
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the description of the tag
     * @return the description
     */
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
