package bshpvs.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CellTest {
    private static Cell c;

    @BeforeEach
    private void setup() {
        c = new Cell();
    }

    @Test
    public void testConstructor() {
        assertEquals(CellType.WATER, c.getType());
        assertEquals(false, c.isHit());
    }

    @Test
    public void testHit() {
        c.hit();
        assertEquals(true, c.isHit());
    }

    @Test
    public void testSetType() {
        c.setType(CellType.SHIP);
        assertEquals(CellType.SHIP, c.getType());
    }

    @Test void testToString() {
        assertEquals("0", c.toString());
    }
}
