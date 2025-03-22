package main.java.core;

import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;


public interface Alertable {

     void showAlert(String contentText, ActionEvent event);
      Dialog<ButtonType> CustomConfirmationDialog(String text, ActionEvent event);
}

