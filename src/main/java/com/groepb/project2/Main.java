package com.groepb.project2;

import com.groepb.project2.Windows.FrameWorkGUI;
import javafx.application.Application;

import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        FrameWorkGUI.start(stage);
    }
}