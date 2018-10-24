package bshpvs.model;

public enum MoveType implements TypeInterface {
    ATTACK(0),
    MOVE_SHIP(1);

    private final int value;


    @Override
    public String getText() {
        return null;
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
    private MoveType(int value) {
        this.value = value;
    }
}
