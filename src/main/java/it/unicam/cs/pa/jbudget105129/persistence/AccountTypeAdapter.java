package it.unicam.cs.pa.jbudget105129.persistence;

import com.google.gson.*;
import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import it.unicam.cs.pa.jbudget105129.model.Account;
import it.unicam.cs.pa.jbudget105129.model.Movement;
import it.unicam.cs.pa.jbudget105129.model.RoundedAccount;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class AccountTypeAdapter implements JsonDeserializer<Account>, JsonSerializer<Account> {

    private Set<Integer> alreadySaved;

    public AccountTypeAdapter(){
        alreadySaved=new HashSet<>();
    }

    @Override
    public Account deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jo = json.getAsJsonObject();
        if(jo.has("lazyID"))
            return RoundedAccount.getInstance(jo.get("lazyID").getAsInt());
        RoundedAccount account= RoundedAccount.getRegistry().getInstance(
                jo.get("ID").getAsInt(),
                jo.get("name").getAsString(),
                jo.get("description").getAsString(),
                jo.get("openingBalance").getAsDouble(),
                context.deserialize(jo.get("type"), AccountType.class)
        );
        if(jo.has("referent"))
            account.setReferent(jo.get("referent").getAsString());
        JsonArray movements = jo.get("movements").getAsJsonArray();
        for (JsonElement element : movements){
            Movement movement = context.deserialize(element,Movement.class);
            movement.setAccount(account);
        }
        return account;
    }

    @Override
    public JsonElement serialize(Account src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jo = new JsonObject();
        if(alreadySaved.contains(src.getID()))
            jo.addProperty("lazyID",src.getID());
        else{
            jo.addProperty("ID",src.getID());
            jo.addProperty("name",src.getName());
            jo.addProperty("description",src.getDescription());
            jo.addProperty("openingBalance",src.getOpeningBalance());
            jo.add("movements",context.serialize(src.getMovements()));
            jo.add("maxAmount",context.serialize(src.getMaxAmount()));
            jo.add("minAmount",context.serialize(src.getMinAmount()));
            jo.addProperty("referent",src.getReferent());
            jo.add("type",context.serialize(src.getType()));
            alreadySaved.add(src.getID());
        }
        return jo;
    }
}
