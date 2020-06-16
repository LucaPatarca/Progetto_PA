package it.unicam.cs.pa.jbudget105129.view;

import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import it.unicam.cs.pa.jbudget105129.model.Account;
import it.unicam.cs.pa.jbudget105129.model.Movement;
import it.unicam.cs.pa.jbudget105129.model.Tag;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class LedgerPrinter {

    private final DateFormatter dateFormatter;

    public LedgerPrinter(){
        dateFormatter=new DateFormatter();
        dateFormatter.setFormat(DateFormat.getDateInstance(DateFormat.MEDIUM));
    }

    public String stringOf(Account account){
        return account.getName()+", "+account.getDescription();
    }

    public String stringOf(Date date) {
        String toReturn="";
        try {
            toReturn= dateFormatter.valueToString(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    public String stringOfMovements(List<Movement> movements){
        // FIXME: 16/06/2020 togliere la virgola finale
        String toReturn="";
        for(Movement m : movements){
            toReturn=toReturn.concat(m.getDescription()+", ");
        }
        return toReturn;
    }

    public String stringOfTags(List<Tag> tags){
        // FIXME: 16/06/2020 togliere la virgola finale
        String toReturn="";
        for(Tag m : tags){
            toReturn=toReturn.concat(m.getName()+", ");
        }
        return toReturn;
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
