//package main.app.staff;
//
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.Button;
//import javafx.scene.control.PasswordField;
//import javafx.scene.control.TextField;
//import javafx.stage.Stage;
//import main.app.heart.App;
//import main.app.heart.DataBase;
//import main.app.logreg.LogController;
//
//import java.net.URL;
//import java.util.ResourceBundle;
//
//public class StaffInfoController implements Initializable {
//    @FXML
//    TextField nameField;
//    @FXML
//    TextField surnameField;
//    @FXML
//    TextField numberField;
//    @FXML
//    TextField passwordField;
//    @FXML
//    Button appointmentBtn;
//    @FXML
//    Button historyBtn;
//    @FXML
//    Button logoutBtn;
//
//
//    public void openStaffAppointment(ActionEvent event) throws Exception{
//        App.openStaffAppointment();
//    }
//    public void openStaffHistory(ActionEvent event) throws Exception{
//
//        App.openStaffHistory();
//    }
//    public void logOut(ActionEvent event) throws Exception {
//        App.openLogin();
//    }
//
//
//
//
//
//
//
//
//    public void update(){
//        if(!(nameField.getText().isEmpty() || surnameField.getText().isEmpty() || numberField.getText().isEmpty() || passwordField.getText().isEmpty())){
//         //   DataBase.updateStaff(nameField.getText(),surnameField.getText(),numberField.getText(),passwordField.getText());
//            refresh();
//        }else{
//
//        }
//    }
//    public void delete(){
//  //     DataBase.deleteStaff(nameField.getText(),surnameField.getText());
//    }
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
////       String[] allInfo= DataBase.getUserInfo(LogController.activeID,"staff").split(" ");
////        nameField.setText(allInfo[0]);
////        surnameField.setText(allInfo[1]);
////        numberField.setText(allInfo[2]);
////        passwordField.setText(allInfo[3]);
//
//    }
//
//    private void refresh() {
////        String[] allInfo= DataBase.getUserInfo(LogController.activeID,"staff").split(" ");
////        nameField.setText(allInfo[0]);
////        surnameField.setText(allInfo[1]);
////        nameField.setText(allInfo[2]);
////        passwordField.setText(allInfo[3]);
//
//    }
//}
