package bshpvs.ai;

import bshpvs.model.Cell;
import bshpvs.model.Player;

import java.awt.Point;
import java.util.*;

public class HunterPlayer extends Player implements Playable {
    private HashMap<Player, Stack<Point>> targets;
    private Player lastPlayer;
    private boolean lastMoveRes;

    public HunterPlayer() {
        super();
        this.targets = new HashMap<>();
        this.lastMoveRes = false;
    }

    public HunterPlayer(int size, Player opp) {
        super(size, opp);
        this.targets = new HashMap<>();
        this.targets.put(opp, new Stack<Point>());
        this.lastMoveRes = false;


    }

    public HunterPlayer(int size, List<Player> opps) {
        super(size, opps);
        this.lastMoveRes = false;
    }

    @Override
    public void addOpponent(Player opp) {
        super.addOpponent(opp);
        this.targets.put(opp, new Stack<Point>());
    }

    @Override
    public Point attack(Player opp) {

        Point target = null;
        // First attack or no more targets
        if (targets.get(opp).empty()) {
            target = genRandomTarget(opp);
            Cell c = this.hitOppCell(target, opp);

            // Update lastMoveRes
            if (c.isShip()) {
                lastMoveRes = true;
            }

            this.updateTargets(c, target, opp);
            
            return target;
        }  else {
            target = targets.get(opp).pop();
            Cell c = this.hitOppCell(target, opp);

            // Update lastMoveRes
            if (c.isShip()) {
                lastMoveRes = true;
            }

            this.updateTargets(c, target, opp);

            return target;
        }
    }

    @Override
    public void move() {
    }

    @Override
    public void turn() {
        if (!lastMoveRes || lastPlayer == null) {
            lastPlayer = genRandomOpp();
            attack(lastPlayer);
        } else {
            attack(lastPlayer);
        }
    }

    /**
     * Given a hit target cell will update candidates
     * @param c the cell that was hit
     */
    private void updateTargets(Cell c, Point p, Player opp) {
        if (c.isShip()) {
            ArrayList<Point> candidates = huntPts(p, opp);
            if (candidates.size() == 0) {
                return;
            }
            for (Point pt : candidates) {
                targets.get(opp).push(pt);
            }
        }
    }

    /**
     * Given a point that is a ship, returns all valid surrounding points
     * @param p
     * @return the next point to hit
     */
    private ArrayList<Point> huntPts(Point p, Player opp) {
        // Start with 4 possible progressions
        ArrayList<Point> candidates = new ArrayList<>();
        candidates.add(new Point(p.x, p.y - 1));
        candidates.add(new Point(p.x, p.y + 1));
        candidates.add(new Point(p.x - 1, p.y));
        candidates.add(new Point(p.x + 1, p.y));
        ArrayList<Point> targets = new ArrayList<>();

        // Remove invalid points
        for (Point pt : candidates) {
            if (this.isValidTgtPoint(pt, opp)) {
                targets.add(pt);
            }
        }

        return targets;
    }
}
