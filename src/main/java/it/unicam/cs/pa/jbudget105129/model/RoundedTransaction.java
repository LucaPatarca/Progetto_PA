package it.unicam.cs.pa.jbudget105129.model;

import it.unicam.cs.pa.jbudget105129.enums.MovementType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
/**
 * Represents a set of {@link Movement}s considered as an atomic operation.
 */
public class RoundedTransaction implements Transaction {

    private final String description;
    private final List<Movement> movements;
    private LocalDate date;
    private BigDecimal totalAmount;

    /**
     * Creates a new {@link RoundedTransaction} with the specified parameters.
     * @param description the new transaction's description
     * @param date the new transaction's date
     */
    public RoundedTransaction(String description, LocalDate date){
        this.date=date;
        this.movements=new ArrayList<>();
        this.description=description;
        this.totalAmount=new BigDecimal(0).setScale(2, RoundingMode.HALF_DOWN);
    }

    /**
     * Returns the description of this transaction.
     * @return the description
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Returns a list of distinct (no repeated elements) {@link Tag} that are present on at least one
     * of the inner {@link Movement}.
     * @return the list of tags
     */
    @Override
    public List<Tag> getTags() {
        return movements.parallelStream().map(Movement::getTags)
                .flatMap(List::stream).distinct().collect(Collectors.toList());
    }

    /**
     * Returns the list of inner {@link Movement} contained in this transaction.
     * @return the list of movements
     */
    @Override
    public List<Movement> getMovements() {
        return movements;
    }

    /**
     * Returns the {@link LocalDate} object representing the time on which this transaction was (or will be) applied.
     * @return the transaction's date
     */
    @Override
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the total resulting amount of this transaction. It is calculated as the sum of all
     * {@link Movement} amount considering as positive the one of type INCOME and negative the one of
     * type OUTFLOW.
     * @return the total amount of this transaction
     */
    @Override
    public double getTotalAmount() {
        return totalAmount.doubleValue();
    }

    /**
     * Adds a tag to all the inner movements and, consequently, to this transaction.
     * @param tag the tag to add
     */
    @Override
    public void addTag(Tag tag) {
        movements.forEach(m->m.addTag(tag));
    }

    /**
     * Removes a tag from all the inner movements and, consequently, from this transaction.
     * @param tag the tag to remove
     */
    @Override
    public void removeTag(Tag tag) {
        movements.forEach(m->m.removeTag(tag));
    }

    /**
     * Adds a {@link Movement} to this transaction's list.
     * @param movement the {@link Movement} to add
     */
    @Override
    public void addMovement(Movement movement) {
        movements.add(movement);
        updateTotalAmount(movement,false);
        movement.setTransaction(this);
    }

    /**
     * Removes a {@link Movement} from this transaction's list.
     * @param movement the {@link Movement} to remove
     */
    @Override
    public void removeMovement(Movement movement) {
        movements.remove(movement);
        updateTotalAmount(movement,true);
    }

    /**
     * Changes the date of this transaction.
     * @param date the new date
     */
    @Override
    public void setDate(LocalDate date) {
        this.date=date;
    }

    private void updateTotalAmount(Movement movement, boolean removing){
        if(movement.getType().equals(MovementType.INCOME)){
            if(removing)
                totalAmount=totalAmount.subtract(BigDecimal.valueOf(movement.getAmount()));
            else
                totalAmount=totalAmount.add(BigDecimal.valueOf(movement.getAmount()));
        }else if (movement.getType().equals(MovementType.OUTFLOW)){
            if (removing)
                totalAmount=totalAmount.add(BigDecimal.valueOf(movement.getAmount()));
            else
                totalAmount=totalAmount.subtract(BigDecimal.valueOf(movement.getAmount()));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoundedTransaction)) return false;
        RoundedTransaction that = (RoundedTransaction) o;
        return getDescription().equals(that.getDescription()) &&
                getMovements().equals(that.getMovements()) &&
                getDate().equals(that.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription(), getMovements(), getDate());
    }

    @Override
    public String toString() {
        return "RoundedTransaction{" +
                "description='" + description + '\'' +
                ", movements=" + movements +
                ", date=" + date +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
