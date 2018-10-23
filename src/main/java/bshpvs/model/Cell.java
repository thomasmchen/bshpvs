package bshpvs.model;

/**
 * Cell Class defined individual node within a Map.
 */
public class Cell {
    private CellType type;
    private boolean hit;

    /**
     * Default Constructor.
     * Initializes Cell to Water with false Hit value
     */
    public Cell() {
        this.type = CellType.WATER;
        this.hit = false;
    }

    /**
     * Mutator method for type field.
     * @param type the type to set the Cell to
     */
    public void setType(CellType type) {
        this.type = type;
    }

    /**
     * Accessor for retrieving type of Cell object.
     * @return
     */
    public CellType getType() {
        return type;
    }

    /**
     * Accessor method for Cell hit value.
     * @return the hit value
     */
    public boolean isHit() {
        return hit;
    }

    /**
     * Returns whether the cell is a ship
     * @return true if ship, false if not
     */
    public boolean isShip() {
        return (this.getType().getGroup().equals(CellGroup.SHIP));
    }

    /**
     * Mutator method for hit value, always sets hit to true.
     */
    public void hit() {
        this.hit = true;
    }

    /**
     * Unhit a cell (used for movement)
     */
    public void unhit() { this.hit = false; }

    /**
     * ToString method for Cell Class.
     * @return string version of Cell
     */
    public String toString() {
        return Integer.toString(type.getValue());
    }
}