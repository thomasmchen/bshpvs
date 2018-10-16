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
        c.setType(CellType.WATER);
        assertEquals(CellType.WATER, c.getType());
    }

    @Test void testToString() {
        assertEquals("0", c.toString());
    }

    @Test void testIsSimpleShip() {
        c.setType(CellType.DESTROYER);
        assertEquals(true, c.isShip());
        c.setType(CellType.CARRIER);
        assertEquals(true, c.isShip());
        c.setType(CellType.CRUISER);
        assertEquals(true, c.isShip());
        c.setType(CellType.SUBMARINE);
        assertEquals(true, c.isShip());
        c.setType(CellType.BATTLESHIP);
        assertEquals(true, c.isShip());
        c.setType(CellType.WATER);
        assertEquals(false, c.isShip());
        c.setType(CellType.LAND);
        assertEquals(false, c.isShip());
    }
}
