package it.unicam.cs.pa.jbudget105129.view;

import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import it.unicam.cs.pa.jbudget105129.model.Account;
import it.unicam.cs.pa.jbudget105129.model.Movement;
import it.unicam.cs.pa.jbudget105129.model.Tag;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LedgerPrinter {
    String stringOf(Account account);

    String stringOf(LocalDate date);

    String stringOfMovements(List<Movement> movements);

    String stringOfTags(List<Tag> tags);

    String stringOf(AccountType type);

    String stringOf(boolean bool);

    String stringOf(MovementType type);

    String stringOf(Optional<Double> value);
}
