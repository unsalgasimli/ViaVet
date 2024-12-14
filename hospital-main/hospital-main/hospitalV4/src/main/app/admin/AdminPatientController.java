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

import java.net.URL;
import java.util.ResourceBundle;

public class AdminPatientController implements Initializable {

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
    Button updateBtn; // Button to trigger the update operation
    @FXML
    private TableView<AdminPatientController.Patient> patientTable;
    @FXML
    private TableColumn<AdminPatientController.Patient, String> tableName;
    @FXML
    private TableColumn<AdminPatientController.Patient, String> tableSurname;
    @FXML
    private TableColumn<AdminPatientController.Patient, String> tableMiddlename;
    @FXML
    private TableColumn<AdminPatientController.Patient, String> tableBday;
    @FXML
    private TableColumn<AdminPatientController.Patient, String> tableNumber;

    ConcreteClass concreteClass = new ConcreteClass();
    private Patient selectedPatient; // Holds the currently selected patient

    // Opens the staff management tab for admin
    public void openStaffManagment(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();
        App.openAdminStaff();
    }

    public void logOut(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();
        App.openLogin();
    }

    // Adds a new patient as an admin
    public void addPatient(ActionEvent event) {
        if (!(nameField.getText().isEmpty() || surnameField.getText().isEmpty() || middlenameField.getText().isEmpty() || datePicker.getValue() == null || numberField.getText().isEmpty() || passwordField.getText().isEmpty())) {
            if (containsOnlyNumbers(numberField)) {
                DataBase.addPatient(nameField.getText(), surnameField.getText(), middlenameField.getText(), datePicker.getValue(), numberField.getText(), passwordField.getText());
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
        return text.matches("\\d*"); // This regex checks if the string contains only digits
    }

    // Resets the input fields and refreshes the table
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
        AdminPatientController.Patient selectedPerson = tableView.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            String name = selectedPerson.getName();
            String surname = selectedPerson.getSurname();
            String middlename = selectedPerson.getMiddlename();
            String bday = selectedPerson.getBday();
            DataBase.deletePatient(name, surname, middlename, bday);
            refreshPatient();
        } else {
            concreteClass.showAlert("No row selected", event);
        }
    }

    private ObservableList<AdminPatientController.Patient> getPatientData() {
        patientTable.getItems().clear();
        ObservableList<AdminPatientController.Patient> data = FXCollections.observableArrayList();
        // Call to database
        String[] text = DataBase.getPatientList().split(" ");
        if (!text[0].equals("")) {
            for (int i = 0; i < text.length - 1; i += 5) {
                data.add(new AdminPatientController.Patient(text[i].trim(), text[i + 1].trim(), text[i + 2].trim(), text[i + 3].trim(), text[i + 4].trim()));
            }
        }
        return data;
    }

    public void showId(TableView<Patient> tableView, ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        AdminPatientController.Patient selectedPerson = tableView.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            String name = selectedPerson.getName();
            String surname = selectedPerson.getSurname();
            String middlename = selectedPerson.getMiddlename();
            concreteClass.showInformativeAlert(DataBase.getPatientDetails(name, surname, middlename), stage);
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
                    DataBase.updatePatient(selectedPatient.getName(), selectedPatient.getSurname(), selectedPatient.getMiddlename(),
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
