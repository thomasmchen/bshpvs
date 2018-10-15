package bshpvs.statistics;

import java.util.ArrayList;

public class Statistics {

    private ArrayList<GameStat> stats;
    private double avgTurns;
    private int numPlayers;
    private int numGames;
    private long avgTime;

    public int getNumGames() {
        return numGames;
    }

    public ArrayList<GameStat> getStats() {
        return stats;
    }

    public double getAvgTurns() {
        return avgTurns;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public long getAvgTime() {
        return avgTime;
    }

    public void printPlayerStats(String player) {
        double avgHitPerc = 0;
        double avgMissPerc = 0;
        int wins = 0;
        int losses = 0;
        double winlossRatio = 0;

        ArrayList<PlayerStat> playerStats = null;
        for (GameStat gs : this.getStats()) {
            playerStats = gs.getPlayerTypeStat(player);

            if (playerStats == null) {
                throw new IllegalArgumentException("Player does not exist in Game Statistics.");
            }

            for (PlayerStat ps : playerStats) {
                avgHitPerc += ps.getHitPerc();
                avgMissPerc += ps.getMissPerc();
                if (gs.getWinner().equals(ps)) {
                    wins++;
                } else {
                    losses++;
                }
            }
        }


        avgHitPerc = avgHitPerc/stats.size();
        avgMissPerc = avgMissPerc/stats.size();
        winlossRatio = wins / (double) (wins + losses);

        System.out.println("Player: " + player +
                "\nAverage Hit Percentage: " + avgHitPerc +
                "\nAverage Miss Percentage: " + avgMissPerc +
                "\nWin/Loss Ratio: " + winlossRatio);
    }

    public Statistics(ArrayList<GameStat> games) {
        if (games.size() == 0) {
            this.stats = new ArrayList<>();
            return;
        }

        int first = games.get(0).getNumPlayers();
        for (GameStat gs : games) {
            if (gs.getNumPlayers() != first) {
                throw new IllegalArgumentException("GameStats have inconsistent number of players.");
            }
        }

        this.stats = games;
        this.numPlayers = first;
        this.numGames = this.getStats().size();
        this.updateStats();
    }

    public void addGameStat(GameStat gs) {
        if (stats.size() != 0 && gs.getNumPlayers() != this.numPlayers) {
            throw new IllegalArgumentException("GameStats have inconsistent number of players.");
        }

        this.stats.add(gs);
        this.updateStats();
    }

    private void updateStats() {
        int totalTurns = 0;
        long totalTime = 0;

        for (GameStat gs : stats) {
            totalTurns += gs.getTotalTurns();
            totalTime += gs.getTime();
        }

        this.avgTime = totalTime / (long) stats.size();
        this.avgTurns = totalTurns / (double) stats.size();
    }

    @Override
    public String toString() {
        return "{" +
                "\n\tnumPlayers: " + numPlayers +
                "\n\tnumGames: " + numGames +
                ",\n\tavgTurns: " + avgTurns +
                ",\n\tavgGameTime: " + (double) avgTime/1000000000.0 + " seconds" +
                "\n}";
    }
}
