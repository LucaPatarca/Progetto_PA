package it.unicam.cs.pa.jbudget105129.view;

import com.google.inject.Guice;
import com.google.inject.Injector;
import it.unicam.cs.pa.jbudget105129.controller.LedgerManagerModule;
import it.unicam.cs.pa.jbudget105129.controller.LedgerManager;
import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import it.unicam.cs.pa.jbudget105129.model.Account;
import it.unicam.cs.pa.jbudget105129.model.ScheduledTransaction;
import it.unicam.cs.pa.jbudget105129.model.Transaction;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

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
    @FXML public TableColumn<Transaction,Double> transactionTotalCol;
    @FXML public TableColumn<Transaction,String> transactionMovementsCol;
    @FXML public TableColumn<Transaction,String> transactionTagsCol;
    @FXML public TableColumn<Account,String> accountNameCol;
    @FXML public TableColumn<Account,String> accountDescriptionCol;
    @FXML public TableColumn<Account,String> accountReferentCol;
    @FXML public TableColumn<Account,String> accountTypeCol;
    @FXML public TableColumn<Account,Double> accountBalanceCol;
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
    @FXML public MenuItem loadMenuItem;
    @FXML public ContextMenu transactionContextMenu;
    @FXML public MenuItem transactionContextDeleteItem;
    @FXML public MenuItem transactionContextShowMovementsItem;

    private LedgerManager ledgerManager;
    private Injector injector;
    private boolean unsavedChanges;

    @FXML protected void handleNewTransaction() {
        FXMLLoader loader = createLoader("/transactionWizard.fxml",param->injector.getInstance(TransactionWizardFXController.class));
        Scene scene = createScene(loader,750,450);
        loadNewScene(scene,"Adding new transaction");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        injector = Guice.createInjector(new LedgerManagerModule(),new ViewModule());
        ledgerManager=injector.getInstance(LedgerManager.class);
        unsavedChanges=false;
        saveMenuItem.setDisable(true);
        try {
            ledgerManager.loadLedger("ledger.txt");
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: 06/06/20 log
            showAlert("Ledger load error",e.getMessage(), Alert.AlertType.ERROR);
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
        transactionTotalCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        transactionMovementsCol.setCellValueFactory(cellData-> new SimpleStringProperty(Long.toString((long) cellData.getValue().getMovements().size())));
        transactionTagsCol.setCellValueFactory(cellData->new SimpleStringProperty(Integer.toString(cellData.getValue().getTags().size())));
        transactionTable.setItems(FXCollections.observableList(ledgerManager.getLedger().getTransactions()));
        transactionTable.setOnContextMenuRequested(event-> transactionContextMenu.show(transactionTable,event.getScreenX(),event.getScreenY()));
    }

    private void initAccountTable(){
        accountNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        accountDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        accountReferentCol.setCellValueFactory(new PropertyValueFactory<>("referent"));
        accountTypeCol.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getType().toString().toLowerCase()));
        accountBalanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));
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
        unsavedChanges=true;
        saveMenuItem.setDisable(false);
        saveMenuItem.getParentMenu().fire();
    }

    @FXML public void handleSavePressed() {
        try {
            ledgerManager.saveLedger("ledger.txt");
            unsavedChanges=false;
            saveMenuItem.setDisable(true);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: 06/06/20 log
            showAlert("Ledger save error",e.getMessage(), Alert.AlertType.ERROR);
        }
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
            // TODO: 07/06/20 log
            showAlert("Transaction error",e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void loadNewScene(Scene scene, String title){
        Stage stage = (Stage) mainVbox.getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(scene);
    }

    private void showNewStage(Scene scene, String title){
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(transactionTable.getScene().getWindow());
        stage.showAndWait();
    }

    private Scene createScene(FXMLLoader loader, int width, int height){
        try {
            Parent root = loader.load();
            return new Scene(root,width,height);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: 15/06/2020 log
        }
        return null;
    }

    private FXMLLoader createLoader(String resource, Callback<Class<?>,Object> factory){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        loader.setControllerFactory(factory);
        return loader;
    }

    @FXML public void handleNewAccountPressed(ActionEvent event) {
        FXMLLoader loader = createLoader("/accountWizard.fxml",param->injector.getInstance(AccountWizardFXController.class));
        Scene scene = createScene(loader,480,350);
        loadNewScene(scene,"Adding new account");
    }

    @FXML public void handleNewSTransactionPressed(ActionEvent event) {
        FXMLLoader loader = createLoader("/scheduledTransactionWizard.fxml",param->injector.getInstance(STWizardFXController.class));
        Scene scene = createScene(loader,750,450);
        loadNewScene(scene,"Adding new scheduled transacion");
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
            // TODO: 11/06/20 log
            showAlert("Ledger load error",e.getMessage(), Alert.AlertType.ERROR);
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
            // TODO: 11/06/20 log
            showAlert("Account error",e.getLocalizedMessage(), Alert.AlertType.ERROR);
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
            // TODO: 11/06/20 log
            showAlert("Scheduled transaction error",e.getMessage(), Alert.AlertType.ERROR);
        }
        sTransactionTable.setItems(FXCollections.emptyObservableList());
    }

    private void showAlert(String title, String content, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML public void handleTransactionContextDeletePressed(ActionEvent actionEvent) {
        removeSelectedTransaction();
    }

    @FXML public void transactionContextShowMovementsPressed(ActionEvent actionEvent) {
        Transaction selected = transactionTable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = createLoader("/movementsPopup.fxml",
                param->new MovementsPopupFXController(selected.getMovements()));
        Scene scene = createScene(loader,400,300);
        showNewStage(scene,selected.getDescription());
    }
}
