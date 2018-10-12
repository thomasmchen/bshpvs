package bshpvs.model;

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
    private Map targetBoard;
    private EnumMap<CellType, Ship> ships;
    private EnumMap<CellType, Boolean> statuses;

    private static final int DEFAULT_GRID_SIZE = 10;

    /**
     * Default constructor for Player.
     */
    public Player() {
        board = new Map(DEFAULT_GRID_SIZE);
        targetBoard = new Map(DEFAULT_GRID_SIZE);
        id = UUID.randomUUID();
        ships = new EnumMap<CellType, Ship>(CellType.class);
        statuses = new EnumMap<CellType, Boolean>(CellType.class);
    }

    /**
     * Constructor for Player specifying board size.
     * @param size the size of the board
     */
    public Player(int size) {
        board = new Map(size);
        targetBoard = new Map(size);
        id = UUID.randomUUID();
        ships = new EnumMap<CellType, Ship>(CellType.class);
        statuses = new EnumMap<CellType, Boolean>(CellType.class);
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
        for (Entry<CellType, Ship> s : ships.entrySet()) {
            if (shp.doesOverlap(s.getValue())) {
                throw new IllegalArgumentException("Cannot place" + shp.getType() +
                        " it overlaps with " +  s.getValue().getType());
            }
        }

        for (Point pt : shp.getPoints()) {
            board.setCell(pt, shp.getType());
        }
    }

    /**
     * Hits the player's cell at point x,y
     * Sets the Cell at the coordinate to hit
     * @param pt the coordinate of the cell
     * @return true if hit a ship, false if hit nothing
     */
    public Cell getHit(Point pt) {
        Cell c = board.getCell(new Point(pt.x, pt.y));
        c.hit();
        resolveShipStatus(c.getType());
        return c;
    }

    /**
     * Returns the status of a Players ship, true if hit, false if not
     * @param ship the ship to be checked
     * @return the status of the ship
     */
    public boolean getShipStatus(CellType ship) {
        return statuses.get(ship);
    }

    /**
     * Hit an opposing players cell
     * @param pt the point to hit
     * @param player the player being attacked
     * @return the Cell that was hit
     */
    public Cell hitOppCell(Point pt, Player player) {
        // Hit the opposing player
        Cell c = player.getHit(pt);

        // Update player record of move
        targetBoard.setCell(pt, c.getType());
        targetBoard.getCell(pt).hit();
        return c;
    }

    /**
     * Returns the status of a specific ship
     * @param type the ship's type
     * @return the status of the ship
     */
    public boolean resolveShipStatus(CellType type) {
        //TODO: Throw Custom Exception
        if (type.getGroup().equals(CellGroup.TERRAIN))
            return false;

        if (ships.get(type).checkSunk(this.board)) {
            statuses.remove(type);
            statuses.put(type,true);
        }

        return statuses.get(type);
    }

    /**
     * Returns the status of the player, true if all ships have been sunk
     * @return the status of the player
     */
    public boolean isDefeated() {
        return !statuses.containsValue(false);
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


    /**
     * Accessor for Player board
     * @return the board of the player
     */
    public Map getMap() {
        return this.board;
    }

    /**
     * Accessor for targetBoard
     * @return the current players view of the board of the player's opponents
     */
    public Map getTargetBoard() {
        return this.targetBoard;
    }

}