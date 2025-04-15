package com.groepb.project2.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteUser {
    private final ConnectDB connectDB;

    public DeleteUser(ConnectDB connectDB) {
        this.connectDB = connectDB;
    }

    public void deleteUser(String email) {
        String query = "DELETE FROM users WHERE email = ?";
        try (Connection connection = connectDB.dbConnect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error tijdens verwijderen van user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteAllUsers() {
        try {
            Connection connection = connectDB.dbConnect();
            String query = "DELETE FROM users";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " users verwijderd.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error tijdens verwijderen van alle gebruikers: " + e.getMessage(), e);
        }
    }
}
