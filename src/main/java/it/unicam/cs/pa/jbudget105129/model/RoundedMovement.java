package it.unicam.cs.pa.jbudget105129.model;

import it.unicam.cs.pa.jbudget105129.enums.MovementType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

//TODO javadoc

/**
 * A simple change in the balance of an {@link Account}, this type of {@link Movement} guarantee that
 * it's amount has always no more than two decimal places.
 */
public class RoundedMovement implements Movement {

    private static RoundedMovementRegistry registry;
    private BigDecimal amount;
    private String description;
    private final MovementType type;
    private Transaction transaction;
    private final List<Tag> tags;
    private Account account;
    private final int ID;

    /**
     * Creates a new {@link RoundedMovement} with the specified parameters. This method is private,
     * you can create a new instance of this class with the method getInstance().
     * @param ID the unique identifier of the new movement
     * @param description the description of the new movement
     * @param amount the amount of the new movement
     * @param type the type of the new movement, this field is set once for all and does not have a setter
     * @param account the account the new movement is linked with
     */
    private RoundedMovement(int ID, String description, double amount, MovementType type, Account account){
        this.description=Objects.requireNonNull(description);
        this.amount=new BigDecimal(amount).setScale(2, RoundingMode.HALF_DOWN);
        this.tags=new ArrayList<>();
        this.ID=ID;
        this.type=Objects.requireNonNull(type);
        this.account=account;
    }

    /**
     * Returns the unique identifier of this movement.
     * @return this movement's ID
     */
    @Override
    public int getID() {
        return this.ID;
    }

    /**
     * Returns the registry of this class used to create new instances.
     * @return the registry
     * @see RoundedMovementRegistry
     */
    public static RoundedMovementRegistry getRegistry(){
        if (registry==null){
            registry= new RoundedMovementRegistry(RoundedMovement::new);
        }
        return registry;
    }

    /**
     * Lets the class registry return the instance of {@link RoundedMovement} given only the ID,
     * if a movement with this ID does not exist it returns null.
     * @param ID the identifier of the requested movement
     * @return the reference of the {@link RoundedMovement} identified with this ID if such a movement
     * exists, null instead.
     * @see RoundedMovementRegistry
     */
    public static RoundedMovement getInstance(int ID){
        return getRegistry().getInstance(ID);
    }

    /**
     * Lets the class registry create a new instance with the specified parameters, the registry will
     * provide a unique ID to the new movement.
     * @param description the description of the new movement
     * @param amount the amount of the new movement
     * @param type the type of the new movement, this field is set once for all and does not have a setter
     * @param account the reference to the account this movement is linked with
     * @return the new instance of the movement
     */
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
    public LocalDate getDate() {
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
