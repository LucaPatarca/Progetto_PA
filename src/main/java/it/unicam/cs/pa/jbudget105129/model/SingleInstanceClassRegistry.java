package it.unicam.cs.pa.jbudget105129.model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SingleInstanceClassRegistry<T> implements ClassRegistry<T>{

    private final Map<Integer, T> registry;

    private final Function<Integer,T> factory;
    private static int nextID=0;

    public SingleInstanceClassRegistry(Function<Integer,T> factory){
        this.factory=factory;
        registry=new HashMap<>();
    }

    @Override
    public T getInstance(int ID) {
        if(registry.containsKey(ID)){
            return registry.get(ID);
        }else {
            T o = factory.apply(ID);
            registry.put(ID,o);
            if(nextID<=ID){
                nextID=ID+1;
            }
            return o;
        }
    }

    public T getInstance() {
        return getInstance(nextID);
    }
}
