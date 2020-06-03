package it.unicam.cs.pa.jbudget105129.persistence;

import com.google.gson.*;
import it.unicam.cs.pa.jbudget105129.model.RoundedTransaction;
import it.unicam.cs.pa.jbudget105129.model.Transaction;

import java.lang.reflect.Type;

public class TransactionTypeAdapter implements JsonDeserializer<Transaction>, JsonSerializer<Transaction>{
    @Override
    public Transaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jo = json.getAsJsonObject();
        RoundedTransaction transaction=context.deserialize(jo,RoundedTransaction.class);
        transaction.getMovements().forEach(m->m.setTransaction(transaction));
        return transaction;
    }
    @Override
    public JsonElement serialize(Transaction src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src);
    }
}
