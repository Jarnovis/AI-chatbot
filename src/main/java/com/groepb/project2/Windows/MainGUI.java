package com.groepb.project2.Windows;

import com.groepb.project2.AI.*;
import com.groepb.project2.AI.BobGPT.BobGPT;
import com.groepb.project2.Database.ConnectDB;
import com.groepb.project2.Database.GetFromDB;
import com.groepb.project2.Database.ConversatieTB;
import com.groepb.project2.data.Conversation;
import com.groepb.project2.data.Environment;
import com.groepb.project2.data.Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainGUI extends FrameWorkGUI implements IResponse{
    private List<Conversation> conversations = new ArrayList<>();
    private VBox conversationList;
    private BorderPane historyPane;
    private BorderPane headerPane;
    private BorderPane root;
    private BorderPane centerPane;
    private Text titleText;
    private HBox titleBox;
    private ToggleButton languageToggle;
    private boolean isEnglish = false;
    private Button addButton;
    private Button changeInfoButton;
    private Button logout;
    private static Label messageLabel;
    private Label conversationLabel;
    private String language = "Dutch";
    private ComboBox bots;
    private IAI bot;
    private GetFromDB getFromDB;
    private ConversatieTB conversatieTB;

    public MainGUI(String currentUserEmail, int userID) throws SQLException {
        Observer.initInstance();
        Observer.getInstance().addObserver(this);

        initialize();
      
        ScrollPane box = new ScrollPane();
        box.setHmax(5);
        box.setStyle("-fx-background: #111111; -fx-background-color: #111111;");
        box.setContent(conversationList);
        bots.setValue("Bob42");
        setBot();

        changeInfoButton.setOnAction(event -> {
            ROOTPANE.getChildren().removeAll();
            ROOTPANE.getChildren().add(new UpdateUserGUI(currentUserEmail, userID).getRootPane());
            Observer.getInstance().removeObserver(this);
        });

        addButton.setOnAction(event -> {
            Conversation conversation = new Conversation("Conversation", userID);

            addConversation(conversation);
        });

        logout.setOnAction(event -> {
            FrameWorkGUI.ROOTPANE.getChildren().clear();
            FrameWorkGUI.ROOTPANE.getChildren().add(new LoginScreen().getRootPane());
            Observer.getInstance().removeObserver(this);
        });

        languageToggle.setOnAction(event -> switchLanguage());

        VBox buttonBox = new VBox();
        buttonBox.getChildren().addAll(bots, languageToggle, changeInfoButton,logout);
        buttonBox.setAlignment(Pos.BOTTOM_LEFT);
        buttonBox.setSpacing(10);

        getAllExistingConversations(userID);

        titleText.setFont(Font.font("Arial", 20));
        titleText.setStyle("-fx-font-weight: bold");
        titleText.setFill(Color.WHITE);
        titleBox.setAlignment(Pos.CENTER);

        BorderPane historyHeader = new BorderPane();
        historyHeader.setTop(titleBox);
        historyHeader.setCenter(addButton);

        centerPane.setStyle("-fx-background-color: #212121");
        historyPane.setStyle("-fx-background-color: #111111");
        historyPane.setCenter(box);
        historyPane.setTop(historyHeader);
        historyPane.setBottom(buttonBox);

        root.setLeft(historyPane);
        root.setRight(centerPane);

        FrameWorkGUI.ROOTPANE.getChildren().clear();
        FrameWorkGUI.ROOTPANE.getChildren().add(root);

        placement();
    }

    @Override
    void placement() {
    }

    private void addConversation(Conversation conversation) {
        conversations.add(conversation);
        ConversatieTB conversatieTB = new ConversatieTB(new ConnectDB(new Environment()));

        HBox conversationChoice = new HBox();
        conversationChoice.setSpacing(10);

        conversationLabel = new Label(conversation.getSubject());
        conversationLabel.setTextFill(Color.WHITE);
        conversationLabel.setOnMouseClicked(event -> {
            showConversation(conversation);
            conversationLabel.setStyle("-fx-background-color: #212121");
        });

        Button changeSubjectButton = new Button("•••");

        TextField changeSubjectField = getChangeSubjectField(conversation, conversationChoice, changeSubjectButton);
        setChangeSubjectButton(changeSubjectButton, conversationChoice, changeSubjectField, conversationLabel);

        conversationChoice.getChildren().addAll(conversationLabel, changeSubjectButton);
        conversationList.getChildren().add(conversationChoice);
    }

    private void showConversation(Conversation conversation) {
        VBox conversationBox = new VBox();
        conversationBox.setAlignment(Pos.TOP_LEFT);

        ScrollPane scrollPane = createScrollPane();
        scrollPane.setContent(conversation.getChatMessages());

        conversation.getChatMessages().setStyle("-fx-background: transparent;");
        conversation.getChatMessages().setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        conversation.getChatMessages().setStyle("-fx-background-color: #212121; -fx-spacing: 10; -fx-padding: 10; -fx-alignment: top-left;");

        BorderPane chatPane = new BorderPane();
        chatPane.setCenter(scrollPane);

        TextField messageField = new TextField();
        messageField.setPadding(new Insets(15));
        messageField.setOnAction(event -> {
            String userPrompt = messageField.getText();
            conversation.saveUserPromptAsMessage(userPrompt);

            messageField.clear();

            conversation.getChatMessages().requestLayout();
            scrollPane.layout();
            scrollPane.setVvalue(1.0);

            String botResponse = "";
            conversation.saveBotResponseAsMessage(botResponse);

            addMessage(conversation.getChatMessages(), "User", conversation.getUserMessage());
            addMessage(conversation.getChatMessages(), "Bot", botResponse);

            try{
                bot.getResponse(userPrompt, language, conversation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            };
        });

        chatPane.setBottom(messageField);

        VBox.setVgrow(chatPane, Priority.ALWAYS);
        VBox.setMargin(chatPane, new Insets(20, 40, 20, 40));

        conversationBox.getChildren().addAll(chatPane);
        centerPane.setCenter(conversationBox);
    }

    public static Label addMessage(VBox chatMessages, String sender, String content) {
        content = content.trim();

        messageLabel = new Label(content);
        messageLabel.setFont(Font.font("Arial", 24));
        messageLabel.setTextFill(Color.WHITE);
        messageLabel.setWrapText(true);
        messageLabel.setStyle("-fx-background-color: " + (sender.equals("User") ? "#357ef2" : "#0ec93a") + "; -fx-background-radius: 20; -fx-padding: 10; -fx-text-overrun: clip; -fx-font-size: fit-to-width;");
        messageLabel.setAlignment(sender.equals("User") ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        Pane messagePane = new StackPane(messageLabel);
        StackPane.setAlignment(messageLabel, sender.equals("User") ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        messagePane.setPadding(new Insets(0, sender.equals("User") ? 0 : 20, 0, sender.equals("User") ? 20 : 0));
        chatMessages.getChildren().add(messagePane);

        return messageLabel;
    }

    private void switchLanguage() {
        if (isEnglish) {
            titleText.setText("BobGPT");
            languageToggle.setText("English");
            logout.setText("Uitloggen");
            changeInfoButton.setText("Gegevens wijzigen");
            addButton.setText("Nieuwe Chat");
            language = "Dutch";
        } else {
            titleText.setText("BobGPT");
            languageToggle.setText("Nederlands");
            logout.setText("Sign Out");
            changeInfoButton.setText("Change Information");
            addButton.setText("New Chat");
            language = "English";
        }
        isEnglish = !isEnglish;
    }

    @Override
    public void update(String line) {
        Platform.runLater(() -> {messageLabel.setText(messageLabel.getText() + line);});
    }

    @Override
    public void fullResponse(String fullResponse, Conversation conversation){
        conversation.saveMessageToDB(new Message("bot", fullResponse), conversationLabel.getText(), conversation.getUserID());
    }

    private void setBot(){
        String[] botNames = new String[] {"Bob42", "BobGPT"};
        IAI[] botClasses = new IAI[] {new Bob42(), new BobGPT()};

        if (bot == null){
            bot = botClasses[0];
        }
        bots.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (int i = 0; i < botNames.length; i++) {
                    if (botNames[i].equals(bots.getSelectionModel().getSelectedItem())) {
                        bot = botClasses[i];
                    }
                }
            }
        });
    }

    private void getAllExistingConversations(int userID) throws SQLException {
        for (int i = 1; i <= getFromDB.getHighestConversationID(); i++) {
            if(getFromDB.hasMessagesForUserInConversation(userID, i)) {
                Conversation conversation = new Conversation(getFromDB.getConversationSubject(i), userID, i);
                Conversation.subtractIDbyOne();
                conversation.getConversationFromDB(i, userID);
                addConversation(conversation);
            }
        }
    }

    private void initialize() {
        getFromDB = new GetFromDB(new ConnectDB(new Environment()));
        conversatieTB = new ConversatieTB(new ConnectDB(new Environment()));
        root = new BorderPane();
        centerPane = new BorderPane();
        historyPane = new BorderPane();
        headerPane = new BorderPane();
        titleText = new Text("BobGPT");
        titleBox = new HBox(titleText);
        bots = createComboBox(new String[] {"Bob42", "BobGPT"});
        conversationList = new VBox();

        addButton = new Button("Nieuwe Chat");
        changeInfoButton = new Button("Gegevens Wijzigen");
        logout = new Button("Uitloggen");
        languageToggle = new ToggleButton("English");
        VBox buttonBox = new VBox();

        setWindowTitle("Main Frame");

        setPreferredSizePane(root, 800, 600);
        setPreferredSizePane(centerPane, 650, 550);
        setPreferredSizePane(headerPane, 800, 50);
        setPreferredSizePane(historyPane, 150, 600);

        conversationList.setSpacing(10);
        conversationList.setPadding(new Insets(10));
        conversationList.setAlignment(Pos.TOP_LEFT);
    }

    private TextField getChangeSubjectField(Conversation conversation, HBox conversationChoice, Button changeSubjectButton){
        TextField changeSubjectField = new TextField();
        changeSubjectField.setOnAction(event -> {
            String label = changeSubjectField.getText();

            conversationLabel.setText(label);
            conversationChoice.getChildren().removeAll(changeSubjectField);
            conversationChoice.getChildren().addAll(conversationLabel, changeSubjectButton);
            conversation.setSubject(label);
            try {
                conversatieTB.updateConversationSubject(conversation.getConversationID(), label);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            changeSubjectField.clear();
        });
        return changeSubjectField;
    }

    private void setChangeSubjectButton(Button changeSubjectButton, HBox conversationChoice, TextField changeSubjectField, Label conversationLabel){

        changeSubjectButton.setPrefSize(4, 4);
        changeSubjectButton.setOnAction(event -> {

            conversationChoice.getChildren().removeAll(conversationLabel, changeSubjectButton);
            conversationChoice.getChildren().add(changeSubjectField);
        });
    }
}