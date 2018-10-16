package bshpvs.model;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void testAddShip() {
        Player pl = new Player(10);
        Ship shp = new Ship(new Point(1,1), new Point(1,5), CellType.CARRIER);
        pl.addShip(shp);
    }

    @Test
    void testAddExistingShip() {
        Player pl = new Player(10);
        Ship shp = new Ship(new Point(1,1), new Point(1,5), CellType.CARRIER);
        pl.addShip(shp);
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> {
            pl.addShip(shp);
        });

        assertEquals("Ship: " + shp.getType().toString() + " already exists.", iae.getMessage());
    }

    @Test
    void testAddOverlappingShip() {
        Player pl = new Player(10);
        Ship carr = new Ship(new Point(1,1), new Point(1,5), CellType.CARRIER);
        Ship sub = new Ship(new Point(1,5), new Point(3,5), CellType.SUBMARINE);
        pl.addShip(carr);
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> {
            pl.addShip(sub);
        });

        assertEquals("Failed to add " + sub.getType().toString() +
                " to Player, Ship overlaps with Existing Ship.", iae.getMessage());
    }

    @Test
    void testGetCell() {
        Player pl1 = new Player(2);
        pl1.getMap().setCell(new Point(1,1), CellType.CARRIER);
        assertEquals(CellType.CARRIER, pl1.getCell(new Point(1,1)).getType());
        assertEquals(CellGroup.SHIP, pl1.getCell(new Point(1,1)).getType().getGroup());
    }

    @Test
    void testHitOppCell() {
        Player pl1 = new Player(10);
        Player pl2 = new Player(10);
        pl1.hitOppCell(new Point(1,1), pl2);
        assertTrue(pl2.getCell(new Point(1,1)).isHit());
    }

    @Test
    void testIsShipSunk() {
        Player pl1 = new Player();
        Ship shp = new Ship(new Point(1,1), new Point(1,5), CellType.CARRIER);

        Player pl2 = new Player();
        pl2.addShip(shp);

        pl1.hitOppCell(new Point(1,1), pl2);
        pl1.hitOppCell(new Point(1,2), pl2);
        pl1.hitOppCell(new Point(1,3), pl2);
        pl1.hitOppCell(new Point(1,4), pl2);
        pl1.hitOppCell(new Point(1,5), pl2);

        assertTrue(pl2.resolveShipStatus(CellType.CARRIER));
    }

    @Test
    void testIsDefeated() {
        Player pl1 = new Player();
        Ship shp = new Ship(new Point(1,1), new Point(1,5), CellType.CARRIER);

        Player pl2 = new Player();
        pl2.addShip(shp);

        pl1.hitOppCell(new Point(1,1), pl2);
        pl1.hitOppCell(new Point(1,2), pl2);
        pl1.hitOppCell(new Point(1,3), pl2);
        pl1.hitOppCell(new Point(1,4), pl2);
        pl1.hitOppCell(new Point(1,5), pl2);

        assertTrue(pl2.isDefeated());
    }

    @Test
    void testUpdateCell() {
        Player pl1 = new Player(10);
    }

    @Test
    void testGetShipStatus() {
        Player pl1 = new Player();
        Ship shp = new Ship(new Point(1,1), new Point(1,5), CellType.CARRIER);

        Player pl2 = new Player();
        pl2.addShip(shp);

        pl1.hitOppCell(new Point(1,1), pl2);
        pl1.hitOppCell(new Point(1,2), pl2);
        pl1.hitOppCell(new Point(1,3), pl2);
        pl1.hitOppCell(new Point(1,4), pl2);
        pl1.hitOppCell(new Point(1,5), pl2);


        assertTrue(pl2.isShipSunk(CellType.CARRIER));
    }

    @Test
    void testGetTargetBoard() {
        Player pl1 = new Player();
        Ship shp = new Ship(new Point(1,1), new Point(1,5), CellType.CARRIER);

        Player pl2 = new Player();
        pl2.addShip(shp);

        pl1.hitOppCell(new Point(1,1), pl2);

        Cell checkHitCell = pl1.getTargetBoard().getCell(new Point(1,1));
        Cell unknownCell = pl1.getTargetBoard().getCell(new Point(1, 2));


        assertEquals(CellType.CARRIER, checkHitCell.getType());
        assertEquals(true, checkHitCell.isHit());
        assertEquals(CellType.WATER, unknownCell.getType());
        assertEquals(false, unknownCell.isHit());
    }

    @Test
    void testEquals() {
        Player pl1 = new Player();
        Player pl2 = new Player();

        assertFalse(pl1.equals(pl2));
        assertTrue(pl1.equals(pl1));
    }

    @Test
    void testIsValidPoint() {
        Player pl1 = new Player(10);

        Point[] validPts = new Point[] {
                new Point(0,0),
                new Point(3,7),
                new Point(9,9),
                new Point(0,9),
                new Point(9,0)
        };

        Point[] invalidPts = new Point[] {
                new Point(-1,0),
                new Point(11,7),
                new Point(16,23),
                new Point(10,10),
                new Point(-20,1000)
        };

        for (Point p : validPts) {
            assertTrue(pl1.isValidPoint(p));
        }

        for (Point p : invalidPts) {
            assertFalse(pl1.isValidPoint(p));
        }
    }

    @Test
    void testGetMap() {
        Player pl = new Player(23);
        assertEquals(23, pl.getMap().getLength());

    }

    @Test
    void testMove() {
        Player pl1 = new Player();
        Ship shp = new Ship(new Point(1,1), new Point(1,5), CellType.CARRIER);

        Player pl2 = new Player();
        pl2.addShip(shp);

        Point tgt = pl1.move(pl2);

        Cell pl2Cell = pl2.getMap().getCell(tgt);
        Cell pl1TrackedCell = pl1.getTargetBoard().getCell(tgt);

        assertTrue(pl2Cell.isHit());
        assertTrue(pl1TrackedCell.isHit());
    }
}