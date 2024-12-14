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
import java.sql.Date;
import java.sql.SQLException;
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

    ConcreteClass concreteClass = new ConcreteClass();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableOne.setCellValueFactory(cellData -> cellData.getValue().doctorProperty());
        tableTwo.setCellValueFactory(cellData -> cellData.getValue().petProperty());
        tableThree.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        tableFour.setCellValueFactory(cellData -> cellData.getValue().infoProperty());

        HistoryTable.setItems(getHistoryData());
        delBtn.setOnAction(event -> deleteHistory(HistoryTable, event));

        String[] text = DataBase.getPetList("patient").split(" ");
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
        App.openPatientAppointment();
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();

    }

    public void openPatientInfo(ActionEvent event) throws Exception {
        App.openPatientInfo();
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();

    }

    public void logOut(ActionEvent event) throws Exception {
        App.openLogin();
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();

    }


    private void handleMenuItemClickForPet(String animal) {
        petMenu.setText(animal);
    }

    public void addHist() throws SQLException {
        String petId = DataBase.getPetId(petMenu.getText().trim());
        petId = petId.trim();
        int pet = Integer.parseInt(petId);

        DataBase.addHistory(LogController.activeID, pet, "owner", infoField.getText(), datePicker.getValue());
        refreshPatientHistory();
    }

    private void refreshPatientHistory() {
        infoField.clear();
        datePicker.setValue(null);
        petMenu.setText("Pet");
        HistoryTable.setItems(getHistoryData());

    }

    private ObservableList<PatientHistoryController.History> getHistoryData() {
        String docName;
        String petName;
        HistoryTable.getItems().clear();
        ObservableList<PatientHistoryController.History> data = FXCollections.observableArrayList();
        String[] text = DataBase.getHistoryList(LogController.activeID).split("\\|");
        if (!text[0].equals("")) {
            for (int i = 0; i < text.length - 1; i += 4) {

                petName = DataBase.getPetName(text[i].trim());
                if (text[i + 1].equals("owner")) {
                    docName = "owner";
                } else {
                    docName = DataBase.getStaffNS(text[i + 1]);
                }
                data.add(new PatientHistoryController.History(docName, petName, text[i + 3], text[i + 2]));

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
            DataBase.deleteHistory(name, pet, info, "patient");
            HistoryTable.setItems(getHistoryData());

        } else {
            concreteClass.showAlert("TextFields not filled", event);
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
