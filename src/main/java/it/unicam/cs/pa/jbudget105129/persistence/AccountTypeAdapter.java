package it.unicam.cs.pa.jbudget105129.persistence;

import com.google.gson.*;
import it.unicam.cs.pa.jbudget105129.model.Account;
import it.unicam.cs.pa.jbudget105129.model.RoundedAccount;

import java.lang.reflect.Type;

public class AccountTypeAdapter implements JsonDeserializer<Account>, JsonSerializer<Account> {
    @Override
    public Account deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        RoundedAccount account= context.deserialize(json, RoundedAccount.class);
        account.getMovements().forEach(m->m.setAccount(account));
        return account;
    }

    @Override
    public JsonElement serialize(Account src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src);
    }
}
