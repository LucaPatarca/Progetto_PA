package it.unicam.cs.pa.jbudget105129.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.pa.jbudget105129.model.*;

import java.io.*;

public class JsonPersistenceManager implements PersistenceManager{

    private final Gson gson;

    public JsonPersistenceManager(){
        GsonBuilder builder = new GsonBuilder();
        MovementTypeAdapter movementTypeAdapter = new MovementTypeAdapter();
        LedgerTypeAdapter ledgerTypeAdapter = new LedgerTypeAdapter();
        TransactionTypeAdapter transactionTypeAdapter = new TransactionTypeAdapter();
        AccountTypeAdapter accountTypeAdapter = new AccountTypeAdapter();
        TagTypeAdapter tagTypeAdapter = new TagTypeAdapter();
        ScheduledTransactionTypeAdapter scheduledTransactionTypeAdapter = new ScheduledTransactionTypeAdapter();

        builder.registerTypeAdapter(Movement.class,movementTypeAdapter);
        builder.registerTypeAdapter(Ledger.class,ledgerTypeAdapter);
        builder.registerTypeAdapter(Transaction.class,transactionTypeAdapter);
        builder.registerTypeAdapter(Account.class,accountTypeAdapter);
        builder.registerTypeAdapter(Tag.class,tagTypeAdapter);
        builder.registerTypeAdapter(RoundedMovement.class,movementTypeAdapter);
        builder.registerTypeAdapter(FamilyLedger.class,ledgerTypeAdapter);
        builder.registerTypeAdapter(RoundedTransaction.class,transactionTypeAdapter);
        builder.registerTypeAdapter(RoundedAccount.class,accountTypeAdapter);
        builder.registerTypeAdapter(SingleTag.class,tagTypeAdapter);
        builder.registerTypeAdapter(ScheduledTransaction.class,scheduledTransactionTypeAdapter);
        builder.registerTypeAdapter(MapScheduledTransaction.class,scheduledTransactionTypeAdapter);

        gson = builder.create();
    }

    @Override
    public void save(Ledger ledger, String path) throws IOException {
        File file = new File(path);
        String json = gson.toJson(ledger);
        FileOutputStream out = new FileOutputStream(file);
        out.write(json.getBytes());
    }

    @Override
    public Ledger load(String path) throws IOException {
        File file = new File(path);
        FileInputStream in = new FileInputStream(file);
        String jsonString = new String(in.readAllBytes());
        return gson.fromJson(jsonString, FamilyLedger.class);
    }
}