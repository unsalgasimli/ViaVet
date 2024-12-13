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
import main.app.logreg.LogController;
import main.app.patient.PatientAppointmentController;
import main.app.patient.PatientHistoryController;

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
    PasswordField passwordField;
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
    @FXML
    private TableColumn<Staff, String> tableTres;

    ConcreteClass concreteClass = new ConcreteClass();

    //USED FOR INITIALIZING VALUES OF TABLE ETC. OTHERWISE U GET POINTER ERROR
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tableUno.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        tableDos.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());
        tableTres.setCellValueFactory(cellData -> cellData.getValue().numberProperty());


        // Initialize the staffTable
        staffTable.setItems(getStaffData());
        delBtn.setOnAction(event -> deleteStaff(staffTable, event));
         idBtn.setOnAction(event -> showDetails(staffTable,event));

    }

    //AS STATED OPENS PATIENT PAGE FOR ADMIN
    public void openPatient(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();
        App.openPatient();
    }

    public void logOut(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();
        App.logOpen();
    }

    public void refresh() {
        staffTable.setItems(getStaffData());
        nameField.setText("");
        surnameField.setText("");
        numberField.setText("");
        passwordField.setText("");
    }


    private ObservableList<AdminStaffController.Staff> getStaffData() {
        staffTable.getItems().clear();
        ObservableList<AdminStaffController.Staff> data = FXCollections.observableArrayList();
        String[] text = DataBase.getStaffList(LogController.activeID, false).split(" ");
        System.out.println(text[0]);
        System.out.println(text[1]);
        if (!text[0].equals("")) {
            for (int i = 0; i < text.length - 1; i += 3) {
                System.out.println(text[i]);
                data.add(new AdminStaffController.Staff(text[i], text[i + 1], text[i + 2]));

            }
        }
        return data;
    }

    public void addStaff(ActionEvent event) {
        try {
            DataBase.addStaff(nameField.getText(), surnameField.getText(), numberField.getText(), passwordField.getText());
            refresh();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void deleteStaff(TableView<AdminStaffController.Staff> tableView, ActionEvent event) {
        AdminStaffController.Staff selectedStaff = tableView.getSelectionModel().getSelectedItem();
        if (selectedStaff != null) {

            String name = selectedStaff.getName().trim();
            String surname = selectedStaff.getSurname().trim();
            String number = selectedStaff.getNumber().trim();
            DataBase.deleteStaff(name, surname, number);
            staffTable.setItems(getStaffData());

        } else {
            concreteClass.showAlert("TextFields not filled", event);
        }
    }

    public void showDetails(TableView<AdminStaffController.Staff> staffTable, ActionEvent event) {
        // Get the current stage
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        // Get the selected appointment from the table
        AdminStaffController.Staff selectedStaff = staffTable.getSelectionModel().getSelectedItem();

        if (selectedStaff != null) {
            String name = selectedStaff.getName();
            String surname = selectedStaff.getSurname();
            String number = selectedStaff.getNumber();

            // Fetch the appointment details from the database
            String staffInfo = DataBase.showStaffInfo(name.trim(), surname.trim(), number.trim());

            // Show an informative alert with the fetched details
            if (staffInfo.startsWith("Error:")) {
                // Show error alert if the database method returned an error
                concreteClass.showAlert(staffInfo, event);
            } else {
                // Show the fetched details in an informative alert
                concreteClass.showInformativeAlert(staffInfo, stage);
            }
        } else {
            // Show an alert when no row is selected
            concreteClass.showAlert("No row selected", event);
        }
    }

    //STAFF CLASS USED FOR TABLE
    private static class Staff {
        private final SimpleStringProperty name;
        private final SimpleStringProperty surname;
        private final SimpleStringProperty number;

        private Staff(String name, String surname, String number) {
            this.name = new SimpleStringProperty(name);
            this.surname = new SimpleStringProperty(surname);
            this.number = new SimpleStringProperty(number);

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

        private String getNumber() {
            return number.get();
        }

        private SimpleStringProperty numberProperty() {
            return number;
        }


    }
}
