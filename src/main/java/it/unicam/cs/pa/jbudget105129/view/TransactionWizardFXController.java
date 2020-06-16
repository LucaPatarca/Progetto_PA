package it.unicam.cs.pa.jbudget105129.view;

import it.unicam.cs.pa.jbudget105129.controller.LedgerManager;
import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import it.unicam.cs.pa.jbudget105129.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.temporal.TemporalField;
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
    @FXML public ContextMenu movementsContextMenu;
    @FXML public MenuItem editTagsMovementMenuItem;

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
        movementTable.setOnContextMenuRequested(event->movementsContextMenu.show(movementTable,event.getScreenX(),event.getScreenY()));

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
        returnToMainScene();
    }

    @FXML public void handleAddTransactionPressed() {
        try {
            ledgerManager.addTransaction(
                    transactionDescriptionTextField.getText(),
                    transactionDate.getValue(),
                    List.copyOf(movementTable.getItems()),
                    new LinkedList<>()
            );
            returnToMainScene();
        } catch (AccountException e) {
            e.printStackTrace();
            // TODO: 05/06/20 gestire
        }
    }

    private void returnToMainScene(){
        Stage stage= (Stage) cancelButton.getScene().getWindow();
        stage.setScene(mainScene);
    }

    @FXML public void handleMovementEditTagPressed(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/editTagsPopup.fxml"));
        loader.setControllerFactory(param->new EditTagsFXController(
                movementTable.getSelectionModel().getSelectedItem(),
                ledgerManager.getAllUsedTags()
        ));
        Stage stage = new Stage();
        try {
            Parent root = loader.load();
            Scene scene=new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML public void movementTableKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.DELETE)){
            Movement toRemove = movementTable.getSelectionModel().getSelectedItem();
            movementTable.getItems().remove(toRemove);
        }
    }
}
