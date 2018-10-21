package bshpvs.ai;

import bshpvs.model.Cell;
import bshpvs.model.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

public class HunterPlayer extends Player implements Playable{
    private Stack<Point> targets;

    public HunterPlayer() {
        super();
        this.targets = new Stack<Point>();
    }

    public HunterPlayer(int size) {
        super(size);
        this.targets = new Stack<Point>();
    }

    public Point move(Player opp) {

        Point target = null;
        // First move or no more targets
        if (targets.empty()) {
            target = genRandomTarget(opp);
            Cell c = this.hitOppCell(target, opp);
            this.updateTargets(c, target);
            
            return target;
        }  else {
            target = targets.pop();
            Cell c = this.hitOppCell(target, opp);
            this.updateTargets(c, target);

            return target;
        }
    }

    /**
     * Given a hit target cell will update candidates
     * @param c the cell that was hit
     */
    private void updateTargets(Cell c, Point p) {
        if (c.isShip()) {
            ArrayList<Point> candidates = huntPts(p);
            if (candidates.size() == 0) {
                return;
            }
            for (Point pt : candidates) {
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
