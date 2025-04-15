package com.groepb.project2.AI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GetDocumentation {
    public JSONArray getDocumentation() {
        JSONArray documentation = new JSONArray();

        File[] folder = new File("bijlagen/bijlagen/").listFiles();
        System.out.println(folder.length);

        for (File file : folder) {
            StringBuilder data = new StringBuilder();
            try {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    data.append(scanner.nextLine());
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            documentation.put(new JSONObject(data.toString()));
        }

        return documentation;
    }
}
