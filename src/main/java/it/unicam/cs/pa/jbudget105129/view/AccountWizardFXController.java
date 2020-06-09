package it.unicam.cs.pa.jbudget105129.view;

import com.google.inject.Inject;
import it.unicam.cs.pa.jbudget105129.controller.LedgerManager;
import javafx.fxml.Initializable;
import javafx.scene.Scene;

import java.net.URL;
import java.util.ResourceBundle;

public class AccountWizardFXController implements Initializable {

    private final Scene mainScene;
    private final LedgerManager manager;

    @Inject
    protected AccountWizardFXController(@MainScene Scene mainScene, LedgerManager manager){
        this.mainScene=mainScene;
        this.manager=manager;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
