package main.app.heart;

import main.app.logreg.LogController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static main.app.heart.DataBase.getPatientID;


public class MySQLDatabaseManager implements DatabaseManager {


    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/hospital";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "0000";

    static {
        // Load the JDBC driver when the class is loaded
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    @Override
    public Statement createStatement(Connection connection) throws SQLException {
        return connection.createStatement();
    }


    public void addHistory(String patient, String pet, String info, String user) throws SQLException {
        DatabaseManager dbManager = new MySQLDatabaseManager();
        if (user.equals("staff")) {
            try (Connection connection = dbManager.getConnection();
                 Statement statement = dbManager.createStatement(connection)) {
                String[] txt = patient.split(" ");
                patient = getPatientID(txt[0], txt[1], txt[2]);
                statement.executeUpdate("INSERT INTO historylist (patientID,petName,staffID,info)" + "VALUES ('" +
                        patient + "','" + pet + "','" + LogController.activeID + "','" + info + "')");
            } catch (Exception e) {
                System.err.println(e);
            }
        } else {
            try (Connection connection = dbManager.getConnection();
                 Statement statement = dbManager.createStatement(connection)) {
                statement.executeUpdate("INSERT INTO historylist (patientID,petName,staffID,info)" + "VALUES ('" +
                        patient + "','" + pet + "','" + "0000" + "','" + info + "')");
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }


}




