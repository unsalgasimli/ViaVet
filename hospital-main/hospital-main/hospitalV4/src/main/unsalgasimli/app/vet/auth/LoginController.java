package main.unsalgasimli.app.vet.auth;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import main.unsalgasimli.app.vet.core.App;
import main.unsalgasimli.app.vet.core.ConcreteClass;
import main.unsalgasimli.app.vet.core.Database;


public class LoginController extends App {
    public static String activeID;
    @FXML
    TextField idField;
    @FXML
    PasswordField passField;
    @FXML
    VBox vbox;

    //OPENS REGISTER PAGE
    public void openRegister(ActionEvent event) throws Exception {
        openRegister();
    }


    //VALIDATING USER INFO & TYPE AND SETTING PROPER STAGE
    public void logIn(ActionEvent event) throws Exception {
        ConcreteClass concreteInstance = new ConcreteClass();
        Database.checkValidity();
        String idF = idField.getText();
        String passF = passField.getText();
        if (!idF.isEmpty() && !passF.isEmpty()) {
                switch( Database.checkAcc(idField.getText(), passField.getText(),event) ){
                    case "patient":{
                        activeID = idF;
                        openPatientAppointment();
                        break;
                    }
                    case "staff":{
                        activeID = idF;
                        openStaffAppointment();
                        break;
                    }
                    case "admin":{
                        activeID = idF;
                        openAdminStaff();
                        break;
                    }
                    default:{
                       concreteInstance.showAlert("Wrong ID/Password.",event);
                    }
                } }else{
           concreteInstance.showAlert("Fields must be filled.",event);
        }
    }
}
