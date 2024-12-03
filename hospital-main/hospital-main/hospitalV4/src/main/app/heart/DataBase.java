package main.app.heart;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import main.app.logreg.LogController;

import java.security.SecureRandom;
import java.sql.*;
import java.time.LocalDate;

public class DataBase {
    public static String uString;

    //RETURNS LIST OF STAFF AS A STRING
    public static String getStaffList() {
        String text = "";
        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);

            ResultSet resultSet = statement.executeQuery("select * from stafflist");
            while (resultSet.next()) {
                text += ((resultSet.getString(3) + " " + resultSet.getString(4) + " " + resultSet.getString(6) + " "));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }


    public static String getPetList(String type) {
        String text = "";
        if (type.equals("patient")) {
            try {
                DatabaseManager dbManager = new MySQLDatabaseManager();
                Connection connection = dbManager.getConnection();
                Statement statement = dbManager.createStatement(connection);

                ResultSet resultSet = statement.executeQuery("select * from petlist where ownerID='" + LogController.activeID + "'");
                while (resultSet.next()) {
                    text += ((resultSet.getString(3) + " " + resultSet.getString(4) + " " + resultSet.getString(5) + " "));

                }
            } catch (Exception e) {
                System.err.println(e);
            }
        } else {
            try {
                DatabaseManager dbManager = new MySQLDatabaseManager();
                Connection connection = dbManager.getConnection();
                Statement statement = dbManager.createStatement(connection);

                ResultSet resultSet = statement.executeQuery("select * from petlist");
                while (resultSet.next()) {
                    text += getPatientNS(resultSet.getString(2)) + "$" + resultSet.getString(3) + "$";

                }
            } catch (Exception e) {
                System.err.println(e);
            }

        }
        return text;
    }


    public static String getVetList() {
        String text = "";
        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);

            ResultSet resultSet = statement.executeQuery("select name,surname from stafflist ");
            while (resultSet.next()) {
                text += ((resultSet.getString(1) + " " + resultSet.getString(2) + "$"));
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return text;
    }

    //RETURNS LIST OF PATIENS AS A STRING
    public static String getPatientList() {
        String text = "";

        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);

