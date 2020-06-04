package it.unicam.cs.pa.jbudget105129.model;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

// TODO: 04/06/20 javadoc
// TODO: 04/06/20 sarebbe meglio estrarre l'interfaccia
// TODO: 04/06/20 il registry potrebbe non essere static tanto questo oggetto e un singleton idem per movement
public class SingleTagRegistry {

    private static final Map<Integer, SingleTag> registry = new HashMap<>();

    private final SingleTagConstructor factory;
    private static int nextID=0;

    /**
     * Creates a new SingleTagRegistry with the constructor as argument.
     * @param factory the constructor for the registry.
     */
    public SingleTagRegistry(SingleTagConstructor factory){
        this.factory=factory;
    }

    /**
     * Returns an entry of the registry matching the specified ID, null if there is no entry for the ID.
     * @param ID the ID used to find the tag.
     * @return the tag in the registry or null if it can't be found.
     */
    public SingleTag getInstance(int ID) {
        return registry.get(ID);
    }

    /**
     * Returns a new instance of SingleTag with the specified parameters and a generated unique ID.
     * @param name the name of the new tag.
     * @param description the description of the new tag.
     * @return a new instance of SingleTag.
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
     * @param ID the id of the tag
     * @param name the name of the tag
     * @param description the description of the tag
     * @return a tag matching the parameter or null if the ID is already used by a different tag.
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
}
