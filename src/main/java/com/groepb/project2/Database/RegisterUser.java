package com.groepb.project2.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class RegisterUser {
    private final ConnectDB connectDB;

    public RegisterUser(ConnectDB connectDB) {
        this.connectDB = connectDB;
    }

    public void registerNewUser(String email, String password) {
        try {
            Connection connection = connectDB.dbConnect();
            resetAutoIncrementPK(connection);
            insertUser(connection, email, password);
        } catch (SQLException e){
            handleSQLException(e, email);
        }
    }
    private void insertUser(Connection connection, String email, String password) {
        String query = "INSERT INTO users (email, password) VALUES(?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleSQLException(SQLException e, String email) {
        if (e.getSQLState().equals("23000")) {
            System.out.println(email + " already exists");
        } else {
            throw new RuntimeException(e);
        }
    }

    private void resetAutoIncrementPK(Connection connection) throws SQLException {
        String sql = "ALTER TABLE users AUTO_INCREMENT = 1";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }
}
