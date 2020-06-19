package it.unicam.cs.pa.jbudget105129.view;

import com.google.inject.Inject;
import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import javafx.util.StringConverter;

public class MovementTypeConverter extends StringConverter<MovementType> {

    private final LedgerPrinter printer;

    @Inject
    public MovementTypeConverter(LedgerPrinter printer){
        this.printer=printer;
    }
    @Override
    public String toString(MovementType movementType) {
        return printer.stringOf(movementType);
    }

    @Override
    public MovementType fromString(String s) {
        return null;
    }
}
