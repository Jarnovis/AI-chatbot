package com.groepb.project2.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteConversations {
    private final ConnectDB connectDB;

    public DeleteConversations(ConnectDB connectDB) {
        this.connectDB = connectDB;
    }

    public void deleteAllConversations() {
        String query = "DELETE FROM conversaties";
        try (Connection connection = connectDB.dbConnect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUserConversations(String email) {
        String query = "DELETE FROM conversaties WHERE userID = (SELECT id FROM users WHERE email = ?)";
        try (Connection connection = connectDB.dbConnect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
