package it.unicam.cs.pa.jbudget105129.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a registry for the class {@link SingleTag}, it is responsible for maintaining a registry
 * for all instances of {@link SingleTag} throughout the application. It guarantee that every {@link SingleTag}
 * has a different and unique ID.
 *
 * The method getInstance is also the only way you can create a new instance of {@link SingleTag}.
 */
public class SingleTagRegistry {

    private Map<Integer, SingleTag> registry;

    private final SingleTagConstructor factory;
    private static int nextID=0;

    /**
     * Creates a new SingleTagRegistry with the constructor as argument.
     * @param factory the constructor used to create new instances of {@link SingleTag}.
     */
    public SingleTagRegistry(SingleTagConstructor factory){
        this.factory=factory;
        this.registry=new HashMap<>();
    }

    /**
     * Returns an entry of the registry matching the specified ID, null if there is no entry for the ID.
     * @param ID the ID used to find the {@link SingleTag}.
     * @return the {@link SingleTag} in the registry or null if it can't be found.
     */
    public SingleTag getInstance(int ID) {
        return registry.get(ID);
    }

    /**
     * Returns a new instance of {@link SingleTag} with the specified parameters and a generated unique ID.
     * @param name the name of the new tag.
     * @param description the description of the new tag.
     * @return a new instance of {@link SingleTag}.
     */
    public SingleTag getInstance(String name, String description) {
        return this.getInstance(nextID,name,description);
    }

    /**
     * If the registry already contains an entry with the same ID:
     * returns the object already in the registry if it has the same ID,name and description,
     * null otherwise.
     * If the registry does not contain an entry with the same ID:
     * returns a new object and puts it in the registry.
     *
     * This method should only be used for persistence purposes.
     * @param ID the id of the tag
     * @param name the name of the tag
     * @param description the description of the tag
     * @return a {@link SingleTag} matching the parameter or null if the ID is already used by a different tag.
     */
    public SingleTag getInstance(int ID, String name, String description){
        SingleTag o = factory.create(ID,name,description);
        if (registry.containsKey(ID)){
            if (registry.get(ID).equals(o))
                return registry.get(ID);
            else throw new IllegalStateException("There is already a different tag with the ID="+ID);
        }
        if(ID>=nextID)
            nextID=ID+1;
        registry.put(ID,o);
        return o;
    }

    /**
     * Resets completely this register by removing all entries from the registry. It should only be used
     * by a LedgerManager to load a new Ledger while overwriting the old one.
     */
    public void reset(){
        registry=new HashMap<>();
        nextID=0;
    }
}
