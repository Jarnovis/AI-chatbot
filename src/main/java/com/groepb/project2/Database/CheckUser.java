package com.groepb.project2.Database;

import java.io.IOException;
import java.sql.*;

public class CheckUser {
    private ConnectDB connectDB;

    public CheckUser(ConnectDB connectDB) {
        this.connectDB = connectDB;
    }

    public Connection databaseConnection() throws SQLException {
        return connectDB.dbConnect();
    }

    public boolean emailExists(Connection connection, String emailToCheck) throws SQLException {
        String query = "SELECT email FROM users WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, emailToCheck);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public boolean checkEmail(String emailToCheck) throws IOException {
        try(Connection connection = databaseConnection()) {
            return emailExists(connection, emailToCheck);
        } catch (SQLException e) {
            System.out.println("Error tijdens checken van E-Mail");
            e.printStackTrace();
        }
        return false;
    }
}