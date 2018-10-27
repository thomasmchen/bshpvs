package bshpvs.engine;

import bshpvs.ai.HunterPlayer;
import bshpvs.ai.NaivePlayer;
import bshpvs.api.core.AttackResponse;
import bshpvs.api.core.MoveResponse;
import bshpvs.api.core.AttackResponse.CoordinateWithInfo;
import bshpvs.model.*;
import bshpvs.statistics.GameStat;
import bshpvs.statistics.PlayerStat;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;


public class Game {
    public Player firstPlayer;
    public Player[] opponents;
    public GameStat gameStat;
    public long startTime;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    public Game(Player first, Player[] players) {
        this.firstPlayer = first;
        this.opponents = players;
        for (int i = 0; i < this.opponents.length; i++) {
            randomPromptShips(this.opponents[i]);
        }
        this.startTime = System.nanoTime();
    }

    public MoveResponse moveTurn(int shipId, String direction) {
        CellType type = CellType.DESTROYER;
        
            switch (shipId) {
                case 0:
                    type = CellType.CARRIER;
                break;
                case 1:
                    type = CellType.BATTLESHIP;
                break;
                case 2:
                    type = CellType.CRUISER;
                break;
                case 3:
                    type = CellType.SUBMARINE;
                break;
                case 4:
                    type = CellType.DESTROYER;
                break;
            }


           
          try {
              Point p  = new Point(1, 1);
              Point[] points = {p};
            if (direction.equals("forward")) {
                Ship result = this.firstPlayer.moveForward(type);
                firstPlayer.getMap().prettyPrintMap();
                if (result == null) {
                    return new MoveResponse(-1, points);
                }
                Point[] spaces = result.getPoints();
                return new MoveResponse(shipId, spaces);
            } else {
                Ship result = this.firstPlayer.moveBackward(type);
                firstPlayer.getMap().prettyPrintMap();
                if (result == null) {
                    return new MoveResponse(-1, points);
                }
                Point[] spaces = result.getPoints();
                return new MoveResponse(shipId, spaces);
            }

            
          } catch (Exception e) {
            Point p  = new Point(1, 1);
            Point[] points = {p};
            return new MoveResponse(-1, points);
        }
        
    }

