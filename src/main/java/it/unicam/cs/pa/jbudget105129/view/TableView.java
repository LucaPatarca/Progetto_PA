package it.unicam.cs.pa.jbudget105129.view;

import com.google.inject.Guice;
import com.google.inject.Injector;
import it.unicam.cs.pa.jbudget105129.Dependency.LedgerManagerModule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TableView extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/mainScene.fxml"));

        primaryStage.setTitle("JBudget");
        primaryStage.setScene(new Scene(root,750,450));
        primaryStage.show();
    }
}
