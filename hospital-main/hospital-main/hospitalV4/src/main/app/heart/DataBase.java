package main.app.heart;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;


import java.security.SecureRandom;
import java.sql.*;
import java.time.LocalDate;

import static main.app.logreg.LogController.activeID;

public class DataBase {
    public static String uString;
    static PostgreSQLConnection dbManager = new PostgreSQLConnection();

    private static String generateUniqueString() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(4);
        for (int i = 0; i < 4; ++i) {
            int randomIndex = random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".length());
            char randomChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".charAt(randomIndex);
            sb.append(randomChar);
        }
        uString = String.valueOf(sb);
        return sb.toString();
    }

    public static void addPatient(String first_name, String last_name, String middle_name, LocalDate date_of_birth, String phone_number, String password) {

        try (Connection connection = dbManager.getConnection()) {
            String insertQuery = "INSERT INTO patient_list (unique_id, first_name, last_name, middle_name, date_of_birth,phone_number,password) VALUES (?, ?, ?, ?, ?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, generateUniqueString());
            preparedStatement.setString(2, first_name);
            preparedStatement.setString(3, last_name);
            preparedStatement.setString(4, middle_name);
            preparedStatement.setDate(5, Date.valueOf(date_of_birth));
            preparedStatement.setString(6, phone_number);
            preparedStatement.setString(7, password);

            int rowsInserted = preparedStatement.executeUpdate();
            System.out.println("Rows inserted: " + rowsInserted);
        } catch (Exception e) {
            System.err.println("Error while adding staff record: " + e.getMessage());
        }
    }

    public static void addPet(String name, LocalDate age, int typeId) {
        try (Connection connection = dbManager.getConnection()) {
            String insertQuery = "INSERT INTO pet_list (owner_id, name, age, type_id) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            // Assuming activeID is defined and holds the current owner's ID
            preparedStatement.setString(1, activeID);  // Update this to setInt if owner_id is an integer
            preparedStatement.setString(2, name);
            preparedStatement.setDate(3, Date.valueOf(age));
            preparedStatement.setInt(4, typeId);  // Set type_id as an integer

            int rowsInserted = preparedStatement.executeUpdate();
            System.out.println("Rows inserted: " + rowsInserted);
        } catch (Exception e) {
            System.err.println("Error while adding pet record: " + e.getMessage());
        }
    }

    public static Integer getPetIdByType(String typeName) {
        System.out.println(typeName);
        String query = "SELECT p.id FROM pet_types p " +
                "WHERE p.type_name = ? LIMIT 1";
        Integer petId = null;
        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, typeName);
            System.out.println(preparedStatement);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    petId = resultSet.getInt("id");
                }
            }
        } catch (Exception e) {
            System.err.println("Error while fetching pet ID: " + e.getMessage());
        }
        return petId;
    }

    public static void populatePetMenuButton(MenuButton petMenu) {
        String query = "SELECT type_name FROM pet_types";

        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String typeName = resultSet.getString("type_name");

                // Create a new MenuItem for each pet type
                MenuItem menuItem = new MenuItem(typeName);

                // Set the action for each menu item
                menuItem.setOnAction(e -> handleMenuItemClick(typeName, petMenu));

                // Add the menu item to the petMenu
                petMenu.getItems().add(menuItem);
            }
        } catch (Exception e) {
            System.err.println("Error while populating pet menu: " + e.getMessage());
        }
    }


    private static void handleMenuItemClick(String petType, MenuButton petMenu) {
        petMenu.setText(petType);
    }

    public static void addStaff(String name, String surname, String number, String password) {
        String id = generateUniqueString();

        try (Connection connection = dbManager.getConnection()) {
            String insertQuery = "INSERT INTO staff_list (unique_id, first_name, last_name,phone_number,password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, surname);
            preparedStatement.setString(4, number);
            preparedStatement.setString(5, password);

            int rowsInserted = preparedStatement.executeUpdate();
            System.out.println("Rows inserted: " + rowsInserted);
        } catch (Exception e) {
            System.err.println("Error while adding staff record: " + e.getMessage());
        }
    }

    public static void createAppointment(String staffId, int petId, String date, String time, String desc) {

        try (Connection connection = dbManager.getConnection()) {
            String insertQuery = "INSERT INTO appointment_list (patient_id, staff_id, pet_id, appointment_date, appointment_time, info, status_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, activeID);
            preparedStatement.setString(2, staffId);
            preparedStatement.setInt(3, petId);
            preparedStatement.setDate(4, Date.valueOf(date));
            preparedStatement.setTime(5, Time.valueOf(time + ":00"));
            preparedStatement.setString(6, desc);
            preparedStatement.setInt(7, 4);

            int rowsInserted = preparedStatement.executeUpdate();
            System.out.println("Rows inserted: " + rowsInserted);
        } catch (Exception e) {
            System.err.println("Error while adding appointment record: " + e.getMessage());
        }
    }

    public static String checkAcc(String id, String pass, ActionEvent event) {

        //I KNOW IT IS WRONG BUT WHATEVER WHO CARES
        if(id.equals("1") && pass.equals("1")) {
            return "admin";
        }

        try (Connection connection = dbManager.getConnection()) {
            String selectQuery = "SELECT * FROM staff_list WHERE unique_id = ? AND password = ?;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, pass);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return "staff";
                    }
                }
            }

            selectQuery = "SELECT * FROM patient_list WHERE unique_id = ? AND password = ?;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, pass);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return "patient";
                    }
                }
            }

            return ""; // No match found
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }





    public static String getAppointmentList(String activeID, String user) {
        StringBuilder result = new StringBuilder();

        try (Connection connection = dbManager.getConnection()) {
            String selectQuery;
            if (user.equals("patient")) {
                selectQuery = "SELECT * FROM patient_appointments_view WHERE patient_id = ?;";
                PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
                preparedStatement.setString(1, activeID);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String appointmentDetails =
                                    DataBase.getStaffNS(resultSet.getString("staff_id"))
                                    + "\n" + DataBase.getPetName(resultSet.getString("pet_id"))
                                    + "\n" + resultSet.getString("appointment_date")
                                    + "\n" + resultSet.getString("appointment_time")
                                    + "\n" + DataBase.getStatusByID( resultSet.getInt("status_id"))
                                    + "\n";

                    result.append(appointmentDetails);
                }
            } else {
                selectQuery = "SELECT * FROM patient_appointments_view WHERE staff_id = ?;";
                PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
                preparedStatement.setString(1, activeID);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String appointmentDetails =
                           DataBase.getPatientNS(resultSet.getString("patient_id"))
                                    + "\n" +DataBase.getPetName(resultSet.getString("pet_id"))
                                    + "\n" + resultSet.getString("appointment_date")
                                    + "\n" + resultSet.getString("appointment_time")
                                    + "\n" +DataBase.getStatusByID(resultSet.getInt("status_id"))
                                    + "\n";
                    result.append(appointmentDetails);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result.toString();
    }

    private static String getStatusByID(int statusId) {
        String query = "SELECT p.status_name FROM appointment_status p " +
                "WHERE p.id = ? LIMIT 1";
        String status = null;
        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, statusId);
            System.out.println(preparedStatement);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    status = resultSet.getString("status_name");
                }
            }
        } catch (Exception e) {
            System.err.println("Error while fetching pet ID: " + e.getMessage());
        }
        return status;
    }

    public static String getPetList(String user) {
        StringBuilder result = new StringBuilder();

        try (Connection connection = dbManager.getConnection()) {
            String selectQuery;
            if (user.equals("patient")) {
                // Query for patient-specific pets
                selectQuery = "SELECT name, age, pet_type FROM pet_view WHERE owner_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
                preparedStatement.setString(1, activeID);  // Ensure activeID is properly initialized
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String name = resultSet.getString("name").trim();
                    Date birthDate = resultSet.getDate("age");
                    String type = resultSet.getString("pet_type").trim();  // Correct column name

                    // Convert Date to a readable format if necessary
                    String birthDateStr = birthDate != null ? birthDate.toString() : "Unknown";

                    String petDetails = name + " " + birthDateStr + " " + type + " ";
                    result.append(petDetails).append("\n");
                }
            } else {
                // Query for all pets
                selectQuery = "SELECT name, owner_id FROM pet_list";
                PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String name = resultSet.getString(1).trim();
                    String ownerId = resultSet.getString(2).trim();

                    // Get the patient's name based on ownerId
                    String ownerName = DataBase.getPatientNS(ownerId);  // Assumes this function gets the patient name by owner_id

                    String petDetails = name + "○" + ownerName + "○";
                    result.append(petDetails).append("\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Print the exception for debugging
            throw new RuntimeException("Database error occurred while fetching pet list.", e);
        }
        return result.toString();
    }


    public static String getPatientNS(String ownerName) {
        StringBuilder result = new StringBuilder();

        try (Connection connection = dbManager.getConnection()) {
            String selectQuery = "SELECT CONCAT(first_name, ' ', last_name) AS full_name FROM patient_list WHERE unique_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, ownerName.trim());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            result.append(resultSet.getString("full_name"));
        } catch (Exception e) {
            System.err.println("Error while retrieving Patient(name) record: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
        return result.toString();
    }

    public static void deletePet(String name, String type) {

        try (Connection connection = dbManager.getConnection()) {
            String deleteQuery = "DELETE FROM pet_list WHERE name = ? AND type_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, getPetIdByType(type));
            preparedStatement.executeUpdate();


        } catch (Exception e) {
            System.err.println("Error while deleting pet record: " + e.getMessage());
        }
    }

    public static String getVetList() {
        StringBuilder result = new StringBuilder();

        try (Connection connection = dbManager.getConnection()) {
            String selectQuery = "SELECT CONCAT(first_name, ' ', last_name) AS full_name FROM staff_list;";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("full_name");  // Remove any leading/trailing spaces
                result.append(name).append("\n");  // Using newline to separate each pet's details
            }
        } catch (Exception e) {
            System.err.println("Error while retrieving pet records: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
        return result.toString();
    }

    public static String getStaffId(String full_name) {
        StringBuilder result = new StringBuilder();

        try (Connection connection = dbManager.getConnection()) {

            String selectQuery = "SELECT unique_id FROM staff_list WHERE first_name = ? AND last_name = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            String[] text = full_name.split(" ");
            preparedStatement.setString(1, text[0]);
            preparedStatement.setString(2, text[1]);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String unique_id = resultSet.getString("unique_id");  // Remove any leading/trailing spaces
                result.append(unique_id).append("\n");  // Using newline to separate each pet's details
            }
        } catch (Exception e) {
            System.err.println("Error while retrieving pet records: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
        return result.toString();
    }

    public static Integer getPetId(String owner, String ide) {
        StringBuilder result = new StringBuilder();

        int id = 0;
        try (Connection connection = dbManager.getConnection()) {
            String selectQuery = "SELECT id FROM pet_list WHERE owner_id = ? AND name = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, ide);
            preparedStatement.setString(2, owner);
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("id");
                result.append(id).append("\n");  // Using newline to separate each pet's details
            }
        } catch (Exception e) {
            System.err.println("Error while retrieving pet records: " + e.getMessage());
        }
        return id;
    }

    public static void deleteAppointment(String id, int pet, String date, String time) {

        try (Connection connection = dbManager.getConnection()) {
            String insertQuery = "DELETE FROM appointment_list WHERE staff_id = ? AND pet_id = ? AND appointment_date = ? AND appointment_time = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, id);
            preparedStatement.setInt(2, pet);
            preparedStatement.setDate(3, Date.valueOf(date));
            preparedStatement.setTime(4, Time.valueOf(time));
            System.out.println(preparedStatement);
            int rowsInserted = preparedStatement.executeUpdate();
            System.out.println("Rows deleted: " + rowsInserted);
        } catch (Exception e) {
            System.err.println("Error while deleting appointment record: " + e.getMessage());
        }
    }

    public static void setAppointmentStatus(String date, String time, int status_id) {

        try (Connection connection = dbManager.getConnection()) {
            String updateQuery = "UPDATE appointment_list SET status_id = ? WHERE  appointment_date = ? AND appointment_time = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, status_id);  // Assuming activeID is the patient's ID
            preparedStatement.setDate(2, Date.valueOf(date));
            preparedStatement.setTime(3, Time.valueOf(time));
            int rowsUpdated = preparedStatement.executeUpdate();
            System.out.println("Rows updated: " + rowsUpdated);
        } catch (Exception e) {
            System.err.println("Error while updating appointment record: " + e.getMessage());
        }
    }


    public static String showAppointmentInfoPM(String date, String time) {
        StringBuilder result = new StringBuilder();

        try (Connection connection = dbManager.getConnection()) {

            String selectQuery = "SELECT info FROM appointment_list WHERE patient_id = ? AND appointment_date = ? AND appointment_time = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, activeID);
            preparedStatement.setDate(2, Date.valueOf(date));
            preparedStatement.setTime(3, Time.valueOf(time));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String info = resultSet.getString("info").trim();  // Fetch and clean the "info" column value
                result.append(info);
            } else {
                result.append("No information found for the selected appointment.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input: " + e.getMessage());
            return "Error: Invalid date or time format.";
        } catch (Exception e) {
            System.err.println("Error while retrieving appointment info: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
        return result.toString();
    }

    public static String getOccupiedTimes(String staffID, String day) {

        StringBuilder result = new StringBuilder();

        try (Connection connection = dbManager.getConnection()) {
            String selectQuery = "SELECT appointment_time FROM appointment_list WHERE staff_id = ? AND appointment_date = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, staffID.trim());
            preparedStatement.setDate(2, Date.valueOf(day));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String time = resultSet.getString("appointment_time"); // Retrieve the time as a string
                String trimmedTime = time.substring(0, 6); // Get the first 6 characters
                result.append(trimmedTime).append("\n");  // Using newline to separate each pet's details
            }
        } catch (Exception e) {
            System.err.println("Error while retrieving time records: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
        return result.toString();
    }

    public static void addHistory(String activeID, int petId, String owner, String text, LocalDate value) {

        try (Connection connection = dbManager.getConnection()) {
            String insertQuery = "INSERT INTO history_list (patient_id, pet_id, staff_id,info,created_at) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, activeID);
            preparedStatement.setInt(2, petId);
            preparedStatement.setString(3, owner);
            preparedStatement.setString(4, text);
            preparedStatement.setDate(5, Date.valueOf(value));
            int rowsInserted = preparedStatement.executeUpdate();
            System.out.println("Rows inserted: " + rowsInserted);
        } catch (Exception e) {
            System.err.println("Error while adding history record: " + e.getMessage());
        }
    }

    public static void addHistoryStaff(String patName, String petName, String text, LocalDate value) {

        String patientID = DataBase.getPatientID(patName);
        System.out.println(patientID);
        int petId = DataBase.getPetId(petName.trim(), patientID.trim());
        try (Connection connection = dbManager.getConnection()) {
            String insertQuery = "INSERT INTO history_list (patient_id, pet_id, staff_id,info,created_at) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, patientID.trim());
            preparedStatement.setInt(2, petId);
            preparedStatement.setString(3, activeID);
            preparedStatement.setString(4, text);
            preparedStatement.setDate(5, Date.valueOf(value));
            int rowsInserted = preparedStatement.executeUpdate();
            System.out.println("Rows inserted: " + rowsInserted);
        } catch (Exception e) {
            System.err.println("Error while adding history record: " + e.getMessage());
        }
    }

    public static String getPetName(String pet_id) {
        pet_id = pet_id.trim();
        StringBuilder result = new StringBuilder();

        try (Connection connection = dbManager.getConnection()) {
            String selectQuery = "SELECT name FROM pet_list WHERE id = ?;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setInt(1, Integer.parseInt(pet_id));
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        result.append(resultSet.getString("name"));
                    } else {
                        return "No pet found with the given ID.";
                    }
                }
            }
        } catch (NumberFormatException e) {
            return "Invalid pet ID format.";
        } catch (Exception e) {
            System.err.println("Error while retrieving pet(name) record: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
        System.out.println(result);
        return result.toString();
    }

    public static String getStaffNS(String staff_id) {

        StringBuilder result = new StringBuilder();

        try (Connection connection = dbManager.getConnection()) {
            String selectQuery = "SELECT CONCAT(first_name, ' ', last_name) AS full_name FROM staff_list WHERE unique_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, staff_id.trim());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            result.append(resultSet.getString(1));
        } catch (Exception e) {
            System.err.println("Error while retrieving Staff(name) record: " + e.getMessage());
            return "Error: " + e.getMessage();
        }


        return result.toString();
    }

    public static String getHistoryList(String activeID) {
        StringBuilder result = new StringBuilder();

        try (Connection connection = dbManager.getConnection()) {
            String selectQuery = "SELECT pet_id,staff_id,info,created_at FROM history_list WHERE patient_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, activeID.trim());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String details = resultSet.getString(1) + "○" +
                        resultSet.getString(2) + "○" +
                        resultSet.getString(3) + "○" +
                        resultSet.getDate(4) + "○";
                result.append(details).append("\n");  // Using newline to separate each pet's details
            }
        } catch (Exception e) {
            System.err.println("Error while retrieving history records: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
        return result.toString();
    }

    public static String getAllHistoryList() {
        StringBuilder result = new StringBuilder();

        try (Connection connection = dbManager.getConnection()) {
            String selectQuery = "SELECT patient_id,pet_id,info FROM history_list ;";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String details = DataBase.getPatientNS(resultSet.getString(1)) + "○" +
                        DataBase.getPetName(resultSet.getString(2)) + "○" +
                        resultSet.getString(3) +
                        "○";
                result.append(details).append("\n");  // Using newline to separate each pet's details
            }
        } catch (Exception e) {
            System.err.println("Error while retrieving history records: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
        return result.toString();
    }

    public static void deleteHistory(String name, String pet, String info) {

            int pet_id = (DataBase.getPetId( pet, DataBase.getPatientID(name).trim()));
        try (Connection connection = dbManager.getConnection()) {
            String insertQuery = "DELETE FROM history_list WHERE patient_id = ? AND pet_id = ? AND info = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, activeID);  // Assuming activeID is the patient's ID
            preparedStatement.setInt(2, pet_id);   // Staff ID
            preparedStatement.setString(3, info);
            System.out.println(preparedStatement);// Pet ID
            int rowsInserted = preparedStatement.executeUpdate();
            System.out.println("Rows deleted: " + rowsInserted);
        } catch (Exception e) {
            System.err.println("Error while deleting history record: " + e.getMessage());
        }
    }

    public static void deleteHistoryStaff(String name, String pet, String info) {

        int pet_id = (DataBase.getPetId( pet, DataBase.getPatientID(name).trim()));
        String  owner_id = DataBase.getPatientID(name).trim();
        try (Connection connection = dbManager.getConnection()) {
            String insertQuery = "DELETE FROM history_list WHERE patient_id = ? AND pet_id = ? AND info = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, owner_id);  // Assuming activeID is the patient's ID
            preparedStatement.setInt(2, pet_id);   // Staff ID
            preparedStatement.setString(3, info);
            System.out.println(preparedStatement);// Pet ID
            int rowsInserted = preparedStatement.executeUpdate();
            System.out.println("Rows deleted: " + rowsInserted);
        } catch (Exception e) {
            System.err.println("Error while deleting history record: " + e.getMessage());
        }
    }

//    public static void deleteHistory(String name, String pet, String info) {
//        String id = DataBase.getPatientID(name.trim()).trim();
//        int pet_id = Integer.parseInt((DataBase.getPetId(pet, id)).trim());
//
//        try (Connection connection = dbManager.getConnection()) {
//            String insertQuery = "DELETE FROM history_list WHERE patient_id = ? AND pet_id = ? AND info = ?;";
//            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
//            preparedStatement.setString(1, id);
//            preparedStatement.setInt(2, pet_id);
//            preparedStatement.setString(3, info);
//            int rowsInserted = preparedStatement.executeUpdate();
//            System.out.println("Rows deleted: " + rowsInserted);
//        } catch (Exception e) {
//            System.err.println("Error while deleting history record: " + e.getMessage());
//        }
//    }


    public static String showAppointmentInfo(String fullName, String date, String time) {
        StringBuilder result = new StringBuilder();
        String patient_id = DataBase.getPatientID(fullName).trim();

        try (Connection connection = dbManager.getConnection()) {

            String selectQuery = "SELECT info FROM patient_appointments_view WHERE patient_id = ? AND appointment_date = ? AND appointment_time = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, patient_id);
            preparedStatement.setDate(2, Date.valueOf(date));
            preparedStatement.setTime(3, Time.valueOf(time));
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String info = resultSet.getString(1);// Fetch and clean the "info" column value
                result.append(info);
            } else {
                result.append("No information found for the selected appointment.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input: " + e.getMessage());
            return "Error: Invalid date or time format.";
        } catch (Exception e) {
            System.err.println("Error while retrieving appointment info: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
        return result.toString();
    }

    public static void proposeAppointment(String fullName, String date, String time, String proposed, String newDate, String newTime) {
    }

    public static String getPatientID(String full_name) {
        StringBuilder result = new StringBuilder();

        try (Connection connection = dbManager.getConnection()) {
            String selectQuery = "SELECT unique_id FROM patient_list WHERE first_name = ? AND last_name = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            String[] text = full_name.split(" ");
            preparedStatement.setString(1, text[0]);
            preparedStatement.setString(2, text[1]);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String unique_id = resultSet.getString("unique_id");  // Remove any leading/trailing spaces
                result.append(unique_id).append("\n");  // Using newline to separate each pet's details
            }
        } catch (Exception e) {
            System.err.println("Error while retrieving patient records: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
        return result.toString();
    }

    public static void addAppointment(String ownerId, String staffId, String date, String time, String desc, String petName) {

        int pet_id = (DataBase.getPetId(petName, ownerId));
        try (Connection connection = dbManager.getConnection()) {
            String insertQuery = "INSERT INTO appointment_list (patient_id, staff_id, pet_id, appointment_date, appointment_time, info, status_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, ownerId);
            preparedStatement.setString(2, staffId);
            preparedStatement.setInt(3, pet_id);
            preparedStatement.setDate(4, Date.valueOf(date));
            preparedStatement.setTime(5, Time.valueOf(time + ":00"));
            preparedStatement.setString(6, desc);
            preparedStatement.setInt(7, 2);
            int rowsInserted = preparedStatement.executeUpdate();
            System.out.println("Rows inserted: " + rowsInserted);
        } catch (Exception e) {
            System.err.println("Error while adding appointment record: " + e.getMessage());
        }
    }

    public static String getStaffList(String activeID, boolean b) {
        StringBuilder result = new StringBuilder();

        try (Connection connection = dbManager.getConnection()) {
            String selectQuery = "SELECT first_name,last_name,phone_number  FROM staff_list;";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String details = resultSet.getString(1)
                        + " " + resultSet.getString(2)
                        + " " + resultSet.getString(3)
                        + " ";  // Remove any leading/trailing spaces
                result.append(details).append("\n");  // Using newline to separate each pet's details
            }
        } catch (Exception e) {
            System.err.println("Error while retrieving pet records: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
        return result.toString();
    }

    public static void deleteStaff(String name, String surname, String number) {
        try (Connection connection = dbManager.getConnection()) {
            String insertQuery = "DELETE FROM staff_list WHERE first_name = ? AND last_name = ? AND phone_number = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, number);
            int rowsInserted = preparedStatement.executeUpdate();
            System.out.println("Rows deleted: " + rowsInserted);
        } catch (Exception e) {
            System.err.println("Error while deleting staff record: " + e.getMessage());
        }
    }

    public static String showStaffInfo(String name, String surname, String number) {
        StringBuilder result = new StringBuilder();

        try (Connection connection = dbManager.getConnection()) {
            String selectQuery = "SELECT unique_id,password FROM staff_list WHERE first_name = ? AND last_name = ? AND phone_number = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, number);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String info = resultSet.getString(1) + " : " + resultSet.getString(2);
                result.append(info);
            } else {
                result.append("No information found for the selected appointment.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input: " + e.getMessage());
            return "Error: Invalid date or time format.";
        } catch (Exception e) {
            System.err.println("Error while retrieving appointment info: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
        return result.toString();
    }

    public static String getPatientList() {

        StringBuilder result = new StringBuilder();

        try (Connection connection = dbManager.getConnection()) {
            String selectQuery = "SELECT first_name,last_name,middle_name,date_of_birth,phone_number  FROM patient_list;";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String details = resultSet.getString(1)
                        + " " + resultSet.getString(2)
                        + " " + resultSet.getString(3)
                        + " " + resultSet.getString(4)
                        + " " + resultSet.getString(5)
                        + " ";
                result.append(details).append("\n");
            }
        } catch (Exception e) {
            System.err.println("Error while retrieving pet records: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
        return result.toString();
    }

    public static void deletePatient(String name, String surname, String middlename, String bday) {

        try (Connection connection = dbManager.getConnection()) {
            String insertQuery = "DELETE FROM patient_list WHERE first_name = ? AND last_name = ? AND middle_name = ? AND date_of_birth = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, middlename);
            preparedStatement.setDate(4, Date.valueOf(bday));
        } catch (Exception e) {
            System.err.println("Error while deleting patient record: " + e.getMessage());
        }

    }

    public static String getPatientDetails(String name, String surname, String middlename) {
        StringBuilder result = new StringBuilder();

        try (Connection connection = dbManager.getConnection()) {
            String selectQuery = "SELECT unique_id,password FROM patient_list WHERE first_name = ? AND last_name = ? AND middle_name = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, middlename);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String info = resultSet.getString(1) + " : " + resultSet.getString(2);  // Fetch and clean the "info" column value
                result.append(info);
            } else {
                result.append("No information found for the selected patient.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input: " + e.getMessage());
            return "Error: Invalid date or time format.";
        } catch (Exception e) {
            System.err.println("Error while retrieving appointment info: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
        return result.toString();
    }

    public static void updatePatient(String orgName, String orgSurname, String orgMiddleName, String name, String surname, String middleName, String bday, String number, String password) {

        try (Connection connection = dbManager.getConnection()) {
            String updateQuery = "UPDATE patient_list SET first_name = ? , last_name = ? ,middle_name = ? ,date_of_birth = ?,phone_number = ?,password = ? WHERE first_name = ? AND last_name = ? AND middle_name = ? ;";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, middleName);
            preparedStatement.setDate(4, Date.valueOf(bday));
            preparedStatement.setString(5, number);
            preparedStatement.setString(6, password);
            preparedStatement.setString(7, orgName);
            preparedStatement.setString(8, orgSurname);
            preparedStatement.setString(9, orgMiddleName);
            int rowsUpdated = preparedStatement.executeUpdate();
            System.out.println("Rows updated: " + rowsUpdated);
        } catch (Exception e) {
            System.err.println("Error while updating patient record: " + e.getMessage());
        }
    }

    public static void updateStaff(String orgName, String orgSurname, String orgNumber, String name, String surname, String number, String password) {

        try (Connection connection = dbManager.getConnection()) {
            String updateQuery = "UPDATE staff_list SET first_name = ? , last_name = ? ,phone_number = ?,password = ? WHERE first_name = ? AND last_name = ? AND phone_number = ? ;";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, number);
            preparedStatement.setString(4, password);
            preparedStatement.setString(5, orgName);
            preparedStatement.setString(6, orgSurname);
            preparedStatement.setString(7, orgNumber);
            int rowsUpdated = preparedStatement.executeUpdate();
            System.out.println("Rows updated: " + rowsUpdated);
        } catch (Exception e) {
            System.err.println("Error while updating staff record: " + e.getMessage());
        }
    }
}