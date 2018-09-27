package bshpvs.model;

import java.util.HashMap;

public enum ShipType {
    CARRIER(5),
    BATTLESHIP(4),
    SUBMARINE(3),
    CRUISER(3),
    DESTROYER(2);

    private int value;

    /**
     * Private constructor for enumeration class.
     * @param value the value of the Cell
     */
    private ShipType(int value) {
        this.value = value;
    }

    /**
     * Retrieve integer value of cell type.
     * @return the integer corresponding to the cell type
     */
    public int getValue() {
        return this.value;
    }


}
