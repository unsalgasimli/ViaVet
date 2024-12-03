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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        tableColumnOne.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        tableColumnTwo.setCellValueFactory(cellData -> cellData.getValue().birthProperty());
        tableColumnThree.setCellValueFactory(cellData -> cellData.getValue().typeProperty());

        petTable.setItems(getPetData());
        deleteBtn.setOnAction(event -> deletePet(petTable,event));


        try (BufferedReader br = new BufferedReader(new FileReader("animals.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                MenuItem menuItem = new MenuItem(line);
                String finalLine = line;
                menuItem.setOnAction(e -> handleMenuItemClick(finalLine));
                petMenu.getItems().add(menuItem);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openPatientAppointment(ActionEvent event) throws Exception {
        App.openPatientAppointment();
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();

    }

    public void openPatientHistory(ActionEvent event) throws Exception {
        App.openPatienHistory();
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();

    }

    public void logOut(ActionEvent event) throws Exception {
        App.logOpen();
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();

    }

    private void handleMenuItemClick(String animal) {
        petMenu.setText(animal);
    }

    private ObservableList<PatientInfoController.Pet> getPetData() {
        petTable.getItems().clear();
        ObservableList<PatientInfoController.Pet> data = FXCollections.observableArrayList();
        String[] text = DataBase.getPetList("patient").split(" ");

        for (int i = 0; i < text.length; i += 3) {
            try {
                data.add(new PatientInfoController.Pet(text[i], text[i + 1], text[i + 2]));
            } catch (Exception e) {
                System.err.println(e);
            }
        }
        return data;
    }

    public void addPet() {
        if (!(nameField.getText().isEmpty() || datePicker.getValue() == null || petMenu.getText().equals("Pet"))) {
            DataBase.addPet(nameField.getText(), datePicker.getValue(), petMenu.getText());
            refresh();
        }
    }

    public void refresh() {
        nameField.clear();
        datePicker.setValue(null);
        petMenu.setText("Pet");
        petTable.setItems(getPetData());
    }

    public void deletePet(TableView<PatientInfoController.Pet> tableView,ActionEvent event) {
        PatientInfoController.Pet selectedPet = tableView.getSelectionModel().getSelectedItem();
        if (selectedPet != null) {
            String name = selectedPet.getName();
            String birth = selectedPet.getBirth();
            String type = selectedPet.getType();
            DataBase.deletePet(name, birth, type);
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
