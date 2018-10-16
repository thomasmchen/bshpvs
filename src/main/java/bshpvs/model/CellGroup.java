package bshpvs.model;

public enum CellGroup implements TypeInterface {
    TERRAIN(0),
    SHIP(1);

    private final int value;

    /**
     * Retrieve the string name of the Cell
     * @return the string name
     */
    public String getText() {
        return this.name();
    }

    /**
     * Retrieve integer value of cell type.
     * @return the integer corresponding to the cell type
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Private constructor for enumeration class.
     * @param value the value of the Cell
     */
    private CellGroup(int value) {
        this.value = value;
    }
}