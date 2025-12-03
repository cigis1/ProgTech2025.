
package hu.amoba;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.nio.file.Path;
import java.util.Scanner;
import java.lang.reflect.Method;

class GameTest {
    private Game game;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    
    @TempDir
    Path tempDir;
    
    @BeforeEach
    void setUp() {
        game = new Game();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }
    
    @Test
    void testConstructor() {
        assertNotNull(game);
        try {
            java.lang.reflect.Field computerField = Game.class.getDeclaredField("computerPlayer");
            computerField.setAccessible(true);
            ComputerPlayer computer = (ComputerPlayer) computerField.get(game);
            assertNotNull(computer);
            assertEquals("Számítógép", computer.getName());
            assertEquals('O', computer.getSymbol());
        } catch (Exception e) {
            fail("Constructor failed: " + e.getMessage());
        }
    }
    
    @Test
    void testParsePosition_Valid() throws Exception {
        Method method = Game.class.getDeclaredMethod("parsePosition", String.class);
        method.setAccessible(true);
        
        Position pos = (Position) method.invoke(game, "a1");
        assertEquals(0, pos.getRow());
        assertEquals(0, pos.getCol());
        
        pos = (Position) method.invoke(game, "b2");
        assertEquals(1, pos.getRow());
        assertEquals(1, pos.getCol());
        
        pos = (Position) method.invoke(game, "j10");
        assertEquals(9, pos.getRow());
        assertEquals(9, pos.getCol());
        
        pos = (Position) method.invoke(game, "a10");
        assertEquals(9, pos.getRow());
        assertEquals(0, pos.getCol());
    }
    
