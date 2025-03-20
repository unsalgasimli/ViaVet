package main.unsalgasimli.app.vet.auth;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import main.unsalgasimli.app.vet.core.App;
import main.unsalgasimli.app.vet.core.ConcreteClass;
import main.unsalgasimli.app.vet.core.Database;

public class RegisterController extends App {

    @FXML
    TextField nameRField;
    @FXML
    TextField surnameRField;
    @FXML
    TextField mnameRField;
    @FXML
    DatePicker dPickerR;
    @FXML
    TextField numberRField;
    @FXML
    TextField passwordRField;


    //REGISTIRATION VALIDATION
    public void regAttempt(ActionEvent event) {
        if (nullChecker(event)) {
            try {
                Database.addPatient(nameRField.getText(), surnameRField.getText(), mnameRField.getText(), dPickerR.getValue(), numberRField.getText(), passwordRField.getText());
                ConcreteClass concreteInstance = new ConcreteClass();
                concreteInstance.showIDAlert(event);
                openLogin();
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }


    //CHECKS FIELDS
    private boolean nullChecker(ActionEvent event) {
        ConcreteClass concreteInstance = new ConcreteClass();
        String nl;
        nl = nameRField.getText();
        if (nl.isEmpty()) {
            concreteInstance.showAlert("Don't leave name empty", event);
            return false;
        }
        nl = surnameRField.getText();
        if (nl.isEmpty()) {
            concreteInstance.showAlert("Don't leave surname empty", event);
            return false;
        }
        nl = mnameRField.getText();
       if (nl.isEmpty()) {
           concreteInstance.showAlert("Don't leave middlename empty", event);
            return false;
        }
        if (dPickerR.getValue() == null) {
            concreteInstance.showAlert("Don't leave date empty", event);
            return false;
        }
        nl = numberRField.getText();
        if (nl.isEmpty() || !containsOnlyNumbers(numberRField)) {
            concreteInstance.showAlert("Don't leave number empty/Wrong number style", event);
            return false;
        }
        nl = passwordRField.getText();
        if (nl.isEmpty()) {
            concreteInstance.showAlert("Don't leave password empty", event);
            return false;
        }
        return true;
    }

    public static boolean containsOnlyNumbers(TextField textField) {
        String text = textField.getText();
        return text.matches("\\d*"); // This regex checks if the string contains only digits
    }


    public void openLog(MouseEvent mouseEvent) {
        App.openLogin();
    }
}