    public AttackResponse turn(Point coordinate, int playerPos) {
        ArrayList<CoordinateWithInfo> coors = new ArrayList<CoordinateWithInfo>();
        String yourMove = "water";
        String theirMove = "water";
         if (coordinate.x != -1) {
            if (playerPos > firstPlayer.getOpponents().size() - 1) {
                System.out.println("Player not reflected in arrays");
                return null;
            }

            Player opp = opponents[playerPos];
            Cell c = firstPlayer.hitOppCell(coordinate, opp);
            if (c.getType().getGroup().equals(CellGroup.SHIP)) {
                yourMove = "hit " + c.getType();
                CoordinateWithInfo info = new CoordinateWithInfo(coordinate.x, coordinate.y, playerPos, "hit");
                coors.add(info);
            } else {
                CoordinateWithInfo info = new CoordinateWithInfo(coordinate.x, coordinate.y, playerPos, "miss");
                coors.add(info);
            }

            if (c.getType().getGroup().equals(CellGroup.SHIP) && opp.isShipSunk(c.getType())) {
                yourMove = "sunk " + c.getType();
                EnumMap<CellType, Ship> ships = opp.getShips();
                for (Entry<CellType, Ship> s : ships.entrySet()) {
                    if (s.getKey() == c.getType()) {
                        System.out.println("We found the sunk ship");
                        Point[] p = s.getValue().getPoints();
                        for (int j = 0; j < p.length; j++){
                            CoordinateWithInfo info = new CoordinateWithInfo(p[j].x, p[j].y, playerPos, "sunk");
                            coors.add(info);
                        }
                    }
                }

            }
         } else {
            yourMove = "move";
         }

         for (int i = 0; i < opponents.length; i++) {
             Player p = opponents[i];
             if (p.isDefeated()) {
                 continue;
             }
             Player opp = p.genRandomOpp();
             Point tgt = p.attack(opp);
            Cell a = opp.getCell(tgt);
            int pos = -1;
            for (int j = 0; j < opponents.length; j++) {
                if (this.opponents[j].getID() == opp.getID()) {
                    pos = j;
                }
            }
            if (a.getType().getGroup().equals(CellGroup.SHIP)) {
                CoordinateWithInfo info = new CoordinateWithInfo(tgt.x, tgt.y, pos, "hit");
                coors.add(info);
            } else {
                CoordinateWithInfo info = new CoordinateWithInfo(tgt.x, tgt.y, pos, "miss");
                coors.add(info);
            }

            if (a.getType().getGroup().equals(CellGroup.SHIP) && opp.isShipSunk(a.getType())) {
                EnumMap<CellType, Ship> ships = opp.getShips();
                for (Entry<CellType, Ship> s : ships.entrySet()) {
                    if (s.getKey() == a.getType()) {
                        System.out.println("We found the sunk ship");
                        Point[] d = s.getValue().getPoints();
                        for (int j = 0; j < d.length; j++){
                            CoordinateWithInfo info = new CoordinateWithInfo(d[j].x, d[j].y, pos, "sunk");
                            coors.add(info);
                        }
                    }
                }

            }
         }

         // check to see if the user has won the game
         boolean won = true;
         for (int i = 0; i < opponents.length; i++) {
            if (!opponents[i].isDefeated()) {
                won = false;
            }
         }
         if (won) {
            CoordinateWithInfo info = new CoordinateWithInfo(0, 0, -1, "won");
            coors.add(info);
             //divide nano time by 1 bil to convert into second
             //get GameStats
            ArrayList<PlayerStat> stats = new ArrayList<PlayerStat>();
            stats.add(firstPlayer.getPlayerStat());
            PlayerStat winner = this.firstPlayer.getPlayerStat();
            for (int i = 0; i < this.opponents.length; i++) {
                Player p  = this.opponents[i];
                stats.add(p.getPlayerStat());
            }
            long gameTime = System.nanoTime() - this.startTime;
            

            //create game stat object
            this.gameStat = new GameStat(stats, gameTime, winner);
         }


         // check to see if the use has lost
         if (this.firstPlayer.isDefeated()) {
            CoordinateWithInfo info = new CoordinateWithInfo(0, 0, -1, "lost");
            coors.add(info);
            //get GameStats
            ArrayList<PlayerStat> stats = new ArrayList<PlayerStat>();
            stats.add(firstPlayer.getPlayerStat());
            PlayerStat winner = this.opponents[0].getPlayerStat();
            for (int i = 0; i < this.opponents.length; i++) {
                Player p  = this.opponents[i];
                stats.add(p.getPlayerStat());
                if (!p.isDefeated()) {
                    winner = p.getPlayerStat();
                }
            }
            long gameTime = System.nanoTime() - this.startTime;
            

            //create game stat object
            this.gameStat = new GameStat(stats, gameTime / 1000000000, winner);
         }
        
        String message = "You: " + yourMove + "          Them: " + theirMove;

        return new AttackResponse(yourMove, theirMove, message, coors.toArray(new CoordinateWithInfo[coors.size()]));
        /*if (coordinate.x != -1) {
            Cell c = firstPlayer.hitOppCell(coordinate, secondPlayer);
            if (c.getType().getGroup().equals(CellGroup.SHIP)) {
                yourMove = "hit " + c.getType();
            } else if (c.getType() == CellType.LAND) {
                yourMove = "land";
            }

            if (c.getType().getGroup().equals(CellGroup.SHIP) && secondPlayer.isShipSunk(c.getType())) {
                yourMove = "sunk " + c.getType();
            }
        }  else {
            yourMove = "move";
        }

        Point tgt = secondPlayer.attack(firstPlayer);
        Cell a = firstPlayer.getCell(tgt);
        if (a.getType().getGroup().equals(CellGroup.SHIP)) {
            theirMove = "hit " + a.getType();
        }

        

        if (a.getType().getGroup().equals(CellGroup.SHIP) && firstPlayer.isShipSunk(a.getType())) {
            theirMove = "sunk " + a.getType();
        }
        */
        
        
            /*
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
        CoordinateWithMap m = new CoordinateWithMap(1, 1, 1);
        CoordinateWithMap[] coors = { m } ;
        return new AttackResponse(tgt.y, tgt.x, yourMove, theirMove, message, coors);*/
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
