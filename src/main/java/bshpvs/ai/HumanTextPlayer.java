package bshpvs.ai;

import bshpvs.model.*;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static bshpvs.engine.Game.ptConv;

public class HumanTextPlayer extends Player implements Playable {

    public HumanTextPlayer() {
        super();
    }

    public HumanTextPlayer(int size) {
        super(size);
    }

    public HumanTextPlayer(int size, Player opp) {
        super(size, opp);
    }

    public HumanTextPlayer(int size, List<Player> opps) {
        super(size, opps);
    }

    /**
     * Implements playable interface with most naive algorithm:
     * Random selection of a valid attack
     * @return the Cell that was hit by the attack
     */
    @Override
    public Point attack(Player opp) {
        Point target = promptPoint();
        Cell c = this.hitOppCell(target, opp);
        return target;
    }

    @Override
    public void move() {
        this.promptMove();
    }

    @Override
    public void turn() {
        this.promptTurn();
    }

    /**
     * Prompts user as to what move to make
     */
    public void promptTurn() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String move = "";

        while (true) {
            System.out.println("Would you like to attack an opponent or move a ship?");
            System.out.println("Enter 1 for Attack");
            System.out.println("Enter 2 for Move");
            try {
                move = reader.readLine();
            } catch(IOException ioe) {
                System.err.println("Failed to read user input");
            }
            if (!move.matches("[1-2]")) {
                System.out.println("Invalid choice, Please Try Again:"); //TODO: Real input validation function
                continue;
            } else {
                break;
            }
        }

        if (move.equals("1")) {
            Player atkOpp = this.promptOpponent();
            this.attack(atkOpp);
        } else {
            this.move();
        }
    }

    /**
     * Prompts text user for new ship position and calls execMove based on input
     */
    public void promptMove() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String entry = "";
        String move = "";

        while (true) {
            System.out.println("Which Ship would you like to move? Type in the name (ex: CRUISER:");

            // Print all ships + values for reference
            for (CellType ct : CellType.values()) {
                if (ct.getGroup().getValue() == CellGroup.SHIP.getValue()) {
                    System.out.println(ct.getText() + " : " + ct.getValue());
                }
            }

            // Read in user selection of ship + validate input
            try {
                entry = reader.readLine().toUpperCase();
            } catch (IOException ioe) {
                System.err.println("Failed to read user input");
            }

            if (!CellType.contains(entry)) {
                System.out.println("Invalid Target, Please Try Again:"); //TODO: Real input validation function
                continue;
            }

            // Read in new Ship points
            System.out.println("Would you like to move the Ship forward or backward?");
            System.out.println("Enter 1 for Forward");
            System.out.println("Enter 2 for Backward");
            try {
                move = reader.readLine().toUpperCase();
            } catch(IOException ioe) {
                System.err.println("Failed to read user input");
            }
            if (!move.matches("[1-2]")) {
                System.out.println("Invalid choice, Please Try Again:"); //TODO: Real input validation function
                continue;
            } else {
                break;
            }
        }

        boolean direction = move.equals("1");

        try {
            executeMove(CellType.valueOf(entry), direction);
        } catch (ImmobileShipException ise) {
            System.out.println("Cannot move ship as it has already been hit! Please try again: ");
            this.promptMove();
        }
    }

    /**
     * Executes a ship movement
     * @param ct the type of ship
     * @param forward the position of the movement: false for backward, true for forward
     */
    public void executeMove(CellType ct, boolean forward) throws ImmobileShipException {
        // Validate cell type
        if (ct.getGroup().getValue() != CellGroup.SHIP.getValue()) {
            throw new IllegalArgumentException("The provided CellType " + ct.getText() + "is not a Ship.");
        }

        if (forward) {
            this.moveForward(ct);
        } else {
            this.moveBackward(ct);
        }
    }


    public Player promptOpponent() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String opponent = "";

        while (true) {
            System.out.println("Which opponent would you like to target? ");

            ArrayList<Player> opps = this.getOpponents();
            for (int i = 0; i < opps.size(); i++) {
                System.out.println(i + " : " +
                        opps.get(i).getPlayerStat().getPlayerType() + "_" +
                        opps.get(i).getID().toString().substring(0, 10));
            }

            System.out.println("Enter in the integer (ex. 1):");

            try {
                opponent = reader.readLine().toUpperCase();
            } catch(IOException ioe) {
                System.err.println("Failed to read user input");
            }

            int oppInt = Integer.parseInt(opponent);

            if (oppInt <= 0 || oppInt >= opps.size()) {
                System.out.println("Invalid Opponent, Please Try Again:"); //TODO: Real input validation function
                continue;
            } else {
                return opps.get(oppInt);
            }
        }
    }

    /**
     * Prompts text user for point to strike
     * @return the point to strike
     */
    public Point promptPoint() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String target = "";
        while (true) {
            System.out.println("Type in Target Coordinate: ");
            try {
                target = reader.readLine().toUpperCase();
            } catch(IOException ioe) {
                System.err.println("Failed to read user input");
            }
            if (!target.matches("[A-J][0-9]")) {
                System.out.println("Invalid Target, Please Try Again:"); //TODO: Real input validation function
                continue;
            } else {
                break;
            }
        }
        return ptConv(target);
    }
}
