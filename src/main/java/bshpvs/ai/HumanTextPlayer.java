package bshpvs.ai;

import bshpvs.model.Cell;
import bshpvs.model.Player;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static bshpvs.engine.Game.ptConv;

public class HumanTextPlayer extends Player implements Playable {

    public HumanTextPlayer() {
        super();
    }

    public HumanTextPlayer(int size) {
        super(size);
    }

    /**
     * Implements playable interface with most naive algorithm:
     * Random selection of a valid move
     * @return the Cell that was hit by the move
     */
    public Point move(Player opp) {
        Point target = promptPoint();
        Cell c = this.hitOppCell(target, opp);
        return target;
    }

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
