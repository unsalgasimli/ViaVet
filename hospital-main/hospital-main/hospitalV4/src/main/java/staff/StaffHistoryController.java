package main.java.staff;

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

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class StaffHistoryController implements Initializable {
    @FXML
    TextField infoField;
    @FXML
    DatePicker datePicker;
    @FXML
    Button addHistBtn;
    @FXML
    Button delBtn;


    @FXML
    private TableView<StaffHistoryController.History> HistoryTable;
    @FXML
    private TableColumn<StaffHistoryController.History, String> tableOne;
    @FXML
    private TableColumn<StaffHistoryController.History, String> tableTwo;
    @FXML
    private TableColumn<StaffHistoryController.History, String> tableThree;

    @FXML
    private TableView<StaffHistoryController.Pet> PetTable;
    @FXML
    private TableColumn<StaffHistoryController.Pet, String> tableOdin;
    @FXML
    private TableColumn<StaffHistoryController.Pet, String> tableDva;

    AlertManager alertManager =new AlertManager();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableOne.setCellValueFactory(cellData -> cellData.getValue().doctorProperty());
        tableTwo.setCellValueFactory(cellData -> cellData.getValue().petProperty());
        tableThree.setCellValueFactory(cellData -> cellData.getValue().infoProperty());


        tableOdin.setCellValueFactory(cellData -> cellData.getValue().ownerProperty());
        tableDva.setCellValueFactory(cellData -> cellData.getValue().petNameProperty());


        addHistBtn.setOnAction((ActionEvent event) -> {
            try {
                addHist(PetTable,event);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        delBtn.setOnAction((ActionEvent event) -> {
            deleteHistory(HistoryTable,event);
        });

        PetTable.setItems(getPetData());
        HistoryTable.setItems(getHistoryData());

    }

    public void openStaffAppointment(ActionEvent event) throws Exception {
        Main.openStaffAppointment();
    }



    public void logOut(ActionEvent event) throws Exception {
        Main.openLogin();
    }


    public void addHist(TableView<Pet> petTable,ActionEvent event) throws SQLException {

        StaffHistoryController.Pet selectedPet = petTable.getSelectionModel().getSelectedItem();
        if (selectedPet != null && datePicker.getValue() != null && !infoField.getText().isEmpty()) {
            if(datePicker.getValue().isBefore(LocalDate.now())) {
                String owner = selectedPet.getOwner();
                String petName = selectedPet.getpetName();
                Database.addHistoryStaff(owner.trim(), petName.trim(), infoField.getText().trim(), datePicker.getValue());
            }
            else{
                alertManager.showAlert("Date can be selected only before today.",event);
            }
        } else {
            alertManager.showAlert("No row selected",event);
        }
        refreshPatientHistory();
    }


    public void deleteHistory(TableView<StaffHistoryController.History> tableView,ActionEvent event) {
        StaffHistoryController.History selectedHistory = tableView.getSelectionModel().getSelectedItem();
        if (selectedHistory != null) {
            String owner = selectedHistory.getDoctor();
            String pet = selectedHistory.getPet();
            String info = selectedHistory.getInfo();
            Database.deleteHistoryStaff(owner, pet, info);
            refreshPatientHistory();
        } else {
            alertManager.showAlert("No row selected",event);
        }

    }

    private void refreshPatientHistory() {
        infoField.clear();
        datePicker.setValue(null);
        HistoryTable.setItems(getHistoryData());


    }

    private ObservableList<StaffHistoryController.History> getHistoryData() {
        HistoryTable.getItems().clear();
        ObservableList<StaffHistoryController.History> data = FXCollections.observableArrayList();
        String[] text = Database.getAllHistoryList().split("○");
        if (!text[0].equals("")) {
            for (int i = 0; i < text.length-1; i += 3) {
                data.add(new StaffHistoryController.History(text[i].trim(), text[i + 1], text[i + 2]));
            }
        }
        return data;
    }


    private ObservableList<StaffHistoryController.Pet> getPetData() {
        HistoryTable.getItems().clear();
        ObservableList<StaffHistoryController.Pet> data = FXCollections.observableArrayList();
        String[] text = Database.getPetList("staff").split("○");
        if (!text[0].equals("")) {
            for (int i = 0; i < text.length-1; i += 2) {
                data.add(new StaffHistoryController.Pet(text[i+1].trim(), text[i].trim()));
            }
        }
        return data;
    }


    public static class History {
        private final SimpleStringProperty doctor;
        private final SimpleStringProperty pet;
        private final SimpleStringProperty info;

        public History(String doctor, String pet, String info) {
            this.doctor = new SimpleStringProperty(doctor);
            this.pet = new SimpleStringProperty(pet);
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

    }


    public static class Pet {
        private final SimpleStringProperty owner;
        private final SimpleStringProperty petName;

        public Pet(String owner, String petName) {
            this.owner = new SimpleStringProperty(owner);
            this.petName = new SimpleStringProperty(petName);


        }

        public String getOwner() {
            return owner.get();
        }

        private SimpleStringProperty ownerProperty() {
            return owner;
        }

        public String getpetName() {
            return petName.get();
        }

        public SimpleStringProperty petNameProperty() {
            return petName;
        }

    }
}
