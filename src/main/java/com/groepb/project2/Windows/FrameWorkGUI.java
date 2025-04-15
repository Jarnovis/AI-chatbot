package com.groepb.project2.Windows;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;

import java.util.Objects;

public abstract class FrameWorkGUI {
    public static Stage STAGE;
    protected static Scene SCENE;
    public static StackPane ROOTPANE = new StackPane();
    protected static Pane UP = new Pane();
    protected static Pane CENTER = new Pane();
    protected static Pane DOWN = new Pane();
    protected Button HOMEBUTTON = createButton("Home");
    protected final String css = getClass().getResource("/com.groepb.project2/Windows/windows.css").toExternalForm();

    protected static final int INLOG = 0;
    protected static final int REGISTREREN = 1;
    protected static final int MAIN = 2;

    public FrameWorkGUI(){
        ROOTPANE.getStyleClass().add("rootPane");
    }
    abstract void placement();

    protected Button createButton(String text) {
        Button btn = new Button(text);
        btn.setPrefSize(100, 75);
        return btn;
    }

    protected void actionHomeButton(int window, Button btn) {
        btn.setOnAction(e -> {
            // Clear the screen
            ROOTPANE.getChildren().clear();

            // Set the next screen
            if (window == INLOG) {
                //rootPane.getChildren().add(new InlogGUI().getRootPane());
                System.out.println("Home");
            }
        });
    }

    protected void setWindowTitle(String title) {
        STAGE.setTitle(title);
    }

    public Pane getRootPane() {
        return ROOTPANE;
    }

    protected void setPreferredSizePane(Pane pane, int width, int height) {
        pane.setPrefSize(width, height);
    }

    protected void addCss() {
        SCENE.getStylesheets().add(css);
        UP.getStyleClass().add("up");
    }

    protected void setCss(Button btn) {
        btn.setStyle(css);
    }

    protected TextField createTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        return textField;
    }

    protected PasswordField createPasswordField(String promptText) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(promptText);
        return passwordField;
    }

    protected ComboBox createComboBox(String[] items){
        ComboBox box = new ComboBox();
        box.setItems(FXCollections.observableArrayList(items));
        return box;
    }


    private static void setTaskbarIcon(Stage stage, String iconPath) {
        try {
            Image icon = new Image(Objects.requireNonNull(FrameWorkGUI.class.getResourceAsStream(iconPath)));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        protected ScrollPane createScrollPane() {
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefHeight(200);
            scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

            return scrollPane;
        }

    public static void start(Stage stage) {
        STAGE = stage;
        ROOTPANE.setPrefSize(800,600);
        STAGE.setResizable(true);
        STAGE.setTitle("BobGPT");

        setTaskbarIcon(stage, "/bobgpt.png");

        StackPane rootPane = new StackPane();
        rootPane.setPrefSize(800,600);
        rootPane.getChildren().add(new LoginScreen().getRootPane());
        rootPane.getStyleClass().add("rootPane");

        Scene scene = new Scene(rootPane);
        scene.getStylesheets().add(FrameWorkGUI.class.getResource("/com.groepb.project2/Windows/windows.css").toExternalForm());
        STAGE.setScene(scene);
        STAGE.show();
    }
}