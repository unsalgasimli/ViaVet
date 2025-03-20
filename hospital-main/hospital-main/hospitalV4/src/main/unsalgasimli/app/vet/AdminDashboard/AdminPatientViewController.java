package main.unsalgasimli.app.vet.AdminDashboard;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.unsalgasimli.app.vet.core.App;
import main.unsalgasimli.app.vet.core.ConcreteClass;
import main.unsalgasimli.app.vet.core.Database;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminPatientViewController implements Initializable {

    @FXML
    TextField nameField;
    @FXML
    TextField surnameField;
    @FXML
    TextField middlenameField;
    @FXML
    TextField numberField;
    @FXML
    TextField passwordField;
    @FXML
    DatePicker datePicker;
    @FXML
    javafx.scene.control.Button delBtn;
    @FXML
    Button idBtn;
    @FXML
    Button updateBtn;
    @FXML
    private TableView<AdminPatientViewController.Patient> patientTable;
    @FXML
    private TableColumn<AdminPatientViewController.Patient, String> tableName;
    @FXML
    private TableColumn<AdminPatientViewController.Patient, String> tableSurname;
    @FXML
    private TableColumn<AdminPatientViewController.Patient, String> tableMiddlename;
    @FXML
    private TableColumn<AdminPatientViewController.Patient, String> tableBday;
    @FXML
    private TableColumn<AdminPatientViewController.Patient, String> tableNumber;

    ConcreteClass concreteClass = new ConcreteClass();
    private Patient selectedPatient;


    public void openStaffManagment(ActionEvent event) {
        App.openAdminStaff();
    }

    public void logOut(ActionEvent event) {
        App.openLogin();
    }


    public void addPatient(ActionEvent event) {
        if (!(nameField.getText().isEmpty() || surnameField.getText().isEmpty() || middlenameField.getText().isEmpty() || datePicker.getValue() == null || numberField.getText().isEmpty() || passwordField.getText().isEmpty())) {
            if (containsOnlyNumbers(numberField)) {
                Database.addPatient(nameField.getText(), surnameField.getText(), middlenameField.getText(), datePicker.getValue(), numberField.getText(), passwordField.getText());
                refreshPatient();
            } else {
                concreteClass.showAlert("Wrong number style", event);
            }
        } else {
            concreteClass.showAlert("Fill the TextFields", event);
        }
    }

    public static boolean containsOnlyNumbers(TextField textField) {
        String text = textField.getText();
        return text.matches("\\d*");
    }


    private void refreshPatient() {
        nameField.clear();
        surnameField.clear();
        middlenameField.clear();
        numberField.clear();
        datePicker.setValue(null);
        passwordField.clear();
        patientTable.setItems(getPatientData());
    }

    // Deletes the selected patient's info as an admin
    public void deleteStaff(TableView<Patient> tableView, ActionEvent event) {
        AdminPatientViewController.Patient selectedPerson = tableView.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            String name = selectedPerson.getName();
            String surname = selectedPerson.getSurname();
            String middlename = selectedPerson.getMiddlename();
            String bday = selectedPerson.getBday();
            Database.deletePatient(name, surname, middlename, bday);
            refreshPatient();
        } else {
            concreteClass.showAlert("No row selected", event);
        }
    }

    private ObservableList<AdminPatientViewController.Patient> getPatientData() {
        patientTable.getItems().clear();
        ObservableList<AdminPatientViewController.Patient> data = FXCollections.observableArrayList();
        String[] text = Database.getPatientList().split(" ");
        if (!text[0].equals("")) {
            for (int i = 0; i < text.length - 1; i += 5) {
                data.add(new AdminPatientViewController.Patient(text[i].trim(), text[i + 1].trim(), text[i + 2].trim(), text[i + 3].trim(), text[i + 4].trim()));
            }
        }
        return data;
    }

    public void showId(TableView<Patient> tableView, ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        AdminPatientViewController.Patient selectedPerson = tableView.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            String name = selectedPerson.getName();
            String surname = selectedPerson.getSurname();
            String middlename = selectedPerson.getMiddlename();
            concreteClass.showInformativeAlert(Database.getPatientDetails(name, surname, middlename), stage);
            refreshPatient();
        } else {
            concreteClass.showAlert("No row selected", event);
        }
    }

    // Updates the selected patient's info
    public void updatePatient(ActionEvent event) {
        if (selectedPatient != null) {
            if (!(nameField.getText().isEmpty() || surnameField.getText().isEmpty() || middlenameField.getText().isEmpty() || datePicker.getValue() == null || numberField.getText().isEmpty())) {
                if (containsOnlyNumbers(numberField)) {
                    // Update the patient in the database
                    Database.updatePatient(selectedPatient.getName(), selectedPatient.getSurname(), selectedPatient.getMiddlename(),
                            nameField.getText(), surnameField.getText(), middlenameField.getText(), datePicker.getValue().toString(), numberField.getText(), passwordField.getText());
                    refreshPatient();
                } else {
                    concreteClass.showAlert("Wrong number style", event);
                }
            } else {
                concreteClass.showAlert("Fill the TextFields", event);
            }
        } else {
            concreteClass.showAlert("No patient selected for update", event);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        tableSurname.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());
        tableMiddlename.setCellValueFactory(cellData -> cellData.getValue().middlenameProperty());
        tableBday.setCellValueFactory(cellData -> cellData.getValue().bdayProperty());
        tableNumber.setCellValueFactory(cellData -> cellData.getValue().numberProperty());

        // Initialize the table
        patientTable.setItems(getPatientData());
        delBtn.setOnAction(event -> deleteStaff(patientTable, event));
        idBtn.setOnAction(event -> showId(patientTable, event));

        // Add listener for row selection
        patientTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedPatient = newValue;
                nameField.setText(newValue.getName());
                surnameField.setText(newValue.getSurname());
                middlenameField.setText(newValue.getMiddlename());
                numberField.setText(newValue.getNumber());
                datePicker.setValue(java.time.LocalDate.parse(newValue.getBday()));
            }
        });

        updateBtn.setOnAction(this::updatePatient);
    }

    public class Patient {
        private final SimpleStringProperty name;
        private final SimpleStringProperty surname;
        private final SimpleStringProperty middlename;
        private final SimpleStringProperty bday;
        private final SimpleStringProperty number;

        public Patient(String name, String surname, String middlename, String bday, String number) {
            this.name = new SimpleStringProperty(name);
            this.surname = new SimpleStringProperty(surname);
            this.middlename = new SimpleStringProperty(middlename);
            this.bday = new SimpleStringProperty(bday);
            this.number = new SimpleStringProperty(number);
        }

        public String getName() {
            return name.get();
        }

        public void setName(String newName) {
            name.set(newName);
        }

        public String getSurname() {
            return surname.get();
        }

        public void setSurname(String newSurname) {
            surname.set(newSurname);
        }

        public String getMiddlename() {
            return middlename.get();
        }

        public void setMiddlename(String newMiddlename) {
            middlename.set(newMiddlename);
        }

        public String getBday() {
            return bday.get();
        }

        public void setBday(String newBday) {
            bday.set(newBday);
        }

        public String getNumber() {
            return number.get();
        }

        public void setNumber(String newNumber) {
            number.set(newNumber);
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public SimpleStringProperty surnameProperty() {
            return surname;
        }

        public SimpleStringProperty middlenameProperty() {
            return middlename;
        }

        public SimpleStringProperty bdayProperty() {
            return bday;
        }

        public SimpleStringProperty numberProperty() {
            return number;
        }
    }
}
