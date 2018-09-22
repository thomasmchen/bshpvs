package bshpvs.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import java.awt.*;

public class ShipTest {

    @Test
    public void testConstructor() {
        assertThrows(IllegalArgumentException.class, () -> {
            Ship s = new Ship(new Point(0,0), new Point(0, 5), ShipType.CARRIER);
        });
    }

    @Test
    public void testGetType() {
        Ship s = new Ship(new Point(0,0), new Point(0, 4), ShipType.CARRIER);
        assertEquals("CARRIER", s.getType().name());
    }

    @Test
    public void testOverlap() {
        Ship a = new Ship(new Point(0,0), new Point(0, 4), ShipType.CARRIER);
        Ship b = new Ship(new Point(0,4), new Point(0, 6), ShipType.CRUISER);
        Ship c = new Ship(new Point(6,6), new Point(6,7), ShipType.DESTROYER);
        assertEquals(true, a.doesOverlap(b));
        assertEquals(false, a.doesOverlap(c));
    }
}
