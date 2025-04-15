package com.groepb.project2.AI.BobGPT;

import com.groepb.project2.data.Conversation;
import java.util.*;
import com.groepb.project2.AI.Observer;

public class ConversationHandler {
    public static String CURRENTLANGUAGE = "english";
    public static String LANGUAGE = "english";

    public static String getResponse(String question, String language, Conversation conversation) {
        if (question.toLowerCase().contains("change language")) {
            return Responses.changeLanguage(question, conversation);
        }

        String[] words = question.split("\\s+");
        for (String word : words) {
            if (word.equalsIgnoreCase("read")) {
                return Responses.readResponse(question, conversation);

            } else if (word.equalsIgnoreCase("search")) {
                return Responses.readKeywords(question, conversation);

            } else if (word.equalsIgnoreCase("list")) {
                return Responses.listResponse(conversation);
            }
        }

        try {
            Observer.getInstance().notifyObservers(handleConversation(question));
            Observer.getInstance().notifyObserversFullResponse(handleConversation(question), conversation);
            return handleConversation(question);

        } catch (Exception e) {
            return "An Error Occurred";
        }
    }

    private static String handleConversation(String question) {
        if (question.equalsIgnoreCase("change language")) {
            return "Which language would you like to switch to? You can say 'English' or 'Dutch'.";
        }

        Set<String> positiveWords = LanguageInitializer.positiveWordsMap.get(CURRENTLANGUAGE);
        Set<String> negativeWords = LanguageInitializer.negativeWordsMap.get(CURRENTLANGUAGE);

        if (EmotionDetector.detectPositiveEmotion(question, positiveWords)) {
            if (CURRENTLANGUAGE.equals("dutch")) {
                return "Dat is goed om te horen!";
            } else {
                return "That's great to hear!";
            }
        } else if (EmotionDetector.detectNegativeEmotion(question, negativeWords)) {
            if (CURRENTLANGUAGE.equals("dutch")) {
                return "Wat vervelend om te horen!";
            } else {
                return "That's unfortunate to hear!";
            }
        }

        Map<String, String> conversationMap = LanguageInitializer.conversationMaps.get(CURRENTLANGUAGE);

        if (conversationMap.containsKey(question.toLowerCase())) {
            return conversationMap.get(question.toLowerCase());
        }

        String closestMatch = getClosestMatch(question, conversationMap);
        if (closestMatch != null) {
            return conversationMap.get(closestMatch);
        }

        return Responses.handleUnknownQuestion(question);
    }

    private static String getClosestMatch(String question, Map<String, String> conversationMap) {
        int minDistance = Integer.MAX_VALUE;
        String closestMatch = null;

        for (String key : conversationMap.keySet()) {
            int distance = StringLookalike(question.toLowerCase(), key.toLowerCase());
            if (distance < minDistance) {
                minDistance = distance;
                closestMatch = key;
            }
        }

        int threshold = 3;
        if (minDistance <= threshold) {
            return closestMatch;
        } else {
            return null;
        }
    }

    // Levenshtein distance algoritme, op google gevonden
    private static int StringLookalike(String a, String b) {
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++) {
            costs[j] = j;
        }
        for (int i = 1; i <= a.length(); i++) {
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }

    public static String changeLanguage(String language) {
        if (language.equalsIgnoreCase("english")) {
            CURRENTLANGUAGE = "english";
            return "Language switched to English.";
        } else if (language.equalsIgnoreCase("dutch")) {
            CURRENTLANGUAGE = "dutch";
            return "Language switched to Dutch.";
        } else {
            return "Unsupported language. Please choose 'english' or 'dutch'.";
        }
    }
}