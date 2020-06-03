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

    private static SingleInstanceClassRegistry<RoundedMovement> registry;
    private BigDecimal amount;
    private String description;
    private MovementType type;
    private Transaction transaction;
    private final List<Tag> tags;
    private Account account;
    private final int ID;

    private RoundedMovement(int ID){
        this.description="";
        this.amount=new BigDecimal(0).setScale(2, RoundingMode.HALF_DOWN);
        this.tags=new ArrayList<>();
        this.ID=ID;
    }

    @Override
    public int getID() {
        return this.ID;
    }

    public static SingleInstanceClassRegistry<RoundedMovement> getRegistry(){
        if (registry==null){
            registry= new SingleInstanceClassRegistry<>(RoundedMovement::new);
        }
        return registry;
    }

    public static RoundedMovement getInstance(int ID){
        return getRegistry().getInstance(ID);
    }

    public static RoundedMovement getInstance(){
        return getRegistry().getInstance();
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
    public Movement setAccount(Account account) {
        this.account=account;
        return this;
    }

    public Movement setTransaction(Transaction transaction) {
        this.transaction = Objects.requireNonNull(transaction);
        return this;
    }

    @Override
    public void addTag(Tag tag) {
        if(!tags.contains(tag)) tags.add(tag);
    }

    @Override
    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    public Movement setAmount(double amount){
        this.amount= new BigDecimal(amount).setScale(2,RoundingMode.HALF_DOWN);
        return this;
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

    public Movement setType(MovementType type) {
        this.type = type;
        return this;
    }

    public Movement setDescription(String description) {
        this.description = description;
        return this;
    }
}
