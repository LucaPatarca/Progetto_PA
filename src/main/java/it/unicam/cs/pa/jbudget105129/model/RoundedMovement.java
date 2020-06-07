package it.unicam.cs.pa.jbudget105129.model;

import it.unicam.cs.pa.jbudget105129.enums.MovementType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

//TODO javadoc
public class RoundedMovement implements Movement {

    private static RoundedMovementRegistry registry;
    private BigDecimal amount;
    private String description;
    private final MovementType type;
    private Transaction transaction;
    private final List<Tag> tags;
    private Account account;
    private final int ID;

    private RoundedMovement(int ID, String description, double amount, MovementType type, Account account){
        this.description=description;
        this.amount=new BigDecimal(amount).setScale(2, RoundingMode.HALF_DOWN);
        this.tags=new ArrayList<>();
        this.ID=ID;
        this.type=type;
        this.account=account;
    }

    @Override
    public int getID() {
        return this.ID;
    }

    public static RoundedMovementRegistry getRegistry(){
        if (registry==null){
            registry= new RoundedMovementRegistry(RoundedMovement::new);
        }
        return registry;
    }

    public static RoundedMovement getInstance(int ID){
        return getRegistry().getInstance(ID);
    }

    public static RoundedMovement getInstance(String description, double amount, MovementType type, Account account){
        return getRegistry().getInstance(description,amount,type,account);
    }

    @Override
    public double getAmount() {
        return amount.doubleValue();
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public MovementType getType() {
        return type;
    }

    @Override
    public Date getDate() {
        if(transaction==null) return null;
        return transaction.getDate();
    }

    @Override
    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    public List<Tag> getTags() {
        return tags;
    }

    @Override
    public Account getAccount() {
        return account;
    }

    @Override
    public void setAccount(Account account) {
        this.account=account;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = Objects.requireNonNull(transaction);
    }

    @Override
    public void addTag(Tag tag) {
        if(!tags.contains(tag)) tags.add(tag);
    }

    @Override
    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    public void setAmount(double amount){
        this.amount= new BigDecimal(amount).setScale(2,RoundingMode.HALF_DOWN);
    }

    @Override
    public boolean equals(Object o) {
        // TODO: 01/06/20 gli oggetti nulli sono cambiati
        if (this == o) return true;
        if (!(o instanceof RoundedMovement)) return false;
        RoundedMovement that = (RoundedMovement) o;
        return getAmount()==that.getAmount() &&
                getDescription().equals(that.getDescription()) &&
                getType() == that.getType() &&
                getTags().equals(that.getTags()) &&
                Objects.equals(getAccount(), that.getAccount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAmount(), getDescription(), getType(), getTags(), getAccount());
    }

    @Override
    public String toString() {
        return "RoundedMovement{" +
                "amount=" + amount +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", transaction=" + (transaction==null?null:transaction.getDescription()) +
                ", tags=" + tags +
                ", account=" + (account==null?null:account.getName()) +
                '}';
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
