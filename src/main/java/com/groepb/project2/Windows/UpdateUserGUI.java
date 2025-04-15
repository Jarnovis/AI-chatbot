package com.groepb.project2.Windows;

import com.groepb.project2.Database.*;
import com.groepb.project2.data.Environment;
import com.groepb.project2.data.Utils;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UpdateUserGUI extends FrameWorkGUI {
    private StackPane rootPane;
    private PasswordField passwordField;
    private TextField currentEmailField;
    private TextField emailField;
    private VBox layout;

    private Map<String, Label> errorLabels = new HashMap<>();

    public UpdateUserGUI(String currentUserEmail, int userID) {
        rootPane = new StackPane();
        Text titleLabel = new Text("Bob-GPT: Gebruiker bijwerken");
        titleLabel.getStyleClass().add("bob");

        emailField = createTextField("Nieuwe Email");
        passwordField = createPasswordField("Nieuw Wachtwoord");

        currentEmailField = new TextField();
        currentEmailField.setText(currentUserEmail);
        currentEmailField.setEditable(false);

        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-background-color: #FFFFFF");

        Button updateButton = createButton("Gebruiker bijwerken");
        updateButton.setOnAction(e -> handleUpdate());
        updateButton.getStyleClass().add("registerButton");

        Button backButton = new Button("Terug");
        backButton.getStyleClass().add("backButton");
        backButton.setOnAction(e -> {
            try {
                new MainGUI(currentUserEmail, userID);
                ROOTPANE.getChildren().clear();
                ROOTPANE.getChildren().add(new MainGUI(currentUserEmail, userID).getRootPane());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button deleteButton = new Button("Verwijder Account");
        deleteButton.getStyleClass().add("deleteButton");
        deleteButton.setOnAction(e -> {
            try {
                DeleteConversations deleteConversations= new DeleteConversations(new ConnectDB(new Environment()));
                DeleteUser deleteUser = new DeleteUser(new ConnectDB(new Environment()));
                deleteConversations.deleteUserConversations(currentUserEmail);
                deleteUser.deleteUser(currentUserEmail);

                ROOTPANE.getChildren().removeAll();
                ROOTPANE.getChildren().add(new LoginScreen().getRootPane());
            } catch (Error error) {
                throw new RuntimeException(error);
            }
        });

        layout = new VBox(10.0, titleLabel, currentEmailField, emailField, passwordField, updateButton, backButton, deleteButton);
        layout.getStyleClass().add("layout");

        pane.setCenter(layout);

        rootPane.getChildren().add(pane);

        emailField.getStyleClass().add("custom-text-field");
        passwordField.getStyleClass().add("custom-text-field");
        currentEmailField.getStyleClass().add("custom-text-field");

        initializeErrorLabels();
        setCSS();
    }

    private void initializeErrorLabels(){
        errorLabels.put("InvalidEmail", createErrorLabel("Ongeldige email"));
        errorLabels.put("InvalidPassword", createErrorLabel("Wachtwoord is niet ingevuld"));
        errorLabels.put("EmailInUse", createErrorLabel("Email al in gebruik"));
    }

    private Label createErrorLabel(String error){
        Label label = new Label(error);
        label.getStyleClass().add("error");
        return label;
    }

    private void handleUpdate() {
        String currentEmail = currentEmailField.getText();
        String newPassword = passwordField.getText();
        String newEmail = emailField.getText();

        CheckUser checkUser = new CheckUser(new ConnectDB(new Environment()));
        UpdateUser updateUser = new UpdateUser(new ConnectDB(new Environment()));
        boolean hasError = false;

        layout.getChildren().removeAll(errorLabels.values());

        try {
            if (!Utils.isEmail(newEmail)) {
                layout.getChildren().add(errorLabels.get("InvalidEmail"));
                hasError = true;
            } else if (checkUser.checkEmail(newEmail)) {
                layout.getChildren().add(errorLabels.get("EmailInUse"));
                hasError = true;
            }

            if (newPassword.isEmpty()){
                layout.getChildren().add(errorLabels.get("InvalidPassword"));
                hasError = true;
            }

            if (!hasError) {
                updateUser.updateUser(currentEmail, newEmail, newPassword);
                System.out.println("User Updated.");
                ROOTPANE.getChildren().removeAll();
                ROOTPANE.getChildren().add(new LoginScreen().getRootPane());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void placement() {
    }

    public StackPane getRootPane() {
        return rootPane;
    }

    private void setCSS() {
        emailField.getStyleClass().add("custom-text-field");
    }
}
