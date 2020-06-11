package it.unicam.cs.pa.jbudget105129.model;

import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * An amount of money that keeps track of the balance and all the {@link Movement} associated whit it.
 *
 * This class guarantee that the balance is represented with no more then two decimal places. Supports
 * a minimum and a maximum value for the balance.
 */
public class RoundedAccount implements Account {

    private BigDecimal balance;
    private final BigDecimal openingBalance;
    private String name;
    private String description;
    private String referent;
    private final List<Movement> movements;
    private final AccountType type;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private int ID;
    private static RoundedAccountRegistry registry;

    /**
     * Creates a new instance of {@link RoundedAccount} with the specified parameters, the minimum and maximum
     * amount are always initially set as null but they can be modified via their setters.
     * @param name the name of the new account
     * @param description the description of the new account
     * @param openingBalance the opening balance of the new account, this field is set once for all and does not have a setter
     * @param type the type of the new account, this field is set once for all and does not have a setter
     */
    private RoundedAccount(int ID, String name, String description, double openingBalance, AccountType type){
        this.name=name;
        this.description=description;
        this.openingBalance=new BigDecimal(openingBalance).setScale(2, RoundingMode.HALF_DOWN);
        this.balance=this.openingBalance;
        this.movements=new ArrayList<>();
        this.type=type;
        minAmount=null;
        maxAmount=null;
        this.ID=ID;
    }

    public static RoundedAccountRegistry getRegistry(){
        if (registry==null)
            registry=new RoundedAccountRegistry(RoundedAccount::new);
        return registry;
    }

    public static RoundedAccount getInstance(int ID){
        return getRegistry().getInstance(ID);
    }

    public static RoundedAccount getInstance(String name, String description, double openingBalance, AccountType type){
        return getRegistry().getInstance(name,description,openingBalance,type);
    }

    @Override
    public int getID() {
        return ID;
    }

    /**
     * Returns the current balance of the account. This value is calculated as the sum of all movements
     * applied to the opening balance of this account.
     *
     * The balance is not calculated every time you call this method, instead it is saved in memory
     * and updated every time you add or remove a {@link Movement} from this account. So by calling
     * this method you just access the value in memory.
     * @return the current balance
     */
    @Override
    public double getBalance() {
        return this.balance.doubleValue();
    }

    /**
     * The balance this account had at the moment of its creation.
     * @return the opening balance
     */
    @Override
    public double getOpeningBalance() {
        return this.openingBalance.doubleValue();
    }

    /**
     * The literal description of this account.
     * @return a {@link String} representing the description
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * The name of this account.
     * @return a {@link String} representing the name
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Returns the name and surname of the owner of this account.
     * @return The referent's name and surname
     */
    @Override
    public String getReferent() {
        return this.referent;
    }

    /**
     * Returns the maximum value the balance of this account can reach, in case of an ASSET
     * this represents the maximum amount of money this account can contain, in case of a
     * LIABILITY this represents the maximum amount the debt can reach. The return value
     * is an {@link Optional} so if an account does not have a maximum value this returns an
     * empty {@link Optional}.
     * @return The max amount.
     */
    @Override
    public Optional<Double> getMaxAmount() {
        if(maxAmount==null) return Optional.empty();
        return Optional.of(maxAmount.doubleValue());
    }

    /**
     * Returns the minimum value the balance of this account can reach, in case of an ASSET
     * this represents the minimum amount of money this account can contain (usually zero but
     * some bank account can handle relatively small negative values), in case of a
     * LIABILITY this should always return zero. The return value is an {@link Optional} so if
     * an account does not have a minimum value should return an empty {@link Optional}.
     * @return The minimum amount.
     */
    @Override
    public Optional<Double>  getMinAmount() {
        if(minAmount==null) return Optional.empty();
        return Optional.of(minAmount.doubleValue());
    }

    /**
     * Returns the type of this account (ASSET OR LIABILITY).
     * @return The type of this account
     */
    @Override
    public AccountType getType() {
        return this.type;
    }

    /**
     * Returns the list of {@link Movement} associated with this account.
     * @return The list of movements
     */
    @Override
    public List<Movement> getMovements() {
        return this.movements;
    }

