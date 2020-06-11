package it.unicam.cs.pa.jbudget105129.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TableView extends Application{

    private static Scene mainScene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/mainScene.fxml"));
        mainScene = new Scene(root,750,450);

        primaryStage.setTitle("JBudget");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    protected static Scene getMainScene(){
        return mainScene;
    }
}
