package main.unsalgasimli.app.vet.core;

import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

// Define an interface for the alert functionality
public interface Alertable {

     void showAlert(String contentText, ActionEvent event);
      Dialog<ButtonType> CustomConfirmationDialog(String text, ActionEvent event);
}

