package com.groepb.project2.AI.BobGPT;

import com.groepb.project2.AI.Observer;
import com.groepb.project2.data.Conversation;

import java.util.List;
import java.util.Map;

public class Responses {
    public static String changeLanguage(String question,  Conversation conversation){
        String[] words = question.split("\\s+");
        ConversationHandler.LANGUAGE = words[words.length - 1].toLowerCase();
        System.out.println(ConversationHandler.LANGUAGE);
        notifyObservers(changeLanguage(ConversationHandler.LANGUAGE), conversation);
        return changeLanguage(ConversationHandler.LANGUAGE);
    }

    public static String readResponse(String question, Conversation conversation){
        String fileName = extractFileName(question);
        notifyObservers(FileOperations.readFileContent(fileName), conversation);
        return FileOperations.readFileContent(fileName);
    }

    public static String readKeywords(String question, Conversation conversation) {
        String keywords = extractKeywords(question);
        notifyObservers(FileOperations.searchFileContent(keywords), conversation);
        return FileOperations.searchFileContent(keywords);
    }

    public static String listResponse(Conversation conversation) {
        List<String> fileList = FileOperations.listFiles();
        StringBuilder response = new StringBuilder();
        response.append("Available files: ");

        if (!fileList.isEmpty()) {
            for (int i = 0; i < fileList.size(); i++) {
                if (i > 0) {
                    response.append(", ");
                }
                response.append(fileList.get(i));
            }
            response.append("\n");
        }
        else {
            response.append("No files found.\n");
        }
        notifyObservers(response.toString(), conversation);
        return response.toString();
    }

    public static String handleUnknownQuestion(String question) {
        String lowerCaseQuestion = question.toLowerCase();
        Map<String, String> unknownResponses = LanguageInitializer.unknownResponsesMap.get(ConversationHandler.CURRENTLANGUAGE);

        for (Map.Entry<String, String> entry : unknownResponses.entrySet()) {
            if (lowerCaseQuestion.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        return "I'm not sure how to respond to that.";
    }

    public static void notifyObservers(String response, Conversation conversation) {
        Observer.getInstance().notifyObservers(response);
        Observer.getInstance().notifyObserversFullResponse(response, conversation);
    }

    private static String extractFileName(String question) {
        String[] words = question.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            if (words[i].equalsIgnoreCase("file") && i < words.length - 1) {
                return words[i + 1].trim();
            }
        }
        return "";
    }

    private static String extractKeywords(String question) {
        String[] words = question.split("\\s+");
        StringBuilder keywordsBuilder = new StringBuilder();
        boolean startAppending = false;

        for (String word : words) {
            if (word.equalsIgnoreCase("file")) {
                startAppending = true;
                continue;
            }
            if (startAppending) {
                keywordsBuilder.append(word).append(" ");
            }
        }
        return keywordsBuilder.toString().trim();
    }

    private static String changeLanguage(String language) {
        if (language.equalsIgnoreCase("english")) {
            ConversationHandler.CURRENTLANGUAGE = "english";
            return "Language switched to English.";
        } else if (language.equalsIgnoreCase("dutch")) {
            ConversationHandler.CURRENTLANGUAGE = "dutch";
            return "Language switched to Dutch.";
        } else {
            return "Unsupported language. Please choose 'english' or 'dutch'.";
        }
    }
}
