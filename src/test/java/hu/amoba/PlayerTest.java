
package hu.amoba;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    
    @Test
    void testPositionCreation() {
        Position pos = new Position(3, 4);
        assertEquals(3, pos.getRow());
        assertEquals(4, pos.getCol());
    }
    
    @Test
    void testPositionEquality() {
        Position pos1 = new Position(3, 4);
        Position pos2 = new Position(3, 4);
        Position pos3 = new Position(4, 3);
        
        assertEquals(pos1, pos2);
        assertNotEquals(pos1, pos3);
    }
}