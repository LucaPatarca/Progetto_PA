package it.unicam.cs.pa.jbudget105129.view;

import it.unicam.cs.pa.jbudget105129.controller.LedgerManager;
import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import it.unicam.cs.pa.jbudget105129.model.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class TransactionWizardFXController implements Initializable {

    @FXML public TableColumn<Movement, String> movementDescriptionCol;
    @FXML public TableColumn<Movement,String> movementTypeCol;
    @FXML public TableColumn<Movement,Double> movementAmountCol;
    @FXML public TableColumn<Movement,String> movementAccountCol;
    @FXML public TableView<Movement> movementTable;
    @FXML public Button addMovementButton;
    @FXML public TextField movementDescriptionTextfield;
    @FXML public ChoiceBox<MovementType> movementTypeSelect;
    @FXML public ChoiceBox<Account> movementAccountSelect;
    @FXML public Spinner<Double> movementAmountSpinner;
    @FXML public Button cancelButton;
    @FXML public TextField transactionDescriptionTextField;
    @FXML public DatePicker transactionDate;
    @FXML public Button addTransactionButton;

    private final LedgerManager ledgerManager;
    private final Scene mainScene;

    public TransactionWizardFXController(Scene mainScene,LedgerManager ledgerManager){
        this.ledgerManager=ledgerManager;
        this.mainScene=mainScene;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        movementDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        movementAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        movementAccountCol.setCellValueFactory(new PropertyValueFactory<>("account"));
        movementTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        movementTypeSelect.setItems(FXCollections.observableArrayList(MovementType.values()));
        movementAccountSelect.setItems(FXCollections.observableArrayList(ledgerManager.getLedger().getAccounts()));

        movementAmountSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-100,1000,1));

    }

    @FXML protected void handleAddMovementPressed(ActionEvent event) {
        movementTable.getItems().add(RoundedMovement.getInstance(
                movementDescriptionTextfield.getText(),
                movementAmountSpinner.getValue(),
                movementTypeSelect.getValue(),
                movementAccountSelect.getValue()
        ));
    }

    @FXML public void handleCancelPressed(ActionEvent event) {
        returnToMainView();
    }

    @FXML public void handleAddTransactionPressed(ActionEvent event) {
        try {
            ledgerManager.addTransaction(
                    transactionDescriptionTextField.getText(),
                    Calendar.getInstance().getTime(),
                    List.copyOf(movementTable.getItems()),
                    new LinkedList<>()
            );
            returnToMainView();
        } catch (AccountException e) {
            e.printStackTrace();
            // TODO: 05/06/20 gestire
        }
    }

    private void returnToMainView(){
        Stage stage= (Stage) cancelButton.getScene().getWindow();
        stage.setScene(mainScene);
    }
}
