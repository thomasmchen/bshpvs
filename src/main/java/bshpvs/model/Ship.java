package bshpvs.model;

import java.awt.Point;
import java.awt.geom.Line2D;

/**
 * Ship class
 */
public class Ship {
    public Point st;
    public Point end;
    private ShipType type;

    /**
     * Constructor for Ship Class.
     * @param start starting coordinate point for ship
     * @param end ending coordinate point for ship
     * @param type type of ship
     */
    public Ship(Point start, Point end, ShipType type) {
        this.st = start;
        this.end = end;

        int length = (int) start.distance(end);
        length++;
        if (length != type.getValue()) {
            throw new IllegalArgumentException("Length from " +
                            start.toString() + " to " + end.toString() + " of " + length +
                            " does not match length of " + type.toString() + " of " + type.getValue());
        } else {
            this.type = type;
        }
    }

    /**
     * Accessor function for type of Ship object.
     * @return the type of the ship
     */
    public ShipType getType() {
        return this.type;
    }

    /**
     * Checks if two ships overlap
     * @param ext the ship to compare with
     * @return whether or not the ships overlap
     */
    public boolean doesOverlap(Ship ext) {
        return Line2D.linesIntersect(
                st.x, st.y,
                end.x, end.y,
                ext.st.x, ext.st.y,
                ext.end.x, ext.end.y);
    }
}