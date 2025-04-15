package com.groepb.project2.AI.BobGPT;

import java.util.ArrayList;
import java.util.List;

public class FileOperations {
    private static List<String> backupFiles = new ArrayList<>();

    public static void clearBackupList() {
        backupFiles.clear();
    }

    public static String readFileContent(String fileName) {
        return ElasticSearch.readFileContent(fileName);
    }

    public static String searchFileContent(String keywords) {
        List<String> searchResults = ElasticSearch.searchFilesForKeywords(keywords);
        if (!searchResults.isEmpty()) {
            StringBuilder response = new StringBuilder();
            response.append("Search results for keywords: ").append(keywords).append("\n");
            for (String result : searchResults) {
                response.append(result).append("\n");
            }
            return response.toString();
        } else {
            return "No content found matching keywords: " + keywords;
        }
    }

    public static List<String> listFiles() {
        return ElasticSearch.listFiles();
    }
}
