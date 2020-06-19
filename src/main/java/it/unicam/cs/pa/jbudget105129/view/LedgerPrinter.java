package it.unicam.cs.pa.jbudget105129.view;

import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import it.unicam.cs.pa.jbudget105129.model.Account;
import it.unicam.cs.pa.jbudget105129.model.Movement;
import it.unicam.cs.pa.jbudget105129.model.Tag;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class LedgerPrinter {
    // TODO: 19/06/2020 extract interface

    public String stringOf(Account account){
        return account.getName()+", "+account.getDescription();
    }

    public String stringOf(LocalDate date) {
        return date.toString();
    }

    public String stringOfMovements(List<Movement> movements){
        String toReturn="";
        if(movements.isEmpty()) return toReturn;
        for(Movement m : movements){
            toReturn=toReturn.concat(m.getDescription()+", ");
        }
        return toReturn.subSequence(0,toReturn.length()-2).toString();
    }

    public String stringOfTags(List<Tag> tags){
        String toReturn="";
        if(tags.isEmpty()) return toReturn;
        for(Tag t : tags){
            toReturn=toReturn.concat(t.getName()+", ");
        }
        return toReturn.subSequence(0,toReturn.length()-2).toString();
    }

    public String stringOf(AccountType type){
        return type.toString().toLowerCase();
    }

    public String stringOf(boolean bool){
        if(bool)
            return "yes";
        else
            return "no";
    }

    public String stringOf(MovementType type){
        return type.toString().toLowerCase();
    }

    public String stringOf(Optional<Double> value){
        if(value.isEmpty()){
            return "";
        } else{
            return value.get().toString();
        }
    }

}
