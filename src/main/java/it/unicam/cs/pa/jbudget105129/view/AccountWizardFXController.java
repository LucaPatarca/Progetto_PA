package it.unicam.cs.pa.jbudget105129.view;

import com.google.inject.Inject;
import it.unicam.cs.pa.jbudget105129.controller.LedgerManager;
import it.unicam.cs.pa.jbudget105129.enums.AccountType;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
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

    @Inject
    protected AccountWizardFXController(@MainScene Scene mainScene, LedgerManager manager){
        this.mainScene=mainScene;
        this.manager=manager;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeSelect.setItems(FXCollections.observableArrayList(AccountType.values()));
        openingBalanceSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                Double.MIN_VALUE,Double.MAX_VALUE,0.0,0.01));
        minAmountSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                Double.MIN_VALUE,Double.MAX_VALUE,0.0,0.01));
        maxAmountSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                Double.MIN_VALUE,Double.MAX_VALUE,0.0,0.01));


    }

    public void cancelButtonPressed(ActionEvent event) {
        returnToMainScene();
    }

    public void addButtonPressed(ActionEvent event) {
        Double maxAmount = null;
        Double minAmount = null;
        if (hasMaximumAmountCheckBox.isSelected()) maxAmount=maxAmountSpinner.getValue();
        if (hasMinimumAmountCheckBox.isSelected()) minAmount=minAmountSpinner.getValue();
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
    }

    private void returnToMainScene(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.setScene(mainScene);
    }

}
