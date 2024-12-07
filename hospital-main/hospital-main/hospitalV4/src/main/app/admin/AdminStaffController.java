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

    //USED FOR INITIALIZING VALUES OF TABLE ETC. OTHERWISE U GET POINTER ERROR
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableUno.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        tableDos.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());

        // Initialize the staffTable
//        staffTable.setItems(getStaffData());
//        delBtn.setOnAction(event -> deleteStaff(staffTable,event));
//        idBtn.setOnAction(event -> showId(staffTable,event));

    }

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
