package com.groepb.project2.Tests;

import com.groepb.project2.Database.*;
import com.groepb.project2.data.Conversation;
import com.groepb.project2.data.Environment;
import com.groepb.project2.data.Message;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class ConversatieOpslaanTest {

    private ConnectDB connectDB;
    private RegisterUser registerUser;
    private GetFromDB getFromDB;
    private Conversation conversation;
    private DeleteUser deleteUser;

    @BeforeEach
    public void setUp() throws SQLException {
        connectDB = new ConnectDB(new Environment());
        getFromDB = new GetFromDB(connectDB);
        registerUser = new RegisterUser(connectDB);
        deleteUser = new DeleteUser(connectDB);
    }

    @Test
    public void testConversatieOpslaan() throws SQLException {

        registerUser.registerNewUser("UnitTest@42.nl", "TestPassword");
        int userId = getFromDB.getUID("UnitTest@42.nl");
        conversation = new Conversation("TestConversation", userId);
        Message message = new Message("user", "TestMessage");

        conversation.saveMessageToDB(message, conversation.getSubject(), userId);
        try (Connection connection = connectDB.dbConnect()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM conversaties WHERE conversationID = ?")) {
                statement.setInt(1, conversation.getConversationID());
                ResultSet resultSet = statement.executeQuery();
                assertTrue(resultSet.next());
                assertEquals(conversation.getConversationID(), resultSet.getInt("conversationID"));
            }
        } catch (SQLException e) {
            fail("Fout opgetreden: " + e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (Connection connection = connectDB.dbConnect()) {
            String query = "DELETE FROM conversaties WHERE conversationID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, conversation.getConversationID());
                preparedStatement.executeUpdate();
            }
        }
        deleteUser.deleteUser("UnitTest@42.nl");
        Message.subtractIDbyOne();
    }
}
