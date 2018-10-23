package bshpvs.engine;

import bshpvs.ai.HumanTextPlayer;
import bshpvs.ai.HunterPlayer;
import bshpvs.ai.NaivePlayer;
import bshpvs.model.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static bshpvs.engine.Game.promptShips;
import static bshpvs.engine.Game.randomPromptShips;

public class TextGame {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("\n" +
                "██████╗ ███████╗██╗  ██╗██████╗ ██╗   ██╗███████╗\n" +
                "██╔══██╗██╔════╝██║  ██║██╔══██╗██║   ██║██╔════╝\n" +
                "██████╔╝███████╗███████║██████╔╝██║   ██║███████╗\n" +
                "██╔══██╗╚════██║██╔══██║██╔═══╝ ╚██╗ ██╔╝╚════██║\n" +
                "██████╔╝███████║██║  ██║██║      ╚████╔╝ ███████║\n" +
                "╚═════╝ ╚══════╝╚═╝  ╚═╝╚═╝       ╚═══╝  ╚══════╝\n" +
                "                                                 \n");

        HumanTextPlayer one = new HumanTextPlayer(10);
        Player two;

        System.out.println("Please select AI Difficulty:\n" +
                "1) Easy\n" +
                "2) Medium\n");

        String difficulty = reader.readLine();
        while (!difficulty.matches("[1-2]")) {
            System.out.println("Invalid Difficulty, Please Try Again: ");
            difficulty = reader.readLine();
        }

        if (Integer.parseInt(difficulty) == 1) {
            two = new NaivePlayer();
        } else {
            two = new HunterPlayer();
        }

        one.addOpponent(two);
        two.addOpponent(one);

        Player current = one;


        // Player 1 Setup
        System.out.println("Welcome Player 1!");
        System.out.println("This is your grid:");
        System.out.println(Map.getKeyMap());
        one.getMap().prettyPrintMap();
        System.out.println("Please place your ships:");

        for (CellType ct : CellType.values()) {
            if (ct.getGroup().equals(CellGroup.SHIP)) {
                promptShips(ct, reader, one);
            }
        }

        randomPromptShips(two);

        System.out.println("============ The AI is ready! =============");

        // While neither player has lost
        while (!one.isDefeated() && !two.isDefeated()) {
            if (current == one) {

                Point targPt = one.attack(two);
                Cell c = one.getCell(targPt);

                System.out.println("Hit target: " + c.getType().getText());
                if (c.getType().getGroup().equals(CellGroup.SHIP) && two.isShipSunk(c.getType()))
                    System.out.println("You sunk your opponents: " + c.getType().getText());
                System.out.println("Target Map: ");
                one.getTargetBoard(two).prettyPrintMap();
                current = two;
            } else {
                Point tgt = two.attack(one);
                Cell c = one.getCell(tgt);
                System.out.println("Opponent hit " + c.getType().getText() + " at " + tgt.x + "," +  tgt.y);
                System.out.println("Your map: ");
                one.getMap().prettyPrintMap();
                current = one;
            }
        }

        if (one.isDefeated()) {
            System.out.println(ANSI_RED + "\n" +
                    "██████╗ ███████╗███████╗███████╗ █████╗ ████████╗\n" +
                    "██╔══██╗██╔════╝██╔════╝██╔════╝██╔══██╗╚══██╔══╝\n" +
                    "██║  ██║█████╗  █████╗  █████╗  ███████║   ██║   \n" +
                    "██║  ██║██╔══╝  ██╔══╝  ██╔══╝  ██╔══██║   ██║   \n" +
                    "██████╔╝███████╗██║     ███████╗██║  ██║   ██║   \n" +
                    "╚═════╝ ╚══════╝╚═╝     ╚══════╝╚═╝  ╚═╝   ╚═╝   \n" +
                    "                                                 \n" + ANSI_RESET);
        } else {
            System.out.println(ANSI_GREEN + "\n" +
                    "██╗   ██╗██╗ ██████╗████████╗ ██████╗ ██████╗ ██╗   ██╗\n" +
                    "██║   ██║██║██╔════╝╚══██╔══╝██╔═══██╗██╔══██╗╚██╗ ██╔╝\n" +
                    "██║   ██║██║██║        ██║   ██║   ██║██████╔╝ ╚████╔╝ \n" +
                    "╚██╗ ██╔╝██║██║        ██║   ██║   ██║██╔══██╗  ╚██╔╝  \n" +
                    " ╚████╔╝ ██║╚██████╗   ██║   ╚██████╔╝██║  ██║   ██║   \n" +
                    "  ╚═══╝  ╚═╝ ╚═════╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝   ╚═╝   \n" +
                    "                                                       \n" + ANSI_RESET);
        }
    }
}
