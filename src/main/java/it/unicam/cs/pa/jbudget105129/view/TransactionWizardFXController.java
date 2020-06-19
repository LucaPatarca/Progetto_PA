package it.unicam.cs.pa.jbudget105129.view;

import com.google.inject.Injector;
import it.unicam.cs.pa.jbudget105129.controller.LedgerManager;
import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import it.unicam.cs.pa.jbudget105129.model.*;
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

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

public class TransactionWizardFXController implements Initializable {

    private final LedgerManager ledgerManager;
    private final Scene mainScene;
    private final LedgerPrinter ledgerPrinter;
    private final Logger logger;
    private final Injector injector;

    @FXML public TableColumn<Movement, String> movementDescriptionCol;
    @FXML public TableColumn<Movement,String> movementTypeCol;
    @FXML public TableColumn<Movement,Double> movementAmountCol;
    @FXML public TableColumn<Movement,String> movementAccountCol;
    @FXML public TableView<Movement> movementTable;
    @FXML public Button addMovementButton;
    @FXML public TextField movementDescriptionTextField;
    @FXML public ChoiceBox<MovementType> movementTypeSelect;
    @FXML public ChoiceBox<Account> movementAccountSelect;
    @FXML public Spinner<Double> movementAmountSpinner;
    @FXML public Button cancelButton;
    @FXML public TextField transactionDescriptionTextField;
    @FXML public DatePicker transactionDate;
    @FXML public Button addTransactionButton;
    @FXML public ContextMenu movementsContextMenu;
    @FXML public MenuItem editTagsMovementMenuItem;

    public TransactionWizardFXController(Scene mainScene, Injector injector){
        this.ledgerManager= Objects.requireNonNull(injector).getInstance(LedgerManager.class);
        this.mainScene=Objects.requireNonNull(mainScene);
        this.ledgerPrinter = injector.getInstance(LedgerPrinter.class);
        this.logger=Logger.getLogger("it.unicam.cs.pa.jbudget105129.view.TransactionWizardFXController");
        this.injector=injector;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("opening transaction wizard");
        movementDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        movementAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        movementAccountCol.setCellValueFactory(cellData->new SimpleStringProperty(ledgerPrinter.stringOf(cellData.getValue().getAccount())));
        movementTypeCol.setCellValueFactory(cellData->new SimpleStringProperty(ledgerPrinter.stringOf(cellData.getValue().getType())));

        movementTypeSelect.setItems(FXCollections.observableArrayList(MovementType.values()));
        movementTypeSelect.setConverter(injector.getInstance(MovementTypeConverter.class));
        movementAccountSelect.setItems(FXCollections.observableArrayList(ledgerManager.getLedger().getAccounts()));
        movementAccountSelect.setConverter(injector.getInstance(AccountConverter.class));
        movementAmountSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                Double.MIN_VALUE,Double.MAX_VALUE,1.0,0.01));
        movementTable.setOnContextMenuRequested(event->movementsContextMenu.show(movementTable,event.getScreenX(),event.getScreenY()));
        transactionDate.setValue(LocalDate.now());
    }

    @FXML protected void handleAddMovementPressed() {
        if (checkMovementInput()) {
            RoundedMovement movement = RoundedMovement.getInstance(
                    movementDescriptionTextField.getText(),
                    movementAmountSpinner.getValue(),
                    movementTypeSelect.getValue(),
                    movementAccountSelect.getValue()
            );
            movementTable.getItems().add(movement);
            logger.info("added new movement: "+ movement);
        } else {
            logger.warning("cannot add new movement");
            showAlert("Movement Input Error", "Check information of the new movement");
        }
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
            logger.info("added new transaction");
            returnToMainScene();
        } catch (Exception e) {
            logger.info("cannot add new transaction");
            showAlert("Error while adding transaction",
                    e.getLocalizedMessage()+"\nCheck new transaction's information");
        }
    }

    private void returnToMainScene(){
        logger.info("closing transaction wizard");
        Stage stage= (Stage) cancelButton.getScene().getWindow();
        stage.setTitle("JBudget");
        stage.setScene(mainScene);
    }

    @FXML public void handleMovementEditTagPressed() {
        Movement movement = movementTable.getSelectionModel().getSelectedItem();
        if (Objects.isNull(movement)) return;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/editTagsPopup.fxml"));
        loader.setControllerFactory(param->new EditTagsFXController(
                movement,
                ledgerManager.getAllUsedTags()
        ));
        Stage stage = new Stage();
        try {
            Parent root = loader.load();
            Scene scene=new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            logger.severe("cannot load edit tags scene: "+ e.getMessage());
        }
    }

    @FXML public void movementTableKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.DELETE)){
            Movement toRemove = movementTable.getSelectionModel().getSelectedItem();
            if(Objects.nonNull(toRemove)) {
                movementTable.getItems().remove(toRemove);
            }
        }
    }

    private boolean checkMovementInput(){
        return Objects.nonNull(movementDescriptionTextField.getText()) &&
                Objects.nonNull(movementTypeSelect.getValue()) &&
                Objects.nonNull(movementAmountSpinner.getValue()) &&
                Objects.nonNull(movementAccountSelect.getValue()) &&
                !movementDescriptionTextField.getText().isBlank();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
