package bshpvs.engine;

import bshpvs.ai.HunterPlayer;
import bshpvs.ai.NaivePlayer;
import bshpvs.api.core.AttackResponse;
import bshpvs.model.*;
import bshpvs.statistics.GameStat;
import bshpvs.statistics.PlayerStat;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

public class Game {
    public Player firstPlayer;
    public Player secondPlayer;
    public GameStat gameStat;
    public long startTime;
    Player current;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    public Game(Player first, Player second) {
        this.firstPlayer = first;
        this.secondPlayer = second;
        this.current = firstPlayer;
        this.startTime = System.nanoTime();
    }

    public void initAi(int difficulty) {
        if (difficulty == 1) {
            secondPlayer = new NaivePlayer();
        } else {
            secondPlayer = new HunterPlayer();
        }
        randomPromptShips(secondPlayer);
        System.out.println("We are ready to play!");
    }

    public AttackResponse turn(Point coordinate) {
        String yourMove = "water";
        String theirMove = "water";
        Cell c = firstPlayer.hitOppCell(coordinate, secondPlayer);
        if (c.getType().getGroup().equals(CellGroup.SHIP)) {
            yourMove = "hit " + c.getType();
        } else if (c.getType() == CellType.LAND) {
            yourMove = "land";
        }

        
        Point tgt = secondPlayer.move(firstPlayer);
        Cell a = firstPlayer.getCell(tgt);
        if (a.getType().getGroup().equals(CellGroup.SHIP)) {
            theirMove = "hit " + a.getType();
        }

        if (c.getType().getGroup().equals(CellGroup.SHIP) && secondPlayer.isShipSunk(c.getType())) {
            yourMove = "sunk " + c.getType();
        }

        if (a.getType().getGroup().equals(CellGroup.SHIP) && firstPlayer.isShipSunk(a.getType())) {
            theirMove = "sunk " + a.getType();
        }

        if (this.firstPlayer.isDefeated()) {
            //get GameStats
            ArrayList<PlayerStat> stats = new ArrayList<PlayerStat>();
            stats.add(firstPlayer.getPlayerStat());
            stats.add(secondPlayer.getPlayerStat());
            long gameTime = System.nanoTime() - this.startTime;
            PlayerStat winner = this.secondPlayer.getPlayerStat();

            //create game stat object
            this.gameStat = new GameStat(stats, gameTime / 1000000000, winner); //divide nano time by 1 bil to convert into second

            yourMove = "lost";
            theirMove = "won";
        }

        if (this.secondPlayer.isDefeated()) {
            //get GameStats
            ArrayList<PlayerStat> stats = new ArrayList<PlayerStat>();
            stats.add(firstPlayer.getPlayerStat());
            stats.add(secondPlayer.getPlayerStat());
            long gameTime = System.nanoTime() - this.startTime;
            PlayerStat winner = this.firstPlayer.getPlayerStat();

            //create game stat object
            this.gameStat = new GameStat(stats, gameTime / 1000000000, winner); //divide nano time by 1 bil to convert into second
            
            theirMove = "lost";
            yourMove = "won";
        }

        String message = "You: " + yourMove + "          Them: " + theirMove;
        return new AttackResponse(tgt.y, tgt.x, yourMove, theirMove, message);
    }

    /**
     * Converts a readable point (E.g, A4) into numerical point (0,4)
     * @param in the readable point in string form
     * @return the Point object of the readable point
     */
    public static Point ptConv(String in) {
        in = in.toUpperCase();
        char c = in.charAt(0);
        int x = c - 'A';

        in = in.substring(1);
        int y = Integer.parseInt(in);

        return new Point(x, y);
    }

    /**
     * CLI Prompt of user to input Ship coordinates
     * @param ct the ship type
     * @param reader buffered reader
     * @param pl player
     * @throws IOException if the coordinate is not suitable
     */
    public static void promptShips(CellType ct, BufferedReader reader, Player pl) throws IOException{
        if (ct.getGroup().equals(CellGroup.TERRAIN))
            throw new IllegalArgumentException("Invalid Ship Type: " + ct.getText());

        System.out.println("Place " +  ct.getText() + " of Length " + ct.getValue() + " : ");
        System.out.println(ct.getText() + " Starting Coordinate (e.g, A4): ");
        String start = reader.readLine().toUpperCase();
        while (!start.matches("[A-J][0-9]")) {
            System.out.print("Invalid Coordinate, Please Try Again: "); //TODO: Real input validation function
            start = reader.readLine();
        }
        System.out.println(ct.getText() + " Ending Coordinate (e.g, A4): ");
        String end = reader.readLine().toUpperCase();
        while (!start.matches("[A-J][0-9]")) {
            System.out.print("Invalid Coordinate, Please Try Again: "); //TODO: Real input validation function
            end = reader.readLine();
        }

        try {
            Ship shp = new Ship(ptConv(start), ptConv(end), ct);
            pl.addShip(shp);
        } catch (IllegalArgumentException ie) {
            System.out.println(ANSI_RED + "Error: invalid coordinates for " + ct.getText() + ANSI_RESET);
            promptShips(ct, reader, pl);
        }
        pl.getMap().prettyPrintMap();
    }

    /**
     * Randomly populates a players map with ships
     * @param pl the player to be populated
     */
    public static void randomPromptShips(Player pl) {
        for (CellType ct : CellType.values()) {
            if (ct.getGroup().equals(CellGroup.SHIP)) {
                genShip(pl.getMap().getLength(), ct, pl);
            }
        }
    }

    //TODO: remove constraint param redundant can be derived from pl
    /**
     * Adds a specified ship to a players board
     * @param constraint the size of the board
     * @param ct type of ship
     * @param pl player
     */
    public static void genShip(int constraint, CellType ct, Player pl) {
        Random gen = new Random();
        Point st = new Point(gen.nextInt(constraint), gen.nextInt(constraint));
        Point end;

        if (gen.nextInt(2) == 1) {
            if (st.x < ct.getValue()) {
                end = new Point(st.x + ct.getValue() - 1, st.y);
            } else {
                end = new Point(st.x - ct.getValue() + 1, st.y);
            }
        } else {
            if (st.y < ct.getValue()) {
                end = new Point(st.x, st.y + ct.getValue() - 1);
            } else {
                end = new Point(st.x, st.y - ct.getValue() + 1);
            }
        }

        try {
            Ship s = new Ship(st, end, ct);
            pl.addShip(s);
        } catch (IllegalArgumentException iae) {
            genShip(constraint, ct, pl);
        }
    }
}
