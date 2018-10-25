package bshpvs.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class ShipTest {

    @Test
    public void testConstructor() {
        assertThrows(IllegalArgumentException.class, () -> {
            Ship s = new Ship(new Point(0,0), new Point(0, 5), CellType.CARRIER);
        });
    }

    @Test
    public void testGetType() {
        Ship s = new Ship(new Point(0,0), new Point(0, 4), CellType.CARRIER);
        assertEquals("CARRIER", s.getType().name());
    }

    @Test
    public void testOverlap() {
        Ship a = new Ship(new Point(0,0), new Point(0, 4), CellType.CARRIER);
        Ship b = new Ship(new Point(0,4), new Point(0, 6), CellType.CRUISER);
        Ship c = new Ship(new Point(6,6), new Point(6,7), CellType.DESTROYER);
        assertEquals(true, a.doesOverlap(b));
        assertEquals(false, a.doesOverlap(c));
    }

    @Test
    public void testGetPoints() {
        Ship a = new Ship(new Point(0,0), new Point(0, 2), CellType.SUBMARINE);
        Point[] pts = a.getPoints();

        assertEquals(new Point(0,0), pts[0]);
        assertEquals(new Point(0,1), pts[1]);
        assertEquals(new Point(0,2), pts[2]);
    }

    @Test
    public void testGetPointsReverse() {
        Ship a = new Ship(new Point(0,2), new Point(0, 0), CellType.SUBMARINE);
        Point[] pts = a.getPoints();

        assertEquals(new Point(0,2), pts[0]);
        assertEquals(new Point(0,1), pts[1]);
        assertEquals(new Point(0,0), pts[2]);
    }

    @Test
    public void testCheckSunk() {
        Ship a = new Ship(new Point(0,2), new Point(0, 0), CellType.SUBMARINE);
        Point[] pts = a.getPoints();

        Player pl = new Player(10);
        pl.addShip(a);

        pl.getMap().prettyPrintMap();


        pl.getHit(new Point(0, 0));
        pl.getHit(new Point(0, 1));
        pl.getHit(new Point(0, 2));

        pl.getMap().prettyPrintMap();

        assertEquals(true, a.checkSunk(pl.getMap()));
    }

    @Test
    public void testCalcPoints() {
        Ship shp = new Ship(new Point(5,0), new Point(1,0), CellType.CARRIER);

        Point[] checkPoints = new Point[]{
                new Point(5,0),
                new Point(4,0),
                new Point(3,0),
                new Point(2,0),
                new Point(1,0)
        };

        Point[] output = shp.getPoints();
        assertTrue(Arrays.equals(checkPoints, shp.getPoints()));
    }
}
