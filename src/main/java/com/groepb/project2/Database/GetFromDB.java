package com.groepb.project2.Database;

import com.groepb.project2.data.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GetFromDB {
    private final ConnectDB connectDB;

    public GetFromDB(ConnectDB connectDB) {
        this.connectDB = connectDB;
    }

    public int getUID(String email) throws SQLException {
        String query = "SELECT id FROM users WHERE email = ? AND password = ?";
        int retrievedUID = 0;
        try (Connection connection = connectDB.dbConnect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, getPassword(email));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    retrievedUID = resultSet.getInt("id");
                }
            }
        }
        return retrievedUID;
    }

    public String getPassword(String email) throws SQLException {
        String password = "";
        String query = "SELECT password FROM users WHERE email = ? ";
        try (Connection connection = connectDB.dbConnect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    password = resultSet.getString("password");
                }
            }
        }
        return password;
    }

    public ArrayList<Message> retrieveMessagesFromDatabase(int conversationID, int userID) throws SQLException {
        String querySQL = "SELECT userType, message FROM conversaties WHERE conversationID = ? AND userID = ? ORDER BY sequenceNumber ASC";
        ArrayList<Message> messages = new ArrayList<>();

        try (Connection connection = connectDB.dbConnect()) {
            PreparedStatement pstmt = connection.prepareStatement(querySQL); {
                pstmt.setInt(1, conversationID);
                pstmt.setInt(2, userID);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        String userType = rs.getString("userType");
                        String message = rs.getString("message");
                        messages.add(new Message(userType, message));
                        Message.subtractIDbyOne();
                    }
                }
                return messages;
            }
        }
    }

    public ArrayList<Message> retrieveAndPrintUserMessages(int conversationID, int userID) throws SQLException {
        ArrayList<Message> userMessages = new ArrayList<>();

        ArrayList<Message> messages = retrieveMessagesFromDatabase(conversationID, userID);

        for (Message message : messages) {
            if (message.getUserType().equals("user")) {
                userMessages.add(message);
            }
        }

        return userMessages;
    }

    public ArrayList<Message> retrieveAndPrintBotMessages(int conversationID, int userID) throws SQLException {
        ArrayList<Message> botResponses = new ArrayList<>();

        ArrayList<Message> messages = retrieveMessagesFromDatabase(conversationID, userID);

        for (Message message : messages) {
            if (message.getUserType().equalsIgnoreCase("bot")) {
                botResponses.add(message);
            }
        }

        return botResponses;
    }

    public int  getHighestConversationID() throws SQLException {
        String querySQL = "SELECT MAX(conversationID) AS maxConversationID FROM conversaties";

        try (Connection connection = connectDB.dbConnect();
             PreparedStatement pstmt = connection.prepareStatement(querySQL);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("maxConversationID");
            } else {
                return 0;
            }
        }
    }

    public int  getHighestMessageID() throws SQLException {
        String querySQL = "SELECT MAX(messageID) AS maxMessageID FROM conversaties";

        try (Connection connection = connectDB.dbConnect();
             PreparedStatement pstmt = connection.prepareStatement(querySQL);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("maxMessageID");
            } else {
                return 0;
            }
        }
    }

    public boolean hasMessagesForUserInConversation(int userID, int conversationID) throws SQLException {
        String querySQL = "SELECT COUNT(*) AS messageCount FROM conversaties WHERE userID = ? AND conversationID = ?";

        try (Connection connection = connectDB.dbConnect();
             PreparedStatement pstmt = connection.prepareStatement(querySQL)) {

            pstmt.setInt(1, userID);
            pstmt.setInt(2, conversationID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("messageCount");
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getConversationSubject(int conversationID) throws SQLException {
        String querySQL = "SELECT conversationSubject FROM conversaties WHERE conversationID = ?";
        String subject = null;

        try (Connection connection = connectDB.dbConnect();
             PreparedStatement pstmt = connection.prepareStatement(querySQL)) {

            pstmt.setInt(1, conversationID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    subject = rs.getString("conversationSubject");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subject;
    }


}
