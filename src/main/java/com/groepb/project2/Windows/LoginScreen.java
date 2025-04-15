package com.groepb.project2.Windows;

import com.groepb.project2.Database.ConnectDB;
import com.groepb.project2.Database.GetFromDB;
import com.groepb.project2.Encryptie.Decryptie;
import com.groepb.project2.data.Environment;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.sql.SQLException;

public class LoginScreen extends FrameWorkGUI {
    private final GetFromDB getFromDB;
    private final ImageView imageView = new SpinningBob().bobImage();

    public LoginScreen() {
        ROOTPANE = createRootPane();
        ConnectDB connectDB = new ConnectDB(new Environment());
        getFromDB = new GetFromDB(connectDB);
    }

    private StackPane createRootPane() {
        VBox rootPane = new VBox();
        rootPane.setPadding(new Insets(20));
        rootPane.setAlignment(Pos.CENTER);
        rootPane.setStyle("-fx-background-color: #FFFFFF;");

        rootPane.getChildren().addAll(createTitleBox(), imageView, createFormPane(rootPane));
        return new StackPane(rootPane);
    }

    private HBox createTitleBox() {
        Text titleLabel = new Text("Bob-GPT");
        titleLabel.setFont(Font.font("Arial", 50));
        titleLabel.setFill(Color.BLACK);

        HBox titleBox = new HBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        return titleBox;
    }

    private VBox createFormPane(VBox rootPane) {
        VBox formPane = new VBox(10);
        formPane.setAlignment(Pos.CENTER);
        formPane.setPadding(new Insets(10));
        formPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-background-radius: 10;");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMaxWidth(300);
        emailField.setStyle("-fx-border-color: #10a37f; -fx-font-size: 16px; -fx-padding: 10;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Wachtwoord");
        passwordField.setMaxWidth(300);
        passwordField.setStyle("-fx-border-color: #10a37f; -fx-font-size: 16px; -fx-padding: 10;");

        Button loginButton = new Button("Inloggen");
        loginButton.setStyle("-fx-background-color: #10a37f; -fx-text-fill: white; -fx-padding: 12 130; -fx-font-size: 16px;");
        loginButton.setDefaultButton(true);

        Label errorMessageLabel = new Label();
        errorMessageLabel.setTextFill(Color.RED);

        loginButton.setOnAction(event -> handleLogin(emailField, passwordField, errorMessageLabel));

        formPane.getChildren().addAll(emailField, passwordField, loginButton, errorMessageLabel, createNoAccountBox(rootPane));
        return formPane;
    }

    private HBox createNoAccountBox(VBox rootPane) {
        Text noAccountText = new Text("Geen account?");
        noAccountText.setFont(Font.font("Arial", 15));

        Hyperlink signUpLink = new Hyperlink("Registreren");
        signUpLink.setFont(Font.font("Arial", 15));
        signUpLink.setOnAction(event -> {
            rootPane.getChildren().clear();
            rootPane.getChildren().add(new Registreren().getRootPane());
        });

        HBox noAccountBox = new HBox(5, noAccountText, signUpLink);
        noAccountBox.setAlignment(Pos.CENTER);
        return noAccountBox;
    }

    private void handleLogin(TextField emailField, PasswordField passwordField, Label errorMessageLabel) {
        String email = emailField.getText();
        String password = passwordField.getText();

        try {
            boolean correctLogin = new Decryptie().decrypt(email, password);
            if (correctLogin) {
                int userId = getFromDB.getUID(email);
                errorMessageLabel.setText("Ingelogd");

                ROOTPANE.getChildren().clear();
                ROOTPANE.getChildren().add(new MainGUI(email, userId).getRootPane());
            } else {
                errorMessageLabel.setText("Verkeerde gebruiksersnaam of wachtwoord!");
            }
        } catch (SQLException e) {
            errorMessageLabel.setText("Database Error");
            e.printStackTrace();
        }
    }

    @Override
    protected void placement() {
    }
}