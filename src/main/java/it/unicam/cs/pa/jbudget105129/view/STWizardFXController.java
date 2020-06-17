package it.unicam.cs.pa.jbudget105129.view;

import it.unicam.cs.pa.jbudget105129.controller.LedgerManager;
import it.unicam.cs.pa.jbudget105129.model.*;
import javafx.beans.property.ReadOnlyStringWrapper;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

    protected STWizardFXController(Scene scene, LedgerManager manager){
        this.mainScene=scene;
        this.manager=manager;
        printer = new LedgerPrinter();
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

    @FXML public void handleCancelButtonPressed(ActionEvent event) {
        returnToMainScene();
    }

    @FXML public void handleAddTransactionButtonPressed(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/miniTransactionWizard.fxml"));
        loader.setControllerFactory(param->new MiniTransactionWizardFXController(transactionTable.getItems()));
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

    @FXML public void handleTransactionMouseClicked(MouseEvent mouseEvent) {
        movementTable.setItems(FXCollections.observableList(
                transactionTable.getSelectionModel().getSelectedItem().getMovements()
        ));
    }

    @FXML public void handleAddMovementPressed(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/miniMovementWizard.fxml"));
        loader.setControllerFactory(param->new MiniMovementWizardFXController(
                transactionTable.getSelectionModel().getSelectedItem(),
                manager.getLedger().getAccounts()
        ));
        Stage stage = new Stage();
        try {
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.showAndWait();
            transactionTable.refresh();
            movementTable.setItems(FXCollections.observableList(
                    transactionTable.getSelectionModel().getSelectedItem().getMovements()));
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: 16/06/2020 log e gestire
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
}
