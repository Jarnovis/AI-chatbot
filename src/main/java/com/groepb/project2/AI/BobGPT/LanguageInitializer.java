package com.groepb.project2.AI.BobGPT;

import java.util.*;

public class LanguageInitializer {
    public static final Map<String, Map<String, String>> conversationMaps = new HashMap<>();
    public static final Map<String, Set<String>> positiveWordsMap = new HashMap<>();
    public static final Map<String, Set<String>> negativeWordsMap = new HashMap<>();
    public static final Map<String, Map<String, String>> unknownResponsesMap = new HashMap<>();

    static {
        initializeEnglishConversation();
        initializeDutchConversation();
    }

    private static void initializeEnglishConversation() {
        Map<String, String> englishConversationMap = new HashMap<>();
        Set<String> englishPositiveWords = new HashSet<>(Arrays.asList("good", "great", "awesome", "fantastic", "excellent", "amazing", "wonderful", "perfectly", "fine"));
        Set<String> englishNegativeWords = new HashSet<>(Arrays.asList("bad", "terrible", "horrible", "awful", "poor", "dreadful", "unpleasant"));
        Map<String, String> englishUnknownResponses = new HashMap<>();

        englishConversationMap.put("hello", "Hi there!");
        englishConversationMap.put("how are you?", "I'm good, thank you! How about you?");
        englishConversationMap.put("what's your name?", "My name is BobGPT.");
        englishConversationMap.put("nice to meet you!", "Likewise!");
        englishConversationMap.put("what is your purpose?", "I am here to chat with you and help with your questions.");
        englishConversationMap.put("tell me a joke", "Why don't scientists trust atoms? Because they make up everything!");
        englishConversationMap.put("what's the weather like?", "I can't check the weather, but I hope it's nice where you are!");
        englishConversationMap.put("what's your favorite color?", "I don't have a favorite color, but I think all colors are wonderful!");
        englishConversationMap.put("do you like music?", "I love the idea of music! It brings joy to many people.");
        englishConversationMap.put("can you help me?", "I'll do my best to assist you. What do you need help with?");
        englishConversationMap.put("what is your favorite food?", "I don't eat, but I've heard pizza is a favorite for many!");
        englishConversationMap.put("do you play games?", "I don't play games, but I've heard they're a lot of fun!");
        englishConversationMap.put("what do you think about AI?", "I think AI is an exciting field with lots of potential to help humanity.");
        englishConversationMap.put("change language", "changeLanguage");

        englishUnknownResponses.put("favorite game", "I don't have a favorite game, but I hear chess and video games are quite popular!");
        englishUnknownResponses.put("favorite color", "I don't have a favorite color, but I think all colors are wonderful!");
        englishUnknownResponses.put("favorite food", "I don't eat, but I've heard that pizza is a favorite for many!");
        englishUnknownResponses.put("favorite movie", "I don't watch movies, but I've heard many people love classics like 'The Shawshank Redemption' and 'The Godfather'.");
        englishUnknownResponses.put("favorite book", "I don't read books, but 'To Kill a Mockingbird' and '1984' are often recommended.");
        englishUnknownResponses.put("time", "I can't tell the time, but you can check a clock or your device for the current time.");
        englishUnknownResponses.put("day", "Every day is a good day to learn something new!");
        englishUnknownResponses.put("year", "It's always good to reflect on the past year and set new goals.");
        englishUnknownResponses.put("season", "Each season has its own beauty. What's your favorite season?");
        englishUnknownResponses.put("sport", "Sports are a great way to stay active and have fun! Do you have a favorite sport?");
        englishUnknownResponses.put("hobby", "Hobbies are a great way to relax and enjoy your free time. Do you have a favorite hobby?");

        LanguageInitializer.conversationMaps.put("english", englishConversationMap);
        LanguageInitializer.positiveWordsMap.put("english", englishPositiveWords);
        LanguageInitializer.negativeWordsMap.put("english", englishNegativeWords);
        LanguageInitializer.unknownResponsesMap.put("english", englishUnknownResponses);
    }

    private static void initializeDutchConversation() {
        Map<String, String> dutchConversationMap = new HashMap<>();
        Set<String> dutchPositiveWords = new HashSet<>(Arrays.asList("goed", "geweldig", "fantastisch", "uitstekend"));
        Set<String> dutchNegativeWords = new HashSet<>(Arrays.asList("slecht", "verschrikkelijk", "vreselijk", "afschuwelijk"));
        Map<String, String> dutchUnknownResponses = new HashMap<>();

        dutchConversationMap.put("hallo", "Hallo!");
        dutchConversationMap.put("hoe gaat het?", "Het gaat goed, dank je! Hoe gaat het met jou?");
        dutchConversationMap.put("wat is je naam?", "Mijn naam is BobGPT.");
        dutchConversationMap.put("leuk je te ontmoeten!", "Leuk je te ontmoeten!");
        dutchConversationMap.put("wat is je doel?", "Ik ben hier om met je te chatten en je vragen te beantwoorden.");
        dutchConversationMap.put("vertel me een grap", "Waarom vertrouwen wetenschappers atomen niet? Omdat ze alles vormen!");
        dutchConversationMap.put("hoe is het weer?", "Ik kan het weer niet controleren, maar ik hoop dat het mooi is waar je bent!");
        dutchConversationMap.put("wat is je favoriete kleur?", "Ik heb geen favoriete kleur, maar ik denk dat alle kleuren prachtig zijn!");
        dutchConversationMap.put("hou je van muziek?", "Ik vind muziek geweldig! Het brengt vreugde bij veel mensen.");
        dutchConversationMap.put("kan je me helpen?", "Ik zal mijn best doen om je te helpen. Waarmee heb je hulp nodig?");
        dutchConversationMap.put("wat is je favoriete eten?", "Ik eet niet, maar ik heb gehoord dat pizza favoriet is bij velen!");
        dutchConversationMap.put("speel je games?", "Ik speel geen games, maar ik heb gehoord dat ze erg leuk zijn!");
        dutchConversationMap.put("wat vind je van AI?", "Ik denk dat AI een spannend gebied is met veel potentieel om de mensheid te helpen.");
        dutchConversationMap.put("verander taal", "changeLanguage");

        dutchUnknownResponses.put("favoriete spel", "Ik heb geen favoriet spel, maar ik hoor dat schaken en videogames behoorlijk populair zijn!");
        LanguageInitializer.conversationMaps.put("dutch", dutchConversationMap);
        LanguageInitializer.positiveWordsMap.put("dutch", dutchPositiveWords);
        LanguageInitializer.negativeWordsMap.put("dutch", dutchNegativeWords);
        LanguageInitializer.unknownResponsesMap.put("dutch", dutchUnknownResponses);
    }
}
