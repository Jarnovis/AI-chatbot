package com.groepb.project2.AI;

import com.groepb.project2.data.Conversation;
import javafx.scene.control.Label;

import java.io.BufferedReader;

public interface IAI {
    String getResponse(String question, String language, Conversation conversation);
}
