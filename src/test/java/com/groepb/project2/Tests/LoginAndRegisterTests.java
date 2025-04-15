package com.groepb.project2.Tests;

import com.groepb.project2.Database.ConnectDB;
import com.groepb.project2.Database.RegisterUser;
import com.groepb.project2.Encryptie.Encryptie;
import com.groepb.project2.Encryptie.Decryptie;
import com.groepb.project2.Database.GetFromDB;
import com.groepb.project2.data.Environment;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class LoginAndRegisterTests {

    private ConnectDB connectDB;
    private RegisterUser registerUser;
    private Encryptie encryptie;
    private Decryptie decryptie;
    private GetFromDB getFromDB;

    @BeforeEach
    void setUp() {
        Environment environment = new Environment();
        connectDB = new ConnectDB(environment);
        registerUser = new RegisterUser(connectDB);
        encryptie = new Encryptie();
        decryptie = new Decryptie();
        getFromDB = new GetFromDB(connectDB);
    }

    @Test
    void testRegisterAndLoginUser() throws SQLException {
        String email = "testuser@42.com";
        String username = "testuser";
        String password = "password123";
        String encryptedPassword = encryptie.encrypt(password);

        assertDoesNotThrow(() -> {
            try (Connection connection = connectDB.dbConnect()) {
                String query = "INSERT INTO users (email, username, password) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, email);
                    preparedStatement.setString(2, username);
                    preparedStatement.setString(3, encryptedPassword);
                    preparedStatement.executeUpdate();
                }
            }
        });

        try (Connection connection = connectDB.dbConnect()) {
            String query = "SELECT * FROM users WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    assertTrue(resultSet.next(), "The user should be present in the database.");
                    assertEquals(email, resultSet.getString("email"));
                    assertEquals(username, resultSet.getString("username"));
                    assertEquals(encryptedPassword, resultSet.getString("password"));
                }
            }
        }

        boolean loginSuccess = assertDoesNotThrow(() -> decryptie.decrypt(email, password));
        assertTrue(loginSuccess, "The user should be able to log in with the correct credentials.");
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Connection connection = connectDB.dbConnect()) {
            String query = "DELETE FROM users WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, "testuser@42.com");
                preparedStatement.executeUpdate();
            }
        }
    }
}
