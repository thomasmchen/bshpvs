package bshpvs.statistics;

import bshpvs.model.Player;

import java.util.Objects;
import java.util.UUID;

public class PlayerStat implements Comparable<PlayerStat> {
    private UUID id;
    private String playerType;
    private int turns;
    private int hits;
    private int misses;

    public UUID getId() {
        return id;
    }

    public String getPlayerType() {
        return playerType;
    }

    public double getHitPerc() {
        return hits/(double) turns;
    }

    public double getMissPerc() {
        return misses/(double) turns;
    }

    public int getTurns() {
        return turns;
    }

    public void incrementTurns() {
        this.turns++;
    }

    public int getHits() {
        return hits;
    }

    public void incrementHits() {
        this.hits++;
    }

    public int getMisses() {
        return misses;
    }

    public void incrementMisses() {
        this.misses++;
    }

    public PlayerStat(Player pl) {
        this.id = pl.getID();
        this.turns = 0;
        this.hits = 0;
        this.misses = 0;
        this.playerType = pl.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return "PlayerStat{" +
                "id=" + id +
                ", playerType='" + playerType + '\'' +
                ", turns=" + turns +
                ", hits=" + hits +
                ", misses=" + misses +
                ", hitPerc=" + this.getHitPerc() +
                ", missPerc=" + this.getMissPerc() +
                '}';
    }

    @Override
    public int compareTo(PlayerStat o) {
        return this.turns - o.getTurns();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerStat that = (PlayerStat) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(playerType, that.playerType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, playerType);
    }
}
