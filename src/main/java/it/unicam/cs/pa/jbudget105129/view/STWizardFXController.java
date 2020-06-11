package it.unicam.cs.pa.jbudget105129.view;

import com.google.inject.Inject;
import it.unicam.cs.pa.jbudget105129.controller.LedgerManager;
import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import it.unicam.cs.pa.jbudget105129.model.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

// TODO: 10/06/20 dividere i due conroller dei mini wizard
public class STWizardFXController implements Initializable {

    @FXML public Button addButton;
    @FXML public TextField descriptionTextField;
    @FXML public Button addTransactionButton;
    @FXML public Button cancelButton;
    @FXML public TableColumn<Transaction,Double> transactionTotalCol;
    @FXML public TableColumn<Transaction,String> transactionDescriptionCol;
    @FXML public TableColumn<Transaction, Date> transactionDateCol;
    @FXML public TableView<Transaction> transactionTable;
    public TableView<Movement> movementTable;
    public TableColumn<Movement,String> movementDescriptionCol;
    public TableColumn<Movement, MovementType> movementTypeCol;
    public TableColumn<Movement,Double> movementAmountCol;
    public TableColumn<Movement, Account> movementAccountCol;
    public Button addMovementButton;
    public TextField transactionTextField;
    public DatePicker transactionDatePicker;
    public Button transactionDoneButton;
    public Button transactionCancelButton;
    public TextField movementTextField;
    public ChoiceBox<MovementType> movementTypeSelect;
    public Spinner<Double> movementAmountSpinner;
    public ChoiceBox<Account> movementAccountSelect;

    private final Scene mainScene;
    private final LedgerManager manager;

    @Inject
    protected STWizardFXController(@MainScene Scene scene, LedgerManager manager){
        this.mainScene=scene;
        this.manager=manager;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transactionDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        transactionDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        transactionTotalCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        movementDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        movementTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        movementAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        movementAccountCol.setCellValueFactory(new PropertyValueFactory<>("account"));
    }

    @FXML public void handleCancelButtonPressed(ActionEvent event) {
        returnToMainScene();
    }

    @FXML public void handleAddTransactionButtonPressed(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/miniTransactionWizard.fxml"));
        loader.setController(this);
        Stage stage = new Stage();
        try {
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML public void handleAddButtonPressed(ActionEvent event) {
        manager.addScheduledTransaction(descriptionTextField.getText(),transactionTable.getItems());
        returnToMainScene();
    }

    private void returnToMainScene(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.setScene(mainScene);
    }

    public void handleTransactionMouseClicked(MouseEvent mouseEvent) {
        movementTable.setItems(FXCollections.observableList(
                transactionTable.getSelectionModel().getSelectedItem().getMovements()
        ));
    }

    public void handleAddMovementPressed(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/miniMovementWizard.fxml"));
        loader.setController(this);
        Stage stage = new Stage();
        try {
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            movementTypeSelect.setItems(FXCollections.observableArrayList(MovementType.values()));
            movementAccountSelect.setItems(FXCollections.observableList(manager.getLedger().getAccounts()));
            movementAmountSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(Double.MIN_VALUE,Double.MAX_VALUE,1,0.01));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleTransactionDonePressed(ActionEvent event) {
        transactionTable.getItems().add(new RoundedTransaction(
                transactionTextField.getText(),
                Calendar.getInstance().getTime()
        ));
        closePopup(event.getSource());
    }

    public void handleTransactionCancelPressed(ActionEvent event) {
        closePopup(event.getSource());
    }

    public void handleMovementCancelPressed(ActionEvent event){
        closePopup(event.getSource());
    }

    public void handleMovementDonePressed(ActionEvent event){
        Transaction transaction = transactionTable.getSelectionModel().getSelectedItem();
        transaction.addMovement(RoundedMovement.getInstance(
                movementTextField.getText(),
                movementAmountSpinner.getValue(),
                movementTypeSelect.getValue(),
                movementAccountSelect.getValue()
        ));
        transactionTable.refresh();
        movementTable.refresh();
        closePopup(event.getSource());
    }

    private void closePopup(Object source){
        Node node = (Node) source;
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
}
