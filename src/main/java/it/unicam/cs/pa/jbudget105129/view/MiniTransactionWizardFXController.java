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
import java.util.logging.Logger;

public class MiniTransactionWizardFXController implements Initializable {

    private final ObservableList<Transaction> list;
    private final Logger logger;

    @FXML public TextField descriptionTextField;
    @FXML public DatePicker datePicker;
    @FXML public Button doneButton;
    @FXML public Button cancelButton;

    public MiniTransactionWizardFXController(ObservableList<Transaction> list){
        this.list=list;
        this.logger=Logger.getLogger("it.unicam.cs.pa.jbudget105129.view.MiniTransactionFXController");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("opening mini transaction wizard");
        datePicker.setValue(LocalDate.now());
    }

    @FXML public void handleDonePressed() {
        if(checkInput()){
            RoundedTransaction transaction=new RoundedTransaction(
                    descriptionTextField.getText(),
                    datePicker.getValue()
            );
            list.add(transaction);
            logger.info("added new transaction: "+transaction);
            closePopup();
        }else {
            logger.warning("tried to add new transaction with wrong parameters: '"
            +descriptionTextField.getText()+"', '"
            +datePicker.getValue().toString()+"'");
            showAlert();
        }
    }

    @FXML public void handleCancelPressed() {
        closePopup();
    }

    private void closePopup(){
        logger.info("closing mini transaction wizard");
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private boolean checkInput(){
        return Objects.nonNull(descriptionTextField.getText()) &&
                Objects.nonNull(datePicker.getValue()) &&
                !descriptionTextField.getText().isBlank();
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Transaction Input Error");
        alert.setContentText("Check new transaction's information");
        alert.showAndWait();
    }
}
