
package main.java.core;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLConnection {
    // Database credentials
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/projectDB";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "4002";

    // Method to get a database connection
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }


    public static void main(String[] args) {
        PostgreSQLConnection db = new PostgreSQLConnection();

    }
}

