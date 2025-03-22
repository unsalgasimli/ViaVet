package main.java.core;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;

// Create an abstract class providing common functionality
public abstract class AbstractAlert implements Alertable {



    public void showAlert(String contentText, ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(stage);
        alert.setTitle("Warning Alert");
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();


    }
    public Dialog<ButtonType> CustomConfirmationDialog(String text, ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Custom Confirmation Dialog");
        dialog.initOwner(stage);
        dialog.setHeaderText(text);
        DialogPane dialogPane = dialog.getDialogPane();
        ButtonType acceptButton = new ButtonType("Accept");
        ButtonType dismissButton = new ButtonType("Dismiss");
        ButtonType cancelButton = new ButtonType("Cancel");
        dialogPane.getButtonTypes().addAll(acceptButton, dismissButton, cancelButton);
        return dialog;
    }


}
