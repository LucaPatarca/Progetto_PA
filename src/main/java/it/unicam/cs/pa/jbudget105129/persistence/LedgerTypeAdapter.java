package it.unicam.cs.pa.jbudget105129.persistence;

import com.google.gson.*;
import it.unicam.cs.pa.jbudget105129.model.FamilyLedger;
import it.unicam.cs.pa.jbudget105129.model.Ledger;
import it.unicam.cs.pa.jbudget105129.model.Transaction;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class LedgerTypeAdapter implements JsonDeserializer<Ledger>, JsonSerializer<Ledger>{
    @Override
    public Ledger deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Ledger ledger = new FamilyLedger();
        JsonObject jo = json.getAsJsonObject();
        List<Transaction> transactions = context.deserialize(jo.get("transactions"),LinkedList.class);
        transactions.forEach(ledger::addTransaction);
        return ledger;
    }
    @Override
    public JsonElement serialize(Ledger src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jo = new JsonObject();
        jo.add("transactions",context.serialize(src.getTransactions()));
        jo.add("accounts",context.serialize(src.getAccounts()));
        jo.add("sheduledTransactions",context.serialize(src.getScheduledTransactions()));
        return jo;
    }
}
