
package hu.amoba;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class ComputerPlayerTest {
    private ComputerPlayer player;
    
    @BeforeEach
    void setUp() {
        player = new ComputerPlayer("Computer", 'O');
    }
    
    @Test
    void testConstructor() {
        assertEquals("Computer", player.getName());
        assertEquals('O', player.getSymbol());
    }
    
    @Test
    void testInheritance() {
        assertTrue(player instanceof Player);
    }
    
    @Test
    void testGetMove_EmptyBoard() {
        Board board = new Board(10, 10);
        
        Position move = player.getMove(board);
        assertNotNull(move);
        assertTrue(move.getRow() >= 0 && move.getRow() < 10);
        assertTrue(move.getCol() >= 0 && move.getCol() < 10);
        
        assertEquals('.', board.getCell(move.getRow(), move.getCol()));
    }
    
    @Test
    void testGetMove_NoValidMoves() {
        Board board = new Board(5, 5);
        
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                board.setCell(i, j, 'X');
            }
        }
        
        Position move = player.getMove(board);
        assertNull(move);
    }
    
    @Test
    void testGetMove_SomeValidMoves() {
        Board board = new Board(5, 5);
        
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (!(i == 2 && j == 2)) {
                    board.setCell(i, j, 'X');
                }
            }
        }
        
        Position move = player.getMove(board);
        assertNotNull(move);
        assertEquals(2, move.getRow());
        assertEquals(2, move.getCol());
    }
    
    @Test
    void testGetMove_MultipleValidMoves() {
        Board board = new Board(5, 5);
        
        board.makeMove(new Position(0, 0), 'X');
        board.makeMove(new Position(4, 4), 'O');
        
        Position move = player.getMove(board);
        assertNotNull(move);
        
        assertTrue(move.getRow() >= 0 && move.getRow() < 5);
        assertTrue(move.getCol() >= 0 && move.getCol() < 5);
        assertEquals('.', board.getCell(move.getRow(), move.getCol()));
        assertFalse((move.getRow() == 0 && move.getCol() == 0) || 
                    (move.getRow() == 4 && move.getCol() == 4));
    }
    
    @Test
    void testGetMove_DifferentSymbol() {
        ComputerPlayer playerX = new ComputerPlayer("AI", 'X');
        Board board = new Board(5, 5);
        
        Position move = playerX.getMove(board);
        assertNotNull(move);
        
        assertTrue(move.getRow() >= 0 && move.getRow() < 5);
        assertTrue(move.getCol() >= 0 && move.getCol() < 5);
        assertEquals('.', board.getCell(move.getRow(), move.getCol()));
    }
    
    @Test
    void testGetMove_AlmostFullBoard() {
        Board board = new Board(6, 6);
        ComputerPlayer player = new ComputerPlayer("TestAI", 'X');
        
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (!(i == 3 && j == 3)) {
                    board.setCell(i, j, 'O');
                }
            }
        }
        
        Position move = player.getMove(board);
        assertNotNull(move);
        assertEquals(3, move.getRow());
        assertEquals(3, move.getCol());
    }
    
    @Test
    void testGetMove_Randomness() {
        Board board = new Board(10, 10);
        ComputerPlayer player = new ComputerPlayer("RandomAI", 'X');
        
        java.util.Set<Position> positions = new java.util.HashSet<>();
        for (int i = 0; i < 5; i++) {
            Position move = player.getMove(board);
            positions.add(move);
            board.makeMove(move, 'X');
        }
        
        assertTrue(positions.size() >= 1);
    }
}