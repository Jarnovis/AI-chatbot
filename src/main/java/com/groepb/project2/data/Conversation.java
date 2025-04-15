package com.groepb.project2.data;

import com.groepb.project2.Database.ConnectDB;
import com.groepb.project2.Database.GetFromDB;
import com.groepb.project2.Database.ConversatieTB;
import com.groepb.project2.Windows.MainGUI;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.ArrayList;

public class Conversation {

    private String subject;
    private int userID;

    private ArrayList<Message> userPromptList;
    private ArrayList<Message> botResponseList;
    private static GetFromDB getFromDB = new GetFromDB(new ConnectDB(new Environment()));
    private VBox chatMessages;
    private static int nextConversationID;

    static {
        try {
            nextConversationID = getFromDB.getHighestConversationID() + 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private int conversationID;
    private ConversatieTB conversatieTB = new ConversatieTB(new ConnectDB(new Environment()));

    public Conversation(String name, int userID) {
        conversationID = nextConversationID++;
        this.subject = name;
        this.userID = userID;
        userPromptList = new ArrayList<>();
        botResponseList = new ArrayList<>();
        chatMessages = new VBox();
    }

    public Conversation(String name, int userID, int conversationID) {
        this(name, userID);
        this.conversationID = conversationID;
    }

    public int getConversationID() {
        return conversationID;
    }

    public void saveMessageToDB(Message message, String subject, int userID) {

        conversatieTB.saveMessageToDatabase(getConversationID(), subject, message, getListSize(), userID);
    }

    public void getConversationFromDB(int conversationID, int userID) throws SQLException {

        botResponseList = getFromDB.retrieveAndPrintBotMessages(conversationID, userID);
        userPromptList = getFromDB.retrieveAndPrintUserMessages(conversationID, userID);

        try{
            for (int i = 0; i < getListSize(); i++) {

                MainGUI.addMessage(chatMessages, "User", userPromptList.get(i).getMessage());
                MainGUI.addMessage(chatMessages, "Bot", botResponseList.get(i).getMessage());
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void addUserPrompt(Message user) {

        userPromptList.add(user);
    }

    public void addBotResponse(Message bot) {

        botResponseList.add(bot);
    }

    public String getSubject() {
        return this.subject;
    }

    public int getListSize() {
        return userPromptList.size();
    }

    public String getUserMessage() {
        return userPromptList.get(userPromptList.size()-1).getMessage();
    }

    public String getSpecificUserMessage(int i) {
        return userPromptList.get(i).getMessage();
    }

    public String getBotMessage() {
        return botResponseList.get(botResponseList.size()-1).getMessage();
    }

    public String getSpecificBotResponse(int i) {
        return botResponseList.get(i).getMessage();
    }

    public int getUserID() {
        return this.userID;
    }

    public VBox getChatMessages() {
        return chatMessages;
    }

    public static void subtractIDbyOne() {
        nextConversationID -= 1;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void saveUserPromptAsMessage(String userPrompt) {

        Message userMessage = new Message("user", userPrompt);
        addUserPrompt(userMessage);
        saveMessageToDB(userMessage, subject, getUserID());
        //addMessage(chatMessages, "Bot", conversation.getBotMessage());
    }

    public void saveBotResponseAsMessage(String botResponse) {
        try {
            Message botMessage = new Message("bot", botResponse);
            addBotResponse(botMessage);

        } catch (Error e) {
            throw new RuntimeException(e);
        }
    }
}
