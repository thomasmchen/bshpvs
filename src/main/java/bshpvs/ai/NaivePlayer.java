package bshpvs.ai;

import bshpvs.model.Player;

import java.awt.*;

public class NaivePlayer extends Player implements Playable {

    public NaivePlayer() {
        super();
    }

    public NaivePlayer(int size) {
        super(size);
    }

    /**
     * Implements playable interface with most naive algorithm:
     * Random selection of a valid move
     * @return the Cell that was hit by the move
     */
    public Point move(Player opp) {
        Point tgt = genRandomTarget(opp);
        this.hitOppCell(tgt, opp);
        return tgt;
    }
}
