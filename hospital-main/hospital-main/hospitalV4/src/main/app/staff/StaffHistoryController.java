package main.app.staff;

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

    ConcreteClass concreteClass=new ConcreteClass();

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
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        App.openStaffAppointment();
        stage.close();
    }

    public void openStaffInfo(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        App.openStaffInfo();
        stage.close();
    }

    public void logOut(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        App.logOpen();
        stage.close();
    }


    public void addHist(TableView<Pet> petTable,ActionEvent event) throws SQLException {

        StaffHistoryController.Pet selectedPet = petTable.getSelectionModel().getSelectedItem();
        if (selectedPet != null && datePicker.getValue() != null && !infoField.getText().isEmpty()) {
            String owner = selectedPet.getOwner();
            String petName = selectedPet.getpetName();
        //    DataBase.addHistory(owner, petName, datePicker.getValue() + ":" + infoField.getText(), "staff");
        } else {
            concreteClass.showAlert("No row selected",event);
        }
        refreshPatientHistory();
    }


    public void deleteHistory(TableView<StaffHistoryController.History> tableView,ActionEvent event) {
        StaffHistoryController.History selectedHistory = tableView.getSelectionModel().getSelectedItem();
        if (selectedHistory != null) {
            String id = LogController.activeID;
            String pet = selectedHistory.getPet();
            String info = selectedHistory.getInfo();
            DataBase.deleteHistory(id, pet, info, "staff");
            refreshPatientHistory();
        } else {
            concreteClass.showAlert("No row selected",event);
        }

    }

    private void refreshPatientHistory() {
        LocalDate currentDate = LocalDate.now();
        infoField.clear();
        datePicker.setValue(currentDate);
        HistoryTable.setItems(getHistoryData());


    }

    private ObservableList<StaffHistoryController.History> getHistoryData() {
        HistoryTable.getItems().clear();
        ObservableList<StaffHistoryController.History> data = FXCollections.observableArrayList();
        String[] text = DataBase.getHistoryList(LogController.activeID).split(" ");
        if (!text[0].equals("")) {
            for (int i = 0; i < text.length; i += 3) {
                data.add(new StaffHistoryController.History(text[i], text[i + 1], text[i + 2]));
            }
        }
        return data;
    }


    private ObservableList<StaffHistoryController.Pet> getPetData() {
        HistoryTable.getItems().clear();
        ObservableList<StaffHistoryController.Pet> data = FXCollections.observableArrayList();
        String[] text = DataBase.getPetList("staff").split("\\$");
        if (!text[0].equals("")) {
            for (int i = 0; i < text.length; i += 2) {
                data.add(new StaffHistoryController.Pet(text[i], text[i + 1]));
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
