package it.unicam.cs.pa.jbudget105129.view;

import com.google.inject.Guice;
import com.google.inject.Injector;
import it.unicam.cs.pa.jbudget105129.Dependency.LedgerManagerModule;
import it.unicam.cs.pa.jbudget105129.controller.LedgerManager;
import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import it.unicam.cs.pa.jbudget105129.model.Account;
import it.unicam.cs.pa.jbudget105129.model.Transaction;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class MainFXController implements Initializable, PropertyChangeListener {
    @FXML public MenuItem newTransactionItem;
    @FXML public VBox mainVbox;
    @FXML public TableView<Transaction> transactionTable;
    @FXML public TableColumn<Transaction, Date> transactionDateCol;
    @FXML public TableColumn<Transaction,String> transactionDescriptionCol;
    @FXML public TableColumn<Transaction,Double> transactionTotalCol;
    @FXML public TableColumn<Transaction,String> transactionMovementsCol;
    @FXML public TableColumn<Transaction,String> transactionTagsCol;
    @FXML public TableColumn<Account,String> accountNameCol;
    @FXML public TableColumn<Account,String> accountDescriptionCol;
    @FXML public TableColumn<Account,String> accountReferentCol;
    @FXML public TableColumn<Account,AccountType> accountTypeCol;
    @FXML public TableColumn<Account,Double> accountBalanceCol;
    @FXML public TableView<Account> accountTable;
    @FXML public MenuItem saveMenuItem;

    private LedgerManager ledgerManager;

    @FXML protected void handleNewTransaction() {
        Stage stage = (Stage) mainVbox.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/transactionWizard.fxml"));
        loader.setControllerFactory(param -> new TransactionWizardFXController(mainVbox.getScene(),ledgerManager));
        Parent root = null;
        try {
            root=loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: 05/06/20 log
        }
        assert root != null;
        Scene scene = new Scene(root,750,450);
        stage.setTitle("Adding new transaction");
        stage.setScene(scene);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Injector injector = Guice.createInjector(new LedgerManagerModule());
        ledgerManager=injector.getInstance(LedgerManager.class);
        try {
            ledgerManager.loadLedger("ledger.txt");
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: 06/06/20 log
        }
        initTransactionTable();
        initAccountTable();
    }

    private void initTransactionTable(){
        transactionDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        transactionDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        transactionTotalCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        transactionMovementsCol.setCellValueFactory(new PropertyValueFactory<>("movements"));
        transactionTagsCol.setCellValueFactory(new PropertyValueFactory<>("tags"));
        ledgerManager.getLedger().getPropertyChangeSupport().addPropertyChangeListener(this);
        transactionTable.setItems(FXCollections.observableList(ledgerManager.getLedger().getTransactions()));
    }

    private void initAccountTable(){
        accountNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        accountDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        accountReferentCol.setCellValueFactory(new PropertyValueFactory<>("referent"));
        accountTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        accountBalanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));
        ledgerManager.getLedger().getPropertyChangeSupport().addPropertyChangeListener(this);
        accountTable.setItems(FXCollections.observableList(ledgerManager.getLedger().getAccounts()));
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        accountTable.refresh();
        transactionTable.refresh();
    }

    @FXML public void handleSavePressed() {
        try {
            ledgerManager.saveLedger("ledger.txt");
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: 06/06/20 gestire e log
        }
    }

    @FXML public void handleTransactionClicked(MouseEvent mouseEvent) {
        // TODO: 07/06/20 menu contestuale
    }

    @FXML public void handleTransactionKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.DELETE) {
            removeSelectedTransaction();
        }
    }

    private void removeSelectedTransaction(){
        Transaction transaction = transactionTable.getSelectionModel().getSelectedItem();
        try {
            ledgerManager.removeTransaction(transaction);
        } catch (AccountException e) {
            e.printStackTrace();
            // TODO: 07/06/20 gestire e log
        }
        transactionTable.refresh();
        accountTable.refresh();
    }
}
