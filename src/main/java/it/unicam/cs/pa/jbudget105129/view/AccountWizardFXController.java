package it.unicam.cs.pa.jbudget105129.view;

import it.unicam.cs.pa.jbudget105129.controller.LedgerManager;
import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AccountWizardFXController implements Initializable {

    private final Scene mainScene;
    private final LedgerManager manager;

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

    public AccountWizardFXController(Scene mainScene, LedgerManager manager){
        this.mainScene=mainScene;
        this.manager=manager;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeSelect.setItems(FXCollections.observableArrayList(AccountType.values()));
        typeSelect.setConverter(new AccountTypeConverter());
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
            returnToMainScene();
        }catch (IllegalArgumentException | NullPointerException e){
            showAlert(e.getLocalizedMessage());
        }
    }

    @FXML public void handleMaxCheckBoxAction(ActionEvent event) {
        maxAmountSpinner.setDisable(!hasMaximumAmountCheckBox.isSelected());
    }

    @FXML public void handleMinCheckBoxAction(ActionEvent event) {
        minAmountSpinner.setDisable(!hasMinimumAmountCheckBox.isSelected());
    }

    private void returnToMainScene(){
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
