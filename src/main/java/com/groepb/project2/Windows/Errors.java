package com.groepb.project2.Windows;

import com.groepb.project2.Database.CheckUser;
import com.groepb.project2.Database.ConnectDB;
import com.groepb.project2.data.Environment;
import com.groepb.project2.data.Utils;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Errors {
    private final Map<String, Label> errorLabels = new HashMap<>();
    private VBox layout;
    private String[] fields;
    public Errors(VBox layout){
        this.layout = layout;
        initalizeErrorLabels();
    }

    public void updateFields(String[] fields){
        this.fields = fields;
    }

    public void removeLabels(){
        layout.getChildren().removeAll(errorLabels.get("InvalidEmail"), errorLabels.get("EmailInUse"), errorLabels.get("InvalidPassword"));
    }

    private void initalizeErrorLabels(){
        errorLabels.put("InvalidEmail", createErrorLabel("Ongeldige email"));
        errorLabels.put("InvalidPassword", createErrorLabel("Wachtwoord is niet ingevuld"));
        errorLabels.put("EmailInUse", createErrorLabel("Email al in gebruik"));
    }

    private Label createErrorLabel(String error){
        Label label = new Label(error);
        label.getStyleClass().add("error");
        return label;
    }

    public boolean invalidEmail(){
        if (!Utils.isEmail(fields[1])){
            layout.getChildren().add(errorLabels.get("InvalidEmail"));
            return true;
        }
        return false;
    }

    public boolean emailInUse() throws IOException {
        if (new CheckUser(new ConnectDB(new Environment())).checkEmail(fields[1])){
            layout.getChildren().add(errorLabels.get("EmailInUse"));
            return true;
        }
        return false;
    }

    public boolean invalidPassword(){
        if (fields[0].isEmpty()){
            layout.getChildren().add(errorLabels.get("InvalidPassword"));
            return true;
        }
        return false;
    }
}
