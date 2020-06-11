package it.unicam.cs.pa.jbudget105129.view;

import com.google.inject.Guice;
import com.google.inject.Injector;
import it.unicam.cs.pa.jbudget105129.controller.LedgerManagerModule;
import it.unicam.cs.pa.jbudget105129.controller.LedgerManager;
import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import it.unicam.cs.pa.jbudget105129.model.Account;
import it.unicam.cs.pa.jbudget105129.model.RoundedTransaction;
import it.unicam.cs.pa.jbudget105129.model.ScheduledTransaction;
import it.unicam.cs.pa.jbudget105129.model.Transaction;
import javafx.beans.property.*;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.text.DateFormatter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.ResourceBundle;

public class MainFXController implements Initializable, PropertyChangeListener {

    @FXML public MenuItem newTransactionItem;
    @FXML public VBox mainVbox;
    @FXML public TableView<Transaction> transactionTable;
    @FXML public TableColumn<Transaction, String> transactionDateCol;
    @FXML public TableColumn<Transaction,String> transactionDescriptionCol;
    @FXML public TableColumn<Transaction,String> transactionTotalCol;
    @FXML public TableColumn<Transaction,String> transactionMovementsCol;
    @FXML public TableColumn<Transaction,String> transactionTagsCol;
    @FXML public TableColumn<Account,String> accountNameCol;
    @FXML public TableColumn<Account,String> accountDescriptionCol;
    @FXML public TableColumn<Account,String> accountReferentCol;
    @FXML public TableColumn<Account,String> accountTypeCol;
    @FXML public TableColumn<Account,String> accountBalanceCol;
    @FXML public TableView<Account> accountTable;
    @FXML public MenuItem saveMenuItem;
    @FXML public MenuItem newAccountMenuItem;
    @FXML public MenuItem newSTransactionMenuItem;
    @FXML public TableView<ScheduledTransaction> scheduledTable;
    @FXML public TableColumn<ScheduledTransaction,String> scheduledDescriptionCol;
    @FXML public TableColumn<ScheduledTransaction,String> scheduledCompletedCol;
    @FXML public TableView<Transaction> sTransactionTable;
    @FXML public TableColumn<Transaction,String> sTransactionDateCol;
    @FXML public TableColumn<Transaction,String> sTransactionDescriptionCol;
    @FXML public TableColumn<Transaction,Double> sTransactionTotalCol;
    @FXML public TableColumn<Transaction,String> sTransactionCompletedCol;

    private LedgerManager ledgerManager;
    private Injector injector;

    @FXML protected void handleNewTransaction() {
        loadNewScene("/transactionWizard.fxml",750,450,"Adding new transaction",TransactionWizardFXController.class);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        injector = Guice.createInjector(new LedgerManagerModule(),new ViewModule());
        ledgerManager=injector.getInstance(LedgerManager.class);
        try {
            ledgerManager.loadLedger("ledger.txt");
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: 06/06/20 log e gestire
        }
        initTransactionTable();
        initAccountTable();
        initScheduledTable();
        ledgerManager.getLedger().getPropertyChangeSupport().addPropertyChangeListener(this);
    }

    private void initTransactionTable(){
        transactionDateCol.setCellValueFactory(cellData-> {
            DateFormatter formatter = new DateFormatter();
            formatter.setFormat(DateFormat.getDateInstance(DateFormat.MEDIUM));
            String date="";
            try {
                date = formatter.valueToString(cellData.getValue().getDate());
            } catch (ParseException e) {
                e.printStackTrace();
                // TODO: 11/06/20 log
            }
            return new SimpleObjectProperty<>(date);
        });
        transactionDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        transactionTotalCol.setCellValueFactory(cellData->new SimpleStringProperty("€"+cellData.getValue().getTotalAmount()));
        transactionMovementsCol.setCellValueFactory(cellData-> new SimpleStringProperty(Long.toString((long) cellData.getValue().getMovements().size())));
        transactionTagsCol.setCellValueFactory(cellData->new SimpleStringProperty(Integer.toString(cellData.getValue().getTags().size())));
        transactionTable.setItems(FXCollections.observableList(ledgerManager.getLedger().getTransactions()));
    }

