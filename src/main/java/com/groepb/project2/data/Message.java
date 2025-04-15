package com.groepb.project2.data;

import com.groepb.project2.Database.ConnectDB;
import com.groepb.project2.Database.GetFromDB;

import java.sql.SQLException;

public class Message {

    private String userType;
    private String message;
    private static GetFromDB getFromDB = new GetFromDB(new ConnectDB(new Environment()));
    private static int nextMessageID;

    static {
        try {
            nextMessageID = getFromDB.getHighestMessageID() + 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private int messageID = 0;

    public Message(String userType, String message) {
        messageID = nextMessageID++;
        this.userType = userType;
        this.message = message;
    }

    public String getUserType() {
        return userType;
    }

    public String getMessage() {
        return message;
    }

    public int getMessageID() {return messageID;}

    public static void subtractIDbyOne() {
        nextMessageID -= 1;
    }
}
