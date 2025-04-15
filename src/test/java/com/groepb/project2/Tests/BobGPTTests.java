package com.groepb.project2.Tests;

import com.groepb.project2.AI.BobGPT.BobGPT;
import com.groepb.project2.AI.BobGPT.FileOperations;
import com.groepb.project2.AI.BobGPT.Responses;
import com.groepb.project2.AI.BobGPT.ElasticSearch;
import com.groepb.project2.AI.Observer;
import com.groepb.project2.data.Conversation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.groepb.project2.AI.BobGPT.ConversationHandler.changeLanguage;
import static org.junit.jupiter.api.Assertions.*;

public class BobGPTTests {
    private BobGPT bobGPT;
    private Conversation conversation;
    private static List<String> initialFilesContent = new ArrayList<>();

    @BeforeAll
    static void gatherInitialFiles() {
        File[] files = new File("bijlagen/bijlagen/").listFiles();
        if (files != null) {
            for (File file : files) {
                String content = FileOperations.readFileContent(file.getName());
                initialFilesContent.add(file.getName() + ":" + content);
            }
        }
    }

    @AfterAll
    static void restoreInitialFiles() {
        ElasticSearch.clearFiles();
        for (String fileContent : initialFilesContent) {
            String[] parts = fileContent.split(":", 2);
            if (parts.length == 2) {
                ElasticSearch.addFile(parts[0], parts[1]);
            }
        }
    }

    @BeforeEach
    void setUp() {
        bobGPT = new BobGPT();
        conversation = new Conversation("test", 1);
        Observer.initInstance();
    }

    @AfterEach
    void tearDown() {
        ElasticSearch.clearFiles();
        FileOperations.clearBackupList();
    }

    @Test
    void testReadFileContent() {
        ElasticSearch.addFile("TestFile.txt", "Een heel leuk en mooi pannenkoeken recept");
        String fileName = "TestFile.txt";
        String expectedContent = "Een heel leuk en mooi pannenkoeken recept";
        assertEquals(expectedContent, FileOperations.readFileContent(fileName));
    }

    @Test
    void testSearchFileContent() {
        ElasticSearch.addFile("supercoolpancakerecipe", "A cool new pancake recipe for the whole family.");
        String keywords = "pancake";
        String expectedContent = "Search results for keywords: pancake\n" +
                "File: supercoolpancakerecipe\n" +
                "Content: A cool new pancake recipe for the whole family.\n" +
                "\n";
        assertEquals(expectedContent, FileOperations.searchFileContent(keywords));
    }

    @Test
    void testHandleUnknownQuestion() {
        assertEquals("I'm not sure how to respond to that.", Responses.handleUnknownQuestion("random question"));
    }

    @Test
    void testListFilesEquivalentClasses() {
        assertEquals(0, FileOperations.listFiles().size(), "Expected an empty list of files.");

        ElasticSearch.addFile("File1.txt", "Even wat test content");
        assertEquals(1, FileOperations.listFiles().size(), "Expected exactly one file in the list.");

        ElasticSearch.addFile("File2.txt", "Even wat test content");
        assertTrue(FileOperations.listFiles().size() > 1, "Expected more than one file in the list.");

        ElasticSearch.addFile("File3.txt", "Even wat test content");
        ElasticSearch.addFile("File4.txt", "Even wat test content");
        ElasticSearch.addFile("File5.txt", "Even wat test content");
        ElasticSearch.addFile("File6.txt", "Even wat test content");
        ElasticSearch.addFile("File7.txt", "Even wat test content");
        assertEquals(7, FileOperations.listFiles().size(), "Expected exactly 7 files in the list.");

        ElasticSearch.addFile("File8.txt", "Even wat test content");
        assertTrue(FileOperations.listFiles().size() > 7, "Expected more than 7 files in the list after adding one more file.");
    }

    @Test
    void testChangeLanguageBeslissingstabellen() {
        assertEquals("Language switched to English.", bobGPT.getResponse("change language english", "english", conversation));
        assertEquals("Language switched to Dutch.", bobGPT.getResponse("change language dutch", "english", conversation));
        assertEquals("Unsupported language. Please choose 'english' or 'dutch'.", bobGPT.getResponse("change language french", "english", conversation));
    }

    @Test
    void testChangeLanguageToDutch() {
        changeLanguage("dutch");
        assertEquals("Language switched to Dutch.", bobGPT.getResponse("change language dutch", "english", conversation));
        assertEquals("Hallo!", bobGPT.getResponse("hallo", "dutch", conversation));
        assertEquals("Het gaat goed, dank je! Hoe gaat het met jou?", bobGPT.getResponse("hoe gaat het?", "dutch", conversation));
        assertEquals("Mijn naam is BobGPT.", bobGPT.getResponse("wat is je naam?", "dutch", conversation));
    }

    @Test
    void testEnglishConversation() {
        changeLanguage("english");
        assertEquals("Hi there!", bobGPT.getResponse("hello", "english", conversation));
        assertEquals("I'm good, thank you! How about you?", bobGPT.getResponse("how are you?", "english", conversation));
        assertEquals("My name is BobGPT.", bobGPT.getResponse("what's your name?", "english", conversation));
    }

    @Test
    void testDutchConversation() {
        assertEquals("Hallo!", bobGPT.getResponse("hallo", "dutch", conversation));
        assertEquals("Het gaat goed, dank je! Hoe gaat het met jou?", bobGPT.getResponse("hoe gaat het?", "dutch", conversation));
        assertEquals("Mijn naam is BobGPT.", bobGPT.getResponse("wat is je naam?", "dutch", conversation));
    }

    @Test
    void testChangeLanguage() {
        assertEquals("Language switched to English.", bobGPT.getResponse("change language english", "english", conversation));
        assertEquals("Hi there!", bobGPT.getResponse("hello", "english", conversation));
        assertEquals("I'm good, thank you! How about you?", bobGPT.getResponse("how are you?", "english", conversation));
        assertEquals("My name is BobGPT.", bobGPT.getResponse("what's your name?", "english", conversation));

        assertEquals("Language switched to Dutch.", bobGPT.getResponse("change language dutch", "english", conversation));
        assertEquals("Hallo!", bobGPT.getResponse("hallo", "dutch", conversation));
        assertEquals("Het gaat goed, dank je! Hoe gaat het met jou?", bobGPT.getResponse("hoe gaat het?", "dutch", conversation));
        assertEquals("Mijn naam is BobGPT.", bobGPT.getResponse("wat is je naam?", "dutch", conversation));

        assertEquals("Unsupported language. Please choose 'english' or 'dutch'.", bobGPT.getResponse("change language french", "english", conversation));
    }
}
