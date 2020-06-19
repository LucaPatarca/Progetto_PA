package it.unicam.cs.pa.jbudget105129.view;

import com.google.inject.Injector;
import it.unicam.cs.pa.jbudget105129.controller.LedgerManager;
import it.unicam.cs.pa.jbudget105129.model.Movement;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
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
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MovementsPopupFXController implements Initializable {
    private final List<Movement> movements;
    private final LedgerPrinter printer;
    private final LedgerManager ledgerManager;
    private final Logger logger;

    @FXML public TableView<Movement> movementTable;
    @FXML public TableColumn<Movement,String> descriptionCol;
    @FXML public TableColumn<Movement,String> typeCol;
    @FXML public TableColumn<Movement,Number> amountCol;
    @FXML public TableColumn<Movement,String > accountCol;
    @FXML public ContextMenu ContextMenu;
    @FXML public MenuItem EditTagsMenuItem;
    @FXML public TableColumn<Movement,String> tagsCol;

    public MovementsPopupFXController(List<Movement> movements, Injector injector){
        this.movements=movements;
        this.printer=injector.getInstance(LedgerPrinter.class);
        this.ledgerManager=injector.getInstance(LedgerManager.class);
        this.logger=Logger.getLogger("it.unicam.cs.pa.jbudget105129.view.MovementsPopupFXController");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("opening movements popup");
        movementTable.setItems(FXCollections.observableList(movements));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeCol.setCellValueFactory(cellData->new ReadOnlyStringWrapper(printer.stringOf(cellData.getValue().getType())));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        accountCol.setCellValueFactory(cellData-> new ReadOnlyStringWrapper(cellData.getValue().getAccount().getName()));
        tagsCol.setCellValueFactory(cellData-> new ReadOnlyStringWrapper(printer.stringOfTags(cellData.getValue().getTags())));
    }

    @FXML public void handleEditTagPressed() {
        Movement movement = movementTable.getSelectionModel().getSelectedItem();
        if(Objects.isNull(movement)) return;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/editTagsPopup.fxml"));
        loader.setControllerFactory(p->new EditTagsFXController(
                movement,
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
            logger.severe("Unable to load editTags scene: "+e.getMessage());
        }
    }
}
