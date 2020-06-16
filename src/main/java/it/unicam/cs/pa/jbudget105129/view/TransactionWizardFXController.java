package it.unicam.cs.pa.jbudget105129.view;

import com.google.inject.Inject;
import it.unicam.cs.pa.jbudget105129.annotations.MainScene;
import it.unicam.cs.pa.jbudget105129.controller.LedgerManager;
import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import it.unicam.cs.pa.jbudget105129.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.FormatStringConverter;

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
    private final LedgerPrinter ledgerPrinter;

    public TransactionWizardFXController(Scene mainScene, LedgerManager ledgerManager){
        this.ledgerManager=ledgerManager;
        this.mainScene=mainScene;
        ledgerPrinter = new LedgerPrinter();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        movementDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        movementAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        movementAccountCol.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getAccount().getName()));
        movementTypeCol.setCellValueFactory(cellData->new SimpleStringProperty(ledgerPrinter.stringOf(cellData.getValue().getType())));

        movementTypeSelect.setItems(FXCollections.observableArrayList(MovementType.values()));
        movementTypeSelect.setConverter(new MovementTypeConverter());
        movementAccountSelect.setItems(FXCollections.observableArrayList(ledgerManager.getLedger().getAccounts()));
        movementAccountSelect.setConverter(new AccountConverter());
        movementAmountSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                Double.MIN_VALUE,Double.MAX_VALUE,1.0,0.01));
    }

    @FXML protected void handleAddMovementPressed() {
        movementTable.getItems().add(RoundedMovement.getInstance(
                movementDescriptionTextfield.getText(),
                movementAmountSpinner.getValue(),
                movementTypeSelect.getValue(),
                movementAccountSelect.getValue()
        ));
    }

    @FXML public void handleCancelPressed() {
        returnToMainView();
    }

    @FXML public void handleAddTransactionPressed() {
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
