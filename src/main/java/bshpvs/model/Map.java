package bshpvs.model;

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

    /**
     * Print function for Map object.
     */
    public void printMap() {
        for (Cell[] row : grid) {
            System.out.printf(Arrays.toString(row));
            System.out.println();
        }
    }
}