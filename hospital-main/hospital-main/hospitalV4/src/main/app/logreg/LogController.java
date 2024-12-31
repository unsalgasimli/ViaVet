package main.app.logreg;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.app.heart.App;
import main.app.heart.ConcreteClass;
import main.app.heart.DataBase;


public class LogController extends App {
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
        DataBase.checkValidity();
        String idF = idField.getText();
        String passF = passField.getText();
        if (!idF.isEmpty() && !passF.isEmpty()) {
                switch( DataBase.checkAcc(idField.getText(), passField.getText(),event) ){
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
