package it.unicam.cs.pa.jbudget105129.view;

import it.unicam.cs.pa.jbudget105129.controller.LedgerManager;
import it.unicam.cs.pa.jbudget105129.model.Movement;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MovementsPopupFXController implements Initializable {
    private final List<Movement> movements;
    private final LedgerPrinter printer;
    private final LedgerManager ledgerManager;

    @FXML public TableView<Movement> movementTable;
    @FXML public TableColumn<Movement,String> descriptionCol;
    @FXML public TableColumn<Movement,String> typeCol;
    @FXML public TableColumn<Movement,Number> amountCol;
    @FXML public TableColumn<Movement,String > accountCol;
    @FXML public ContextMenu ContextMenu;
    @FXML public MenuItem EditTagsMenuItem;
    @FXML public TableColumn<Movement,String> tagsCol;

    public MovementsPopupFXController(List<Movement> movements, LedgerManager manager){
        this.movements=movements;
        this.printer=new LedgerPrinter();
        this.ledgerManager=manager;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movementTable.setItems(FXCollections.observableList(movements));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeCol.setCellValueFactory(cellData->new ReadOnlyStringWrapper(printer.stringOf(cellData.getValue().getType())));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        accountCol.setCellValueFactory(cellData-> new ReadOnlyStringWrapper(cellData.getValue().getAccount().getName()));
        tagsCol.setCellValueFactory(cellData-> new ReadOnlyStringWrapper(printer.stringOfTags(cellData.getValue().getTags())));
    }

    @FXML public void handleEditTagPressed(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/editTagsPopup.fxml"));
        loader.setControllerFactory(p->new EditTagsFXController(
                movementTable.getSelectionModel().getSelectedItem(),
                ledgerManager.getAllUsedTags()
        ));
        Stage stage = new Stage();
        stage.initOwner(movementTable.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Edit Tags");
            stage.setScene(scene);
            stage.showAndWait();
            movementTable.setItems(FXCollections.observableList(movements));
            movementTable.refresh();
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: 17/06/2020 gestire e log
        }
    }
}
