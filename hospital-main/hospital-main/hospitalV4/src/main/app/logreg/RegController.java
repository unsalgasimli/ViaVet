package main.app.logreg;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.app.heart.App;
import main.app.heart.ConcreteClass;
import main.app.heart.DataBase;

public class RegController extends App {

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
                DataBase.addPatient(nameRField.getText(), surnameRField.getText(), mnameRField.getText(), dPickerR.getValue(), numberRField.getText(), passwordRField.getText());
                ConcreteClass concreteInstance = new ConcreteClass();
                concreteInstance.showIDAlert(event);
                Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
                stage.close();
                logOpen();
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


}
