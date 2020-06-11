package it.unicam.cs.pa.jbudget105129.persistence;

import com.google.gson.*;
import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import it.unicam.cs.pa.jbudget105129.model.*;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovementTypeAdapter implements JsonSerializer<Movement>, JsonDeserializer<Movement> {

    private static Set<Integer> alreadySaved;

    public MovementTypeAdapter(){
        alreadySaved = new HashSet<>();
    }

    @Override
    public Movement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jo =json.getAsJsonObject();
        if(jo.has("lazyID")){
            return RoundedMovement.getInstance(jo.get("lazyID").getAsInt());
        } else {
            Movement o = RoundedMovement.getRegistry().getInstance(jo.get("ID").getAsInt(),
                    jo.get("description").getAsString(),
                    jo.get("amount").getAsDouble(),
                    context.deserialize(jo.get("type"), MovementType.class),
                    null);
            JsonArray ja = jo.getAsJsonArray("tags");
            ja.forEach(element->{
                JsonObject jtag = element.getAsJsonObject();
                Tag tag = context.deserialize(jtag,Tag.class);
                o.addTag(tag);
            });
            return o;
        }
    }

    @Override
    public JsonElement serialize(Movement src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jo = new JsonObject();
        if (alreadySaved.contains(src.getID())){
            jo.addProperty("lazyID",src.getID());
        } else {
            jo.addProperty("ID",src.getID());
            jo.addProperty("amount",src.getAmount());
            jo.addProperty("description",src.getDescription());
            jo.add("type",context.serialize(src.getType()));
            jo.add("tags",context.serialize(src.getTags()));
            jo.addProperty("account",src.getAccount().getID());
            alreadySaved.add(src.getID());
        }
        return jo;
    }
}
