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
//TODO javadoc
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

    public RoundedAccount(String name, String description, double openingBalance, AccountType type){
        this.name=name;
        this.description=description;
        this.openingBalance=new BigDecimal(openingBalance).setScale(2, RoundingMode.HALF_DOWN);
        this.balance=this.openingBalance;
        this.movements=new ArrayList<>();
        this.type=type;
        minAmount=null;
        maxAmount=null;
    }

    @Override
    public double getBalance() {
        return this.balance.doubleValue();
    }

    @Override
    public double getOpeningBalance() {
        return this.openingBalance.doubleValue();
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getReferent() {
        return this.referent;
    }

    @Override
    public Optional<Double> getMaxAmount() {
        if(maxAmount==null) return Optional.empty();
        return Optional.of(maxAmount.doubleValue());
    }

    @Override
    public Optional<Double>  getMinAmount() {
        if(minAmount==null) return Optional.empty();
        return Optional.of(minAmount.doubleValue());
    }

    @Override
    public AccountType getType() {
        return this.type;
    }

    @Override
    public List<Movement> getMovements() {
        return this.movements;
    }

    @Override
    public List<Movement> getMovements(Predicate<Movement> predicate) {
        return this.movements.stream().filter(predicate).collect(Collectors.toList());
    }

    @Override
    public void addMovement(Movement movement) throws IllegalStateException {
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

    @Override
    public void removeMovement(Movement movement) throws IllegalStateException {
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

    @Override
    public void setReferent(String referent) {
        this.referent=referent;
    }

    @Override
    public void setDescription(String description) {
        this.description=description;
    }

    @Override
    public void setName(String name) {
        this.name=name;
    }

    @Override
    public void setMaxAmount(double maxAmount) {
        this.maxAmount=new BigDecimal(maxAmount).setScale(2,RoundingMode.HALF_DOWN);
    }

    @Override
    public void setMinAmount(double minAmount) {
        this.minAmount=new BigDecimal(minAmount).setScale(2,RoundingMode.HALF_DOWN);
    }

    private void subtract(Movement movement) throws IllegalStateException {
        BigDecimal newBalance = balance.subtract(BigDecimal.valueOf(movement.getAmount()));
        if (minAmount!=null&&newBalance.compareTo(minAmount)<0)
            throw new IllegalStateException("Not enough money on the account "+this.getName());
        balance=newBalance;
    }

    private void add(Movement movement) throws IllegalStateException {
        BigDecimal newBalance = balance.add(BigDecimal.valueOf(movement.getAmount()));
        if (maxAmount!=null&&newBalance.compareTo(maxAmount)>0)
            throw new IllegalStateException("Exceeded max amount on the account "+this.getName());
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