    /**
     * Returns the list of {@link Movement} associated with the account that satisfy a {@link Predicate}
     * @param predicate The predicate used to filter the movements
     * @return The list of movements
     */
    @Override
    public List<Movement> getMovements(Predicate<Movement> predicate) {
        return this.movements.stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * Adds a {@link Movement} to the list and update the balance. It also set the movement's account
     * to this account to make sure everything is set properly.
     * @param movement The new movement to add
     * @throws AccountException  If the new movement introduces a problem to the account balance
     * (for example if the balance goes below the minimum or above the maximum value).
     */
    @Override
    public void addMovement(Movement movement) throws AccountException {
        if(getType()==AccountType.ASSET){
            if(movement.getType()== MovementType.INCOME)
                add(movement);
            else if(movement.getType()==MovementType.OUTFLOW)
                subtract(movement);
        }else if (getType()==AccountType.LIABILITY){
            if(movement.getType()==MovementType.INCOME)
                subtract(movement);
            else if(movement.getType()==MovementType.OUTFLOW)
                add(movement);
        }
        movement.setAccount(this);
        movements.add(movement);
    }

    /**
     * Removes a {@link Movement} from the account, the balance is recalculated considering the
     * missing movement.
     * @param movement The movement to be removed
     * @throws AccountException If the missing movement introduces a problem to the account balance
     * (for example if the balance goes below the minimum or above the maximum value).
     */
    @Override
    public void removeMovement(Movement movement) throws AccountException {
        if(getType()==AccountType.ASSET){
            if(movement.getType()==MovementType.INCOME)
                subtract(movement);
            else if(movement.getType()==MovementType.OUTFLOW)
                add(movement);
        }else if (getType()==AccountType.LIABILITY){
            if(movement.getType()==MovementType.INCOME)
                add(movement);
            else if(movement.getType()==MovementType.OUTFLOW)
                subtract(movement);
        }
        movements.remove(movement);
    }

    /**
     * Changes the referent (the owner of the account).
     * @param referent The name and surname of the new referent
     */
    @Override
    public void setReferent(String referent) {
        this.referent=referent;
    }

    /**
     * Changes the description of the account.
     * @param description The new description
     */
    @Override
    public void setDescription(String description) {
        this.description=description;
    }

    /**
     * Changes the name of the account.
     * @param name The new name
     */
    @Override
    public void setName(String name) {
        this.name=name;
    }

    /**
     * Sets a new value as the maximum value.
     * @param maxAmount The new maximum value
     * @throws UnsupportedOperationException if this account does not support a maximum amount
     */
    @Override
    public void setMaxAmount(Double maxAmount) {
        if(Objects.nonNull(maxAmount))
            this.maxAmount=new BigDecimal(maxAmount).setScale(2,RoundingMode.HALF_DOWN);
        else this.maxAmount=null;
    }

    /**
     * Sets a new value as the minimum value.
     * @param minAmount The new minimum value
     * @throws UnsupportedOperationException if this account does not support a minimum amount
     */
    @Override
    public void setMinAmount(Double minAmount) {
        if(Objects.nonNull(minAmount))
            this.minAmount=new BigDecimal(minAmount).setScale(2,RoundingMode.HALF_DOWN);
        else this.minAmount=null;
    }

    private void subtract(Movement movement) throws AccountException {
        BigDecimal newBalance = balance.subtract(BigDecimal.valueOf(movement.getAmount()));
        if (minAmount!=null&&newBalance.compareTo(minAmount)<0)
            throw new AccountException("Not enough money on the account "+this.getName());
        balance=newBalance;
    }

    private void add(Movement movement) throws AccountException {
        BigDecimal newBalance = balance.add(BigDecimal.valueOf(movement.getAmount()));
        if (maxAmount!=null&&newBalance.compareTo(maxAmount)>0)
            throw new AccountException("Exceeded max amount on the account "+this.getName());
        balance=newBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoundedAccount)) return false;
        RoundedAccount account = (RoundedAccount) o;
        return getBalance()==(account.getBalance()) &&
                getOpeningBalance()==(account.getOpeningBalance()) &&
                getName().equals(account.getName()) &&
                getDescription().equals(account.getDescription()) &&
                Objects.equals(getReferent(), account.getReferent()) &&
                getType() == account.getType() &&
                Objects.equals(getMinAmount(), account.getMinAmount()) &&
                Objects.equals(getMaxAmount(), account.getMaxAmount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBalance(), getOpeningBalance(), getName(), getDescription(), getReferent(), getType(), getMinAmount(), getMaxAmount());
    }

    @Override
    public String toString() {
        return "RoundedAccount{" +
                "balance=" + balance +
                ", openingBalance=" + openingBalance +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", referent='" + referent + '\'' +
                ", type=" + type +
                ", minAmount=" + minAmount +
                ", maxAmount=" + maxAmount +
                '}';
    }
}