    private void initAccountTable(){
        accountNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        accountDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        accountReferentCol.setCellValueFactory(new PropertyValueFactory<>("referent"));
        accountTypeCol.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getType().toString().toLowerCase()));
        accountBalanceCol.setCellValueFactory(cellData-> new SimpleStringProperty("€"+cellData.getValue().getBalance()));
        accountTable.setItems(FXCollections.observableList(ledgerManager.getLedger().getAccounts()));
    }

    private void initScheduledTable(){
        scheduledDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        scheduledCompletedCol.setCellValueFactory(cellData->{
            if(cellData.getValue().isCompleted())
                return new SimpleStringProperty("yes");
            else
                return new SimpleStringProperty("no");
        });
        scheduledTable.setItems(FXCollections.observableList(ledgerManager.getLedger().getScheduledTransactions()));

        sTransactionDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        sTransactionDateCol.setCellValueFactory(cellData->{
            DateFormatter formatter = new DateFormatter();
            formatter.setFormat(DateFormat.getDateInstance(DateFormat.MEDIUM));
            String date = "";
            try {
                date= formatter.valueToString(cellData.getValue().getDate());
            } catch (ParseException e) {
                e.printStackTrace();
                // TODO: 11/06/20 log e refactor di questo metodo
            }
            return new SimpleStringProperty(date);
        });
        sTransactionTotalCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        sTransactionCompletedCol.setCellValueFactory(cellData-> {
            if(scheduledTable.getSelectionModel().getSelectedItem().isCompleted(cellData.getValue()))
                return new SimpleStringProperty("yes");
            else
                return new SimpleStringProperty("no");
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        accountTable.refresh();
        transactionTable.refresh();
        scheduledTable.refresh();
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
    }

    private void loadNewScene(String resource,int width, int height,String title, Class<?> controller){
        Stage stage = (Stage) mainVbox.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        loader.setControllerFactory(param -> injector.getInstance(controller));
        try {
            Parent root=loader.load();
            Scene scene = new Scene(root,width,height);
            stage.setTitle(title);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: 05/06/20 log
        }
    }

    @FXML public void handleNewAccountPressed(ActionEvent event) {
        loadNewScene("/accountWizard.fxml",480,350,"Adding new account",AccountWizardFXController.class);
    }

    @FXML public void handleNewSTransactionPressed(ActionEvent event) {
        loadNewScene("/scheduledTransactionWizard.fxml",750,450,"Adding new scheduled transacion",STWizardFXController.class);
    }

    @FXML public void handleScheduledMouseClicked(MouseEvent mouseEvent) {
        ScheduledTransaction st = scheduledTable.getSelectionModel().getSelectedItem();
        sTransactionTable.setItems(FXCollections.observableList(st.getTransactions()));
    }

    @FXML public void handleLoadPressed(ActionEvent event) {
        try {
            ledgerManager.loadLedger("ledger.txt");
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: 11/06/20 log e gestire
        }
        transactionTable.setItems(FXCollections.observableList(ledgerManager.getLedger().getTransactions()));
        accountTable.setItems(FXCollections.observableList(ledgerManager.getLedger().getAccounts()));
        scheduledTable.setItems(FXCollections.observableList(ledgerManager.getLedger().getScheduledTransactions()));
        ledgerManager.getLedger().getPropertyChangeSupport().addPropertyChangeListener(this);
    }

    @FXML public void handleAccountClicked(MouseEvent mouseEvent) {
        // TODO: 11/06/20 menu
    }

    @FXML public void handleAccountKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.DELETE)){
            removeSelectedAccount();
        }
    }

    private void removeSelectedAccount(){
        Account account = accountTable.getSelectionModel().getSelectedItem();
        try {
            ledgerManager.removeAccount(account);
        } catch (AccountException e) {
            e.printStackTrace();
            // TODO: 11/06/20 gestire e log
        }
    }

    @FXML public void handleScheduledKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.DELETE)){
            removeSelectedScheduledTransaction();
        }
    }

    private void removeSelectedScheduledTransaction(){
        ScheduledTransaction st = scheduledTable.getSelectionModel().getSelectedItem();
        try {
            ledgerManager.removeScheduledTransaction(st);
        } catch (AccountException e) {
            e.printStackTrace();
            // TODO: 11/06/20 gestire
        }
        sTransactionTable.setItems(FXCollections.emptyObservableList());
    }
}
