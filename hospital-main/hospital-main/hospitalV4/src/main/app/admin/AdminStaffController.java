package main.app.admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.app.heart.App;
import main.app.heart.ConcreteClass;
import main.app.heart.DataBase;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class AdminStaffController implements Initializable {

    @FXML
    TextField nameField;
    @FXML
    TextField surnameField;
    @FXML
    TextField numberField;
    @FXML
    TextField passwordField;
    @FXML
    Button delBtn;
    @FXML
    Button idBtn;
    @FXML
    Button staffBtn;
    @FXML
    private TableView<Staff> staffTable;
    @FXML
    private TableColumn<Staff, String> tableUno;
    @FXML
    private TableColumn<Staff, String> tableDos;

    ConcreteClass concreteClass=new ConcreteClass();

    //AS STATED OPENS PATIENT PAGE FOR ADMIN
    public void openPatient(ActionEvent event)  {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();
        App.openPatient();
    }
    public void logOut(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();
        App.logOpen();
    }

    //ADDING STAFF BY ADMIN
    public void addStaff(ActionEvent event) {
      if(!(nameField.getText().isEmpty() || surnameField.getText().isEmpty() || numberField.getText().isEmpty() || passwordField.getText().isEmpty())){
          if(containsOnlyNumbers(numberField)){
            DataBase.addStaff(nameField.getText(), surnameField.getText(), numberField.getText(), passwordField.getText());
            refreshStaff(); }
          else{
              concreteClass.showAlert("Wrong number style",event);
          }
        }else{
          concreteClass.showAlert("Fill the TextFields",event);
        }
    }


    public static boolean containsOnlyNumbers(TextField textField) {
        String text = textField.getText();
        return text.matches("\\d*"); // This regex checks if the string contains only digits
    }

    //DELETING STAFF BY ADMIN
    public void deleteStaff(TableView<Staff> tableView,ActionEvent event) {
        Staff selectedPerson = tableView.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            // Retrieve values for specific columns
            String name = selectedPerson.getName();
            String surname = selectedPerson.getSurname();
            DataBase.deleteStaff(name, surname);
            refreshStaff();
        } else {
           concreteClass.showAlert("No row selected",event);
        }
    }


    private void refreshStaff() {
        nameField.clear();
        surnameField.clear();
        numberField.clear();
        passwordField.clear();
        staffTable.setItems(getStaffData());
    }

    //USED FOR INITIALIZING VALUES OF TABLE ETC. OTHERWISE U GET POINTER ERROR
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableUno.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        tableDos.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());

        // Initialize the staffTable
        staffTable.setItems(getStaffData());
        delBtn.setOnAction(event -> deleteStaff(staffTable,event));
        idBtn.setOnAction(event -> showId(staffTable,event));

    }

    private void showId(TableView<Staff> staffTable, ActionEvent event) {
        Staff selectedPerson = staffTable.getSelectionModel().getSelectedItem();
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        if (selectedPerson != null) {
            String name = selectedPerson.getName();
            String surname = selectedPerson.getSurname();
            concreteClass.showInformativeAlert("User ID is: "+DataBase.getStaffID(name,surname),stage);
            refreshStaff();
        } else {
            concreteClass.showAlert("No row selected",event);
        }
    }

    //LIST USED BY TABLE
    private ObservableList<Staff> getStaffData() {
        staffTable.getItems().clear();
        ObservableList<Staff> data = FXCollections.observableArrayList();
        String[] text = DataBase.getStaffList().split(" ");
        if (!text[0].equals("")) {
            for (int i = 0; i < text.length; i += 3) {
                data.add(new Staff(text[i], text[i + 1]));
            }
        }

        return data;
    }



    //STAFF CLASS USED FOR TABLE
    private static class Staff {
        private final SimpleStringProperty name;
        private final SimpleStringProperty surname;


        private Staff(String name, String surname) {
            this.name = new SimpleStringProperty(name);
            this.surname = new SimpleStringProperty(surname);

        }

        private String getName() {
            return name.get();
        }

        private SimpleStringProperty nameProperty() {
            return name;
        }

        private String getSurname() {
            return surname.get();
        }

        private SimpleStringProperty surnameProperty() {
            return surname;
        }


    }
}
