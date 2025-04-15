package com.groepb.project2.Windows;

import com.groepb.project2.Database.RegisterUser;
import com.groepb.project2.Encryptie.Encryptie;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import com.groepb.project2.Database.ConnectDB;
import com.groepb.project2.data.Environment;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class Registreren extends FrameWorkGUI {
    private final StackPane rootPane;
    private PasswordField passwordField;
    private TextField emailField;
    private VBox layout;
    private Text titleLabel;
    private final ImageView imageView = new SpinningBob().bobImage();
    private Button registerButton;
    private String[] fields;
    Errors getError;

    public Registreren() {
        rootPane = new StackPane();
        setup();
        createLayout();
        getError = new Errors(layout);
        rootPane.getChildren().add(layout);
    }

    private void createLayout() {
        layout = new VBox(10, titleLabel, imageView, emailField, passwordField, registerButton, createHBox());
        layout.getStyleClass().add("layout");
    }

    private void setup(){
        titleLabel = new Text("Bob-GPT: Registreren");
        titleLabel.getStyleClass().add("bob");

        passwordField = createPasswordField("Wachtwoord");
        emailField = createTextField("Email");

        emailField.getStyleClass().add("custom-text-field");
        passwordField.getStyleClass().add("custom-text-field");

        registerButton = createButton("Registreren");
        registerButton.setOnAction(e -> handleRegistration());
        registerButton.getStyleClass().add("registerButton");

        passwordField.setOnKeyPressed(this::onEnterPress);
        emailField.setOnKeyPressed(this::onEnterPress);
    }

    private HBox createHBox(){
        HBox box = new HBox();
        box.getStyleClass().add("box");
        Text text = new Text("Al een account?");
        Hyperlink accountLink = new Hyperlink("Druk hier");

        accountLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ROOTPANE.getChildren().clear();
                ROOTPANE.getChildren().add(new LoginScreen().getRootPane());
            }
        });

        box.getChildren().addAll(text, accountLink);
        return box;
    }

    private void handleRegistration() {
        getError.removeLabels();
        boolean hasError = false;

        try {
            getError.updateFields(fields = new String[]{passwordField.getText(), emailField.getText()});
            boolean emailInvalid = getError.invalidEmail();
            boolean emailUsed = getError.emailInUse();
            boolean passwordInvalid = getError.invalidPassword();

            hasError = emailInvalid || emailUsed || passwordInvalid;
        } catch (Exception ignored) {}

        if (!hasError) { writeToDataBase(); }
    }

    private void writeToDataBase(){
        RegisterUser registerUser = new RegisterUser(new ConnectDB(new Environment()));
        registerUser.registerNewUser(fields[1], new Encryptie().encrypt(fields[0]));

        ROOTPANE.getChildren().clear();
        ROOTPANE.getChildren().add(new LoginScreen().getRootPane());
    }

    private void onEnterPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            registerButton.fire();
        }
    }

    public StackPane getRootPane() {
        return rootPane;
    }

    @Override
    protected void placement(){}
}