            ResultSet resultSet = statement.executeQuery("select * from patientlist");
            while (resultSet.next()) {
                text += ((resultSet.getString(3) + " " + resultSet.getString(4) + " " + resultSet.getString(5) + " " + resultSet.getString(6) + " " + resultSet.getString(7) + " "));

            }
        } catch (Exception e) {
            System.err.println(e);
        }

        return text;
    }


    //RETURNS LIST OF APPOINTMENTS AS A STRING
    public static String getAppointmentList(String id, boolean logic) {
        String text = "";
        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);

            if (logic == false) {
                ResultSet resultSet = statement.executeQuery("select * from appointmentlist WHERE patientID='" + id + "';");
                while (resultSet.next()) {
                    text += ((resultSet.getString(3) + " " + resultSet.getString(8) + " " + resultSet.getString(4) + " " + resultSet.getString(5) + " " + resultSet.getString(7) + " "));
                }
            } else if (logic == true) {
                ResultSet resultSet = statement.executeQuery("select * from appointmentlist WHERE staffID='" + id + "';");
                while (resultSet.next()) {
                    text += getPatientNS((resultSet.getString(2))) + " " + resultSet.getString(4) + " " + resultSet.getString(5) + " " + resultSet.getString(7) + " " + resultSet.getString(8) + " ";
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return text;
    }


    public static String getHistoryList(String id) {
        String text = "";
        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);


            ResultSet resultSet = statement.executeQuery("select * from historylist WHERE patientID='" + id + "';");

            while (resultSet.next()) {

                text += ((resultSet.getString(4) + "$" + resultSet.getString(3) + "$" + resultSet.getString(5) + "$"));
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return text;
    }


    public static String getHistoryList() {
        String text = "";
        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);

            ResultSet resultSet = statement.executeQuery("select * from historylist;");
            while (resultSet.next()) {
                text += getPatientNS((resultSet.getString(2))) + "$" + resultSet.getString(3) + "$" + resultSet.getString(5) + "$";
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return text;
    }


    //RETURNS PATIENT FULL NAME USING ITS ID
    public static String getPatientNS(String id) {
        String text = "";

        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);
            ResultSet resultSet = statement.executeQuery("select * from patientlist where uniqueID='" + id + "';");
            while (resultSet.next()) {
                text += ((resultSet.getString(3) + " " + resultSet.getString(4) + " " + resultSet.getString(5)));

            }
            statement.close();
            connection.close();
        } catch (Exception e) {
            System.err.println(e);
        }

        return text;

    }

    //Validating ID-Password & TYPE OF USER
    public static String checkAcc(String accID, String password, ActionEvent event) {
        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);
            ResultSet resultSet = statement.executeQuery("select * from patientlist where uniqueID = '" + accID + "';");
            resultSet.next();
            if (resultSet.getString(8).equals(password)) {
                statement.close();
                connection.close();
                return "patient";
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);
            ResultSet resultSet = statement.executeQuery("select * from stafflist where uniqueID = '" + accID + "';");
            resultSet.next();
            if (resultSet.getString(6).equals(password)) {
                statement.close();
                connection.close();
                return "staff";
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        if (accID.equals("AAAA") && password.equals("1111")) {
            return "admin";
        }


        return "null";
    }


    private static void showAlert(Alert.AlertType alertType, String title, String contentText, ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        Alert alert = new Alert(alertType);
        alert.initOwner(stage);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    //ADDS NEW PATIENT TO DATABASE
    public static void addPatient(String name, String surname, String middlename, LocalDate date, String number, String password) {
        String accID = generateUniqueString();
        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);


            statement.executeUpdate("INSERT INTO patientlist (uniqueID,name, surname, middlename,birthday,number,password)" + "VALUES ('" +
                    accID + "','" + name + "','" + surname + "','" + middlename + "','" + date + "','" + number + "','" + password + "')");
        } catch (Exception e) {
            System.err.println(e);
        }

    }


    public static void addHistory(String patient, String pet, String info, String user) throws SQLException {
        MySQLDatabaseManager manager = new MySQLDatabaseManager();
        manager.addHistory(patient, pet, info, user);
    }


    //ADDS NEW STAFF TO DATABASE
    public static void addStaff(String name, String surname, String number, String password) {

        String accID = generateUniqueString();
        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);

            statement.executeUpdate("INSERT INTO stafflist (uniqueID,name,surname,number,password)" + "VALUES ('" +
                    accID + "','" + name + "','" + surname + "','" + number + "','" + password + "')");
        } catch (Exception e) {
            System.err.println(e);
        }

    }


    public static void addPet(String name, LocalDate date, String type) {
        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);

            statement.executeUpdate("INSERT INTO petlist (ownerID,petName,petAge,petType)" + "VALUES ('" +
                    LogController.activeID + "','" + name + "','" + date + "','" + type + "')");
        } catch (Exception e) {
            System.err.println(e);
        }

    }

    //ADDS NEW APPOINTMENT TO DATABASE
    public static void addAppointment(String patientID, String staffID, LocalDate date, String time, String info, String status, String petName) {

        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);

            statement.executeUpdate("INSERT INTO appointmentlist (patientID,staffID,date,time,info,status,petName)" + "VALUES ('" +
                    patientID + "','" + staffID + "','" + date + "','" + time + "','" + info + "','" + status + "','" + petName + "')");
        } catch (Exception e) {
            System.err.println(e);
        }


    }

    //RETURNS STAFF ID USING ITS NAME,SURNAME AND PROFESSION
    public static String getStaffID(String name, String surname) {
        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);

            ResultSet resultSet = statement.executeQuery("SELECT uniqueID FROM stafflist WHERE name = '" + name + "' AND surname = '" + surname + "';");
            resultSet.next();
            return resultSet.getString(1);
        } catch (Exception e) {
            System.err.println(e);
        }
        return "ERROR";
    }

    public static String getStaffNS(String id) {
        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);

            if (!id.equals("0000")) {
                ResultSet resultSet = statement.executeQuery("SELECT name,surname FROM stafflist WHERE uniqueID = '" + id + "';");
                resultSet.next();
                return resultSet.getString(1) + " " + resultSet.getString(2);
            } else {
                return "Owner";
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return "ERROR";
    }


    public static String getOccupiedTimes(String staffid, String date) {
        String times = "";
        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);

            ResultSet resultSet = statement.executeQuery("SELECT time FROM appointmentlist WHERE staffID = '" + staffid + "' AND date = '" + date + "';");
            while (resultSet.next()) {
                times += resultSet.getString(1);
            }
            return times;
        } catch (Exception e) {
            System.err.println(e);
        }
        return "ERROR";
    }

    //RETURNS PATIENT ID USING ITS NAME,SURNAME AND MIDDLENAME
    public static String getPatientID(String name, String surname, String middlename) {
        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);
            ResultSet resultSet = statement.executeQuery("SELECT uniqueID FROM patientlist WHERE name = '" + name + "' AND surname = '" + surname + "' AND middlename = '" + middlename + "';");
            resultSet.next();
            return resultSet.getString(1);
        } catch (Exception e) {
            System.err.println(e);
        }

        return "ERROR";
    }

    public static String getUserInfo(String id, String user) {
        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);
            if (user.equals("staff")) {

                ResultSet resultSet = statement.executeQuery("SELECT * FROM stafflist WHERE uniqueID = '" + id + "';");
                resultSet.next();
                return resultSet.getString(3) + " " + resultSet.getString(4) + " " + resultSet.getString(5) + " " + resultSet.getString(6);
            } else {

                //    ResultSet resultSet = statement.executeQuery("SELECT uniqueID FROM patientlist WHERE name = '" + name + "' AND surname = '" + surname + "' AND middlename = '" + middlename + "';");
                //     resultSet.next();
            }
        } catch (Exception e) {
            System.err.println(e);
        }

        return "ERROR";
    }

    public static String updateStaff(String name, String surname, String number, String password) {
        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);
            statement.executeUpdate("UPDATE stafflist SET name = '" + name + "',surname = '" + surname + "',number = '" + number + "', password = '" + password + "' WHERE uniqueID = '" + LogController.activeID + "';");


        } catch (Exception e) {
            System.err.println(e);
        }

        return "ERROR";
    }

    //DELETES STAFF FORM DATABASE USING ITS NAME,SURNAME AND PROFESSION
    public static void deleteStaff(String name, String surname) {
        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);
            statement.executeUpdate("DELETE FROM stafflist WHERE name = '" + name + "' AND surname = '" + surname + "';");
        } catch (Exception e) {
            System.err.println(e);
        }

    }


    public static void deleteAppointment(String id, String pet, String date) {
        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);
            statement.executeUpdate("DELETE FROM appointmentlist WHERE staffID = '" + id + "' AND date = '" + date + "' AND petName = '" + pet + "';");
        } catch (Exception e) {
            System.err.println(e);
        }

    }

    public static void deleteHistory(String name, String pet, String info, String user) {
        String id;
        if (user.equals("patient")) {
            String[] text = name.split(" ");

            if (!name.equals("Owner")) {
                id = getStaffID(text[0], text[1]);
            } else {
                id = "0000";
            }
        } else {
            id = name;
        }

        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);
            System.out.println("DELETE FROM historylist WHERE  info = '" + info + "' AND petName = '" + pet + "';");
            statement.executeUpdate("DELETE FROM historylist WHERE  info = '" + info + "' AND petName = '" + pet + "';");
        } catch (Exception e) {
            System.err.println(e);
        }

    }


    public static void deletePet(String name, String birth, String type) {

        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);
            statement.executeUpdate("DELETE FROM petlist WHERE petName = '" + name + "' AND petAge = '" + birth + "' AND petType = '" + type + "';");
            statement.executeUpdate("DELETE FROM appointmentlist WHERE patientID = '" + LogController.activeID + "' AND petName = '" + name + "';");
            statement.close();
            connection.close();
        } catch (Exception e) {
            System.err.println(e);
        }


    }

    //DELETES PATIENT FORM DATABASE USING ITS NAME,SURNAME AND MIDDLENAME
    public static void deletePatient(String name, String surname, String middlename, String bday) {
        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);

            statement.executeUpdate("DELETE FROM patientlist WHERE name = '" + name + "' AND surname = '" + surname + "' AND middlename = '" + middlename + "' AND birthday = '" + bday + "';");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    //RETURNS APPOINTMENT INFO FROM DATABASE USING ITS FullName,DATE AND TIME
    public static String showAppointmentInfo(String fName, String date, String time) {
        String[] text = fName.split(" ");
        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);

            ResultSet resultSet = statement.executeQuery("SELECT info FROM appointmentlist WHERE patientID = '" + getPatientID(text[0], text[1], text[2]) + "' AND date = '" + date + "' AND time = '" + time + "';");
            resultSet.next();
            return resultSet.getString(1);

        } catch (Exception e) {
            System.err.println(e);
        }
        return "ERROR";
    }

    public static String showAppointmentInfoPM(String fName, String date, String time) {
        String[] text = fName.split(" ");
        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);
            ResultSet resultSet = statement.executeQuery("SELECT info FROM appointmentlist WHERE staffID = '" + getStaffID(text[0], text[1]) + "' AND date = '" + date + "' AND time = '" + time + "';");
            resultSet.next();
            return resultSet.getString(1);

        } catch (Exception e) {
            System.err.println(e);
        }
        return fName;
    }

    //CHANGES APPOINTMENT STATUS USING FullName,DATE,TIME AND STATUS
    public static void setAppointmentStatus(String fname, String date, String time, String status) {
        String[] text = fname.split(" ");


        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);
            statement.executeUpdate("UPDATE appointmentlist SET status = '" + status + "'" + " WHERE patientID ='" + getPatientID(text[0], text[1], text[2]) + "' AND date = '" + date + "' AND time = '" + time + "';");

        } catch (Exception e) {
            System.err.println(e);

        }
    }

    public static void setAppointmentStatus(String date, String time, String status, ActionEvent event) {

        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);

            ResultSet resultSet = statement.executeQuery("select status from appointmentlist WHERE patientID ='" + LogController.activeID + "' AND date = '" + date + "' AND time = '" + time + "';");
            resultSet.next();
            if (resultSet.getString(1).equals("Proposed")) {
                statement.executeUpdate("UPDATE appointmentlist SET status = '" + status + "'" + " WHERE patientID ='" + LogController.activeID + "' AND date = '" + date + "' AND time = '" + time + "';");
            } else {
                showAlert(Alert.AlertType.WARNING, "STATUS ERROR", "You can only change PROPOSED status", event);
            }
        } catch (Exception e) {
            System.err.println(e);

        }
    }

    public static void proposeAppointment(String fname, String date, String time, String status, String newDate, String newTime) {
        String[] text = fname.split(" ");

        try {
            DatabaseManager dbManager = new MySQLDatabaseManager();
            Connection connection = dbManager.getConnection();
            Statement statement = dbManager.createStatement(connection);
            statement.executeUpdate("UPDATE appointmentlist SET status = '" + status + "',date = '" + newDate + "',time ='" + newTime + "'" + " WHERE patientID ='" + getPatientID(text[0], text[1], text[2]) + "' AND date = '" + date + "' AND time = '" + time + "';");

        } catch (Exception e) {
            System.err.println(e);

        }
    }

    //GENERATES RANDOM STRING FOR SECURITY
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
}
