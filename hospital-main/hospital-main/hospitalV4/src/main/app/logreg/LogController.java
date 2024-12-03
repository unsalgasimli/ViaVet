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
        // Create a container (empty StackPane for now)
        StackPane root = new StackPane();

        // Create a Scene
        Scene scene = new Scene(root, 800, 600);

        // Get the current stage
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();

        // Close the current stage
        stage.close();

        // Open the registration page
        regOpen();
    }



    //VALIDATING USER INFO & TYPE AND SETTING PROPER STAGE
    public void logIn(ActionEvent event) throws Exception {
        ConcreteClass concreteInstance = new ConcreteClass();
        String idF = idField.getText();
        String passF = passField.getText();
        if (!idF.isEmpty() && !passF.isEmpty()) {
                switch( DataBase.checkAcc(idField.getText(), passField.getText(),event) ){
                    case "patient":{
                        activeID = idF;
                        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
                        stage.close();
                        openPatientAppointment();
                        break;
                    }
                    case "staff":{
                        activeID = idF;
                        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
                        stage.close();
                        openStaffAppointment();
                        break;
                    }
                    case "admin":{
                        activeID = idF;
                        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
                        stage.close();
                        openMain();
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
