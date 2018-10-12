package bshpvs.model;

import java.awt.Point;
import java.awt.geom.Line2D;

/**
 * Ship class
 */
public class Ship {
    public Point st;
    public Point end;
    private CellType type;
    private Point[] points;

    /**
     * Constructor for Ship Class.
     * @param start starting coordinate point for ship
     * @param end ending coordinate point for ship
     * @param type type of ship
     */
    public Ship(Point start, Point end, CellType type) {
        this.st = start;
        this.end = end;

        int length = (int) start.distance(end);
        length++;
        if (length != type.getValue() || type.getGroup().equals(CellGroup.TERRAIN)) {
            throw new IllegalArgumentException("Length from " +
                            start.toString() + " to " + end.toString() + " of " + length +
                            " does not match length of " + type.toString() + " of " + type.getValue());
        } else {
            this.type = type;
        }

        this.points = calcShipPoints();

    }

    /**
     * Calculates the the individual points of the Ship
     * @return the points of the ship
     */
    private Point[] calcShipPoints() {
        Point[] pts = new Point[type.getValue()];

        int x = this.st.x;
        int y = this.st.y;
        int i = 0;

        if (this.st.x == this.end.x) {
            if (this.st.y < this.end.y) {
                while (y != (this.end.y + 1)) {
                    pts[i] = new Point(this.st.x, y);
                    y++;
                    i++;
                }
            } else {
                while (y != (this.end.y - 1)) {
                    pts[i] = new Point(this.st.x, y);
                    y--;
                    i++;
                }
            }
        } else {
            if (this.st.x < this.end.x) {
                while ((x != this.end.x + 1)) {
                    pts[i] = new Point(x, this.st.y);
                    x++;
                    i++;
                }
            } else {
                while ((x != this.end.x - 1)) {
                    pts[i] = new Point(x, this.st.y);
                    x--;
                    i++;
                }
            }
        }

        return pts;
    }

    /**
     * Checks if given Ship is sunk on a given board
     * @param board the board the ship is on
     * @return whether the Ship is sunk
     */
    public boolean checkSunk(Map board) {
        Cell[][] map = board.getMap();
        int hitCount = 0;
        for (Point p : points) {
            if (map[p.y][p.x].isHit())
                hitCount++;
        }

        return (hitCount == points.length);
    }

    /**
     * Accessor function for type of Ship object.
     * @return the type of the ship
     */
    public CellType getType() {
        return this.type;
    }

    /**
     * Accessor function for the points of a ship
     * @return the points of a ship
     */
    public Point[] getPoints() {
        return this.points;
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