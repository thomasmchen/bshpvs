package bshpvs.model;

import java.util.Arrays;

public class Map {
    private Cell[][] map;

    public Map(int gridSize) {
        map = new Cell[gridSize][gridSize];
        for (Cell[] row: map)
            Arrays.fill(row, new Cell());
    }

    public Cell[][] getMap() {
        return this.map;
    }

    public Cell[][] setShip(int x1, int y1, int x2, int y2) {

        // TODO: Verify if client will be doing overlap validation
        map[x1][y1].setType(CellType.SHIP);
        map[x2][y2].setType(CellType.SHIP);

        if (x1 == x2) {
            final int x = x1;
            int y = ++y1;
            while (y != y2) {
                map[x][y].setType(CellType.SHIP);
                y++;
            }
        } else {
            int x = ++x1;
            final int y = y1;
            while (x != x2) {
                map[x][y].setType(CellType.SHIP);
                x++;
            }
        }

        return map;
    }

    public void printMap() {
        System.out.println(Arrays.deepToString(map));
    }

    private int getY(int x1, int y1, int x2, int y2, int x) {
        return  y1 + ((y2 - y1)/(x2 - x1))*(x - x1);
    }

    private int getX(int x1, int y1, int x2, int y2, int y) {
        return ((x1*y2-x2*y1)+(y*(x2-x1)))/(y2-y1);
    }

    public static void main(String[] args) {
        Map testMap = new Map(10);
        testMap.printMap();

        testMap.setShip(0,0,0,4);
        testMap.printMap();
    }

}