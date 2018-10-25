package bshpvs.ai;

import bshpvs.model.Player;

import java.awt.*;

public class NaivePlayer extends Player implements Playable {

    public NaivePlayer() {
        super();
    }

    public NaivePlayer(int size, Player opp) {
        super(size, opp);
    }

    /**
     * Implements playable interface with most naive algorithm:
     * Random selection of a valid attack
     * @return the Cell that was hit by the attack
     */
    @Override
    public Point attack(Player opp) {
        Point tgt = genRandomTarget(opp);
        this.hitOppCell(tgt, opp);
        return tgt;
    }

    @Override
    public void move() {
        attack(genRandomOpp());
    }

    @Override
    public void turn() {
        attack(genRandomOpp());
    }
}
