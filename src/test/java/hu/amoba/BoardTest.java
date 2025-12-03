
package hu.amoba;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class BoardTest {
    private Board board;
    
    @BeforeEach
    void setUp() {
        board = new Board(10, 10);
    }
    
    @Test
    void testBoardConstructor_ValidSize() {
        Board b = new Board(10, 10);
        assertEquals(10, b.getRows());
        assertEquals(10, b.getCols());
    }
    
    @Test
    void testBoardConstructor_ValidMinSize() {
        Board b = new Board(5, 5);
        assertEquals(5, b.getRows());
        assertEquals(5, b.getCols());
    }
    
    @Test
    void testBoardConstructor_ValidMaxSize() {
        Board b = new Board(25, 25);
        assertEquals(25, b.getRows());
        assertEquals(25, b.getCols());
    }
    
    @Test
    void testBoardConstructor_InvalidSizeTooSmall() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Board(3, 3);
        });
    }
    
    @Test
    void testBoardConstructor_InvalidSizeTooLarge() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Board(30, 30);
        });
    }
    
    @Test
    void testBoardConstructor_InvalidRowsTooSmall() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Board(4, 10);
        });
    }
    
    @Test
    void testBoardConstructor_InvalidColsTooSmall() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Board(10, 4);
        });
    }
    
    @Test
    void testGetRowsAndCols() {
        assertEquals(10, board.getRows());
        assertEquals(10, board.getCols());
    }
    
    @Test
    void testGetWinCount() {
        assertEquals(5, Board.getWinCount());
    }
    
    @Test
    void testInitializeBoard() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals('.', board.getCell(i, j));
            }
        }
    }
    
    @Test
    void testMakeMove_FirstMoveValid() {
        Position pos = new Position(5, 5);
        boolean result = board.makeMove(pos, 'X');
        assertTrue(result);
        assertEquals('X', board.getCell(5, 5));
    }
    
    @Test
    void testMakeMove_SecondMoveAnywhere() {
        Position pos1 = new Position(5, 5);
        board.makeMove(pos1, 'X');
        
        Position pos2 = new Position(9, 9);
        boolean result = board.makeMove(pos2, 'O');
        assertTrue(result);
        assertEquals('O', board.getCell(9, 9));
    }
    
    @Test
    void testMakeMove_OccupiedCell() {
        Position pos = new Position(5, 5);
        board.makeMove(pos, 'X');
        
        boolean result = board.makeMove(pos, 'O');
        assertFalse(result);
        assertEquals('X', board.getCell(5, 5));
    }
    
    @Test
    void testMakeMove_InvalidPosition() {
        Position pos = new Position(15, 15);
        boolean result = board.makeMove(pos, 'X');
        assertFalse(result);
    }
    
    @Test
    void testMakeMove_InvalidNegativePosition() {
        Position pos = new Position(-1, 5);
        boolean result = board.makeMove(pos, 'X');
        assertFalse(result);
    }
    
    @Test
    void testMakeMove_InvalidSymbol() {
        Position pos = new Position(5, 5);
        boolean result = board.makeMove(pos, 'Z');
        assertTrue(result);
        assertEquals('Z', board.getCell(5, 5));
    }
    
    @Test
    void testGetValidMoves_FirstMove() {
        List<Position> moves = board.getValidMoves('X');
        assertEquals(100, moves.size());
    }
    
    @Test
    void testGetValidMoves_AfterFirstMove() {
        board.makeMove(new Position(5, 5), 'X');
        
        List<Position> moves = board.getValidMoves('O');
        
        assertEquals(99, moves.size());
        
        for (Position move : moves) {
            assertEquals('.', board.getCell(move.getRow(), move.getCol()));
            assertFalse(move.getRow() == 5 && move.getCol() == 5);
        }
    }
    
    @Test
    void testGetValidMoves_NoValidMoves() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board.setCell(i, j, 'X');
            }
        }
        
        List<Position> moves = board.getValidMoves('O');
        assertEquals(0, moves.size());
    }
    
    @Test
    void testCheckWinner_NoWinner() {
        assertEquals('.', board.checkWinner());
    }
    
    @Test
    void testCheckWinner_HorizontalWinRight() {
        for (int i = 0; i < 5; i++) {
            board.setCell(5, i, 'X');
        }
        
        assertEquals('X', board.checkWinner());
    }
    
    @Test
    void testCheckWinner_HorizontalWinMiddle() {
        for (int i = 3; i < 8; i++) {
            board.setCell(7, i, 'O');
        }
        
        assertEquals('O', board.checkWinner());
    }
    
    @Test
    void testCheckWinner_VerticalWin() {
        for (int i = 0; i < 5; i++) {
            board.setCell(i, 5, 'O');
        }
        
        assertEquals('O', board.checkWinner());
    }
    
    @Test
    void testCheckWinner_DiagonalDownRightWin() {
        for (int i = 0; i < 5; i++) {
            board.setCell(i, i, 'X');
        }
        
        assertEquals('X', board.checkWinner());
    }
    
    @Test
    void testCheckWinner_DiagonalDownLeftWin() {
        for (int i = 0; i < 5; i++) {
            board.setCell(i, 9 - i, 'O');
        }
        
        assertEquals('O', board.checkWinner());
    }
    
    @Test
    void testCheckWinner_MultiplePossibleWins() {
        for (int i = 0; i < 5; i++) {
            board.setCell(0, i, 'X');
            board.setCell(i, 0, 'X');
        }
        
        assertEquals('X', board.checkWinner());
    }
    
    @Test
    void testCheckWinner_NotEnoughInRow() {
        for (int i = 0; i < 4; i++) {
            board.setCell(5, i, 'X');
        }
        
        assertEquals('.', board.checkWinner());
    }
    
    @Test
    void testCheckWinner_BrokenSequence() {
        for (int i = 0; i < 4; i++) {
            board.setCell(0, i, 'X');
        }
        board.setCell(0, 4, 'O');
        board.setCell(0, 5, 'X');
        
        assertEquals('.', board.checkWinner());
    }
    
    @Test
    void testIsFull_False() {
        assertFalse(board.isFull());
    }
    
    @Test
    void testIsFull_AlmostFull() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (!(i == 5 && j == 5)) {
                    board.setCell(i, j, 'X');
                }
            }
        }
        
        assertFalse(board.isFull());
    }
    
    @Test
    void testIsFull_True() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board.setCell(i, j, (i + j) % 2 == 0 ? 'X' : 'O');
            }
        }
        
        assertTrue(board.isFull());
    }
    
    @Test
    void testSetCellAndGetCell() {
        board.setCell(3, 4, 'X');
        assertEquals('X', board.getCell(3, 4));
        
        board.setCell(7, 8, 'O');
        assertEquals('O', board.getCell(7, 8));
    }
    
    @Test
    void testSetCell_Overwrite() {
        board.setCell(3, 4, 'X');
        assertEquals('X', board.getCell(3, 4));
        
        board.setCell(3, 4, 'O');
        assertEquals('O', board.getCell(3, 4));
    }
    
    @Test
    void testPrintBoard_NoException() {
        assertDoesNotThrow(() -> board.printBoard());
    }
    
    @Test
    void testPrintBoard_WithContent() {
        board.setCell(0, 0, 'X');
        board.setCell(9, 9, 'O');
        
        assertDoesNotThrow(() -> board.printBoard());
    }
    
    @Test
    void testMakeMove_MultipleMoves() {
        assertTrue(board.makeMove(new Position(0, 0), 'X'));
        assertTrue(board.makeMove(new Position(9, 9), 'O'));
        assertTrue(board.makeMove(new Position(0, 9), 'X'));
        assertTrue(board.makeMove(new Position(9, 0), 'O'));
        
        assertEquals('X', board.getCell(0, 0));
        assertEquals('O', board.getCell(9, 9));
        assertEquals('X', board.getCell(0, 9));
        assertEquals('O', board.getCell(9, 0));
    }
    
    @Test
    void testMakeMove_BoundaryPositions() {
        assertTrue(board.makeMove(new Position(0, 0), 'X'));
        assertTrue(board.makeMove(new Position(0, 9), 'O'));
        assertTrue(board.makeMove(new Position(9, 0), 'X'));
        assertTrue(board.makeMove(new Position(9, 9), 'O'));
        
        assertEquals('X', board.getCell(0, 0));
        assertEquals('O', board.getCell(0, 9));
        assertEquals('X', board.getCell(9, 0));
        assertEquals('O', board.getCell(9, 9));
    }
    
    @Test
    void testGetCell_InvalidPosition() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            board.getCell(10, 10);
        });
    }
    
    @Test
    void testSetCell_InvalidPosition() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            board.setCell(10, 10, 'X');
        });
    }
}