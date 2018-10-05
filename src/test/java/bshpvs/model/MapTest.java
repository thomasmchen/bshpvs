package bshpvs.model;


import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

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
        mp.setCell(new Point(1,1), CellType.SHIP);
        assertEquals(CellType.SHIP, mp.getMap()[1][1].getType());
    }

    @Test
    void printMap() {
        Map mp = new Map(10);
        mp.setCell(new Point(0,0), CellType.SHIP);
        mp.setCell(new Point(0,1), CellType.SHIP);
        mp.setCell(new Point(0,2), CellType.SHIP);
        mp.setCell(new Point(0,3), CellType.SHIP);
        mp.setCell(new Point(0,4), CellType.SHIP);
        mp.setCell(new Point(0,5), CellType.SHIP);


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        mp.printMap();
        String expectedOutput =
                "[1, 0, 0, 0, 0, 0, 0, 0, 0, 0]\n" +
                "[1, 0, 0, 0, 0, 0, 0, 0, 0, 0]\n" +
                "[1, 0, 0, 0, 0, 0, 0, 0, 0, 0]\n" +
                "[1, 0, 0, 0, 0, 0, 0, 0, 0, 0]\n" +
                "[1, 0, 0, 0, 0, 0, 0, 0, 0, 0]\n" +
                "[1, 0, 0, 0, 0, 0, 0, 0, 0, 0]\n" +
                "[0, 0, 0, 0, 0, 0, 0, 0, 0, 0]\n" +
                "[0, 0, 0, 0, 0, 0, 0, 0, 0, 0]\n" +
                "[0, 0, 0, 0, 0, 0, 0, 0, 0, 0]\n" +
                "[0, 0, 0, 0, 0, 0, 0, 0, 0, 0]\n";

        assertEquals(expectedOutput, outContent.toString());
    }
}