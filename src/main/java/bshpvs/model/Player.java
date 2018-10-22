package bshpvs.model;

import bshpvs.ai.Playable;
import bshpvs.statistics.PlayerStat;

import java.awt.Point;
import java.util.*;
import java.util.Map.Entry;

/**
 * Player class.
 */
public class Player implements Playable{
    private UUID id;
    private Map board;
    private HashMap<Player, Map> targetBoard;
    private EnumMap<CellType, Ship> ships;
    private EnumMap<CellType, Boolean> statuses;
    private PlayerStat stat;

    private static final int DEFAULT_GRID_SIZE = 10;


    //TODO: Massive input validation overhaul for Player constructors
    // Automatic size checking
    /**
     * Default constructor for Player.
     */
    public Player() {
        board = new Map(DEFAULT_GRID_SIZE);
        targetBoard = new HashMap<>();
        id = UUID.randomUUID();
        ships = new EnumMap<CellType, Ship>(CellType.class);
        statuses = new EnumMap<CellType, Boolean>(CellType.class);
        stat = new PlayerStat(this);
    }

    /**
     * Default constructor for board size, no opponents
     * @param size the size of the board
     */
    public Player(int size) {
        board = new Map(size);
        targetBoard = new HashMap<>();
        id = UUID.randomUUID();
        ships = new EnumMap<CellType, Ship>(CellType.class);
        statuses = new EnumMap<CellType, Boolean>(CellType.class);
        stat = new PlayerStat(this);
    }

    /**
     * Constructor for Player specifying board size.
     * @param size the size of the board
     */
    public Player(int size, Player opp) {
        board = new Map(size);
        id = UUID.randomUUID();
        ships = new EnumMap<CellType, Ship>(CellType.class);
        statuses = new EnumMap<CellType, Boolean>(CellType.class);
        stat = new PlayerStat(this);

        targetBoard = new HashMap<>();
        targetBoard.put(opp, new Map(size));
    }

    /**
     * Constructor for multiple Players specifying board size
     * @param size size of the boards
     * @param opps opponents
     */
    public Player(int size, List<Player> opps) {
        board = new Map(size);
        id = UUID.randomUUID();
        ships = new EnumMap<CellType, Ship>(CellType.class);
        statuses = new EnumMap<CellType, Boolean>(CellType.class);
        stat = new PlayerStat(this);

        targetBoard = new HashMap<>();
        for (Player opp : opps) {
            targetBoard.put(opp, new Map(size));
        }
    }

    /**
     * Adds a new opponent to the Player
     * @param opp the opposing player to be added
     */
    public void addOpponent(Player opp) {
        targetBoard.put(opp, new Map(opp.getMap().getLength()));
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
            throw new IllegalArgumentException("Failed to add " + shp.getType().toString() +
                    " to Player, Ship overlaps with Existing Ship.");
        }

        // Store ship in Player's EnumMap
        ships.put(shp.getType(), shp);

