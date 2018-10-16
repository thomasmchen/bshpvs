package bshpvs.statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class GameStat {
    private ArrayList<PlayerStat> playerStats = new ArrayList<>();
    private PlayerStat winner;
    private int totalTurns;
    private int numPlayers;
    private long time;

    public GameStat(ArrayList<PlayerStat> ps, long time, PlayerStat winner) {
        this.playerStats = ps;
        this.numPlayers = ps.size();
        this.time = time;
        this.winner = winner;
        Collections.sort(this.playerStats);

        this.totalTurns = winner.getTurns();
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public PlayerStat getWinner() {
        return winner;
    }

    public int getTotalTurns() {
        return totalTurns;
    }

    public long getTime() {
        return time;
    }

    public ArrayList<PlayerStat> getPlayerStats() {
        return playerStats;
    }

    public PlayerStat getPlayerStat(UUID playerID) {
        for (PlayerStat ps : playerStats) {
            if (ps.getId().equals(playerID)) {
                return ps;
            }
        }
        return null;
    }

    public ArrayList<PlayerStat> getPlayerTypeStat(String playerType) {
        ArrayList<PlayerStat> stats = new ArrayList<>();
        for (PlayerStat ps : playerStats) {
            if (ps.getPlayerType().equals(playerType)) {
                stats.add(ps);
            }
        }
        return stats;
    }

    public void printPlayerRanks() {
        int i = 0;
        System.out.println("Winner: " + this.getWinner().getPlayerType());
        for (PlayerStat ps : playerStats) {
            System.out.println("Rank " + i + ": " + ps.toString());
            i++;
        }
    }






}
