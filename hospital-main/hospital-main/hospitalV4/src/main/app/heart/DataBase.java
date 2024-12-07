package main.app.heart;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import main.app.logreg.LogController;

import java.security.SecureRandom;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

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

            // Set parameters
            preparedStatement.setString(1, generateUniqueString());
            preparedStatement.setString(2, first_name);
            preparedStatement.setString(3, last_name);
            preparedStatement.setString(4, middle_name);
            preparedStatement.setDate(5, Date.valueOf(date_of_birth));
            preparedStatement.setString(6, phone_number);
            preparedStatement.setString(7, password);

            // Execute update
            int rowsInserted = preparedStatement.executeUpdate();
            System.out.println("Rows inserted: " + rowsInserted);
        } catch (Exception e) {
            System.err.println("Error while adding staff record: " + e.getMessage());
        }

    }

    public static String checkAcc(String text, String text1, ActionEvent event) {
        return "patient";
    }


    public static String getAppointmentList(String activeID,boolean b) {

        StringBuilder result = new StringBuilder();
        try (Connection connection = dbManager.getConnection()) {
            String selectQuery = "SELECT * FROM patient_appointments_view WHERE patient_unique_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

            // Set parameters
            preparedStatement.setString(1, activeID);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                // Example of processing and appending data; customize this according to your needs
                String appointmentDetails = "Appointment ID: " + resultSet.getString("appointment_id") +
                        ", Date: " + resultSet.getString("appointment_date") +
                        ", Time: " + resultSet.getString("appointment_time");
                result.append(appointmentDetails).append("\n");
            }
        } catch (Exception e) {
            System.err.println("Error while retrieving appointment records: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
        return result.toString();

    }

    public static void addPet(String name, LocalDate age, String type) {

        try (Connection connection = dbManager.getConnection()) {
            String insertQuery = "INSERT INTO pet_list (owner_id, name, age, type) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            // Set parameters
            preparedStatement.setString(1, activeID);
            preparedStatement.setString(2, name);
            preparedStatement.setDate(3, Date.valueOf(age));
            preparedStatement.setString(4, type);

            // Execute update
            int rowsInserted = preparedStatement.executeUpdate();
            System.out.println("Rows inserted: " + rowsInserted);
        } catch (Exception e) {
            System.err.println("Error while adding staff record: " + e.getMessage());
        }

    }




    public static String getPetList(String patient) {
        StringBuilder result = new StringBuilder();

        try (Connection connection = dbManager.getConnection()) {
            // Query to select pet details
            String selectQuery = "SELECT name, age, type FROM pet_list WHERE owner_id = (?);";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

            // Set the parameter (patient unique_id)
            preparedStatement.setString(1, patient); // Assuming `patient` is the unique ID

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                String name = resultSet.getString("name").trim();  // Remove any leading/trailing spaces
                Date birthDate = resultSet.getDate("age");  // 'age' is a birthdate
                String type = resultSet.getString("type").trim();  // Remove any leading/trailing spaces

                // Calculate the age from birthDate
                LocalDate birthLocalDate = birthDate.toLocalDate();
                LocalDate currentDate = LocalDate.now();
                Period period = Period.between(birthLocalDate, currentDate);
                int ageInYears = period.getYears();

                // Append pet details with age in years, formatted with a separator
                String petDetails = name + "|" + birthDate + " years|" + type;
                result.append(petDetails).append("\n");  // Using newline to separate each pet's details
            }

        } catch (Exception e) {
            System.err.println("Error while retrieving pet records: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
        return result.toString();
    }







    public static void deletePet(String name, String type) {

        try (Connection connection = dbManager.getConnection()) {
            // SQL delete query
            String deleteQuery = "DELETE FROM pet_list WHERE name = ? AND type = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);

            // Set parameters for the query
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, type);

            // Print the SQL query with actual values in the desired format
            System.out.println("Executing query: DELETE FROM pet_list WHERE name = ('" + name + "') AND type = ('" + type + "')");

            // Execute the delete statement
            int rowDeleted = preparedStatement.executeUpdate();
            System.out.println("Rows Deleted: " + rowDeleted);

        } catch (Exception e) {
            // Print a more specific error message
            System.err.println("Error while deleting pet record: " + e.getMessage());
        }
    }


}

