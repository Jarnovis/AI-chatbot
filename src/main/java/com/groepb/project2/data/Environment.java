package com.groepb.project2.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class Environment extends HashMap<String, String> {
    public Environment() {
        String dir = System.getProperty("com.groepb.project2.user.dir");
        Path path = Paths.get(".env");
        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                String[] pair = line.split("=", 2);
                if (pair.length != 2) continue;
                put(pair[0], pair[1]);
            }

        } catch (IOException e) {
            System.err.println("Fout in .env file in " + dir);
        }
    }

    public boolean isConfigured() {
        return containsKey("DB_URL") && containsKey("DB_USER") && containsKey("DB_PASS");
    }
}