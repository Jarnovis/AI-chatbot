package com.groepb.project2.AI;

import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectToHost {
    public HttpURLConnection getConnection() {
        try{
            URL url = new URL("http://localhost:11434/api/generate");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            return connection;
        } catch (Exception ignored) {
            return null;
        }
    }
}
