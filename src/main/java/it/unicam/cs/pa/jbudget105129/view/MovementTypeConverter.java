package it.unicam.cs.pa.jbudget105129.view;

import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import javafx.util.StringConverter;

public class MovementTypeConverter extends StringConverter<MovementType> {
    @Override
    public String toString(MovementType movementType) {
        return movementType.toString().toLowerCase();
    }

    @Override
    public MovementType fromString(String s) {
        return null;
    }
}
