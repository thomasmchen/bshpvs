package bshpvs.statistics;

import bshpvs.ai.HunterPlayer;
import bshpvs.ai.NaivePlayer;
import bshpvs.model.CellType;
import bshpvs.model.Player;
import bshpvs.model.Ship;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerStatTest {

    @Test
    void getId() {
        Player pl = new Player();
        PlayerStat ps = new PlayerStat(pl);
        assertEquals(pl.getID(), ps.getId());
    }

    @Test
    void getPlayerType() {
        HunterPlayer hp = new HunterPlayer();
        NaivePlayer np = new NaivePlayer();
        Player p = new Player();

        assertEquals("HunterPlayer", hp.getPlayerStat().getPlayerType());
        assertEquals("NaivePlayer", np.getPlayerStat().getPlayerType());
        assertEquals("Player", p.getPlayerStat().getPlayerType());
    }

    @Test
    void getHitPerc() {
        Player pl1 = new Player();
        Ship shp = new Ship(new Point(1,1), new Point(1,5), CellType.CARRIER);

        Player pl2 = new Player();
        pl2.addShip(shp);

        pl1.addOpponent(pl2);
        pl2.addOpponent(pl1);

        pl1.hitOppCell(new Point(7,1), pl2);
        assertEquals(0.0, pl1.getPlayerStat().getHitPerc());


        pl1.hitOppCell(new Point(1,1), pl2);
        pl1.hitOppCell(new Point(1,2), pl2);

        assertEquals(2.0/3.0, pl1.getPlayerStat().getHitPerc());

        pl1.hitOppCell(new Point(0,3), pl2);
        pl1.hitOppCell(new Point(1,4), pl2);
        pl1.hitOppCell(new Point(1,5), pl2);

        assertEquals(4.0/6.0, pl1.getPlayerStat().getHitPerc());
    }

    @Test
    void getMissPerc() {
        Player pl1 = new Player();
        Ship shp = new Ship(new Point(1,1), new Point(1,5), CellType.CARRIER);

        Player pl2 = new Player();
        pl2.addShip(shp);

        pl1.addOpponent(pl2);
        pl2.addOpponent(pl1);

        pl1.hitOppCell(new Point(2,1), pl2);
        pl1.hitOppCell(new Point(3,2), pl2);
        assertEquals(1.0, pl1.getPlayerStat().getMissPerc());

        pl1.hitOppCell(new Point(1,2), pl2);
        assertEquals(2.0/3.0, pl1.getPlayerStat().getMissPerc());
    }

    @Test
    void getTurns() {
        Player pl1 = new Player();
        Ship shp = new Ship(new Point(1,1), new Point(1,5), CellType.CARRIER);

        Player pl2 = new Player();
        pl2.addShip(shp);

        pl1.addOpponent(pl2);
        pl2.addOpponent(pl1);

        pl1.attack(pl2);
        pl2.attack(pl1);
        pl1.attack(pl2);

        assertEquals(2, pl1.getPlayerStat().getTurns());
        assertEquals(1, pl2.getPlayerStat().getTurns());
    }

    @Test
    void getTurnsNone() {
        Player pl1 = new Player();
        Ship shp = new Ship(new Point(1,1), new Point(1,5), CellType.CARRIER);

        Player pl2 = new Player();
        pl2.addShip(shp);

        assertEquals(0, pl1.getPlayerStat().getTurns());
        assertEquals(0, pl2.getPlayerStat().getTurns());
    }

    @Test
    void incrementTurns() {
        Player pl1 = new Player();

        assertEquals(0, pl1.getPlayerStat().getTurns());
        pl1.getPlayerStat().incrementTurns();
        assertEquals(1, pl1.getPlayerStat().getTurns());
    }

    @Test
    void getHits() {
        Player pl1 = new Player();
        Ship shp = new Ship(new Point(1,1), new Point(1,5), CellType.CARRIER);

        Player pl2 = new Player();
        pl2.addShip(shp);

        pl1.addOpponent(pl2);
        pl2.addOpponent(pl1);

        pl1.hitOppCell(new Point(7,1), pl2);
        pl1.hitOppCell(new Point(1,1), pl2);
        pl1.hitOppCell(new Point(1,2), pl2);
        pl1.hitOppCell(new Point(0,3), pl2);
        pl1.hitOppCell(new Point(1,4), pl2);
        pl1.hitOppCell(new Point(1,5), pl2);

        assertEquals(4, pl1.getPlayerStat().getHits());
        assertEquals(0, pl2.getPlayerStat().getHits());
    }

    @Test
    void incrementHits() {
        Player pl1 = new Player();

        assertEquals(0, pl1.getPlayerStat().getHits());
        pl1.getPlayerStat().incrementHits();
        assertEquals(1, pl1.getPlayerStat().getHits());

    }

    @Test
    void getMisses() {
        Player pl1 = new Player();
        Ship shp = new Ship(new Point(1,1), new Point(1,5), CellType.CARRIER);

        Player pl2 = new Player();
        pl2.addShip(shp);

        pl1.addOpponent(pl2);
        pl2.addOpponent(pl1);

        pl1.hitOppCell(new Point(6,1), pl2);
        pl1.hitOppCell(new Point(1,1), pl2);
        pl1.hitOppCell(new Point(1,2), pl2);
        pl1.hitOppCell(new Point(0,3), pl2);
        pl1.hitOppCell(new Point(1,4), pl2);
        pl1.hitOppCell(new Point(1,5), pl2);

        assertEquals(2, pl1.getPlayerStat().getMisses());
        assertEquals(0, pl2.getPlayerStat().getMisses());
    }

    @Test
    void incrementMisses() {
        Player pl1 = new Player();

        assertEquals(0, pl1.getPlayerStat().getMisses());
        pl1.getPlayerStat().incrementMisses();
        assertEquals(1, pl1.getPlayerStat().getMisses());
    }

    @Test
    void compareTo() {
        Player pl1 = new Player();
        Ship shp = new Ship(new Point(1,1), new Point(1,5), CellType.CARRIER);

        Player pl2 = new Player();
        pl2.addShip(shp);

        pl1.addOpponent(pl2);
        pl2.addOpponent(pl1);

        pl1.hitOppCell(new Point(6,1), pl2);
        pl1.hitOppCell(new Point(1,1), pl2);
        pl1.hitOppCell(new Point(1,2), pl2);
        pl1.hitOppCell(new Point(0,3), pl2);
        pl1.hitOppCell(new Point(1,4), pl2);
        pl1.hitOppCell(new Point(1,5), pl2);

        pl2.hitOppCell(new Point(1,1), pl1);

        assertEquals(5, pl1.getPlayerStat().compareTo(pl2.getPlayerStat()));
        assertEquals(-5, pl2.getPlayerStat().compareTo(pl1.getPlayerStat()));
    }

    @Test
    void equals() {
        Player pl1 = new Player();
        Ship shp = new Ship(new Point(1,1), new Point(1,5), CellType.CARRIER);

        Player pl2 = new Player();
        pl2.addShip(shp);

        pl1.addOpponent(pl2);
        pl2.addOpponent(pl1);

        assertEquals(pl1.getPlayerStat(), pl1.getPlayerStat());
        assertNotEquals(pl1.getPlayerStat(), pl2.getPlayerStat());

        pl1.hitOppCell(new Point(6,1), pl2);
        pl1.hitOppCell(new Point(1,1), pl2);
        pl1.hitOppCell(new Point(1,2), pl2);
        pl1.hitOppCell(new Point(0,3), pl2);
        pl1.hitOppCell(new Point(1,4), pl2);
        pl1.hitOppCell(new Point(1,5), pl2);

        pl2.hitOppCell(new Point(1,1), pl1);

        assertEquals(pl1.getPlayerStat(), pl1.getPlayerStat());
        assertNotEquals(pl1.getPlayerStat(), pl2.getPlayerStat());
    }

    @Test
    void testToString() {
        Player pl1 = new Player();
        Ship shp = new Ship(new Point(1,1), new Point(1,5), CellType.CARRIER);

        Player pl2 = new Player();
        pl2.addShip(shp);

        pl1.addOpponent(pl2);
        pl2.addOpponent(pl1);

        assertEquals(pl1.getPlayerStat(), pl1.getPlayerStat());
        assertNotEquals(pl1.getPlayerStat(), pl2.getPlayerStat());

        pl1.hitOppCell(new Point(6,1), pl2);
        pl1.hitOppCell(new Point(1,1), pl2);
        pl1.hitOppCell(new Point(1,2), pl2);
        pl1.hitOppCell(new Point(0,3), pl2);
        pl1.hitOppCell(new Point(1,4), pl2);
        pl1.hitOppCell(new Point(1,5), pl2);

        pl2.hitOppCell(new Point(1,1), pl1);

        assertEquals("PlayerStat{id="+ pl1.getID() +", playerType='Player', turns=6, hits=4, misses=2, hitPerc=0.6666666666666666, missPerc=0.3333333333333333}", pl1.getPlayerStat().toString());
        assertEquals("PlayerStat{id="+ pl2.getID() +", playerType='Player', turns=1, hits=0, misses=1, hitPerc=0.0, missPerc=1.0}", pl2.getPlayerStat().toString());
    }

    @Test
    void testHashCode() {
        Player pl1 = new Player();
        int p1hash = pl1.getPlayerStat().hashCode();

        Player pl2 = new Player();
        int p2hash = pl2.getPlayerStat().hashCode();

        assertNotEquals(p1hash, p2hash);
        assertEquals(p1hash, pl1.getPlayerStat().hashCode());
    }
}