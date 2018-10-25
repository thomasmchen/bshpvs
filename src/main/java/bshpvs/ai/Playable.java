package bshpvs.ai;

import bshpvs.model.Player;

import java.awt.*;

public interface Playable {
    public Point attack(Player pl);

    public void move();

    public void turn();
}
