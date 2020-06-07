package it.unicam.cs.pa.jbudget105129.view;

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
