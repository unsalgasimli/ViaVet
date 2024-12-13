package main.app.heart;

import javafx.event.ActionEvent;

import java.security.SecureRandom;
import java.sql.*;
import java.time.LocalDate;

import static main.app.heart.App.openPatienHistory;
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
        return "admin";
    }


    public static String getAppointmentList(String activeID, boolean b) {

        StringBuilder result = new StringBuilder();
        try (Connection connection = dbManager.getConnection()) {
            String selectQuery = "SELECT * FROM patient_appointments_view WHERE patient_unique_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            // Set parameters
            preparedStatement.setString(1, activeID);
            System.out.println(preparedStatement);


            // Execute the query

            ResultSet resultSet = preparedStatement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                // Example of processing and appending data; customize this according to your needs
                String appointmentDetails =
                        resultSet.getString("staff_name")
                                + "\n" + resultSet.getString("pet_name")
                                + "\n" + resultSet.getString("appointment_date")
                                + "\n" + resultSet.getString("appointment_time")
                                + "\n" + resultSet.getString("status")
                                + "\n";

                result.append(appointmentDetails);
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
            preparedStatement.setString(1, activeID); // Assuming `patient` is the unique ID
            System.out.println(preparedStatement);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                String name = resultSet.getString("name").trim();  // Remove any leading/trailing spaces
                Date birthDate = resultSet.getDate("age");  // 'age' is a birthdate
                String type = resultSet.getString("type").trim();  // Remove any leading/trailing spaces


                // Append pet details with age in years, formatted with a separator
                String petDetails = name + " " + birthDate + " " + type + " ";
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


    public static String getVetList() {
        StringBuilder result = new StringBuilder();

        try (Connection connection = dbManager.getConnection()) {
            // Query to select pet details
            String selectQuery = "SELECT CONCAT(first_name, ' ', last_name) AS full_name FROM staff_list;";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

            // Set the parameter (patient unique_id)

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process the result set
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
            // Query to select pet details
            String selectQuery = "SELECT unique_id FROM staff_list WHERE first_name = ? AND last_name = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            String[] text = full_name.split(" ");
            preparedStatement.setString(1, text[0]);
            preparedStatement.setString(2, text[1]);

            // Set the parameter (patient unique_id)

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process the result set
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


    public static String getPetId(String full_name) {
        StringBuilder result = new StringBuilder();

        try (Connection connection = dbManager.getConnection()) {
            // Query to select pet details
            String selectQuery = "SELECT id FROM pet_list WHERE owner_id = ? AND name = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

            preparedStatement.setString(1, activeID);
            preparedStatement.setString(2, full_name);

            // Set the parameter (patient unique_id)

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                String id = resultSet.getString("id");  // Remove any leading/trailing spaces
                result.append(id).append("\n");  // Using newline to separate each pet's details
            }

        } catch (Exception e) {
            System.err.println("Error while retrieving pet records: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
        return result.toString();
    }


    public static void createAppointment(String staffId, int petId, String date, String time, String desc) {

        try (Connection connection = dbManager.getConnection()) {
            // Query to insert the appointment record
            String insertQuery = "INSERT INTO appointment_list (patient_id, staff_id, pet_id, appointment_date, appointment_time, info, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            // Set parameters
            preparedStatement.setString(1, activeID);  // Assuming activeID is the patient's ID
            preparedStatement.setString(2, staffId);   // Staff ID
            preparedStatement.setInt(3, petId);     // Pet ID

            // Set appointment date (assuming it's in the format 'YYYY-MM-DD')
            preparedStatement.setDate(4, Date.valueOf(date));  // Set the date correctly

            // Set appointment time (assuming it's in the format 'HH:MM')
            preparedStatement.setTime(5, Time.valueOf(time + ":00"));  // Append ":00" to make it a valid time format (HH:MM:SS)

            preparedStatement.setString(6, desc);        // Description
            preparedStatement.setString(7, "Scheduled"); // Status (assuming it's 'Requested' by default)

            // Execute the update
            int rowsInserted = preparedStatement.executeUpdate();
            System.out.println("Rows inserted: " + rowsInserted);
        } catch (Exception e) {
            System.err.println("Error while adding appointment record: " + e.getMessage());
        }
    }

    public static void deleteAppointment(String id, int pet, String date) {

        try (Connection connection = dbManager.getConnection()) {
            // Query to insert the appointment record
            String insertQuery = "DELETE FROM appointment_list WHERE staff_id = ? AND pet_id = ? AND appointment_date = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            // Set parameters
            preparedStatement.setString(1, id);  // Assuming activeID is the patient's ID
            preparedStatement.setInt(2, pet);   // Staff ID
            preparedStatement.setDate(3, Date.valueOf(date));    // Pet ID


            // Execute the update
            int rowsInserted = preparedStatement.executeUpdate();
            System.out.println("Rows deleted: " + rowsInserted);
        } catch (Exception e) {
            System.err.println("Error while deleting appointment record: " + e.getMessage());
        }

    }

    public static void setAppointmentStatus(String date, String time, String status, String event) {

        try (Connection connection = dbManager.getConnection()) {
            // Query to update the appointment record
            String updateQuery = "UPDATE appointment_list SET status = ? WHERE appointment_date = ? AND appointment_time = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);


            // Set parameters
            preparedStatement.setString(1, status);  // Assuming activeID is the patient's ID
            preparedStatement.setDate(2, Date.valueOf(date));
            preparedStatement.setTime(3, Time.valueOf(time));



            // Execute the update
            int rowsUpdated = preparedStatement.executeUpdate();
            System.out.println("Rows updated: " + rowsUpdated);
        } catch (Exception e) {
            System.err.println("Error while updating appointment record: " + e.getMessage());
        }

    }

    public static String showAppointmentInfoPM(String date, String time) {
        StringBuilder result = new StringBuilder();

        try (Connection connection = dbManager.getConnection()) {
            // SQL query to fetch the appointment info
            String selectQuery = "SELECT info FROM appointment_list WHERE patient_id = ? AND appointment_date = ? AND appointment_time = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

            preparedStatement.setString(1, activeID);  // Use the active patient's ID
            preparedStatement.setDate(2, Date.valueOf(date));  // Convert date to SQL Date format
            preparedStatement.setTime(3, Time.valueOf(time));  // Convert time to SQL Time format


            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process the result set
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
            // Query to select pet details
            String selectQuery = "SELECT appointment_time FROM appointment_list WHERE staff_id = ? AND appointment_date = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

            preparedStatement.setString(1, staffID.trim());
            preparedStatement.setDate(2, Date.valueOf(day));


            // Set the parameter (patient unique_id)

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process the result set
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
            // Query to insert the history record
            String insertQuery = "INSERT INTO history_list (patient_id, pet_id, staff_id,info,created_at) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            // Set parameters
            preparedStatement.setString(1, activeID);  // Assuming activeID is the patient's ID
            preparedStatement.setInt(2, petId);   // Staff ID
            preparedStatement.setString(3, owner);     // Pet ID
            preparedStatement.setString(4, text);
            preparedStatement.setDate(5, Date.valueOf(value));  // Set the date correctly
            // Set appointment date (assuming it's in the format 'YYYY-MM-DD')




            // Execute the update
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
            // Query to select pet details
            String selectQuery = "SELECT name FROM pet_list WHERE id = ?;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setInt(1, Integer.parseInt(pet_id));

                // Execute the query
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Fetch the pet name
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

        return result.toString();
    }

    public static String getStaffNS(String staff_id) {

        StringBuilder result = new StringBuilder();

        try (Connection connection = dbManager.getConnection()) {
            // Query to select staff details
            String selectQuery = "SELECT CONCAT(first_name, ' ', last_name) AS full_name FROM staff_list WHERE unique_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, staff_id.trim());

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

        } catch (Exception e) {
            System.err.println("Error while retrieving Staff(name) record: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
        return result.toString();


    }

    public static String getHistoryList(String activeID) {

        StringBuilder result = new StringBuilder();

        try (Connection connection = dbManager.getConnection()) {
            // Query to select history details
            String selectQuery = "SELECT pet_id,staff_id,info,created_at FROM history_list WHERE patient_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, activeID.trim());

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                String details = resultSet.getString(1)+"|"+
                              resultSet.getString(2)+"|"+
                              resultSet.getString(3)+"|"+
                              resultSet.getDate(4)+ "|";
                result.append(details).append("\n");  // Using newline to separate each pet's details
            }

        } catch (Exception e) {
            System.err.println("Error while retrieving history records: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
        return result.toString();
    }

    public static void deleteHistory(String name, String pet, String info, String patient) {

        if(!name.equals("owner")){
            name = DataBase.getStaffId(name);
        }
        int pet_id = Integer.parseInt((DataBase.getPetId(pet)).trim());


        try (Connection connection = dbManager.getConnection()) {
            // Query to insert the appointment record
            String insertQuery = "DELETE FROM history_list WHERE patient_id = ? AND pet_id = ? AND info = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            // Set parameters
            preparedStatement.setString(1, activeID);  // Assuming activeID is the patient's ID
            preparedStatement.setInt(2, pet_id);   // Staff ID
            preparedStatement.setString(3, info);    // Pet ID



            // Execute the update
            int rowsInserted = preparedStatement.executeUpdate();
            System.out.println("Rows deleted: " + rowsInserted);
        } catch (Exception e) {
            System.err.println("Error while deleting history record: " + e.getMessage());
        }

    }

    public static String showAppointmentInfo(String fullName, String date, String time) {
        return "salam";
    }

    public static void proposeAppointment(String fullName, String date, String time, String proposed, String newDate, String newTime) {
    }

    public static String getPatientID(String s, String s1, String s2) {
        return "salam";
    }

    public static void addAppointment(String ownerID, String staffID, LocalDate date, String time, String info, String proposed, String petName) {
    }

    public static String getStaffList(String activeID, boolean b) {

        StringBuilder result = new StringBuilder();

        try (Connection connection = dbManager.getConnection()) {
            // Query to select pet details
            String selectQuery = "SELECT first_name,last_name,phone_number  FROM staff_list;";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

            // Set the parameter (patient unique_id)

            // Execute the query
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                String details = resultSet.getString(1)
                        +" " + resultSet.getString(2)
                        +" " + resultSet.getString(3)
                        +" ";  // Remove any leading/trailing spaces
                result.append(details).append("\n");  // Using newline to separate each pet's details
            }


        } catch (Exception e) {
            System.err.println("Error while retrieving pet records: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
        System.out.println(result);
        return result.toString();
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

    public static void deleteStaff(String name, String surname, String number) {


        try (Connection connection = dbManager.getConnection()) {
            // Query to insert the appointment record
            String insertQuery = "DELETE FROM staff_list WHERE first_name = ? AND last_name = ? AND phone_number = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            // Set parameters
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
            // SQL query to fetch the appointment info
            String selectQuery = "SELECT unique_id,password FROM staff_list WHERE first_name = ? AND last_name = ? AND phone_number = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

            preparedStatement.setString(1, name);  // Use the active patient's ID
            preparedStatement.setString(2, surname);  // Convert date to SQL Date format
            preparedStatement.setString(3, number);  // Convert time to SQL Time format


            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println(preparedStatement);

            // Process the result set
            if (resultSet.next()) {
                String info = resultSet.getString(1)+" : "+ resultSet.getString(2);  // Fetch and clean the "info" column value
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
            // Query to select pet details
            String selectQuery = "SELECT first_name,last_name,middle_name,date_of_birth,phone_number  FROM patient_list;";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

            // Set the parameter (patient unique_id)

            // Execute the query
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                String details = resultSet.getString(1)
                        +" " + resultSet.getString(2)
                        +" " + resultSet.getString(3)
                        +" " + resultSet.getString(4)
                        +" " + resultSet.getString(5)
                        +" ";  // Remove any leading/trailing spaces
                result.append(details).append("\n");  // Using newline to separate each pet's details
            }


        } catch (Exception e) {
            System.err.println("Error while retrieving pet records: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
        System.out.println(result);
        return result.toString();
    }

    public static void deletePatient(String name, String surname, String middlename, String bday) {

        try (Connection connection = dbManager.getConnection()) {
            // Query to insert the appointment record
            String insertQuery = "DELETE FROM patient_list WHERE first_name = ? AND last_name = ? AND middle_name = ? AND date_of_birth = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            // Set parameters
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, middlename);
            preparedStatement.setDate(4, Date.valueOf(bday));


            int rowsInserted = preparedStatement.executeUpdate();
            System.out.println("Rows deleted: " + rowsInserted);
        } catch (Exception e) {
            System.err.println("Error while deleting patient record: " + e.getMessage());
        }

    }

    public static String getPatientDetails(String name, String surname, String middlename) {
        StringBuilder result = new StringBuilder();

        try (Connection connection = dbManager.getConnection()) {
            // SQL query to fetch the appointment info
            String selectQuery = "SELECT unique_id,password FROM patient_list WHERE first_name = ? AND last_name = ? AND middle_name = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

            preparedStatement.setString(1, name);  // Use the active patient's ID
            preparedStatement.setString(2, surname);  // Convert date to SQL Date format
            preparedStatement.setString(3, middlename);  // Convert time to SQL Time format


            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println(preparedStatement);

            // Process the result set
            if (resultSet.next()) {
                String info = resultSet.getString(1)+" : "+ resultSet.getString(2);  // Fetch and clean the "info" column value
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
}

