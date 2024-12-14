package main.app.patient;

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

import java.net.URL;
import java.util.ResourceBundle;

public class PatientAppointmentController implements Initializable {


    String times;
    @FXML
    TextField descriptionField;
    @FXML
    DatePicker datePicker;
    @FXML
    MenuButton petMenu;
    @FXML
    MenuButton vetMenu;
    @FXML
    Button deleteAppointment;
    @FXML
    Button acceptBtn;
    @FXML
    Button dismissBtn;
    @FXML
    MenuButton timeMenu;
    @FXML
    Button appointmentDetailBtn;
    @FXML
    private TableView<PatientAppointmentController.Appointment> AppointmentTable;
    @FXML
    private TableColumn<PatientAppointmentController.Appointment, String> tableColumnOne;
    @FXML
    private TableColumn<PatientAppointmentController.Appointment, String> tableColumnTwo;
    @FXML
    private TableColumn<PatientAppointmentController.Appointment, String> tableColumnThree;
    @FXML
    private TableColumn<PatientAppointmentController.Appointment, String> tableColumnFour;
    @FXML
    private TableColumn<PatientAppointmentController.Appointment, String> tableColumnFive;

    ConcreteClass concreteClass = new ConcreteClass();

    public void logOut(ActionEvent event) {
        App.openLogin();
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();

    }

    public void openPatientInfo(ActionEvent event) {
        App.openPatientInfo();
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();

    }

    public void openPatientHistory(ActionEvent event) {
        App.openPatientHistory();
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();

    }


    private void refresh() {
        descriptionField.clear();
        datePicker.setValue(null);
        petMenu.setText("Pets");
        vetMenu.setText("Veteranerian");
        timeMenu.setText("Time");
        AppointmentTable.setItems(getAppointmentData());
    }

    public void cancelSelection() {
        AppointmentTable.getSelectionModel().clearSelection();
        acceptBtn.setVisible(false);
        dismissBtn.setVisible(false);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableColumnOne.setCellValueFactory(cellData -> cellData.getValue().doctorProperty());
        tableColumnTwo.setCellValueFactory(cellData -> cellData.getValue().petProperty());
        tableColumnThree.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        tableColumnFour.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        tableColumnFive.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        AppointmentTable.setItems(getAppointmentData());

        acceptBtn.setVisible(false);
        dismissBtn.setVisible(false);
        datePicker.setOnAction(event -> changeTimeStatus());
        acceptBtn.setOnAction(event -> setAppStatus(AppointmentTable, "Accepted", event));
        dismissBtn.setOnAction(event -> setAppStatus(AppointmentTable, "Dismissed", event));
        AppointmentTable.setOnMouseClicked(event -> check(AppointmentTable));

         deleteAppointment.setOnAction(event -> deleteAppointment(AppointmentTable,event));
         appointmentDetailBtn.setOnAction(event -> showDetails(AppointmentTable, event));

        String petListData = DataBase.getPetList("patient");
        String[] text = petListData.split(" ");  // Split by newline to separate each pet



        for (int i = 0; i < text.length-1; i+=3) {
            try {

                String name = text[i].trim();  // Remove leading/trailing spaces
                try {
                    MenuItem menuItem = new MenuItem(name);
                    menuItem.setOnAction(e -> handleMenuItemClickForPet(name));
                    petMenu.getItems().add(menuItem);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                System.err.println("Error processing pet data: " + e);
            }
        }





        String getVetData =  DataBase.getVetList();
        text = getVetData.split("\n");  // Split by newline to separate each pet

        for (String vetDetails : text) {
            // Skip empty lines
            if (vetDetails.trim().isEmpty()) continue;

            try {
                String[] vetInfo = vetDetails.split("\n");  // Split by pipe ("|")

                String name = vetInfo[0].trim();  // Remove leading/trailing spaces
                try {
                    MenuItem menuItem = new MenuItem(name);
                    menuItem.setOnAction(e -> handleMenuItemClickForVet(name));
                    vetMenu.getItems().add(menuItem);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                System.err.println("Error processing vet data: " + e);
            }
        }

        for (int hour = 8; hour < 21; hour++) {
            String timeStr = String.format("%02d:00", hour);
            MenuItem menuItem = new MenuItem(timeStr);
            menuItem.setOnAction(e -> onTimeSelected(timeStr));
            timeMenu.getItems().add(menuItem);

        }

    }

    private void changeTimeStatus() {
        setTime();
    }

    private void handleMenuItemClickForVet(String name) {
        vetMenu.setText(name);
        setTime();
    }

    private void onTimeSelected(String timeStr) {
        timeMenu.setText(timeStr);
    }

    private void handleMenuItemClickForPet(String name) {
        petMenu.setText(name);
    }


    private ObservableList<PatientAppointmentController.Appointment> getAppointmentData() {
        AppointmentTable.getItems().clear();
        ObservableList<PatientAppointmentController.Appointment> data = FXCollections.observableArrayList();
        String[] text = DataBase.getAppointmentList(LogController.activeID, false).split("\n");
        if (!text[0].equals("")) {
            for (int i = 0; i < text.length-1; i += 5) {
               System.out.println(text[i]);
               data.add(new PatientAppointmentController.Appointment(text[i], text[i + 1], text[i + 2], text[i + 3], text[i + 4]));
            }
        }
        return data;
    }

    public void requestAppointment(ActionEvent actionEvent) {

           String staffId = DataBase.getStaffId(vetMenu.getText());
           int petId = Integer.parseInt(DataBase.getPetId(petMenu.getText()).trim());
           DataBase.createAppointment(staffId.trim(),petId,datePicker.getValue().toString(),timeMenu.getText(),descriptionField.getText());
            refresh();
    }



    private void setAppStatus(TableView<Appointment> appointmentTable, String status, ActionEvent event) {
        PatientAppointmentController.Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null && selectedAppointment.getStatus().equals("Proposed")) {
            DataBase.setAppointmentStatus(selectedAppointment.getDate(), selectedAppointment.getTime(), status,"Proposed");
            refresh();
        }else{
            concreteClass.showAlert("No row selected/Syntax error",event);
        }
    }


    public void showDetails(TableView<Appointment> appointmentTable, ActionEvent event) {
        // Get the current stage
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        // Get the selected appointment from the table
        PatientAppointmentController.Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment != null) {
            String date = selectedAppointment.getDate();
            String time = selectedAppointment.getTime();

            // Fetch the appointment details from the database
            String appointmentInfo = DataBase.showAppointmentInfoPM(date, time);

            // Show an informative alert with the fetched details
            if (appointmentInfo.startsWith("Error:")) {
                // Show error alert if the database method returned an error
                concreteClass.showAlert(appointmentInfo, event);
            } else {
                // Show the fetched details in an informative alert
                concreteClass.showInformativeAlert(appointmentInfo, stage);
            }
        } else {
            // Show an alert when no row is selected
            concreteClass.showAlert("No row selected", event);
        }
    }


