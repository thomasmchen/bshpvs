package bshpvs.model;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void addShip() {
        Player pl = new Player(10);
        Ship shp = new Ship(new Point(1,1), new Point(1,5), CellType.CARRIER);
        pl.addShip(shp);
    }

    @Test
    void getCell() {
        Player pl1 = new Player(2);
        assertEquals(CellType.WATER, pl1.getCell(new Point(1,1)).getType());

    }

    @Test
    void hitOppCell() {
        Player pl1 = new Player(10);
        Player pl2 = new Player(10);
        pl1.hitOppCell(new Point(1,1), pl2);
        assertTrue(pl2.getCell(new Point(1,1)).isHit());
    }

    @Test
    void isShipSunk() {
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
    void isDefeated() {
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
    void updateCell() {
        Player pl1 = new Player(10);


    }
}