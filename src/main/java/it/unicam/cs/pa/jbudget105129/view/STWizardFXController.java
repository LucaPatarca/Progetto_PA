package it.unicam.cs.pa.jbudget105129.view;

import it.unicam.cs.pa.jbudget105129.controller.LedgerManager;
import it.unicam.cs.pa.jbudget105129.model.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class STWizardFXController implements Initializable {

    @FXML public Button addButton;
    @FXML public TextField descriptionTextField;
    @FXML public Button addTransactionButton;
    @FXML public Button cancelButton;
    @FXML public TableColumn<Transaction,Double> transactionTotalCol;
    @FXML public TableColumn<Transaction,String> transactionDescriptionCol;
    @FXML public TableColumn<Transaction, String> transactionDateCol;
    @FXML public TableView<Transaction> transactionTable;
    @FXML public TableView<Movement> movementTable;
    @FXML public TableColumn<Movement,String> movementDescriptionCol;
    @FXML public TableColumn<Movement, String> movementTypeCol;
    @FXML public TableColumn<Movement,Double> movementAmountCol;
    @FXML public TableColumn<Movement, String> movementAccountCol;
    @FXML public Button addMovementButton;

    private final Scene mainScene;
    private final LedgerManager manager;
    private final LedgerPrinter printer;
    private final Logger logger;

    protected STWizardFXController(Scene scene, LedgerManager manager){
        this.mainScene=scene;
        this.manager=manager;
        printer = new LedgerPrinter();
        this.logger = Logger.getLogger("it.unicam.cs.pa.jbudget105129.view.STWizardFXController");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transactionDateCol.setCellValueFactory(cellData->new ReadOnlyStringWrapper(printer.stringOf(cellData.getValue().getDate())));
        transactionDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        transactionTotalCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        movementDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        movementTypeCol.setCellValueFactory(cellData->new SimpleStringProperty(printer.stringOf(cellData.getValue().getType())));
        movementAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        movementAccountCol.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getAccount().getName()));
    }

    @FXML public void handleCancelButtonPressed() {
        returnToMainScene();
    }

    @FXML public void handleAddTransactionButtonPressed() {
        loadNewScene("/miniTransactionWizard.fxml", p->new MiniTransactionWizardFXController(transactionTable.getItems()));
    }

    @FXML public void handleAddButtonPressed() {
        try{
            manager.addScheduledTransaction(descriptionTextField.getText(), transactionTable.getItems());
            returnToMainScene();
        }catch (IllegalArgumentException e){
            showAlert("Scheduled Transaction Information Error",
                    e.getLocalizedMessage()+"\nCheck new scheduled transaction's information");
        }
    }

    @FXML public void handleTransactionMouseClicked() {
        movementTable.setItems(FXCollections.observableList(
                transactionTable.getSelectionModel().getSelectedItem().getMovements()
        ));
    }

    @FXML public void handleAddMovementPressed() {
        Transaction transaction = transactionTable.getSelectionModel().getSelectedItem();
        if(Objects.isNull(transaction)){
            showAlert("No Transaction Selected","Please select a transaction to add a movement");
        } else{
            loadNewScene("/miniMovementWizard.fxml",p->new MiniMovementWizardFXController(
                    transaction, manager.getLedger().getAccounts()
            ));
            transactionTable.refresh();
            movementTable.setItems(FXCollections.observableList(
                    transactionTable.getSelectionModel().getSelectedItem().getMovements()));
        }
    }

    @FXML public void handleTransactionKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.DELETE)){
            Transaction toRemove = transactionTable.getSelectionModel().getSelectedItem();
            transactionTable.getItems().remove(toRemove);
            movementTable.getItems().clear();
        }
    }

    @FXML public void handleMovementKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.DELETE)){
            Movement toRemove = movementTable.getSelectionModel().getSelectedItem();
            Transaction transaction = transactionTable.getSelectionModel().getSelectedItem();
            transaction.removeMovement(toRemove);
            movementTable.setItems(FXCollections.observableList(
                    transactionTable.getSelectionModel().getSelectedItem().getMovements()));
        }
    }

    private void returnToMainScene(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.setTitle("JBudget");
        stage.setScene(mainScene);
    }

    private void loadNewScene(String resource, Callback<Class<?>,Object> controllerFactory){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        loader.setControllerFactory(controllerFactory);
        Stage stage = new Stage();
        try {
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            logger.severe("Unable to open '"+resource+"' scene: "+e.getMessage());
        }
    }

    private void showAlert(String title,String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
