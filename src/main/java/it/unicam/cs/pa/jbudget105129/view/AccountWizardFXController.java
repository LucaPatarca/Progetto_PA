package it.unicam.cs.pa.jbudget105129.view;

import com.google.inject.Injector;
import it.unicam.cs.pa.jbudget105129.controller.LedgerManager;
import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class AccountWizardFXController implements Initializable {

    private final Scene mainScene;
    private final LedgerManager manager;
    private final Logger logger;
    private final Injector injector;

    public TextField nameTextField;
    public TextField descriptionTextField;
    public TextField referentTextField;
    public Spinner<Double> openingBalanceSpinner;
    public ChoiceBox<AccountType> typeSelect;
    public Spinner<Double> minAmountSpinner;
    public Spinner<Double> maxAmountSpinner;
    public Button cancelButton;
    public Button addButton;
    public CheckBox hasMaximumAmountCheckBox;
    public CheckBox hasMinimumAmountCheckBox;

    public AccountWizardFXController(Scene mainScene, Injector injector){
        this.mainScene=mainScene;
        this.manager=injector.getInstance(LedgerManager.class);
        this.logger=Logger.getLogger("it.unicam.cs.pa.jbudget105129.view.AccountWizardFXController");
        this.injector=injector;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("opening account wizard");
        typeSelect.setItems(FXCollections.observableArrayList(AccountType.values()));
        typeSelect.setConverter(injector.getInstance(AccountTypeConverter.class));
        openingBalanceSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                -Double.MAX_VALUE,Double.MAX_VALUE,0.0,0.01));
        minAmountSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                -Double.MAX_VALUE,Double.MAX_VALUE,0.0,0.01));
        maxAmountSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                -Double.MAX_VALUE,Double.MAX_VALUE,0.0,0.01));
    }

    @FXML public void cancelButtonPressed() {
        returnToMainScene();
    }

    @FXML public void addButtonPressed() {
        Double maxAmount = null;
        Double minAmount = null;
        if (hasMaximumAmountCheckBox.isSelected()) maxAmount=maxAmountSpinner.getValue();
        if (hasMinimumAmountCheckBox.isSelected()) minAmount=minAmountSpinner.getValue();
        try{
            manager.addAccount(
                    nameTextField.getText(),
                    descriptionTextField.getText(),
                    referentTextField.getText(),
                    openingBalanceSpinner.getValue(),
                    typeSelect.getValue(),
                    minAmount,
                    maxAmount
            );
            logger.info("new account successfully added");
            returnToMainScene();
        }catch (IllegalArgumentException | NullPointerException e){
            logger.warning("Error trying to add new account: "+e.getMessage());
            showAlert(e.getLocalizedMessage());
        }
    }

    @FXML public void handleMaxCheckBoxAction() {
        maxAmountSpinner.setDisable(!hasMaximumAmountCheckBox.isSelected());
    }

    @FXML public void handleMinCheckBoxAction() {
        minAmountSpinner.setDisable(!hasMinimumAmountCheckBox.isSelected());
    }

    private void returnToMainScene(){
        logger.info("closing account wizard");
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.setTitle("JBudget");
        stage.setScene(mainScene);
    }

    private void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Account Input Error");
        alert.setContentText(content+"\nCheck new account's information");
        alert.showAndWait();
    }
}
