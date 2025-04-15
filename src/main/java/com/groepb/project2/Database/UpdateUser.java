package com.groepb.project2.Database;

import com.groepb.project2.Encryptie.Encryptie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateUser {
    private final ConnectDB connectDB;

    public UpdateUser(ConnectDB connectDB) {
        this.connectDB = connectDB;
    }

    public void updateUser(String currentEmail, String newEmail, String newPassword) {
        try (Connection connection = connectDB.dbConnect()){
            String encryptPass = new Encryptie().encrypt(newPassword);
            executeUpdate(connection,currentEmail,newEmail,encryptPass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeUpdate(Connection connection, String currentEmail, String newEmail, String encryptPass) {
        String query = "UPDATE users SET email = ?, password = ? WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newEmail);
            preparedStatement.setString(2, encryptPass);
            preparedStatement.setString(3, currentEmail);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
