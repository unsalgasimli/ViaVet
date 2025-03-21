package main.java.patient;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.Main;
import main.java.core.AlertManager;
import main.java.core.Database;
import main.java.auth.LoginController;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PatientHistoryController implements Initializable {

    @FXML
    TextField infoField;
    @FXML
    MenuButton petMenu;
    @FXML
    DatePicker datePicker;
    @FXML
    Button delBtn;
    @FXML
    private TableView<PatientHistoryController.History> HistoryTable;
    @FXML
    private TableColumn<PatientHistoryController.History, String> tableOne;
    @FXML
    private TableColumn<PatientHistoryController.History, String> tableTwo;
    @FXML
    private TableColumn<PatientHistoryController.History, String> tableThree;
    @FXML
    private TableColumn<PatientHistoryController.History, String> tableFour;

    AlertManager alertManager = new AlertManager();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableOne.setCellValueFactory(cellData -> cellData.getValue().doctorProperty());
        tableTwo.setCellValueFactory(cellData -> cellData.getValue().petProperty());
        tableThree.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        tableFour.setCellValueFactory(cellData -> cellData.getValue().infoProperty());

        HistoryTable.setItems(getHistoryData());
        delBtn.setOnAction(event -> deleteHistory(HistoryTable, event));

        String[] text = Database.getPetList("patient").split(" ");
        for (int i = 0; i < text.length - 1; i += 3) {
            try {
                text[i] = text[i].trim();
                MenuItem menuItem = new MenuItem(text[i]);
                String finalLine = text[i];
                menuItem.setOnAction(e -> handleMenuItemClickForPet(finalLine));
                petMenu.getItems().add(menuItem);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void openPatientAppointment(ActionEvent event) throws Exception {
        Main.openPatientAppointment();

    }

    public void openPatientInfo(ActionEvent event) throws Exception {
        Main.openPatientInfo();
    }

    public void logOut(ActionEvent event) throws Exception {
        Main.openLogin();

    }


    private void handleMenuItemClickForPet(String animal) {
        petMenu.setText(animal);
    }



    public void addHist(ActionEvent actionEvent) throws SQLException {
        AlertManager concreteInstance = new AlertManager();
        if(!(infoField.getText().isEmpty() || datePicker.getValue() == null || petMenu.getText().equals("Pet"))) {
            if(datePicker.getValue().isBefore(LocalDate.now())) {
                int pet = Database.getPetId(petMenu.getText(), LoginController.activeID);
                Database.addHistory(LoginController.activeID, pet, "owner", infoField.getText(), datePicker.getValue());
                refreshPatientHistory();
            }else{
                concreteInstance.showAlert("Date can be selected only before today.",actionEvent);
            } }else{
            concreteInstance.showAlert("Fields must be filled.",actionEvent);
            }
    }

    private void refreshPatientHistory() {
        infoField.clear();
        datePicker.setValue(null);
        petMenu.setText("Pet");
        HistoryTable.setItems(getHistoryData());

    }

    private ObservableList<PatientHistoryController.History> getHistoryData() {
        String name;
        String petName;
        HistoryTable.getItems().clear();
        ObservableList<PatientHistoryController.History> data = FXCollections.observableArrayList();
        String[] text = Database.getHistoryList(LoginController.activeID).split("○");
        if (!text[0].equals("")) {
            for (int i = 0; i < text.length - 1; i += 4) {

                petName = Database.getPetName(text[i].trim());
                if (text[i + 1].equals("owner")) {
                    name = Database.getPatientNS(LoginController.activeID);
                } else {
                    name = Database.getStaffNS(text[i + 1]);
                }
                data.add(new PatientHistoryController.History(name, petName, text[i + 3], text[i + 2]));

            }
        }
        return data;
    }

    public void deleteHistory(TableView<PatientHistoryController.History> tableView, ActionEvent event) {
        PatientHistoryController.History selectedHistory = tableView.getSelectionModel().getSelectedItem();
        if (selectedHistory != null) {

            String name = selectedHistory.getDoctor().trim();
            String pet = selectedHistory.getPet().trim();
            String info = selectedHistory.getInfo().trim();
            Database.deleteHistory(name, pet, info);
            HistoryTable.setItems(getHistoryData());

        } else {
            alertManager.showAlert("TextFields not filled", event);
        }
    }


    public static class History {
        private final SimpleStringProperty doctor;
        private final SimpleStringProperty pet;
        private final SimpleStringProperty date;
        private final SimpleStringProperty info;

        public History(String doctor, String pet, String date, String info) {
            this.doctor = new SimpleStringProperty(doctor);
            this.pet = new SimpleStringProperty(pet);
            this.date = new SimpleStringProperty(date);
            this.info = new SimpleStringProperty(info);

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

        public SimpleStringProperty petProperty() {
            return pet;
        }

        public String getInfo() {
            return info.get();
        }

        public SimpleStringProperty infoProperty() {
            return info;
        }

        public String getDate() {
            return date.get();
        }

        public SimpleStringProperty dateProperty() {
            return date;
        }

    }
}
