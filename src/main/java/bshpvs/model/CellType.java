package bshpvs.model;


/**
 * Enumeration to define Type of Cell
 */
public enum CellType {
    WATER(0),
    SHIP(1);

    private int value;

    /**
     * Retrieve integer value of cell type
     * @return the integer corresponding to the cell type
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Private constructor for enumeration class
     * @param value the value of the Cell
     */
    private CellType(int value) {
        this.value = value;
    }
}
