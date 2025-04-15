package com.groepb.project2.AI.BobGPT;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ElasticSearch {

    private static final String DATA_DIRECTORY = "bijlagen" + File.separator + "bijlagen";

    public static List<String> listFiles() {
        List<String> fileList = new ArrayList<>();

        Path directoryPath = Paths.get(DATA_DIRECTORY);

        if (Files.exists(directoryPath) && Files.isDirectory(directoryPath)) {
            File[] files = directoryPath.toFile().listFiles();
            if (files != null) {
                for (File file : files) {
                    fileList.add(file.getName());
                }
            }
        }

        return fileList;
    }

    public static String readFileContent(String fileName) {
        Path filePath = getDirectoryPath().resolve(fileName);

        try {
            if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
                return Files.readString(filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "File '" + fileName + "' not found.";
    }

    public static List<String> searchFilesForKeywords(String keywords) {
        List<String> searchResults = new ArrayList<>();
        String[] keywordArray = keywords.split("\\s+");

        List<String> fileList = listFiles();
        Path directoryPath = getDirectoryPath();

        for (String fileName : fileList) {
            Path filePath = directoryPath.resolve(fileName);

            try {
                if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
                    String content = Files.readString(filePath);

                    boolean containsKeywords = Arrays.stream(keywordArray)
                            .allMatch(keyword -> content.toLowerCase().contains(keyword.toLowerCase()));

                    if (containsKeywords) {
                        searchResults.add("File: " + fileName + "\nContent: " + content + "\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return searchResults;
    }

    public static void clearFiles() {
        Path directoryPath = getDirectoryPath();

        if (directoryPath != null && Files.exists(directoryPath) && Files.isDirectory(directoryPath)) {
            File[] files = directoryPath.toFile().listFiles();
            if (files != null) {
                for (File file : files) {
                    try {
                        Files.deleteIfExists(file.toPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void addFile(String fileName, String fileContent) {
        Path directoryPath = getDirectoryPath();
        Path filePath = directoryPath.resolve(fileName);

        try {
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Files.write(filePath, fileContent.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Path getDirectoryPath() {
        Path basePath = Paths.get("").toAbsolutePath();
        return basePath.resolve(DATA_DIRECTORY);
    }
}
