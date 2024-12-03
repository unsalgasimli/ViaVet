package main.app.heart;
import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public interface DatabaseManager {
    Connection getConnection() throws SQLException;

    Statement createStatement(Connection connection) throws SQLException;
    void addHistory(String patient, String pet, String info, String staffID) throws SQLException;

}