    @Test
    void testParsePosition_Invalid() throws Exception {
        Method method = Game.class.getDeclaredMethod("parsePosition", String.class);
        method.setAccessible(true);
        
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                method.invoke(game, "a");
            } catch (Exception e) {
                throw e.getCause();
            }
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                method.invoke(game, "aa");
            } catch (Exception e) {
                throw e.getCause();
            }
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                method.invoke(game, "");
            } catch (Exception e) {
                throw e.getCause();
            }
        });
    }
    
    @Test
    void testGetPositionString() throws Exception {
        Method method = Game.class.getDeclaredMethod("getPositionString", Position.class);
        method.setAccessible(true);
        
        Position pos1 = new Position(0, 0);
        String result = (String) method.invoke(game, pos1);
        assertEquals("a1", result);
        
        Position pos2 = new Position(1, 1);
        result = (String) method.invoke(game, pos2);
        assertEquals("b2", result);
        
        Position pos3 = new Position(9, 9);
        result = (String) method.invoke(game, pos3);
        assertEquals("j10", result);
    }
    
    @Test
    void testSaveGameToFile_Success() throws Exception {
        File testFile = tempDir.resolve("test_save.txt").toFile();
        
        Board board = new Board(10, 10);
        board.setCell(0, 0, 'X');
        board.setCell(1, 1, 'O');
        
        java.lang.reflect.Field boardField = Game.class.getDeclaredField("board");
        boardField.setAccessible(true);
        boardField.set(game, board);
        
        String input = testFile.getAbsolutePath() + "\n";
        Scanner scanner = new Scanner(input);
        
        Method method = Game.class.getDeclaredMethod("saveGameToFile", Scanner.class);
        method.setAccessible(true);
        method.invoke(game, scanner);
        
        assertTrue(testFile.exists());
        
        try (BufferedReader reader = new BufferedReader(new FileReader(testFile))) {
            assertEquals("10", reader.readLine());
            assertEquals("10", reader.readLine());
            String firstLine = reader.readLine();
            assertNotNull(firstLine);
            assertEquals('X', firstLine.charAt(0));
        }
    }
    
    @Test
    void testSaveGameToFile_IOException() throws Exception {
        String invalidPath = "/invalid/path/that/does/not/exist/test.txt";
        Scanner scanner = new Scanner(invalidPath + "\n");
        
        Method method = Game.class.getDeclaredMethod("saveGameToFile", Scanner.class);
        method.setAccessible(true);
        
        assertDoesNotThrow(() -> method.invoke(game, scanner));
    }
    
    @Test
    void testLoadGameFromFile_Success() throws Exception {
        File testFile = tempDir.resolve("test_load.txt").toFile();
        try (PrintWriter writer = new PrintWriter(testFile)) {
            writer.println("10");
            writer.println("10");
            writer.println("X.........");
            writer.println(".O........");
            writer.println("..........");
            writer.println("..........");
            writer.println("..........");
            writer.println("..........");
            writer.println("..........");
            writer.println("..........");
            writer.println("..........");
            writer.println("..........");
        }
        
        Scanner scanner = new Scanner(testFile.getAbsolutePath() + "\n");
        
        Method method = Game.class.getDeclaredMethod("loadGameFromFile", Scanner.class);
        method.setAccessible(true);
        method.invoke(game, scanner);
        
        java.lang.reflect.Field boardField = Game.class.getDeclaredField("board");
        boardField.setAccessible(true);
        Board loadedBoard = (Board) boardField.get(game);
        
        assertNotNull(loadedBoard);
        assertEquals(10, loadedBoard.getRows());
        assertEquals(10, loadedBoard.getCols());
        assertEquals('X', loadedBoard.getCell(0, 0));
        assertEquals('O', loadedBoard.getCell(1, 1));
    }
    
    @Test
    void testLoadGameFromFile_FileNotFound() throws Exception {
        Scanner scanner = new Scanner("non_existent_file_xyz.txt\n");
        
        Method method = Game.class.getDeclaredMethod("loadGameFromFile", Scanner.class);
        method.setAccessible(true);
        
        assertDoesNotThrow(() -> method.invoke(game, scanner));
    }
    
    @Test
    void testShowHighScores() {
        assertDoesNotThrow(() -> game.showHighScores());
        
        String output = outputStream.toString();
        assertNotNull(output);
        assertTrue(output.contains("HIGH SCORE"));
    }
    
    @Test
    void testCheckGameStatus_WinX() throws Exception {
        Board board = new Board(10, 10);
        for (int i = 0; i < 5; i++) {
            board.setCell(0, i, 'X');
        }
        
        java.lang.reflect.Field boardField = Game.class.getDeclaredField("board");
        boardField.setAccessible(true);
        boardField.set(game, board);
        
        java.lang.reflect.Field nameField = Game.class.getDeclaredField("playerName");
        nameField.setAccessible(true);
        nameField.set(game, "TestPlayer");
        
        java.lang.reflect.Field activeField = Game.class.getDeclaredField("gameActive");
        activeField.setAccessible(true);
        activeField.set(game, true);
        
        Method method = Game.class.getDeclaredMethod("checkGameStatus");
        method.setAccessible(true);
        method.invoke(game);
        
        assertFalse((Boolean) activeField.get(game));
    }
    
    @Test
    void testCheckGameStatus_WinO() throws Exception {
        Board board = new Board(10, 10);
        for (int i = 0; i < 5; i++) {
            board.setCell(i, 0, 'O');
        }
        
        java.lang.reflect.Field boardField = Game.class.getDeclaredField("board");
        boardField.setAccessible(true);
        boardField.set(game, board);
        
        java.lang.reflect.Field activeField = Game.class.getDeclaredField("gameActive");
        activeField.setAccessible(true);
        activeField.set(game, true);
        
        Method method = Game.class.getDeclaredMethod("checkGameStatus");
        method.setAccessible(true);
        method.invoke(game);
        
        assertFalse((Boolean) activeField.get(game));
    }
    
    @Test
    void testCheckGameStatus_Draw() throws Exception {
        Board board = new Board(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                board.setCell(i, j, (i + j) % 2 == 0 ? 'X' : 'O');
            }
        }
        
        java.lang.reflect.Field boardField = Game.class.getDeclaredField("board");
        boardField.setAccessible(true);
        boardField.set(game, board);
        
        java.lang.reflect.Field activeField = Game.class.getDeclaredField("gameActive");
        activeField.setAccessible(true);
        activeField.set(game, true);
        
        Method method = Game.class.getDeclaredMethod("checkGameStatus");
        method.setAccessible(true);
        method.invoke(game);
        
        assertFalse((Boolean) activeField.get(game));
    }
    
    @Test
    void testCheckGameStatus_GameContinues() throws Exception {
        Board board = new Board(10, 10);
        board.setCell(0, 0, 'X');
        board.setCell(1, 1, 'O');
        
        java.lang.reflect.Field boardField = Game.class.getDeclaredField("board");
        boardField.setAccessible(true);
        boardField.set(game, board);
        
        java.lang.reflect.Field activeField = Game.class.getDeclaredField("gameActive");
        activeField.setAccessible(true);
        activeField.set(game, true);
        
        Method method = Game.class.getDeclaredMethod("checkGameStatus");
        method.setAccessible(true);
        method.invoke(game);
        
        assertTrue((Boolean) activeField.get(game));
    }
}