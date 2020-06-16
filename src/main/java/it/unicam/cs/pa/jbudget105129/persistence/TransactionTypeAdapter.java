package it.unicam.cs.pa.jbudget105129.persistence;

import com.google.gson.*;
import it.unicam.cs.pa.jbudget105129.model.Movement;
import it.unicam.cs.pa.jbudget105129.model.RoundedTransaction;
import it.unicam.cs.pa.jbudget105129.model.Transaction;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Date;

public class TransactionTypeAdapter implements JsonDeserializer<Transaction>, JsonSerializer<Transaction>{
    @Override
    public Transaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jo = json.getAsJsonObject();
        RoundedTransaction transaction=new RoundedTransaction(
                jo.get("description").getAsString(),
                LocalDate.parse(jo.get("date").getAsString())
        );
        JsonArray array = jo.get("movements").getAsJsonArray();
        for (JsonElement element: array) {
            Movement movement = context.deserialize(element,Movement.class);
            transaction.addMovement(movement);
        }
        transaction.getMovements().forEach(m->m.setTransaction(transaction));
        return transaction;
    }
    @Override
    public JsonElement serialize(Transaction src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jo = new JsonObject();
        jo.addProperty("description",src.getDescription());
        jo.addProperty("date",src.getDate().toString());
        jo.addProperty("totalAmount",src.getTotalAmount());
        jo.add("movements",context.serialize(src.getMovements()));
        return jo;
    }
}
