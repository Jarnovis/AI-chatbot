package com.groepb.project2.AI.BobGPT;

import java.util.Set;

public class EmotionDetector {

    public static boolean detectNegativeEmotion(String question, Set<String> negativeWords) {
        for (String word : question.toLowerCase().split("\\s+")) {
            if (negativeWords.contains(word)) {
                return true;
            }
        }
        return false;
    }

    public static boolean detectPositiveEmotion(String question, Set<String> positiveWords) {
        for (String word : question.toLowerCase().split("\\s+")) {
            if (positiveWords.contains(word)) {
                return true;
            }
        }
        return false;
    }
}
