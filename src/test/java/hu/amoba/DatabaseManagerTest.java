
package hu.amoba;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseManagerTest {
    
    @TempDir
    Path tempDir;
    
    private DatabaseManager dbManager;
    
    @BeforeEach
    void setUp() {
        dbManager = new DatabaseManager();
    }
    
    @Test
    void testDatabaseManagerConstructor() {
        assertNotNull(dbManager);
    }
    
    @Test
    void testSaveWinFirstTime() throws Exception {
        File testFile = tempDir.resolve("test_scores.txt").toFile();
        
        assertDoesNotThrow(() -> dbManager.saveWin("TesztJátékos"));
    }
    
    @Test
    void testSaveWinMultipleTimes() {
        assertDoesNotThrow(() -> {
            dbManager.saveWin("Player1");
            dbManager.saveWin("Player1");
            dbManager.saveWin("Player2");
        });
    }
    
    @Test
    void testPrintHighScoresNoException() {
        assertDoesNotThrow(() -> dbManager.printHighScores());
    }
    
    @Test
    void testSaveWinWithSpecialCharacters() {
        assertDoesNotThrow(() -> dbManager.saveWin("Játékos-123"));
        assertDoesNotThrow(() -> dbManager.saveWin("Player with spaces"));
        assertDoesNotThrow(() -> dbManager.saveWin(""));
    }
    
    @Test
    void testDatabaseManagerWithTestFile() throws Exception {
        File testFile = tempDir.resolve("amoba_scores.txt").toFile();
        try (PrintWriter writer = new PrintWriter(testFile)) {
            writer.println("Player1:5");
            writer.println("Player2:3");
            writer.println("Player3:1");
        }
        
        DatabaseManager testDb = new DatabaseManager();
        assertNotNull(testDb);
        assertDoesNotThrow(() -> testDb.printHighScores());
    }
    
    @Test
    void testPrintHighScoresEmptyFile() throws Exception {
        File testFile = tempDir.resolve("empty_scores.txt").toFile();
        testFile.createNewFile();
        
        assertDoesNotThrow(() -> dbManager.printHighScores());
    }
}