package bshpvs.model;

public class Cell {
    private CellType type;
    private boolean hit;

    public Cell() {
        this.type = CellType.WATER;
        this.hit = false;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public CellType getType() {
        return type;
    }

    public boolean isHit() {
        return hit;
    }

    public void hit() {
        this.hit = true;
    }

    public String toString() {
        if (type.equals(CellType.SHIP))
            return "0";
        else
            return "1";
    }
}