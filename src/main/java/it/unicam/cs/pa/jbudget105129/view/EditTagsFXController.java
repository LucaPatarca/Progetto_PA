package it.unicam.cs.pa.jbudget105129.view;

import it.unicam.cs.pa.jbudget105129.model.SingleTag;
import it.unicam.cs.pa.jbudget105129.model.Tag;
import it.unicam.cs.pa.jbudget105129.model.Tagged;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class EditTagsFXController implements Initializable {

    private final List<Tag> available;
    private final Tagged element;
    private final Logger logger;

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

    public EditTagsFXController(Tagged element, List<Tag> available){
        this.available=available;
        this.element = element;
        this.element.getTags().forEach(this.available::remove);
        this.logger=Logger.getLogger("it.unicam.cs.pa.jbudget105129.view.EditTagsFXController");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("opening edit tags popup");
        availableNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        availableDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        currentNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        currentDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        availableTable.setItems(FXCollections.observableList(available));
        currentTable.setItems(FXCollections.observableList(element.getTags()));
    }

    public void handleAddAvailablePressed() {
        Tag tag = availableTable.getSelectionModel().getSelectedItem();
        if(Objects.isNull(tag)) return;
        availableTable.getItems().remove(tag);
        element.addTag(tag);
        currentTable.setItems(FXCollections.observableList(element.getTags()));
        logger.info("available tag '"+tag.getName()+"' added");
    }

    public void handleRemovePressed() {
        Tag tag = currentTable.getSelectionModel().getSelectedItem();
        if(tag==null) return;
        element.removeTag(tag);
        availableTable.getItems().add(tag);
        currentTable.setItems(FXCollections.observableList(element.getTags()));
        logger.info("tag '"+tag.getName()+"' removed from element");
    }

    public void handleDonePressed() {
        logger.info("closing edit tags popup");
        Stage stage = (Stage) doneButton.getScene().getWindow();
        stage.close();
    }

    public void handleAddNewPressed() {
        if(newNameTextField.getText().isBlank()){
            logger.warning("tried to add a new tag with blank name");
            showAlert();
        }else {
            Tag tag = SingleTag.getInstance(newNameTextField.getText(), newDescriptionTextField.getText());
            element.addTag(tag);
            currentTable.setItems(FXCollections.observableList(element.getTags()));
            logger.info("new tag added: '"+tag.getName()+"', '"+tag.getDescription()+"'");
        }
    }

    private void showAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Tag Input Error");
        alert.setContentText("check new Tag's information");
        alert.showAndWait();
    }
}
