package it.unicam.cs.pa.jbudget105129.model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class RoundedMovementRegistry implements ClassRegistry<RoundedMovement>{

    private static Map<Integer, RoundedMovement> registry;

    private final Function<Integer,RoundedMovement> factory;
    private static int nextID=0;

    public RoundedMovementRegistry(Function<Integer,RoundedMovement> factory){
        this.factory=factory;
        if(registry==null)
            registry=new HashMap<>();
    }

    @Override
    public RoundedMovement getInstance(int ID) {
        if(registry.containsKey(ID)){
            return registry.get(ID);
        }else {
            RoundedMovement movement = factory.apply(nextID++);
            registry.put(ID,movement);
            return movement;
        }
    }

    public RoundedMovement getInstance() {
        return getInstance(nextID);
    }
}
