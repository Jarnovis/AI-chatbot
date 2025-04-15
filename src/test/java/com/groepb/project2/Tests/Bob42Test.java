package com.groepb.project2.Tests;

import com.groepb.project2.AI.ConnectToHost;
import com.groepb.project2.AI.GetDocumentation;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Bob42Test {
    private static HttpURLConnection CONNECTION;
    @Test
    @Order(1)
    public void testConnectionOllama() {
        try{
            setupConnection();

            try (OutputStreamWriter writer = new OutputStreamWriter(CONNECTION.getOutputStream(), "UTF-8")) {
                writer.write(createPayLoad("English", "Why is the sky blue?").toString());
                writer.flush();
            }

            Assertions.assertEquals(HttpURLConnection.HTTP_OK, CONNECTION.getResponseCode());
        } catch (Exception e){
            Assertions.fail();
        }
    }

    @Test
    @Order(2)
    public void getResponseInternet() {
        getResponse();
    }

    @Test
    @Order(3)
    public void getResponseWithoutInternet(){
        disableInternet();
        System.out.println("Running");
        testConnectionOllama();
        getResponse();
        enableInternet();
    }

    private JSONObject createPayLoad(String language, String question){
        JSONObject requestPayLoad = new JSONObject();
        requestPayLoad.put("model", "Bob42");
        requestPayLoad.put("language", language);
        requestPayLoad.put("prompt", "User documentation: " + new GetDocumentation().getDocumentation() + ". Question: " + question + ". Language: " + language);
        return requestPayLoad;
    }

    @Test
    @Order(4)
    public void giveDocumentation(){
        Assert.assertTrue(new GetDocumentation().getDocumentation() != null);
    }

    private void setupConnection(){
        CONNECTION = new ConnectToHost().getConnection();
        System.out.println(CONNECTION.getURL());
    }

    private void getResponse(){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(CONNECTION.getInputStream(), "UTF-8"))) {
            String response = reader.readLine();
            System.out.println(response);
            Assertions.assertNotNull(response);

        } catch (Exception ignored) {
            Assertions.fail();
        }
    }

    private void disableInternet(){
        try{
            Runtime.getRuntime().exec("cmd /c ipconfig /release");
            Thread.sleep(3000);
        } catch (Exception ignored) {}
    }

    private void enableInternet(){
        try{
            Runtime.getRuntime().exec("cmd /c ipconfig /renew");
        } catch (Exception ignored) {}
    }
}
