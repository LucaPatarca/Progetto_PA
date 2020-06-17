package it.unicam.cs.pa.jbudget105129.persistence;

import com.google.gson.*;
import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import it.unicam.cs.pa.jbudget105129.model.MapScheduledTransaction;
import it.unicam.cs.pa.jbudget105129.model.Movement;
import it.unicam.cs.pa.jbudget105129.model.ScheduledTransaction;
import it.unicam.cs.pa.jbudget105129.model.Transaction;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ScheduledTransactionTypeAdapter implements JsonSerializer<ScheduledTransaction>, JsonDeserializer<ScheduledTransaction> {
    @Override
    public ScheduledTransaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jo = json.getAsJsonObject();
        String description = jo.get("description").getAsString();
        List<Transaction> allTransactions = new LinkedList<>();
        List<Transaction> completed = new LinkedList<>();
        JsonArray jTransactions = jo.getAsJsonArray("transactions");
        for(JsonElement element:jTransactions){
            JsonObject jTransaction = element.getAsJsonObject();
            Transaction transaction = context.deserialize(jTransaction.get("transaction"),Transaction.class);
            if(jTransaction.get("completed").getAsBoolean())
                completed.add(transaction);
            allTransactions.add(transaction);
        }
        MapScheduledTransaction st = new MapScheduledTransaction(description,allTransactions);
        completed.forEach(st::markTransactionAsCompleted);
        addMovementsToAccount(completed);
        return st;
    }

    @Override
    public JsonElement serialize(ScheduledTransaction src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jo = new JsonObject();
        jo.addProperty("description",src.getDescription());
        JsonArray ja = new JsonArray();
        for (Transaction transaction: src.getTransactions()) {
            JsonObject jTransaction = new JsonObject();
            jTransaction.add("transaction",context.serialize(transaction));
            jTransaction.addProperty("completed",src.isCompleted(transaction));
            ja.add(jTransaction);
        }
        jo.add("transactions",ja);
        return jo;
    }

    private void addMovementsToAccount(List<Transaction> transactions){
        List<Movement> movements = transactions.stream().map(Transaction::getMovements).flatMap(List::stream)
                .collect(Collectors.toList());
        movements.forEach(m-> {
            try {
                m.getAccount().addMovement(m);
            } catch (AccountException e) {
                e.printStackTrace();
                // TODO: 17/06/2020 log
            }
        });
    }
}
