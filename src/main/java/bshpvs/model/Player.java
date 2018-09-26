package bshpvs.model;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.awt.Point;
import java.util.EnumMap;
import java.util.Map.Entry;
import java.util.UUID;

/**
 * Player class.
 */
public class Player {
    private UUID id;
    private Map board;
    private Map oppBoard;
    private EnumMap<ShipType, Ship> ships;
    private EnumMap<ShipType, Boolean> statuses;

    private static final int DEFAULT_GRID_SIZE = 10;

    /**
     * Default constructor for Player.
     */
    public Player() {
        board = new Map(DEFAULT_GRID_SIZE);
        oppBoard = new Map(DEFAULT_GRID_SIZE);
        id = UUID.randomUUID();
    }

    /**
     * Constructor for Player specifying board size.
     * @param size the size of the board
     */
    public Player(int size) {
        board = new Map(size);
        oppBoard = new Map(size);
        id = UUID.randomUUID();
    }

    /**
     * Adds ship to Player.
     * @param shp the ship to be added
     * @return true if successful, false if not
     */
    public void addShip(Ship shp) {
        // Verify ship does not exist on Player's board
        if (ships.containsKey(shp.getType())) {
            throw new IllegalArgumentException("Ship: " + shp.getType().toString() + " already exists.");
        }

        // Attempt to write Ship to Player's board, throw exception if ship overlaps
        try {
            this.setShip(shp);
        } catch (ShipOverlapException soe) {
            throw new IllegalArgumentException("Failed to add Ship to Player, Ship overlaps with Existing Ship.");
        }

        // Store ship in Player's EnumMap
        ships.put(shp.getType(), shp);

        // Store the ship's status
        statuses.put(shp.getType(), false);
    }

    /**
     * Set ship function places ships on map.
     * @param shp the ship to be placed
     */
    private void setShip(Ship shp) throws ShipOverlapException {
        for (Entry<ShipType, Ship> s : ships.entrySet()) {
            if (shp.doesOverlap(s.getValue())) {
                throw new IllegalArgumentException("Cannot place" + shp.getType() +
                        " it overlaps with " +  s.getValue().getType());
            }
        }

        int x = shp.st.x;
        int y = shp.st.y;

        if (shp.st.x == shp.end.x) {
            while (y != shp.end.y) {
                board.setCell(new Point(shp.st.x, y), CellType.SHIP);
                y++;
            }
        } else {
            while (x != shp.end.x) {
                board.setCell(new Point(x, shp.st.y), CellType.SHIP);
                x++;
            }
        }

        board.printMap();
    }


    /**
     * Hit an opposing players cell
     * @param pt the point to hit
     * @param player the player being attacked
     * @return the Cell that was hit
     */
    public Cell hitOppCell(Point pt, Player player) {
        player.updateCell(pt);
        return player.getCell(pt);
    }


    /**
     * Returns the status of a specific ship
     * @param type the ship's type
     * @return the status of the ship
     */
    public boolean isShipSunk(ShipType type) {
        return statuses.get(type);
    }

    /**
     * Returns the status of the player, true if all ships have been sunk
     * @return the status of the player
     */
    public boolean isDefeated() {
        return !statuses.containsValue(true);
    }

    /**
     * Retrieve specific Cell from Player's board (Map.Cell[][])
     * @param p the Point representing the ell
     * @return the cell
     */
    public Cell getCell(Point p) {
        return board.getMap()[p.x][p.y];
    }

    /**
     * Update the hit status of a specific cell to HIT
     * @param p the point to be updated
     */
    public void updateCell(Point p) {
        board.getMap()[p.x][p.y].hit();
    }

    /**
     * Custom Exception class for overlap of two ships
     */
    private class ShipOverlapException extends Exception {
        /**
         * Custom Exception when two ships overlap
         * @param errorMessage the message to be outputted
         */
        public ShipOverlapException(String errorMessage) {
            //TODO: Provide additional functionality
            super(errorMessage);
        }
    }

}