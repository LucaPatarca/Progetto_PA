package it.unicam.cs.pa.jbudget105129.view;

import it.unicam.cs.pa.jbudget105129.model.SingleTag;
import it.unicam.cs.pa.jbudget105129.model.Tag;
import it.unicam.cs.pa.jbudget105129.model.Tagged;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditMovementTagsFXController implements Initializable {
    private final List<Tag> available;
    private final Tagged element;

    public TableView<Tag> availableTable;
    public TableColumn<Tag,String> availableDescriptionCol;
    public TableColumn<Tag,String> availableNameCol;
    public TableView<Tag> currentTable;
    public TableColumn<Tag,String> currentNameCol;
    public TableColumn<Tag,String> currentDescriptionCol;
    public Button availableAddButton;
    public Button currentRemoveButton;
    public Button doneButton;
    public TextField newNameTextField;
    public TextField newDescriptionTextField;
    public Button newAddButton;

    public EditMovementTagsFXController(Tagged element, List<Tag> available){
        this.available=available;
        this.element = element;
        this.element.getTags().forEach(this.available::remove);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        availableNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        availableDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        currentNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        currentDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        availableTable.setItems(FXCollections.observableList(available));
        currentTable.setItems(FXCollections.observableList(element.getTags()));
    }

    public void handleAddAvailablePressed(ActionEvent actionEvent) {
        Tag tag = availableTable.getSelectionModel().getSelectedItem();
        availableTable.getItems().remove(tag);
        element.addTag(tag);
        currentTable.setItems(FXCollections.observableList(element.getTags()));
    }

    public void handleRemovePressed(ActionEvent actionEvent) {
        Tag tag = currentTable.getSelectionModel().getSelectedItem();
        if(tag==null) return;
        element.removeTag(tag);
        availableTable.getItems().add(tag);
        currentTable.setItems(FXCollections.observableList(element.getTags()));
    }

    public void handleDonePressed(ActionEvent actionEvent) {
        Stage stage = (Stage) doneButton.getScene().getWindow();
        stage.close();
    }

    public void handleAddNewPressed(ActionEvent actionEvent) {
        Tag tag = SingleTag.getInstance(newNameTextField.getText(),newDescriptionTextField.getText());
        element.addTag(tag);
        currentTable.setItems(FXCollections.observableList(element.getTags()));
    }
}
