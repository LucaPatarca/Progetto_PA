package it.unicam.cs.pa.jbudget105129.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

public class BasicTagRegistry {
    private static Map<Integer,BasicTag> registry;
    private BiFunction<String,String,BasicTag> factory;

    public BasicTagRegistry(BiFunction<String,String,BasicTag> factory){
        if(registry==null) registry=new HashMap<>();
        this.factory=factory;
    }

    public BasicTag getInstance(String name, String description){
        int hash = Objects.hash(name,description);
        if(registry.containsKey(hash))
            return registry.get(hash);
        else{
            return factory.apply(name,description);
        }
    }
}
