package it.unicam.cs.pa.jbudget105129.view;

import it.unicam.cs.pa.jbudget105129.model.RoundedTransaction;
import it.unicam.cs.pa.jbudget105129.model.Transaction;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Calendar;

public class MiniTransactionWizardFXController {

    private final ObservableList<Transaction> list;
    public TextField descriptionTextField;
    public DatePicker datePicker;
    public Button doneButton;
    public Button cancelButton;

    public MiniTransactionWizardFXController(ObservableList<Transaction> list){
        this.list=list;
    }


    public void handleDonePressed(ActionEvent actionEvent) {
        list.add(new RoundedTransaction(
                descriptionTextField.getText(),
                Calendar.getInstance().getTime()
        ));
        closePopup();
    }

    public void handleCancelPressed(ActionEvent actionEvent) {
        closePopup();
    }

    private void closePopup(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
