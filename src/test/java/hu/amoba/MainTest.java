
package hu.amoba;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    
    @Test
    void testMainClassExists() {
        try {
            Main main = new Main();
            assertNotNull(main);
        } catch (Exception e) {
            fail("Main osztály nem példányosítható: " + e.getMessage());
        }
    }
}