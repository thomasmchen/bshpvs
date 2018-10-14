package bshpvs.model;

public enum CellType implements TypeInterface {

    WATER(0, CellGroup.TERRAIN),
    LAND(1, CellGroup.TERRAIN),
    DESTROYER(2, CellGroup.SHIP),
    CRUISER(3, CellGroup.SHIP),
    SUBMARINE(3, CellGroup.SHIP),
    BATTLESHIP(4, CellGroup.SHIP),
    CARRIER(5, CellGroup.SHIP);

    private final int shipVal;
    private final CellGroup group;

    /**
     * Return the string name of the ship
     * @return the string name of the ship
     */
    public String getText() {
        return this.name();
    }

    /**
     * Private constructor for enumeration class.
     * @param value the value of the Cell
     */
    private CellType(int value, CellGroup group) {
        this.shipVal = value;
        this.group = group;
    }

    /**
     * Retrieve integer value of cell type.
     * @return the integer corresponding to the cell type
     */
    public int getValue() {
        return this.shipVal;
    }

    public CellGroup getGroup() {
        return this.group;
    }

}