        // Store the ship's status
        statuses.put(shp.getType(), false);
    }

    /**
     * Removes a ship from the board
     * @param ct the ship type to be removed
     */
    public void removeShip(CellType ct) {
        // Verify ship exists on Player's board
        if (ships.containsKey(ct)) {
            throw new IllegalArgumentException("Ship: " + ct.toString() + " does not exist.");
        }

        // Remove Ship coordinates from Player's board
        unsetShip(ct);
    }

    /**
     * Set ship function places ships on map.
     * @param shp the ship to be placed
     */
    private void setShip(Ship shp) throws ShipOverlapException {
        for (Entry<CellType, Ship> s : ships.entrySet()) {
            if (shp.doesOverlap(s.getValue())) {
                throw new ShipOverlapException("Cannot place " + shp.getType() +
                        " it overlaps with " +  s.getValue().getType());
            }
        }

        for (Point pt : shp.getPoints()) {
            board.setCell(pt, shp.getType());
        }
    }

    /**
     * Unsets a ship on map
     * @param ct the ship type to be unset
     */
    private void unsetShip(CellType ct) {
        for (Point pt : ships.get(ct).getPoints()) {
            board.setCell(pt, CellType.WATER);
            board.getCell(pt).unhit();
        }
    }

    /**
     * Hits the player's cell at point x,y
     * Sets the Cell at the coordinate to hit
     * @param pt the coordinate of the cell
     * @return true if hit a ship, false if hit nothing
     */
    public Cell getHit(Point pt) {
        Cell c = this.getMap().getCell(pt);
        c.hit();
        resolveShipStatus(c.getType());
        return c;
    }

    /**
     * Returns the status of a Players ship, true if sunk, false if not
     * @param ship the ship to be checked
     * @return the status of the ship
     */
    public boolean isShipSunk(CellType ship) {
        return statuses.get(ship);
    }

    /**
     * Hit an opposing players cell
     * @param pt the point to hit
     * @param player the player being attacked
     * @return the Cell that was hit
     */
    public Cell hitOppCell(Point pt, Player player) {
        // Check if Player exists
        if (!targetBoard.containsKey(player)) {
            throw new IllegalArgumentException("Player does not exist in current Player's records.");
        }

        // Hit the opposing player
        Cell c = player.getHit(pt);

        // Update Stats
        this.getPlayerStat().incrementTurns();

        if (c.isShip()) {
            this.getPlayerStat().incrementHits();
        } else {
            this.getPlayerStat().incrementMisses();
        }

        // Update player record of attack
        this.targetBoard.get(player).setCell(pt, c.getType());
        this.targetBoard.get(player).getCell(pt).hit();
        //this.targetBoard.setCell(pt, c.getType());
        //this.targetBoard.getCell(pt).hit();
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
        return board.getMap()[p.y][p.x];
    }

    /**
     * Implement Playable Interface
     * @param pl player to attack against
     * @return point moved upon
     */
    @Override
    public Point attack(Player pl) {
        Point tgt = genRandomTarget(pl);
        this.hitOppCell(tgt, pl);
        return tgt;
    }

    @Override
    public void move() {

    }

    @Override
    public void turn() {

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
    public Map getTargetBoard(Player opp) {
        return this.targetBoard.get(opp);
    }

    /**
     * Return list of a Player's opponents
     * @return the current player's opponents
     */
    public ArrayList<Player> getOpponents() {
        return new ArrayList<Player>(this.targetBoard.keySet());
    }

    /**
     * Randomly generate a valid target
     * @return the valid target
     */
    public Point genRandomTarget(Player opp) {
        Random gen = new Random();
        Point tgt = new Point(gen.nextInt(targetBoard.get(opp).getLength()), gen.nextInt(targetBoard.get(opp).getLength()));
        if (opp.getCell(tgt).isHit()) {
            for (int i = 0; i < opp.getMap().getLength(); i++) {
                for (int j = 0; j < opp.getMap().getLength(); j++) {
                    tgt = new Point(i ,j);
                    if (!opp.getCell(tgt).isHit())
                        return  tgt;
                }
            }
        }

        return tgt;
    }

    /**
     * Selects a random Player opponent from targets
     * @return the randomly selected Player
     */
    public Player genRandomOpp() {
        List<Player> players = new ArrayList<Player>(targetBoard.keySet());
        Random gen = new Random();
        int index = gen.nextInt(players.size());
        return players.get(index);
    }

    /**
     * Checks if an opponents point is valid on own target map:
     * 1) Is on the map
     * 2) Has not been hit already
     * @return true if valid false if invalid
     */
    public boolean isValidTgtPoint(Point p, Player opp) {
        return (isValidPoint(p, opp.getMap()) && !targetBoard.get(opp).getCell(p).isHit());
    }

    /**
     * Checks if a point falls inside a map
     * @param p the point
     * @param m the map
     * @return true if valid, false if invalid
     */
    public static boolean isValidPoint(Point p, Map m) {
        return (p.x < m.getLength() &&
                p.y < m.getLength() &&
                p.x >= 0 && p.y >= 0);
    }

    /**
     * Accessor method for player ID
     * @return the id of the player
     */
    public UUID getID() {
        return this.id;
    }

    /**
     * Accessor method for player's statistics
     * @return the statistics of the player
     */
    public PlayerStat getPlayerStat() {
        return this.stat;
    }

    /**
     * Equals method based on unique id
     * @param pl the player to be compared to
     * @return whether the two player objects are equal
     */
    public boolean equals(Player pl) {
        return this.id.equals(pl.getID());
    }


    /**
     * Checks if new point position of Ship of given CellType is possible (no overlapping, inside map)
     * @param st new starting point
     * @param end new ending point
     * @param ct ship type
     * @return true if valid, false if invalid
     */
    public boolean isMovementValid(Point st, Point end, CellType ct) {
        // Validate that Ship will not overlap with any other Ship
        Ship newShip = new Ship(st, end, ct);
        if (isValidPoint(st, this.getMap()) && isValidPoint(end, this.getMap())) {
            for (CellType s : ships.keySet()) {
                if (ct.getValue() != s.getValue()) {
                    if (ships.get(s).doesOverlap(newShip)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    /**
     * Move a specific ship forward
     * @param ct the ship to be moved
     */
    public void moveForward(CellType ct) throws ImmobileShipException{
        Ship shipToBeMoved = ships.get(ct);

        if (shipToBeMoved.checkHit(this.getMap())) {
            throw new ImmobileShipException(shipToBeMoved.getType().getText() +
                    "has been hit, and cannot be moved!");
        }

        Point newSt = shipToBeMoved.st;
        Point newEnd = shipToBeMoved.end;

        if (shipToBeMoved.isVertical()) {
            newSt.y--;
            newEnd.y++;
        } else {
            newSt.x++;
            newEnd.x--;
        }

        if (isMovementValid(newSt, newEnd, ct)) {
            ships.remove(ct);
            this.addShip(new Ship(newSt, newEnd, ct));
        }
    }

    /**
     * Move a specific ship backward
     * @param ct the ship to be moved
     */
    public void moveBackward(CellType ct) throws ImmobileShipException {
        Ship shipToBeMoved = ships.get(ct);
        Point newSt = shipToBeMoved.st;
        Point newEnd = shipToBeMoved.end;

        if (shipToBeMoved.checkHit(this.getMap())) {
            throw new ImmobileShipException(shipToBeMoved.getType().getText() +
                    "has been hit, and cannot be moved!");
        }

        if (shipToBeMoved.isVertical()) {
            newSt.y++;
            newEnd.y--;
        } else {
            newSt.x--;
            newEnd.x++;
        }

        if (isMovementValid(newSt, newEnd, ct)) {
            ships.remove(ct);
            this.addShip(new Ship(newSt, newEnd, ct));
        }
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
     * Custom Exception class for overlap of two ships
     */
    public class ImmobileShipException extends Exception {
        /**
         * Custom Exception when two ships overlap
         * @param errorMessage the message to be outputted
         */
        public ImmobileShipException(String errorMessage) {
            //TODO: Provide additional functionality
            super(errorMessage);
        }
    }


}