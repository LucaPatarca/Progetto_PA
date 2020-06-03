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

    @Override
    public Movement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        alreadySaved = new HashSet<>();
        JsonObject jo =json.getAsJsonObject();
        if(jo.has("lazyID")){
            return RoundedMovement.getInstance(jo.get("lazyID").getAsInt());
        } else {
            Movement o = RoundedMovement.getInstance(jo.get("ID").getAsInt())
                    .setDescription(jo.get("description").getAsString())
                    .setAmount(jo.get("amount").getAsDouble())
                    .setType(context.deserialize(jo.get("type"), MovementType.class));
            List<Tag> tags = context.deserialize(jo.get("tags"), List.class);
            tags.forEach(o::addTag);
            return o;
        }
    }

    @Override
    public JsonElement serialize(Movement src, Type typeOfSrc, JsonSerializationContext context) {
        alreadySaved = new HashSet<>();
        JsonObject jo = new JsonObject();
        if (alreadySaved.contains(src.getID())){
            jo.addProperty("lazyID",src.getID());
        } else {
            jo.addProperty("ID",src.getID());
            jo.addProperty("amount",src.getAmount());
            jo.addProperty("description",src.getDescription());
            jo.add("type",context.serialize(src.getType()));
            jo.add("tags",context.serialize(src.getTags()));
            alreadySaved.add(src.getID());
        }
        return jo;
    }
}
