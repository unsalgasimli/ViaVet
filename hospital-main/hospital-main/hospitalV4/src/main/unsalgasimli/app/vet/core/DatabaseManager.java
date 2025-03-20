package main.unsalgasimli.app.vet.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public interface DatabaseManager {
    Connection getConnection() throws SQLException;

    Statement createStatement(Connection connection) throws SQLException;


}
