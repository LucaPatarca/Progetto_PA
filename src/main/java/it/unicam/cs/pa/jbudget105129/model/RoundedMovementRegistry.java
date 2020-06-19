package it.unicam.cs.pa.jbudget105129.model;

import it.unicam.cs.pa.jbudget105129.enums.MovementType;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a registry for the class {@link RoundedMovement}, it is responsible for maintaining a registry
 * for all instances of {@link RoundedMovement} throughout the application. It guarantee that every {@link RoundedMovement}
 * has a different and unique ID.
 *
 * The method getInstance is also the only way you can create a new instance of {@link RoundedMovement}.
 */
public class RoundedMovementRegistry {

    private Map<Integer,RoundedMovement> registry = new HashMap<>();
    private final RoundedMovementConstructor factory;
    private static int nextID=0;

    /**
     * Creates a new RoundedMovementRegistry with the constructor as argument.
     * @param factory the constructor used to create new instances of {@link RoundedMovement}.
     */
    public RoundedMovementRegistry(RoundedMovementConstructor factory){
        this.factory=factory;
    }

    /**
     * Returns an entry of the registry matching the specified ID, null if there is no entry for the ID.
     * @param ID the ID used to find the {@link RoundedMovement}.
     * @return the {@link RoundedMovement} in the registry or null if it can't be found.
     */
    public RoundedMovement getInstance(int ID){
        return registry.get(ID);
    }

    /**
     * Returns a new instance of {@link RoundedMovement} with the specified parameters and a generated unique ID.
     * @param description the description of the movement
     * @param amount the amount of the movement
     * @param type the type of the movement
     * @param account the account of the movement
     * @return a new instance of {@link RoundedMovement}.
     */
    public RoundedMovement getInstance(String description, double amount, MovementType type, Account account){
        return getInstance(nextID,description,amount,type,account);
    }

    /**
     * If the registry already contains an entry with the same ID:
     * returns the object already in the registry if it has the same ID,name and description,
     * null otherwise.
     * If the registry does not contain an entry with the same ID:
     * returns a new object and puts it in the registry.
     * @param ID the id of the movement
     * @param description the description of the movement
     * @param amount the amount of the movement
     * @param type the type of the movement
     * @param account the account of the movement
     * @return a {@link RoundedMovement} matching the parameter or null if the ID is already used by a different movement.
     */
    public RoundedMovement getInstance(int ID, String description, double amount, MovementType type, Account account){
        RoundedMovement o = factory.create(ID,description,amount,type,account);
        if (registry.containsKey(ID)){
            if (registry.get(ID).equals(o))
                return registry.get(ID);
            else throw new IllegalStateException("There is already a different movement with the ID="+ID);
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
