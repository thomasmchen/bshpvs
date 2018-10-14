package bshpvs.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

public class HunterPlayer extends Player implements Playable{
    private Stack<Point> targets;
    private Point target;
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RESET = "\u001B[0m";


    public HunterPlayer() {
        super();
        this.targets = new Stack<Point>();
        target = null;
    }

    public HunterPlayer(int size) {
        super(size);
        this.targets = new Stack<Point>();
        target = null;
    }

    public Point move(Player opp) {
        System.out.println(ANSI_YELLOW + "AI MOVE" + ANSI_RESET);

        // First move or no more targets
        if (target == null || targets.empty()) {
            target = genRandomTarget();
            System.out.println(ANSI_YELLOW + "Calculating Target: (" + target.x + ", " + target.y + ")" + ANSI_RESET);
            Cell c = this.hitOppCell(target, opp);
            this.updateTargets(c);
            
            return target;
        }  else {
            target = targets.pop();
            System.out.println(ANSI_YELLOW + "Calculating Target: (" + target.x + ", " + target.y + ")" + ANSI_RESET);
            Cell c = this.hitOppCell(target, opp);
            this.updateTargets(c);

            return target;
        }
    }

    /**
     * Given a hit target cell will update candidates
     * @param c the cell that was hit
     */
    private void updateTargets(Cell c) {
        if (c.isShip()) {
            System.out.println(ANSI_YELLOW + "Target Hit! Adding new Candidate Targets: " + ANSI_RESET);
            ArrayList<Point> candidates = huntPts(target);
            if (candidates.size() == 0) {
                System.out.println(ANSI_YELLOW + "No Suitable Targets!" + ANSI_RESET);
                return;
            }
            for (Point pt : candidates) {
                System.out.println(ANSI_YELLOW + pt + ANSI_RESET);
                targets.push(pt);
            }
        }
    }

    /**
     * Given a point that is a ship, returns all valid surrounding points
     * @param p
     * @return the next point to hit
     */
    private ArrayList<Point> huntPts(Point p) {
        // Start with 4 possible progressions
        ArrayList<Point> candidates = new ArrayList<>();
        candidates.add(new Point(p.x, p.y - 1));
        candidates.add(new Point(p.x, p.y + 1));
        candidates.add(new Point(p.x - 1, p.y));
        candidates.add(new Point(p.x + 1, p.y));
        ArrayList<Point> targets = new ArrayList<>();

        // Remove invalid points
        for (Point pt : candidates) {
            if (this.isValidPoint(pt)) {
                targets.add(pt);
            }
        }


        return targets;
    }
}
