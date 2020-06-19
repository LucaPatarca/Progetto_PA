package it.unicam.cs.pa.jbudget105129.view;

import com.google.inject.Guice;
import com.google.inject.Injector;
import it.unicam.cs.pa.jbudget105129.enums.MovementType;
import it.unicam.cs.pa.jbudget105129.model.Account;
import it.unicam.cs.pa.jbudget105129.model.RoundedMovement;
import it.unicam.cs.pa.jbudget105129.model.Transaction;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MiniMovementWizardFXController implements Initializable {

    private final Transaction transaction;
    private final List<Account> accounts;
    private final Logger logger;
    private final Injector injector;

    @FXML public Button cancelButton;
    @FXML public Button doneButton;
    @FXML public TextField descriptionTextField;
    @FXML public Spinner<Double> amountSpinner;
    @FXML public ChoiceBox<MovementType> typeSelect;
    @FXML public ChoiceBox<Account> accountSelect;

    public MiniMovementWizardFXController(Transaction transaction, List<Account> accounts){
        this.transaction=transaction;
        this.accounts=accounts;
        this.logger=Logger.getLogger("it.unicam.cs.pa.jbudget105129.view.MiniMovementWizardFXController");
        this.injector= Guice.createInjector(new ViewModule());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("opening mini movement wizard");
        typeSelect.setItems(FXCollections.observableArrayList(MovementType.values()));
        typeSelect.setConverter(injector.getInstance(MovementTypeConverter.class));
        accountSelect.setItems(FXCollections.observableList(accounts));
        accountSelect.setConverter(injector.getInstance(AccountConverter.class));
        amountSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                Double.MIN_VALUE,Double.MAX_VALUE,1,0.01));
    }

    @FXML public void handleCancelPressed(ActionEvent actionEvent) {
        closePopup();
    }

    @FXML public void handleDonePressed(ActionEvent actionEvent) {
        if(checkInput()){
            RoundedMovement movement = RoundedMovement.getInstance(
                    descriptionTextField.getText(),
                    amountSpinner.getValue(),
                    typeSelect.getValue(),
                    accountSelect.getValue()
            );
            transaction.addMovement(movement);
            logger.info("added new movement: "+movement);
            closePopup();
        } else {
            logger.warning("tried to add a movement with wrong parameter: '"
                            +descriptionTextField.getText()+"', '"
                    +amountSpinner.getValue()+"', '"
                    +typeSelect.getValue()+"', '"
                    +accountSelect.getValue()+"'");
            showAlert();
        }
    }

    private boolean checkInput(){
        return Objects.nonNull(descriptionTextField.getText()) &&
                Objects.nonNull(amountSpinner.getValue()) &&
                Objects.nonNull(typeSelect.getValue()) &&
                Objects.nonNull(accountSelect.getValue()) &&
                !descriptionTextField.getText().isBlank();
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Movement Input Error");
        alert.setContentText("check new movement's information");
        alert.showAndWait();
    }

    private void closePopup(){
        logger.info("closing mini movement wizard");
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
