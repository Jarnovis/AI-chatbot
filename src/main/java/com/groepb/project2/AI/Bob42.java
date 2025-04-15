package com.groepb.project2.AI;

import com.groepb.project2.data.Conversation;
import javafx.concurrent.Task;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;

public class Bob42 implements IAI {
    @Override
    public String getResponse(String question, String language, Conversation conversation) {
        StringBuilder response = new StringBuilder();
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    HttpURLConnection connection = new ConnectToHost().getConnection();

                    try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8")) {
                        writer.write(createPayLoad(language, question).toString());
                        writer.flush();
                    }

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                gatherResponse(response, line);
                            }
                        }
                    } else {
                        Observer.getInstance().notifyObservers("Not able to connect to Bob42");
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Observer.getInstance().notifyObserversFullResponse(response.toString(), conversation);
                return null;
            }
        };

        new Thread(task).start();

        return response.toString();
    }

    private JSONObject createPayLoad(String language, String question){
        JSONObject requestPayLoad = new JSONObject();
        requestPayLoad.put("model", "Bob42");
        requestPayLoad.put("language", language);
        requestPayLoad.put("prompt", "User documentation: " + new GetDocumentation().getDocumentation() + ". Question: " + question + ". Language: " + language);
        return requestPayLoad;
    }

    private void gatherResponse(StringBuilder response, String line) {
        JSONObject jsonObject = new JSONObject(line);
        Observer.getInstance().notifyObservers(jsonObject.getString("response"));
        response.append(jsonObject.getString("response"));
    }


}
