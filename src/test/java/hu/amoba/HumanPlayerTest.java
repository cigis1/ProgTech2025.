
package hu.amoba;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HumanPlayerTest {
    
    @Test
    void testConstructor() {
        HumanPlayer player = new HumanPlayer("John", 'X');
        assertEquals("John", player.getName());
        assertEquals('X', player.getSymbol());
    }
    
    @Test
    void testInheritance() {
        HumanPlayer player = new HumanPlayer("Alice", 'O');
        assertTrue(player instanceof Player);
    }
    
    @Test
    void testDifferentSymbols() {
        HumanPlayer player1 = new HumanPlayer("Player1", 'X');
        HumanPlayer player2 = new HumanPlayer("Player2", 'O');
        
        assertEquals('X', player1.getSymbol());
        assertEquals('O', player2.getSymbol());
        assertEquals("Player1", player1.getName());
        assertEquals("Player2", player2.getName());
    }
}