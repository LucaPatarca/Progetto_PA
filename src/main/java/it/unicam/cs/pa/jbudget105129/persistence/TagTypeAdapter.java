package it.unicam.cs.pa.jbudget105129.persistence;

import com.google.gson.*;
import it.unicam.cs.pa.jbudget105129.model.BasicTag;
import it.unicam.cs.pa.jbudget105129.model.Tag;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TagTypeAdapter implements JsonSerializer<Tag>, JsonDeserializer<Tag> {

    @Override
    public JsonElement serialize(Tag src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src);
    }

    @Override
    public Tag deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jo = json.getAsJsonObject();
        return BasicTag.getInstance(jo.get("name").getAsString(),jo.get("description").getAsString());
    }
}
