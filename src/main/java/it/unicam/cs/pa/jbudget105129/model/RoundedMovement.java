package it.unicam.cs.pa.jbudget105129.model;

import it.unicam.cs.pa.jbudget105129.enums.MovementType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    /**
     * Returns the double value representing the amount of this movement. The amount is never negative,
     * to know if the value should be considered positive or negative just look at the movement type.
     * @return the amount of this movement
     * @see MovementType
     */
    @Override
    public double getAmount() {
        return amount.doubleValue();
    }

    /**
     * Returns the string that describes this movement
     * @return the description
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Returns the type of this movement. Depending on the type, a movement is treated differently by
     * operations that use it. For instance, the type could be used to know if the amount of the movement
     * should be treated as positive or negative.
     * @return the type of this movement
     */
    @Override
    public MovementType getType() {
        return type;
    }

    /**
     * Returns the date on which this movement was (or will be) made. This information does not belong to
     * this movement, instead it is be obtained from the movement's transaction.
     * @return the date of this movement
     */
    @Override
    public LocalDate getDate() {
        if(transaction==null) return null;
        return transaction.getDate();
    }

    /**
     * Returns a reference to the transaction this movement is part of.
     * @return the movement's transaction
     */
    @Override
    public Transaction getTransaction() {
        return transaction;
    }

    /**
     * Returns all the tags of this movement
     * @return the list of {@link Tag}
     */
    @Override
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * Returns the {@link Account} to which this movement applies
     * @return the movement's account.
     */
    @Override
    public Account getAccount() {
        return account;
    }

    /**
     * Changes the {@link Account} reference of this movement, null values are not allowed.
     * @param account the new account reference
     */
    @Override
    public void setAccount(Account account) {
        this.account=account;
    }

    /**
     * Changes the {@link Transaction} reference of this movement, null values are not allowed.
     * @param transaction the new transaction reference
     */
    public void setTransaction(Transaction transaction) {
        this.transaction = Objects.requireNonNull(transaction);
    }

    /**
     * Adds a new {@link Tag} to this movement, null values are not allowed.
     * @param tag the new {@link Tag}
     */
    @Override
    public void addTag(Tag tag) {
        if(!tags.contains(tag)) tags.add(tag);
    }

    /**
     * Removes a {@link Tag} from this movement.
     * @param tag the {@link Tag} to remove
     */
    @Override
    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    /**
     * Changes the amount of this movement.
     * @param amount the new amount
     */
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

    /**
     * Changes the description of this movement, null values are not allowed.
     * @param description the new description
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }
}
