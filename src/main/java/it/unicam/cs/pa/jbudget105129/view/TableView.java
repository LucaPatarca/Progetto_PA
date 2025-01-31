package it.unicam.cs.pa.jbudget105129.view;

import com.google.gson.JsonParseException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import it.unicam.cs.pa.jbudget105129.controller.LedgerManager;
import it.unicam.cs.pa.jbudget105129.controller.LedgerManagerModule;
import it.unicam.cs.pa.jbudget105129.exceptions.AccountException;
import it.unicam.cs.pa.jbudget105129.model.*;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class TableView extends Application implements Initializable,PropertyChangeListener {

    @FXML public MenuItem newTransactionItem;
    @FXML public VBox mainVbox;
    @FXML public javafx.scene.control.TableView<Transaction> transactionTable;
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
    @FXML public TableColumn<Account,String> accountMinCol;
    @FXML public TableColumn<Account,String> accountMaxCol;
    @FXML public javafx.scene.control.TableView<Account> accountTable;
    @FXML public MenuItem saveMenuItem;
    @FXML public MenuItem newAccountMenuItem;
    @FXML public MenuItem newSTransactionMenuItem;
    @FXML public javafx.scene.control.TableView<ScheduledTransaction> scheduledTable;
    @FXML public TableColumn<ScheduledTransaction,String> scheduledDescriptionCol;
    @FXML public TableColumn<ScheduledTransaction,String> scheduledCompletedCol;
    @FXML public javafx.scene.control.TableView<Transaction> sTransactionTable;
    @FXML public TableColumn<Transaction,String> sTransactionDateCol;
    @FXML public TableColumn<Transaction,String> sTransactionDescriptionCol;
    @FXML public TableColumn<Transaction,Double> sTransactionTotalCol;
    @FXML public TableColumn<Transaction,String> sTransactionCompletedCol;
    @FXML public MenuItem loadMenuItem;
    @FXML public ContextMenu transactionContextMenu;
    @FXML public MenuItem transactionContextDeleteItem;
    @FXML public MenuItem transactionContextShowMovementsItem;
    @FXML public ContextMenu scheduledContextMenu;
    @FXML public MenuItem saveAsMenuItem;
    @FXML public MenuItem transactionContextEditTagsItem;

    private static Scene mainScene;
    private static boolean unsavedChanges;
    private static final Logger logger=Logger.getLogger("it.unicam.cs.pa.jbudget105129.view.TableView");
    private Stage primaryStage;
    private LedgerManager ledgerManager;
    private LedgerPrinter ledgerPrinter;
    private File currentFile;
    private Injector injector;

    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.info("starting application");

        this.primaryStage=primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/mainScene.fxml"));
        mainScene = new Scene(root,750,450);

        primaryStage.setTitle("JBudget");
        primaryStage.setScene(mainScene);
        primaryStage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST,this::handleClose);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        logger.info("closing application");
        super.stop();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.fine("initializing main view");
        injector = Guice.createInjector(new LedgerManagerModule(),new ViewModule());
        ledgerManager= injector.getInstance(LedgerManager.class);
        unsavedChanges=false;
        ledgerPrinter=injector.getInstance(LedgerPrinter.class);
        saveMenuItem.setDisable(true);
        initTransactionTable();
        initAccountTable();
        initScheduledTable();
        ledgerManager.getLedger().addListener(this);
    }

    private void initTransactionTable(){
        transactionDateCol.setCellValueFactory(cellData-> new ReadOnlyStringWrapper(ledgerPrinter.stringOf(cellData.getValue().getDate())));
        transactionDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        transactionTotalCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        transactionMovementsCol.setCellValueFactory(cellData-> new SimpleStringProperty(ledgerPrinter.stringOfMovements(cellData.getValue().getMovements())));
        transactionTagsCol.setCellValueFactory(cellData->new SimpleStringProperty(ledgerPrinter.stringOfTags(cellData.getValue().getTags())));
        transactionTable.setItems(FXCollections.observableList(ledgerManager.getLedger().getTransactions()));
        transactionTable.setOnContextMenuRequested(event-> transactionContextMenu.show(transactionTable,event.getScreenX(),event.getScreenY()));
    }

    private void initAccountTable(){
        accountNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        accountDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        accountReferentCol.setCellValueFactory(new PropertyValueFactory<>("referent"));
        accountTypeCol.setCellValueFactory(cellData-> new SimpleStringProperty(ledgerPrinter.stringOf(cellData.getValue().getType())));
        accountBalanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));
        accountMinCol.setCellValueFactory(cellData->new ReadOnlyStringWrapper(ledgerPrinter.stringOf(cellData.getValue().getMinAmount())));
        accountMaxCol.setCellValueFactory(cellData->new ReadOnlyStringWrapper(ledgerPrinter.stringOf(cellData.getValue().getMaxAmount())));
        accountTable.setItems(FXCollections.observableList(ledgerManager.getLedger().getAccounts()));
    }

    private void initScheduledTable(){
        scheduledDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        scheduledCompletedCol.setCellValueFactory(cellData->new ReadOnlyStringWrapper(ledgerPrinter.stringOf(cellData.getValue().isCompleted())));
        scheduledTable.setItems(FXCollections.observableList(ledgerManager.getLedger().getScheduledTransactions()));
        sTransactionDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        sTransactionDateCol.setCellValueFactory(cellData->new ReadOnlyStringWrapper(ledgerPrinter.stringOf(cellData.getValue().getDate())));
        sTransactionTotalCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        sTransactionCompletedCol.setCellValueFactory(cellData-> new SimpleStringProperty(
                ledgerPrinter.stringOf(scheduledTable.getSelectionModel().getSelectedItem().isCompleted(cellData.getValue()))));
        scheduledTable.setOnContextMenuRequested(event->scheduledContextMenu.show(scheduledTable,event.getScreenX(),event.getScreenY()));
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        logger.fine("Ledger changed");
        accountTable.setItems(FXCollections.observableList(ledgerManager.getLedger().getAccounts()));
        transactionTable.setItems(FXCollections.observableList(ledgerManager.getLedger().getTransactions()));
        scheduledTable.setItems(FXCollections.observableList(ledgerManager.getLedger().getScheduledTransactions()));
        accountTable.refresh();
        transactionTable.refresh();
        scheduledTable.refresh();
        unsavedChanges=true;
        saveMenuItem.setDisable(false);
    }

    @FXML public void handleSavePressed() {
        if(currentFile!=null)
            save();
        else
            saveAs();
    }

    @FXML public void handleSaveAsPressed() {
        saveAs();
    }

    @FXML public void handleLoadPressed() {
        load();
        transactionTable.setItems(FXCollections.observableList(ledgerManager.getLedger().getTransactions()));
        accountTable.setItems(FXCollections.observableList(ledgerManager.getLedger().getAccounts()));
        scheduledTable.setItems(FXCollections.observableList(ledgerManager.getLedger().getScheduledTransactions()));
        ledgerManager.getLedger().addListener(this);
    }

    @FXML protected void handleNewTransaction() {
        FXMLLoader loader = createLoader("/transactionWizard.fxml",param->new TransactionWizardFXController(mainScene,injector));
        Scene scene = createScene(loader,750,450);
        loadScene(scene,"Adding new transaction");
    }

    @FXML public void handleNewAccountPressed() {
        FXMLLoader loader = createLoader("/accountWizard.fxml",param->new AccountWizardFXController(mainScene,injector));
        Scene scene = createScene(loader,480,350);
        loadScene(scene,"Adding new account");
    }

    @FXML public void handleNewScheduledPressed() {
        FXMLLoader loader = createLoader("/scheduledTransactionWizard.fxml",param->new STWizardFXController(mainScene,injector));
        Scene scene = createScene(loader,750,450);
        loadScene(scene,"Adding new scheduled transacion");
    }

    @FXML public void handleTransactionKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.DELETE) {
            removeSelectedTransaction();
        }
    }

    @FXML public void transactionContextShowMovementsPressed() {
        Transaction transaction = transactionTable.getSelectionModel().getSelectedItem();
        if(transaction==null) return;
        showMovementPopup(transaction);
        transactionTable.refresh();
    }

    @FXML public void transactionContextEditTagsPressed() {
        unsavedChanges=true;
        saveMenuItem.setDisable(false);
        Transaction transaction = transactionTable.getSelectionModel().getSelectedItem();
        if(transaction==null) return;
        showEditTagsPopup(transaction);
        transactionTable.refresh();
    }

    @FXML public void handleTransactionContextDeletePressed() {
        removeSelectedTransaction();
    }

    @FXML public void handleAccountKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.DELETE)){
            removeSelectedAccount();
        }
    }

    @FXML public void handleScheduledMouseClicked() {
        ScheduledTransaction st = scheduledTable.getSelectionModel().getSelectedItem();
        if(st==null) return;
        sTransactionTable.setItems(FXCollections.observableList(st.getTransactions()));
    }

    @FXML public void handleScheduledKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.DELETE)){
            removeSelectedScheduledTransaction();
        }
    }

    @FXML public void handleScheduledShowMovementsPressed() {
        Transaction transaction = sTransactionTable.getSelectionModel().getSelectedItem();
        if(transaction==null) return;
        showMovementPopup(transaction);
    }

    @FXML public void handleScheduledEditTagsPressed() {
        unsavedChanges=true;
        saveMenuItem.setDisable(false);
        Transaction transaction = sTransactionTable.getSelectionModel().getSelectedItem();
        if (transaction==null) return;
        showEditTagsPopup(transaction);
    }

    private void removeSelectedTransaction(){
        Transaction transaction = transactionTable.getSelectionModel().getSelectedItem();
        if(transaction==null) return;
        try {
            ledgerManager.removeTransaction(transaction);
        } catch (AccountException e) {
            logger.warning(e.getMessage());
            showErrorAlert("Transaction error", e.getMessage());
        }
    }

    private void removeSelectedAccount(){
        Account account = accountTable.getSelectionModel().getSelectedItem();
        if (account==null) return;
        try {
            ledgerManager.removeAccount(account);
        } catch (AccountException e) {
            logger.warning(e.getMessage());
            showErrorAlert("Account error",e.getLocalizedMessage());
        }
    }

    private void removeSelectedScheduledTransaction(){
        ScheduledTransaction st = scheduledTable.getSelectionModel().getSelectedItem();
        if(st==null) return;
        try {
            ledgerManager.removeScheduledTransaction(st);
        } catch (AccountException e) {
            logger.warning(e.getMessage());
            showErrorAlert("Scheduled transaction error",e.getMessage());
        }
        sTransactionTable.setItems(FXCollections.emptyObservableList());
    }

    private void loadScene(Scene scene, String title){
        Stage stage = (Stage) mainVbox.getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(scene);
        logger.fine("New scene loaded '"+title+"'");
    }

    private void showNewStage(Scene scene, String title){
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(transactionTable.getScene().getWindow());
        logger.fine("New stage opened '"+title+"'");
        stage.showAndWait();
    }

    private Scene createScene(FXMLLoader loader, int width, int height){
        try {
            Parent root = loader.load();
            logger.fine("New scene created");
            return new Scene(root,width,height);
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
        return null;
    }

    private FXMLLoader createLoader(String resource, Callback<Class<?>,Object> factory){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        loader.setControllerFactory(factory);
        logger.fine("New loader created from '"+resource+"'");
        return loader;
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void save(){
        try {
            ledgerManager.saveLedger(currentFile.getAbsolutePath());
            unsavedChanges=false;
            saveMenuItem.setDisable(true);
        } catch (IOException e) {
            logger.severe(e.getMessage());
            showErrorAlert("Ledger save error",e.getLocalizedMessage());
        }
    }

    private void saveAs(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Ledger File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JBudget file","*.jb")
        );
        File file = fileChooser.showSaveDialog(primaryStage);
        if(file==null) {
            logger.fine("Operation 'save as' aborted");
            return;
        }
        currentFile=file;
        save();
    }

    private void load(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Ledger File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JBudget file","*.jb"),
                new FileChooser.ExtensionFilter("All files","*.*")
        );
        File file = fileChooser.showOpenDialog(primaryStage);
        if(file==null) {
            logger.fine("Ledger load aborted");
            return;
        }
        currentFile=file;
        try {
            ledgerManager.loadLedger(currentFile.getAbsolutePath());
            ledgerManager.schedule();
            unsavedChanges=false;
            saveMenuItem.setDisable(true);
        } catch (IOException e) {
            logger.severe(e.getMessage());
            showErrorAlert("Ledger load error",e.getLocalizedMessage());
        } catch (AccountException e) {
            logger.severe(e.getMessage());
            showErrorAlert("Ledger schedule error",e.getLocalizedMessage());
        } catch (JsonParseException e){
            logger.severe(e.getMessage());
            showErrorAlert("File error",e.getLocalizedMessage());
        }
    }

    private void handleClose(WindowEvent event){
        if(unsavedChanges){
            logger.fine("closing with unsaved changes");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Quit application");
            alert.setContentText("Close without saving?");
            alert.initOwner(primaryStage.getOwner());
            Optional<ButtonType> res = alert.showAndWait();

            if (res.isPresent()) {
                if (res.get().equals(ButtonType.CANCEL)) {
                    logger.fine("aborting application close");
                    event.consume();
                } else {
                    logger.fine("exiting without saving");
                }
            }else logger.fine("exiting without saving");
        }
    }

    private void showMovementPopup(Transaction selected){
        FXMLLoader loader = createLoader("/movementsPopup.fxml",
                param->new MovementsPopupFXController(selected.getMovements(),injector));
        Scene scene = createScene(loader,550,300);
        showNewStage(scene,selected.getDescription());
    }

    private void showEditTagsPopup(Tagged element){
        FXMLLoader loader = createLoader("/editTagsPopup.fxml",
                param->new EditTagsFXController(element,ledgerManager.getAllUsedTags()));
        Scene scene = createScene(loader,600,400);
        showNewStage(scene,"Edit tags");
    }
}