    private void check(TableView<Appointment> appointmentTable) {
        PatientAppointmentController.Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            acceptBtn.setVisible(true);
            dismissBtn.setVisible(true);

        }
    }


    public void setTime() {

        timeMenu.getItems().clear();
        if (!(datePicker.getValue() == null || vetMenu.getText().equals("Veteranerian"))) {
            String staffID = DataBase.getStaffId(vetMenu.getText().toString().trim());
            String day = String.valueOf(datePicker.getValue());
            times = DataBase.getOccupiedTimes(staffID, day);

            for (int hour = 8; hour < 21; hour++) {
                String timeStr = String.format("%02d:00", hour);
                if (!times.contains(timeStr)) {
                    MenuItem menuItem = new MenuItem(timeStr);
                    menuItem.setOnAction(e -> onTimeSelected(timeStr));
                    timeMenu.getItems().add(menuItem);
                }
            }
        }
    }

    public void deleteAppointment(TableView<PatientAppointmentController.Appointment> tableView,ActionEvent event) {
        PatientAppointmentController.Appointment selectedAppointment = tableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {

            String id = DataBase.getStaffId(selectedAppointment.getDoctor()).trim();

            int pet = Integer.parseInt(DataBase.getPetId(selectedAppointment.getPet()).trim());
            String date = selectedAppointment.getDate();
            DataBase.deleteAppointment(id, pet, date);
            AppointmentTable.setItems(getAppointmentData());

        } else {
            concreteClass.showAlert( "TextFields not filled",event);
        }
        refresh();
    }




    public static class Appointment {
        private final SimpleStringProperty doctor;
        private final SimpleStringProperty pet;
        private final SimpleStringProperty date;
        private final SimpleStringProperty time;
        private final SimpleStringProperty status;

        public Appointment(String doctor, String pet, String date, String time, String status) {
            this.doctor = new SimpleStringProperty(doctor);
            this.pet = new SimpleStringProperty(pet);
            this.date = new SimpleStringProperty(date);
            this.time = new SimpleStringProperty(time);
            this.status = new SimpleStringProperty(status);
        }

        public String getDoctor() {
            return doctor.get();
        }

        private SimpleStringProperty doctorProperty() {
            return doctor;
        }

        public String getPet() {
            return pet.get();
        }

        private SimpleStringProperty petProperty() {
            return pet;
        }

        public String getDate() {
            return date.get();
        }

        public SimpleStringProperty dateProperty() {
            return date;
        }

        public String getTime() {
            return time.get();
        }

        public SimpleStringProperty timeProperty() {
            return time;
        }

        public String getStatus() {
            return status.get();
        }

        public SimpleStringProperty statusProperty() {
            return status;
        }
    }

}
