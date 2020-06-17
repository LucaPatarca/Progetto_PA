package it.unicam.cs.pa.jbudget105129.view;

import it.unicam.cs.pa.jbudget105129.model.RoundedTransaction;
import it.unicam.cs.pa.jbudget105129.model.Transaction;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class MiniTransactionWizardFXController implements Initializable {

    private final ObservableList<Transaction> list;
    public TextField descriptionTextField;
    public DatePicker datePicker;
    public Button doneButton;
    public Button cancelButton;

    public MiniTransactionWizardFXController(ObservableList<Transaction> list){
        this.list=list;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        datePicker.setValue(LocalDate.now());
    }

    @FXML public void handleDonePressed() {
        if(checkInput()){
            list.add(new RoundedTransaction(
                    descriptionTextField.getText(),
                    datePicker.getValue()
            ));
            closePopup();
        }else {
            showAlert();
        }
    }

    @FXML public void handleCancelPressed() {
        closePopup();
    }

    private void closePopup(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private boolean checkInput(){
        return Objects.nonNull(descriptionTextField.getText()) &&
                Objects.nonNull(datePicker.getValue()) &&
                !descriptionTextField.getText().equals("");
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Transaction Input Error");
        alert.setContentText("Check new transaction's information");
        alert.showAndWait();
    }
}
