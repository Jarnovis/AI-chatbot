package com.groepb.project2.Database;

import com.groepb.project2.data.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConversatieTB {
    private final ConnectDB connectDB;

    public ConversatieTB(ConnectDB connectDB) {
        this.connectDB = connectDB;
    }

    public void saveMessageToDatabase(int conversationID, String subject, Message message, int sequenceNumber, int userID) {
        String insertSQL = "INSERT INTO conversaties (conversationID, userID, conversationSubject ,userType, message, messageID, sequenceNumber) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = connectDB.dbConnect();
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {

            statement.setInt(1, conversationID);
            statement.setInt(2, userID);
            statement.setString(3, subject);
            statement.setString(4, message.getUserType());
            statement.setString(5, message.getMessage());
            statement.setInt(6, message.getMessageID());
            statement.setInt(7, sequenceNumber++);
            statement.executeUpdate();

            System.out.println("Message inserted: " + message.getMessage());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateConversationSubject(int conversationID, String newSubject) throws SQLException {
        String updateSQL = "UPDATE conversaties SET conversationSubject = ? WHERE conversationID = ?";

        try (Connection connection = connectDB.dbConnect();
             PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {

            pstmt.setString(1, newSubject);
            pstmt.setInt(2, conversationID);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Conversation subject updated successfully.");
            } else {
                System.out.println("No conversation found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
