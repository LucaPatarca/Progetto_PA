package it.unicam.cs.pa.jbudget105129.model;

import it.unicam.cs.pa.jbudget105129.enums.AccountType;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a registry for the class {@link RoundedAccount}, it is responsible for maintaining a registry
 * for all instances of {@link RoundedAccount} throughout the application. It guarantee that every {@link RoundedAccount}
 * has a different and unique ID.
 *
 * The method getInstance is also the only way you can create a new instance of {@link RoundedAccount}.
 */
public class RoundedAccountRegistry {

    private Map<Integer,RoundedAccount> registry = new HashMap<>();
    private final RoundedAccountConstructor factory;
    private static int nextID=0;

    /**
     * Creates a new {@link RoundedAccountRegistry} with the constructor as argument.
     * @param factory the constructor used to create new instances of {@link RoundedAccount}.
     */
    protected RoundedAccountRegistry(RoundedAccountConstructor factory){
        this.factory=factory;
    }

    /**
     * Returns an entry of the registry matching the specified ID, null if there is no entry for the ID.
     * @param ID the ID used to find the {@link RoundedAccount}.
     * @return the {@link RoundedAccount} in the registry or null if it can't be found.
     */
    public RoundedAccount getInstance(int ID){
        return registry.get(ID);
    }

    /**
     * Returns a new instance of {@link RoundedMovement} with the specified parameters and a generated unique ID.
     * @param name the name of the account
     * @param description the description of the account
     * @param openingBalance the opening balance of the account
     * @param type the type of the account
     * @return a new instance of {@link RoundedAccount}.
     */
    public RoundedAccount getInstance(String name, String description, double openingBalance, AccountType type){
        return getInstance(nextID,name,description,openingBalance,type);
    }

    /**
     * If the registry already contains an entry with the same ID:
     * returns the object already in the registry if it has the same ID,name and description,
     * null otherwise.
     * If the registry does not contain an entry with the same ID:
     * returns a new object and puts it in the registry.
     * @param ID the id of the account
     * @param name the name of the account
     * @param description the description of the account
     * @param openingBalance the opening balance of the account
     * @param type the type of the account
     * @return a {@link RoundedMovement} matching the parameter or null if the ID is already used by a different movement.
     */
    public RoundedAccount getInstance(int ID, String name, String description, double openingBalance, AccountType type){
        RoundedAccount o = factory.create(ID,name,description,openingBalance,type);
        if (registry.containsKey(ID)){
            if (registry.get(ID).equals(o))
                return registry.get(ID);
            else throw new IllegalStateException("There is already a different account with the ID="+ID);
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
