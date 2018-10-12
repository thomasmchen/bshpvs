package bshpvs.model;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Game {
    Player firstPlayer;
    Player secondPlayer;
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

    public Game(Player first, Player second, int size) {
        this.firstPlayer = first;
        this.secondPlayer = second;
        this.current = firstPlayer;
    }

    public void init() {
    }

    public static void main(String[] args) throws IOException {
        System.out.println("\n" +
                "██████╗ ███████╗██╗  ██╗██████╗ ██╗   ██╗███████╗\n" +
                "██╔══██╗██╔════╝██║  ██║██╔══██╗██║   ██║██╔════╝\n" +
                "██████╔╝███████╗███████║██████╔╝██║   ██║███████╗\n" +
                "██╔══██╗╚════██║██╔══██║██╔═══╝ ╚██╗ ██╔╝╚════██║\n" +
                "██████╔╝███████║██║  ██║██║      ╚████╔╝ ███████║\n" +
                "╚═════╝ ╚══════╝╚═╝  ╚═╝╚═╝       ╚═══╝  ╚══════╝\n" +
                "                                                 \n");

        Player one = new Player(10);
        Player two = new Player(10);
        Player current = one;


        // Player 1 Setup
        System.out.println("Welcome Player 1!");
        System.out.println("This is your grid:");
        Map.printKey();
        one.getMap().prettyPrintMap();
        System.out.println("Please place your ships:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        for (CellType ct : CellType.values()) {
            if (ct.getGroup().equals(CellGroup.SHIP)) {
                promptShips(ct, reader, one);
            }
        }

        System.out.println("============ Switching Players =============");

        randomPromptShips(two);
        // two.getMap().prettyPrintMap(); // Prints AI Map

        System.out.println("============ Shipes Placed, Players Ready! =============");

        // While neither player has lost
        while (!one.isDefeated() && !two.isDefeated()) {
            if (current == one) {
                System.out.print("Type in Target Coordinate: ");
                String target = reader.readLine();
                while (target.length() != 2) {
                    System.out.print("Invalid Target, Please Try Again:"); //TODO: Real input validation function
                    target = reader.readLine();
                }
                Point targPt = ptConv(target);
                Cell c = two.getHit(targPt);
                System.out.println("Hit target: " + c.getType().getText());
                if (c.getType().getGroup().equals(CellGroup.SHIP) && two.getShipStatus(c.getType()))
                    System.out.println("You sunk your opponents: " + c.getType().getText());
                System.out.println("Opponents Map: ");
                two.getMap().prettyPrintBlindMap();
                current = two;
            } else {
                Random gen = new Random();
                Point tgt = new Point(gen.nextInt(one.getMap().getLength()), gen.nextInt(one.getMap().getLength()));
                Cell c = one.getHit(tgt);
                System.out.println("Opponent hit " + c.getType().getText() + " at " + tgt.x + "," +  tgt.y);
                System.out.println("Your map: ");
                one.getMap().prettyPrintMap();
                current = one;

            }
        }

        if (one.isDefeated()) {
            System.out.println("You Lost!");
        } else {
            System.out.println("You Won!");
        }
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
        System.out.print(ct.getText() + " Starting Coordinate (e.g, A4): ");
        String start = reader.readLine();
        while (start.length() != 2) {
            System.out.print("Invalid Coordinate, Please Try Again: "); //TODO: Real input validation function
            start = reader.readLine();
        }
        System.out.print(ct.getText() + " Ending Coordinate (e.g, A4): ");
        String end = reader.readLine();
        while (end.length() != 2) {
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
            System.out.println(s.getType().getText() +
                    " - (" + s.st.x + "," + s.st.y + ")" +
                    " to (" + s.end.x + "," + s.end.y + ")");
        } catch (IllegalArgumentException iae) {
            genShip(constraint, ct, pl);
        }
    }


}
