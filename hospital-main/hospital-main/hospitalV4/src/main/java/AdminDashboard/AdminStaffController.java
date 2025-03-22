package main.java.AdminDashboard;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.Main;
import main.java.core.AlertManager;
import main.java.core.Database;
import main.java.auth.LoginController;

import java.net.URL;
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
    Button updateBtn;
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

    AlertManager alertManager = new AlertManager();
    private AdminStaffController.Staff selectedStaff;

    //USED FOR INITIALIZING VALUES OF TABLE ETC. OTHERWISE U GET POINTER ERROR
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tableUno.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        tableDos.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());
        tableTres.setCellValueFactory(cellData -> cellData.getValue().numberProperty());


        // Initialize the staffTable
        staffTable.setItems(getStaffData());
        delBtn.setOnAction(event -> deleteStaff(staffTable, event));
        idBtn.setOnAction(event -> showDetails(staffTable, event));
        updateBtn.setOnAction(this::updateStaff);

        staffTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedStaff = newValue;
                nameField.setText(newValue.getName());
                surnameField.setText(newValue.getSurname());
                numberField.setText(newValue.getNumber());
            }
        });


    }

    //AS STATED OPENS PATIENT PAGE FOR ADMIN
    public void openPatient(ActionEvent event) {
        Main.openAdminPatient();
    }

    public void logOut(ActionEvent event) {
        Main.openLogin();
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
        String[] text = Database.getStaffList(LoginController.activeID, false).split(" ");
        if (!text[0].equals("")) {
            for (int i = 0; i < text.length - 1; i += 3) {

                data.add(new AdminStaffController.Staff(text[i].trim(), text[i + 1].trim(), text[i + 2].trim()));

            }
        }
        return data;
    }

    public void addStaff(ActionEvent event) {
        try {
            Database.addStaff(nameField.getText(), surnameField.getText(), numberField.getText(), passwordField.getText());
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
            Database.deleteStaff(name, surname, number);
            staffTable.setItems(getStaffData());

        } else {
            alertManager.showAlert("TextFields not filled", event);
        }
    }

    public static boolean containsOnlyNumbers(TextField textField) {
        String text = textField.getText();
        return text.matches("\\d*"); // This regex checks if the string contains only digits
    }

    public void updateStaff(ActionEvent event) {
        if (selectedStaff != null) {
            if (!(nameField.getText().isEmpty() || surnameField.getText().isEmpty() || numberField.getText().isEmpty())) {
                if (containsOnlyNumbers(numberField)) {
                    // Update the patient in the database
                    Database.updateStaff(selectedStaff.getName(), selectedStaff.getSurname(), selectedStaff.getNumber(),
                            nameField.getText(), surnameField.getText(), numberField.getText(), passwordField.getText());
                    refresh();
                } else {
                    alertManager.showAlert("Wrong number style", event);
                }
            } else {
                alertManager.showAlert("Fill the TextFields", event);
            }
        } else {
            alertManager.showAlert("No patient selected for update", event);
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
            String staffInfo = Database.showStaffInfo(name.trim(), surname.trim(), number.trim());

            // Show an informative alert with the fetched details
            if (staffInfo.startsWith("Error:")) {
                // Show error alert if the database method returned an error
                alertManager.showAlert(staffInfo, event);
            } else {
                // Show the fetched details in an informative alert
                alertManager.showInformativeAlert(staffInfo, stage);
            }
        } else {
            // Show an alert when no row is selected
            alertManager.showAlert("No row selected", event);
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
