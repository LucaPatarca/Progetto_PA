package it.unicam.cs.pa.jbudget105129.persistence;

import com.google.gson.*;
import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import it.unicam.cs.pa.jbudget105129.model.*;

import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class LedgerTypeAdapter implements JsonDeserializer<Ledger>, JsonSerializer<Ledger>{
    @Override
    public Ledger deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Ledger ledger = new FamilyLedger();
        JsonObject jo = json.getAsJsonObject();
        deserializeMovements(ledger,jo,context);
        deserializeAccounts(ledger,jo,context);
        deserializeTransactions(ledger,jo,context);
        return ledger;
    }
    @Override
    public JsonElement serialize(Ledger src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jo = new JsonObject();
        JsonArray ja = new JsonArray();
        List<Movement> movements = src.getTransactions().stream().map(Transaction::getMovements).flatMap(List::stream).collect(Collectors.toList());
        movements.forEach(m->ja.add(context.serialize(m)));
        jo.add("movements",ja);
        jo.add("transactions",context.serialize(src.getTransactions()));
        jo.add("accounts",context.serialize(src.getAccounts()));
        jo.add("sheduledTransactions",context.serialize(src.getScheduledTransactions()));
        return jo;
    }

    private void deserializeTransactions(Ledger ledger, JsonObject jo, JsonDeserializationContext context){
        JsonArray transactions = jo.get("transactions").getAsJsonArray();
        for (JsonElement element : transactions) {
            try {
                Transaction transaction = context.deserialize(element,Transaction.class);
                ledger.addTransaction(transaction);
            } catch (AccountException e) {
                // TODO: 03/06/20 logging
                e.printStackTrace();
            }
        }
    }

    private void deserializeAccounts(Ledger ledger, JsonObject jo, JsonDeserializationContext context){
        JsonArray accounts = jo.get("accounts").getAsJsonArray();
        for (JsonElement element : accounts) {
            Account account = context.deserialize(element,Account.class);
            ledger.addAccount(account);
        }
    }

    private void deserializeMovements(Ledger ledger, JsonObject jo, JsonDeserializationContext context){
        JsonArray movements = jo.get("movements").getAsJsonArray();
        for(JsonElement element : movements){
            JsonObject movement =element.getAsJsonObject();
            RoundedMovement.getInstance(
                    movement.get("description").getAsString(),
                    movement.get("amount").getAsDouble(),
                    context.deserialize(movement.get("type"), MovementType.class),
                    null);
        }
    }
}
