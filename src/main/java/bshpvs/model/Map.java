package bshpvs.model;

import org.apache.commons.lang3.StringUtils;

import java.awt.Point;
import java.util.Arrays;

/**
 * Map Class.
 */
public class Map {
    private Cell[][] grid;

    /**
     * Constructor for Map with specific grid size.
     * @param gridSize the length and width of the grid
     */
    public Map(int gridSize) {
        grid = new Cell[gridSize][gridSize];

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    /**
     * Accessor method for the map field.
     * @return the grid field of the object
     */
    public Cell[][] getMap() {
        return this.grid;
    }

    /**
     * Sets the type of a specific cell on the grid.
     * @param p the Point coordinates of the cell
     * @param c the value to change the cell type to
     */
    public void setCell(Point p, CellType c) {
        grid[p.y][p.x].setType(c);
    }

    public Cell getCell(Point p) {
        return grid[p.y][p.x];
    }

    public int getLength() {
        return grid.length;
    }

    /**
     * Print function for Map object.
     */
    public void printMap() {
        for (Cell[] row : grid) {
            System.out.printf(Arrays.toString(row));
            System.out.println();
        }
    }

    public static void printKey() {
        System.out.println("----------------");
        System.out.println("Battleship Key");
        System.out.println("----------------");
        for (CellType ct : CellType.values()) {
            System.out.println(ct.getValue() + " : " + ct.getText());
        }
        System.out.println("----------------");
        System.out.println();
    }

    /**
     * Pretty printing of Map object
     */
    public void prettyPrintMap() {
        String border = StringUtils.repeat("-", grid.length * 4 + 3);
        final String ANSI_PURPLE = "\u001B[35m";
        final String ANSI_CYAN = "\u001B[36m";
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_RED = "\u001B[31m";
        final String ANSI_RESET = "\u001B[0m";


        System.out.print(ANSI_PURPLE +  "  | " + ANSI_RESET);
        for (int i = 0; i < grid.length; i++) {
            int pos = i + 'A';
            char c = (char) pos;
            System.out.print(ANSI_PURPLE + c + " | " + ANSI_RESET);
        }
        System.out.println();


        for (int i = 0; i < grid.length; i++) {
            if (i == 0) {
                System.out.println(ANSI_PURPLE + border + ANSI_RESET);
            } else
                System.out.println(border);
            System.out.print(ANSI_PURPLE + i +  " | " + ANSI_RESET);
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].getType().getValue() == 0)
                    if (grid[i][j].isHit())
                        System.out.print(ANSI_RED + grid[i][j] +  ANSI_RESET + " | ");
                    else
                        System.out.print(ANSI_CYAN + grid[i][j] +  ANSI_RESET + " | ");
                else
                    if (grid[i][j].isHit())
                        System.out.print(ANSI_RED + grid[i][j] + ANSI_RESET + " | ");
                    else
                        System.out.print(ANSI_GREEN + grid[i][j] + ANSI_RESET + " | ");

            }
            System.out.println();
        }
        System.out.println();
    }


    /**
     * Pretty printing of Map object
     */
    public void prettyPrintBlindMap() {
        String border = StringUtils.repeat("-", grid.length * 4 + 3);
        final String ANSI_PURPLE = "\u001B[35m";
        final String ANSI_CYAN = "\u001B[36m";
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_RED = "\u001B[31m";
        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_BLACK = "\u001B[30m";


        System.out.print(ANSI_PURPLE +  "  | " + ANSI_RESET);
        for (int i = 0; i < grid.length; i++) {
            int pos = i + 'A';
            char c = (char) pos;
            System.out.print(ANSI_PURPLE + c + " | " + ANSI_RESET);
        }
        System.out.println();


        for (int i = 0; i < grid.length; i++) {
            if (i == 0) {
                System.out.println(ANSI_PURPLE + border + ANSI_RESET);
            } else
                System.out.println(border);
            System.out.print(ANSI_PURPLE + i +  " | " + ANSI_RESET);
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].getType().getValue() == 0)
                    if (grid[i][j].isHit())
                        System.out.print(ANSI_RED + grid[i][j] +  ANSI_RESET + " | ");
                    else
                        System.out.print(ANSI_BLACK + "?" +  ANSI_RESET + " | ");
                else
                if (grid[i][j].isHit())
                    System.out.print(ANSI_RED + grid[i][j] + ANSI_RESET + " | ");
                else
                    System.out.print(ANSI_BLACK + "?" + ANSI_RESET + " | ");

            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Ship shp = new Ship(new Point(0, 0), new Point(0,4), CellType.CARRIER);

        Player p = new Player(10);
        p.addShip(shp);

        p.getMap().prettyPrintMap();
    }
}