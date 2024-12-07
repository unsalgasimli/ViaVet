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

    ConcreteClass concreteClass=new ConcreteClass();

    public void logOut(ActionEvent event) {
        App.logOpen();
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();

    }

    public void openPatientInfo(ActionEvent event) {
        App.openPatienInfo();
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();

    }

    public void openPatientHistory(ActionEvent event)  {
        App.openPatienHistory();
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();

    }


    private void refresh() {
        descriptionField.clear();
        datePicker.setValue(null);
        petMenu.setText("Pets");
        vetMenu.setText("Veteranerian");
        timeMenu.setText("Time");
      //  AppointmentTable.setItems(getAppointmentData());
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

     //   acceptBtn.setOnAction(event -> setAppStatus(AppointmentTable, "Accepted", event));
    //    dismissBtn.setOnAction(event -> setAppStatus(AppointmentTable, "Dismissed", event));
    //    AppointmentTable.setOnMouseClicked(event -> check(AppointmentTable));

    //    deleteAppointment.setOnAction(event -> deleteAppointment(AppointmentTable,event));
   //     appointmentDetailBtn.setOnAction(event -> showDetails(AppointmentTable, event));

//        String[] text = DataBase.getPetList("patient").split(" ");


//        for (int i = 0; i < text.length; i += 3) {
//            try {
//                MenuItem menuItem = new MenuItem(text[i]);
//                String finalLine = text[i];
//                menuItem.setOnAction(e -> handleMenuItemClickForPet(finalLine));
//                petMenu.getItems().add(menuItem);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        String[] txt = DataBase.getVetList().split("\\$");
//        for (String s : txt) {
//            try {
//                MenuItem menuItem = new MenuItem(s);
//                menuItem.setOnAction(e -> handleMenuItemClickForVet(s));
//                vetMenu.getItems().add(menuItem);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        for (int hour = 8; hour < 21; hour++) {
//            String timeStr = String.format("%02d:00", hour);
//            MenuItem menuItem = new MenuItem(timeStr);
//            menuItem.setOnAction(e -> onTimeSelected(timeStr));
//            timeMenu.getItems().add(menuItem);
//
//        }

    }



    private ObservableList<PatientAppointmentController.Appointment> getAppointmentData() {
        String docName;
        AppointmentTable.getItems().clear();
        ObservableList<PatientAppointmentController.Appointment> data = FXCollections.observableArrayList();
        String[] text = DataBase.getAppointmentList(LogController.activeID, false).split(" ");
        if (!text[0].equals("")) {
            for (int i = 0; i < text.length; i += 5) {

             data.add(new PatientAppointmentController.Appointment(text[i], text[i + 1], text[i + 2], text[i + 3], text[i + 4]));

            }
        }
        return data;
    }



//    private void setAppStatus(TableView<Appointment> appointmentTable, String status, ActionEvent event) {
//        PatientAppointmentController.Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
//        if (selectedAppointment != null && selectedAppointment.getStatus().equals("Proposed")) {
//            DataBase.setAppointmentStatus(selectedAppointment.getDate(), selectedAppointment.getTime(), status, event);
//            refresh();
//        }else{
//            concreteClass.showAlert("No row selected/Syntax error",event);
//        }
//    }
//
//
//    public void showDetails(TableView<Appointment> appointmentTable, ActionEvent event) {
//        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
//        PatientAppointmentController.Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
//        if (selectedAppointment != null) {
//            String name = selectedAppointment.getDoctor();
//            String date = selectedAppointment.getDate();
//            String time = selectedAppointment.getTime();
//
//            concreteClass.showInformativeAlert(DataBase.showAppointmentInfoPM(name, date, time),stage);
//
//        } else {
//            concreteClass.showAlert( "No row selected",event);
//        }
//    }
//
//
//    private void check(TableView<Appointment> appointmentTable) {
//        PatientAppointmentController.Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
//        if (selectedAppointment != null) {
//            acceptBtn.setVisible(true);
//            dismissBtn.setVisible(true);
//
//        }
//    }
//
//
//    private void onTimeSelected(String selectedTime) {
//        timeMenu.setText(selectedTime);
//    }
//
//    public void setTime() {
//
//        timeMenu.getItems().clear();
//        if (!(datePicker.getValue() == null || vetMenu.getText().equals("Veteranerian"))) {
//            String[] ns = vetMenu.getText().split(" ");
//            String staffID = DataBase.getStaffID(ns[0], ns[1]);
//            String day = String.valueOf(datePicker.getValue());
//            times = DataBase.getOccupiedTimes(staffID, day);
//            for (int hour = 8; hour < 21; hour++) {
//                String timeStr = String.format("%02d:00", hour);
//                if (!times.contains(timeStr)) {
//                    MenuItem menuItem = new MenuItem(timeStr);
//                    menuItem.setOnAction(e -> onTimeSelected(timeStr));
//                    timeMenu.getItems().add(menuItem);
//                }
//            }
//        }
//    }
//
//    public void deleteAppointment(TableView<PatientAppointmentController.Appointment> tableView,ActionEvent event) {
//        PatientAppointmentController.Appointment selectedAppointment = tableView.getSelectionModel().getSelectedItem();
//        if (selectedAppointment != null) {
//
//            String[] name = selectedAppointment.getDoctor().split(" ");
//            String id = DataBase.getStaffID(name[0], name[1]);
//
//            String pet = selectedAppointment.getPet();
//            String date = selectedAppointment.getDate();
//            DataBase.deleteAppointment(id, pet, date);
//            AppointmentTable.setItems(getAppointmentData());
//
//        } else {
//            concreteClass.showAlert( "TextFields not filled",event);
//        }
//    }
//
//
//    private void handleMenuItemClickForPet(String animal) {
//        petMenu.setText(animal);
//    }
//
//    private void handleMenuItemClickForVet(String vet) {
//        vetMenu.setText(vet);
//    }
//



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
