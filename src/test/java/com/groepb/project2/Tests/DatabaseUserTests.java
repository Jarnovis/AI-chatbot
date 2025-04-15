package com.groepb.project2.Tests;

import com.groepb.project2.Database.ConnectDB;
import com.groepb.project2.Database.DeleteUser;
import com.groepb.project2.Database.RegisterUser;
import com.groepb.project2.Database.ConversatieTB;
import com.groepb.project2.data.Environment;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseUserTests {
    private Environment environment;
    private ConversatieTB conversatieTB;
    private ConnectDB connectDB;
    private DeleteUser deleteUser;
    private RegisterUser registerUser;

    @Before
    public void setUp() {
        environment = new Environment();
        connectDB = new ConnectDB(environment);
        conversatieTB = new ConversatieTB(connectDB);
        deleteUser = new DeleteUser(connectDB);
        registerUser = new RegisterUser(connectDB);

    }

    private Connection getConnection() throws SQLException {
        String url = environment.get("DB_URL");
        String user = environment.get("DB_USER");
        String password = environment.get("DB_PASS");

        return DriverManager.getConnection(url, user, password);
    }

    @Test
    public void testDatabaseConnection() {
        try (Connection connection = connectDB.dbConnect()) {
            assertNotNull(connection, "Connection niet null");
            assertTrue(connection.isValid(2), "Connection moet valid zijn");
        } catch (SQLException e) {
            fail("Kan geen connectie maken met de databse: " + e.getMessage());
        }
    }

    @Test
    public void testRegisterUser() {
        registerUser.registerNewUser("TestMail@42.nl", "TestPassword");

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email = ?")){
            statement.setString(1,"TestMail@42.nl");
            ResultSet resultSet = statement.executeQuery();
            assertTrue(resultSet.next());
            assertEquals("TestMail@42.nl", resultSet.getString("email"));
            assertEquals("TestPassword", resultSet.getString("password"));

        } catch (SQLException e) {
            fail("Fout opgetreden: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteUser() {
        deleteUser.deleteUser("TestMail@42.nl");
        System.out.println("TestMail@42.nl is deleted");

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email = ?")){
            statement.setString(1,"TestMail@42.nl");
            ResultSet resultSet = statement.executeQuery();
            assertFalse(resultSet.next());

        } catch (SQLException e) {
            fail("Fout opgetreden: " + e.getMessage());
        }
    }
}