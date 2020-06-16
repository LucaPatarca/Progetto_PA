package it.unicam.cs.pa.jbudget105129.view;

import it.unicam.cs.pa.jbudget105129.model.Movement;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MovementsPopupFXController implements Initializable {
    private final List<Movement> movements;
    private final LedgerPrinter printer;

    @FXML public TableView<Movement> movementTable;
    @FXML public TableColumn<Movement,String> descriptionCol;
    @FXML public TableColumn<Movement,String> typeCol;
    @FXML public TableColumn<Movement,Number> amountCol;
    @FXML public TableColumn<Movement,String > accountCol;

    public MovementsPopupFXController(List<Movement> movements){
        this.movements=movements;
        this.printer=new LedgerPrinter();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movementTable.setItems(FXCollections.observableList(movements));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeCol.setCellValueFactory(cellData->new ReadOnlyStringWrapper(printer.stringOf(cellData.getValue().getType())));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        accountCol.setCellValueFactory(cellData-> new ReadOnlyStringWrapper(cellData.getValue().getAccount().getName()));
    }
}
