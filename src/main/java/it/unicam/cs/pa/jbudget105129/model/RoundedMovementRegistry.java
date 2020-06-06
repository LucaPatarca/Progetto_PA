package it.unicam.cs.pa.jbudget105129.model;

import it.unicam.cs.pa.jbudget105129.enums.MovementType;

import java.util.HashMap;
import java.util.Map;

// TODO: 04/06/20 estrarre l'interfaccia (forse) e capire se si può migliorare il codice duplicato
public class RoundedMovementRegistry {

    private static Map<Integer,RoundedMovement> registry = new HashMap<>();
    private final RoundedMovementConstructor factory;
    private static int nextID=0;

    public RoundedMovementRegistry(RoundedMovementConstructor factory){
        this.factory=factory;
    }

    public RoundedMovement getInstance(int ID){
        return registry.get(ID);
    }

    public RoundedMovement getInstance(String description, double amount, MovementType type, Account account){
        return getInstance(nextID,description,amount,type,account);
    }

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
}
