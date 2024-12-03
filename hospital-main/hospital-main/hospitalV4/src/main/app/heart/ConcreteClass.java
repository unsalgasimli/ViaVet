package main.app.heart;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;


public class ConcreteClass extends AbstractAlert {

    public void showIDAlert(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(stage);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Unique User id:" + DataBase.uString);
        alert.showAndWait();
    }
    public void showInformativeAlert(String contentText, Stage stage) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(stage);
            alert.setTitle("Details");
            alert.setHeaderText(null);
            alert.setContentText(contentText);
            alert.showAndWait();
        }
}
