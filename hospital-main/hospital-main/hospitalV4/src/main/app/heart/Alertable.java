package main.app.heart;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;

// Define an interface for the alert functionality
public interface Alertable {

     void showAlert(String contentText, ActionEvent event);
      Dialog<ButtonType> CustomConfirmationDialog(String text, ActionEvent event);
}

