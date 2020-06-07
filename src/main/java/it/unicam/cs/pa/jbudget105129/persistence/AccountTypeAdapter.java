package it.unicam.cs.pa.jbudget105129.persistence;

import com.google.gson.*;
import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import it.unicam.cs.pa.jbudget105129.model.Account;
import it.unicam.cs.pa.jbudget105129.model.Movement;
import it.unicam.cs.pa.jbudget105129.model.RoundedAccount;

import java.lang.reflect.Type;

public class AccountTypeAdapter implements JsonDeserializer<Account>, JsonSerializer<Account> {
    @Override
    public Account deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jo = json.getAsJsonObject();
        RoundedAccount account= new RoundedAccount(
                jo.get("name").getAsString(),
                jo.get("description").getAsString(),
                jo.get("openingBalance").getAsDouble(),
                context.deserialize(jo.get("type"), AccountType.class)
        );
        //account.setReferent(jo.get("referent").getAsString());
        // FIXME: 06/06/20 aggiornare anche il referent
        JsonArray movements = jo.get("movements").getAsJsonArray();
        for (JsonElement element : movements){
            Movement movement = context.deserialize(element,Movement.class);
            movement.setAccount(account);
        }
        account.getMovements().forEach(m->m.setAccount(account));
        return account;
    }

    @Override
    public JsonElement serialize(Account src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jo = new JsonObject();
        jo.addProperty("name",src.getName());
        jo.addProperty("description",src.getDescription());
        jo.addProperty("openingBalance",src.getOpeningBalance());
        jo.add("movements",context.serialize(src.getMovements()));
        jo.add("maxAmount",context.serialize(src.getMaxAmount()));
        jo.add("minAmount",context.serialize(src.getMinAmount()));
        jo.addProperty("referent",src.getReferent());
        jo.add("type",context.serialize(src.getType()));
        return jo;
    }
}
