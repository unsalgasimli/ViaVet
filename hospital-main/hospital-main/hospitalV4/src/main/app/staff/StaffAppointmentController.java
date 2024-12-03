package main.app.staff;

import javafx.beans.binding.Bindings;
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
import java.time.LocalDate;
import java.util.ResourceBundle;

public class StaffAppointmentController implements Initializable {
    @FXML
    Button showDetails;
    @FXML
    Button proposeBtn;
    @FXML
    Button addBtn;
    @FXML
    DatePicker datePicker;
    @FXML
    MenuButton timeMenu;
    @FXML
    TextField infoField;
    @FXML
    Button deleteBtn;

    @FXML
    private TableView<StaffAppointmentController.Appointment> appointmentTable;
    @FXML
    private TableColumn<StaffAppointmentController.Appointment, String> tableColumnOne;
    @FXML
    private TableColumn<StaffAppointmentController.Appointment, String> tableColumnTwo;
    @FXML
    private TableColumn<StaffAppointmentController.Appointment, String> tableColumnThree;
    @FXML
    private TableColumn<StaffAppointmentController.Appointment, String> tableColumnFour;
    @FXML
    private TableColumn<StaffAppointmentController.Appointment, String> tableColumnFive;


    @FXML
    private TableView<StaffAppointmentController.Pets> petTable;
    @FXML
    private TableColumn<StaffAppointmentController.Pets, String> tableF;
    @FXML
    private TableColumn<StaffAppointmentController.Pets, String> tableS;

    ConcreteClass concreteClass=new ConcreteClass();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableColumnOne.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        tableColumnTwo.setCellValueFactory(cellData -> cellData.getValue().pnameProperty());
        tableColumnThree.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        tableColumnFour.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        tableColumnFive.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        tableF.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        tableS.setCellValueFactory(cellData -> cellData.getValue().pnameProperty());



        appointmentTable.setItems(getAppointmentData());
        petTable.setItems(getPetData());
        appointmentTable.setItems(getAppointmentData());

        showDetails.visibleProperty().bind(Bindings.isNotEmpty(appointmentTable.getSelectionModel().getSelectedItems()));
        deleteBtn.visibleProperty().bind(Bindings.isNotEmpty(appointmentTable.getSelectionModel().getSelectedItems()));

        showDetails.setOnAction((ActionEvent event) -> {
            showAppointmentInfo(appointmentTable,event);
        });
        proposeBtn.setOnAction((ActionEvent event) -> {
            proposeAppointment(appointmentTable,event);
        });
        addBtn.setOnAction((ActionEvent event) -> {
            addAppointment(petTable,event);
        });
        deleteBtn.setOnAction((ActionEvent event) -> {
            deleteAppointment(appointmentTable,event);
        });


