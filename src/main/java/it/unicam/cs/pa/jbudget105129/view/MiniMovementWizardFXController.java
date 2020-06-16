package it.unicam.cs.pa.jbudget105129.view;

import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import it.unicam.cs.pa.jbudget105129.model.Account;
import it.unicam.cs.pa.jbudget105129.model.RoundedMovement;
import it.unicam.cs.pa.jbudget105129.model.Transaction;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MiniMovementWizardFXController implements Initializable {

    private final Transaction transaction;
    private final List<Account> accounts;

    @FXML public Button cancelButton;
    @FXML public Button doneButton;
    @FXML public TextField descriptionTextField;
    @FXML public Spinner<Double> amountSpinner;
    @FXML public ChoiceBox<MovementType> typeSelect;
    @FXML public ChoiceBox<Account> accountSelect;

    public MiniMovementWizardFXController(Transaction transaction, List<Account> accounts){
        this.transaction=transaction;
        this.accounts=accounts;
    }

    public void handleCancelPressed(ActionEvent actionEvent) {
        closePopup();
    }

    public void handleDonePressed(ActionEvent actionEvent) {
        transaction.addMovement(RoundedMovement.getInstance(
                descriptionTextField.getText(),
                amountSpinner.getValue(),
                typeSelect.getValue(),
                accountSelect.getValue()
        ));
        closePopup();
    }

    private void closePopup(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeSelect.setItems(FXCollections.observableArrayList(MovementType.values()));
        typeSelect.setConverter(new MovementTypeConverter());
        accountSelect.setItems(FXCollections.observableList(accounts));
        accountSelect.setConverter(new AccountConverter());
        amountSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                Double.MIN_VALUE,Double.MAX_VALUE,1,0.01));
    }
}
