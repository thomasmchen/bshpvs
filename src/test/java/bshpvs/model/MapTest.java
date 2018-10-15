package bshpvs.model;


import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {

    @Test
    void getMap() {
        Map mp = new Map(2);
        Cell[][] cells = mp.getMap();
        assertEquals(2, cells.length);
        assertEquals(2, cells[1].length);
    }

    @Test
    void setCell() {
        Map mp = new Map(5);
        mp.setCell(new Point(1,1), CellType.CARRIER);
        assertEquals(CellType.CARRIER, mp.getMap()[1][1].getType());
    }

    @Test
    void printMap() {
        Map mp = new Map(10);
        mp.setCell(new Point(0,0), CellType.CARRIER);
        mp.setCell(new Point(0,1), CellType.CARRIER);
        mp.setCell(new Point(0,2), CellType.CARRIER);
        mp.setCell(new Point(0,3), CellType.CARRIER);
        mp.setCell(new Point(0,4), CellType.CARRIER);
        mp.setCell(new Point(0,5), CellType.CARRIER);


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        mp.printMap();
        String expectedOutput =
                "[5, 0, 0, 0, 0, 0, 0, 0, 0, 0]\n" +
                "[5, 0, 0, 0, 0, 0, 0, 0, 0, 0]\n" +
                "[5, 0, 0, 0, 0, 0, 0, 0, 0, 0]\n" +
                "[5, 0, 0, 0, 0, 0, 0, 0, 0, 0]\n" +
                "[5, 0, 0, 0, 0, 0, 0, 0, 0, 0]\n" +
                "[5, 0, 0, 0, 0, 0, 0, 0, 0, 0]\n" +
                "[0, 0, 0, 0, 0, 0, 0, 0, 0, 0]\n" +
                "[0, 0, 0, 0, 0, 0, 0, 0, 0, 0]\n" +
                "[0, 0, 0, 0, 0, 0, 0, 0, 0, 0]\n" +
                "[0, 0, 0, 0, 0, 0, 0, 0, 0, 0]\n";

        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void getCell() {
        Map mp = new Map(10);
        mp.setCell(new Point(0,5), CellType.CARRIER);
        Cell c = mp.getCell(new Point(0, 5));
        assertEquals(CellType.CARRIER, c.getType());
    }

    @Test
    void testGetKeyMap() {
        assertEquals("----------------\n" +
                "Battleship Key\n" +
                "----------------\n" +
                "0 : WATER\n" +
                "1 : LAND\n" +
                "2 : DESTROYER\n" +
                "3 : CRUISER\n" +
                "3 : SUBMARINE\n" +
                "4 : BATTLESHIP\n" +
                "5 : CARRIER\n" +
                "----------------\n", Map.getKeyMap());
    }
}