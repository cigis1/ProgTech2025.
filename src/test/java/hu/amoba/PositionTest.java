
package hu.amoba;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PositionTest {
    
    @Test
    void testConstructor() {
        Position pos = new Position(3, 5);
        assertEquals(3, pos.getRow());
        assertEquals(5, pos.getCol());
    }
    
    @Test
    void testEquals_SameObject() {
        Position pos = new Position(1, 2);
        assertTrue(pos.equals(pos));
    }
    
    @Test
    void testEquals_SameValues() {
        Position pos1 = new Position(1, 2);
        Position pos2 = new Position(1, 2);
        assertTrue(pos1.equals(pos2));
    }
    
    @Test
    void testEquals_DifferentValues() {
        Position pos1 = new Position(1, 2);
        Position pos2 = new Position(3, 4);
        assertFalse(pos1.equals(pos2));
    }
    
    @Test
    void testEquals_Null() {
        Position pos = new Position(1, 2);
        assertFalse(pos.equals(null));
    }
    
    @Test
    void testEquals_DifferentClass() {
        Position pos = new Position(1, 2);
        assertFalse(pos.equals("Not a Position"));
    }
    
    @Test
    void testHashCode() {
        Position pos1 = new Position(1, 2);
        Position pos2 = new Position(1, 2);
        
        assertEquals(pos1.hashCode(), pos2.hashCode());
    }
    
    @Test
    void testToString() {
        Position pos = new Position(3, 4);
        String result = pos.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("3"));
        assertTrue(result.contains("4"));
        assertTrue(result.contains("row") || result.contains("col"));
    }
}