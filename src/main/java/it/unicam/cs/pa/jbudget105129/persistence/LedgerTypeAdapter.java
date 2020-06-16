package it.unicam.cs.pa.jbudget105129.persistence;

import com.google.gson.*;
import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import it.unicam.cs.pa.jbudget105129.model.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LedgerTypeAdapter implements JsonDeserializer<Ledger>, JsonSerializer<Ledger>{

    Map<Integer,Integer> movementAccount;

    public LedgerTypeAdapter(){
        movementAccount=new HashMap<>();
    }

    @Override
    public Ledger deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Ledger ledger = new FamilyLedger();
        JsonObject jo = json.getAsJsonObject();
        deserializeTags(jo,context);
        deserializeMovements(jo,context);
        deserializeAccounts(ledger,jo,context);
        deserializeTransactions(ledger,jo,context);
        deserializeScheduledTransactions(ledger,jo,context);
        movementAccount.keySet().forEach(mID->RoundedMovement.getInstance(mID)
                .setAccount(RoundedAccount.getInstance(movementAccount.get(mID))));
        return ledger;
    }

    private void deserializeTags(JsonObject jo, JsonDeserializationContext context) {
        JsonArray ja = jo.getAsJsonArray("tags");
        ja.forEach(t-> context.deserialize(t,Tag.class));
    }

    @Override
    public JsonElement serialize(Ledger src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jo = new JsonObject();
        List<Movement> movements = src.getTransactions().stream().map(Transaction::getMovements).flatMap(List::stream).collect(Collectors.toList());
        movements.addAll(src.getScheduledTransactions().stream().map(ScheduledTransaction::getTransactions)
        .flatMap(List::stream).map(Transaction::getMovements).flatMap(List::stream).collect(Collectors.toList()));
        List<Tag> tags = movements.stream().map(Movement::getTags).flatMap(List::stream).distinct().collect(Collectors.toList());
        jo.add("tags",context.serialize(tags));
        jo.add("movements",context.serialize(movements));
        jo.add("transactions",context.serialize(src.getTransactions()));
        jo.add("accounts",context.serialize(src.getAccounts()));
        jo.add("scheduledTransactions",context.serialize(src.getScheduledTransactions()));
        return jo;
    }

    private void deserializeTransactions(Ledger ledger, JsonObject jo, JsonDeserializationContext context){
        JsonArray transactions = jo.get("transactions").getAsJsonArray();
        for (JsonElement element : transactions) {
            try {
                ledger.addTransaction(context.deserialize(element,Transaction.class));
            } catch (AccountException e) {
                // TODO: 03/06/20 log
                e.printStackTrace();
            }
        }
    }

    private void deserializeAccounts(Ledger ledger, JsonObject jo, JsonDeserializationContext context){
        JsonArray accounts = jo.get("accounts").getAsJsonArray();
        for (JsonElement element : accounts) {
            ledger.addAccount(context.deserialize(element,Account.class));
        }
    }

    private void deserializeMovements(JsonObject jo, JsonDeserializationContext context){
        JsonArray movements = jo.get("movements").getAsJsonArray();
        for(JsonElement element : movements){
            JsonObject jMovement = element.getAsJsonObject();
            Movement movement = context.deserialize(element,Movement.class);
            movementAccount.put(movement.getID(),jMovement.get("account").getAsInt());
        }
    }

    private void deserializeScheduledTransactions(Ledger ledger, JsonObject jo, JsonDeserializationContext context){
        JsonArray ja = jo.getAsJsonArray("scheduledTransactions");
        ja.forEach(st->ledger.addScheduledTransaction(context.deserialize(st,ScheduledTransaction.class)));
    }
}
