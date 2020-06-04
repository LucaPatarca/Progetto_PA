package it.unicam.cs.pa.jbudget105129.persistence;

import com.google.gson.*;
import it.unicam.cs.pa.jbudget105129.model.SingleTag;
import it.unicam.cs.pa.jbudget105129.model.Tag;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class TagTypeAdapter implements JsonSerializer<Tag>, JsonDeserializer<Tag> {

    private final Set<Integer> alreadySaved;

    public TagTypeAdapter(){
        alreadySaved=new HashSet<>();
    }

    @Override
    public JsonElement serialize(Tag src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jo = new JsonObject();
        if(alreadySaved.contains(src.getID())){
            jo.addProperty("lazyID",src.getID());
        }else{
            jo.addProperty("ID",src.getID());
            jo.addProperty("name",src.getName());
            jo.addProperty("description",src.getDescription());
        }
        return jo;
    }

    @Override
    public Tag deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jo = json.getAsJsonObject();
        if(jo.has("lazyID")){
            return SingleTag.getInstance(jo.get("lazyID").getAsInt());
        } else{
            SingleTag o = SingleTag.getRegistry().getInstance(
                    jo.get("ID").getAsInt(),
                    jo.get("name").getAsString(),
                    jo.get("description").getAsString()
            );
            alreadySaved.add(o.getID());
            return o;
        }
    }
}
