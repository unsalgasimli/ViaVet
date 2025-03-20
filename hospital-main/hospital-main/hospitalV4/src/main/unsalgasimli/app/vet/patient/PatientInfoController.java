package main.unsalgasimli.app.vet.patient;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.unsalgasimli.app.vet.core.App;
import main.unsalgasimli.app.vet.core.ConcreteClass;
import main.unsalgasimli.app.vet.core.Database;

import java.net.URL;
import java.util.ResourceBundle;


public class PatientInfoController implements Initializable {
    @FXML
    Button deleteBtn;
    @FXML
    TextField nameField;
    @FXML
    DatePicker datePicker;
    @FXML
    MenuButton petMenu;
    @FXML
    private TableView<PatientInfoController.Pet> petTable;
    @FXML
    private TableColumn<PatientInfoController.Pet, String> tableColumnOne;
    @FXML
    private TableColumn<PatientInfoController.Pet, String> tableColumnTwo;
    @FXML
    private TableColumn<PatientInfoController.Pet, String> tableColumnThree;

    ConcreteClass concreteClass=new ConcreteClass();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up TableView columns
        tableColumnOne.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        tableColumnTwo.setCellValueFactory(cellData -> cellData.getValue().birthProperty());
        tableColumnThree.setCellValueFactory(cellData -> cellData.getValue().typeProperty());

        // Populate TableView with pet data
        petTable.setItems(getPetData());

        // Set up delete button action
        deleteBtn.setOnAction(event -> deletePet(petTable, event));

        // Populate petMenu with pet types
        Database.populatePetMenuButton(petMenu);

    }


    public void openPatientAppointment(ActionEvent event) throws Exception {
        App.openPatientAppointment();

    }

    public void openPatientHistory(ActionEvent event) throws Exception {
        App.openPatientHistory();
    }
    public void logOut(ActionEvent event) throws Exception {
        App.openLogin();

    }

    public void refresh() {
        nameField.clear();
        datePicker.setValue(null);
        petMenu.setText("Pet");
        petTable.setItems(getPetData());
    }


    private ObservableList<PatientInfoController.Pet> getPetData() {
        petTable.getItems().clear();
        ObservableList<PatientInfoController.Pet> data = FXCollections.observableArrayList();

        String petListData = Database.getPetList("patient");
        String[] text = petListData.split("\n");  // Split by newline to separate each pet

        for (String petDetails : text) {
            // Skip empty lines
            if (petDetails.trim().isEmpty()) continue;


            try {
                String[] petInfo = petDetails.split(" ");

                // Ensure there are exactly 3 parts: name, age, and type
                if (petInfo.length == 3) {
                    String name = petInfo[0].trim();  // Remove leading/trailing spaces
                    String age = petInfo[1].trim();
                    String type = petInfo[2].trim();

                    // Add the pet to the data list
                    data.add(new PatientInfoController.Pet(name, age, type));
                } else {
                    System.err.println("Invalid pet data format: " + petDetails);
                }
            } catch (Exception e) {
                System.err.println("Error processing pet data: " + e);
            }
        }
        return data;
    }

    public void addPet() {
        if (!(nameField.getText().isEmpty() || datePicker.getValue() == null || petMenu.getText().equals("Pet"))) {
            Database.addPet(nameField.getText(), datePicker.getValue(), Database.getPetIdByType(petMenu.getText().trim()));
            refresh();
        }
    }



    public void deletePet(TableView<PatientInfoController.Pet> tableView,ActionEvent event) {
        PatientInfoController.Pet selectedPet = tableView.getSelectionModel().getSelectedItem();
        if (selectedPet != null) {
            String name = selectedPet.getName();
            String type = selectedPet.getType();
            Database.deletePet(name, type);
            petTable.setItems(getPetData());

        } else {
        concreteClass.showAlert("No row selected",event);
        }
    }


    public static class Pet {
        private final SimpleStringProperty name;
        private final SimpleStringProperty birth;
        private final SimpleStringProperty type;

        public Pet(String name, String birth, String type) {
            this.name = new SimpleStringProperty(name);
            this.birth = new SimpleStringProperty(birth);
            this.type = new SimpleStringProperty(type);
        }

        public String getName() {
            return name.get();
        }

        private SimpleStringProperty nameProperty() {
            return name;
        }

        public String getBirth() {
            return birth.get();
        }

        public SimpleStringProperty birthProperty() {
            return birth;
        }

        public String getType() {
            return type.get();
        }

        public SimpleStringProperty typeProperty() {
            return type;
        }
    }
}
