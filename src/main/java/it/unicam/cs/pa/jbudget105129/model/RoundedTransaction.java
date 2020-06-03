package it.unicam.cs.pa.jbudget105129.model;

import it.unicam.cs.pa.jbudget105129.enums.MovementType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
//TODO javadoc
public class RoundedTransaction implements Transaction {

    private final String description;
    private final List<Movement> movements;
    private Date date;
    private BigDecimal totalAmount;

    public RoundedTransaction(String description, Date date){
        this.date=date;
        this.movements=new ArrayList<>();
        this.description=description;
        this.totalAmount=new BigDecimal(0).setScale(2, RoundingMode.HALF_DOWN);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<Tag> getTags() {
        return movements.parallelStream().map(Movement::getTags)
                .flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    public List<Movement> getMovements() {
        return movements;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public double getTotalAmount() {
        return totalAmount.doubleValue();
    }

    @Override
    public void addTag(Tag tag) {
        movements.forEach(m->m.addTag(tag));
    }

    @Override
    public void removeTag(Tag tag) {
        movements.forEach(m->m.removeTag(tag));
    }

    @Override
    public void addMovement(Movement movement) {
        movements.add(movement);
        updateTotalAmount(movement,false);
        movement.setTransaction(this);
    }

    @Override
    public void removeMovement(Movement movement) {
        movements.remove(movement);
        updateTotalAmount(movement,true);
    }

    @Override
    public void setDate(Date date) {
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