        for (int hour = 8; hour < 21; hour++) {
            String timeStr = String.format("%02d:00", hour);
            MenuItem menuItem = new MenuItem(timeStr);
            menuItem.setOnAction(e -> onTimeSelected(timeStr));
            timeMenu.getItems().add(menuItem);

        }


    }

    public void openStaffHistory(ActionEvent event) throws Exception{
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();
        App.openStaffHistory();
    }
    public void logOut(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();
        App.logOpen();
    }
    public void openStaffInfo(ActionEvent event) throws Exception{
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();
        App.openStaffInfo();
    }

    public void cancelSelection(){
        appointmentTable.getSelectionModel().clearSelection();
        petTable.getSelectionModel().clearSelection();
    }
 public void refresh(){
        infoField.clear();
        datePicker.setValue(null);
        timeMenu.setText("Time");
        appointmentTable.getSelectionModel().clearSelection();
        petTable.getSelectionModel().clearSelection();
        appointmentTable.setItems(getAppointmentData());
        petTable.setItems(getPetData());
        appointmentTable.setItems(getAppointmentData());
 }



    private void onTimeSelected(String selectedTime) {
        timeMenu.setText(selectedTime);
    }


    private ObservableList<StaffAppointmentController.Pets> getPetData() {
        appointmentTable.getItems().clear();
        ObservableList<StaffAppointmentController.Pets> data = FXCollections.observableArrayList();
        String[] text = DataBase.getPetList("staff").split("\\$");
        for (int i = 0; i < text.length; i += 2) {
            data.add(new StaffAppointmentController.Pets(text[i], text[i + 1]));
        }
        return data;
    }

    private ObservableList<StaffAppointmentController.Appointment> getAppointmentData() {
        appointmentTable.getItems().clear();
        ObservableList<StaffAppointmentController.Appointment> data = FXCollections.observableArrayList();
        String[] text = DataBase.getAppointmentList(LogController.activeID, true).split(" ");
        for (int i = 0; i < text.length; i += 5) {
            data.add(new StaffAppointmentController.Appointment(text[i], text[i + 4], text[i + 1], text[i + 2],text[i+3]));
        }
        return data;
    }

    //SHOWS DETAILS OF APPOINTMENT AND INTRODUCES OPTIONS TO CHANGE STATUS
    public void showAppointmentInfo(TableView<StaffAppointmentController.Appointment> tableView, ActionEvent event) {
        StaffAppointmentController.Appointment selectedAppointment = tableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null && selectedAppointment.getStatus().equals("Pending")) {
            String fullName = selectedAppointment.getName();
            String date = selectedAppointment.getDate();
            String time = selectedAppointment.getTime();
            Dialog<ButtonType> confirmationDialog =concreteClass.CustomConfirmationDialog((DataBase.showAppointmentInfo(fullName, date, time)),event);
            ButtonType result = confirmationDialog.showAndWait().orElse(ButtonType.CANCEL);
           concreteClass.CustomConfirmationDialog(DataBase.showAppointmentInfo(fullName, date, time), event);
            if (result.getText().equals("Accept")) {
                DataBase.setAppointmentStatus(fullName, date, time, "Accepted");
                appointmentTable.setItems(getAppointmentData());
            } else if (result.getText().equals("Dismiss")) {
                DataBase.setAppointmentStatus(fullName, date, time, "Dismissed");
                appointmentTable.setItems(getAppointmentData());
            }

        } else {
           concreteClass.showAlert("No row selected/Wrong syntax",event);
        }
    }


    public void proposeAppointment(TableView<StaffAppointmentController.Appointment> tableView,ActionEvent event) {
        StaffAppointmentController.Appointment selectedAppointment = tableView.getSelectionModel().getSelectedItem();
        if(selectedAppointment != null && selectedAppointment.getStatus().equals("Pending")  ){
        if ( datePicker.getValue()!=null && timeMenu.getText()!="Time" ) {
            String fullName = selectedAppointment.getName();
            String date = selectedAppointment.getDate();
            String time = selectedAppointment.getTime();
            String newDate = String.valueOf(datePicker.getValue());
            String newTime = timeMenu.getText();
            DataBase.proposeAppointment(fullName, date, time, "Proposed", newDate, newTime);
            refresh();
        }
        } else {
            concreteClass.showAlert("No row selected/Context Error",event);
        }
    }


    public void addAppointment(TableView<StaffAppointmentController.Pets> tableView,ActionEvent event) {
        StaffAppointmentController.Pets selectedPet = tableView.getSelectionModel().getSelectedItem();
        if (selectedPet != null && datePicker.getValue()!=null && timeMenu.getText()!="Time" ) {
            String[] ownerName = selectedPet.getName().split(" ");
            String ownerID=DataBase.getPatientID(ownerName[0],ownerName[1],ownerName[2]);
            String staffID= LogController.activeID;
            LocalDate date=datePicker.getValue();
            String time=timeMenu.getText();
            String info=infoField.getText();
            String petName=selectedPet.getPname();

            DataBase.addAppointment(ownerID,staffID, date, time,info, "Proposed",petName);
            refresh();

        } else {
            concreteClass.showAlert("No row selected",event);
        }
    }


    public void deleteAppointment(TableView<StaffAppointmentController.Appointment> tableView,ActionEvent event) {
        StaffAppointmentController.Appointment selectedAppointment = tableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {

            String id = LogController.activeID;
            String pet = selectedAppointment.getPname();
            String date=selectedAppointment.getDate();
            DataBase.deleteAppointment(id,pet,date);
            appointmentTable.setItems(getAppointmentData());

        } else {
            concreteClass.showAlert("No row selected",event);
        }
    }



    public static class Appointment {
        private final SimpleStringProperty name;
        private final SimpleStringProperty pname;
        private final SimpleStringProperty date;
        private final SimpleStringProperty time;
        private final SimpleStringProperty status;


        public Appointment(String name,String pname, String date, String time, String status) {
            this.name = new SimpleStringProperty(name);
            this.pname = new SimpleStringProperty(pname);
            this.date = new SimpleStringProperty(date);
            this.time = new SimpleStringProperty(time);
            this.status = new SimpleStringProperty(status);

        }

        public String getName() {
            return name.get();
        }

        private SimpleStringProperty nameProperty() {
            return name;
        }
        public String getPname() {
            return pname.get();
        }

        public SimpleStringProperty pnameProperty() {
            return pname;
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

    public static class Pets {
        private final SimpleStringProperty name;
        private final SimpleStringProperty pname;


        public Pets(String name,String pname) {
            this.name = new SimpleStringProperty(name);
            this.pname = new SimpleStringProperty(pname);

        }

        public String getName() {
            return name.get();
        }

        private SimpleStringProperty nameProperty() {
            return name;
        }
        public String getPname() {
            return pname.get();
        }

        public SimpleStringProperty pnameProperty() {
            return pname;
        }


    }}