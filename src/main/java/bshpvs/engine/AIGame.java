package bshpvs.engine;

import bshpvs.ai.HunterPlayer;
import bshpvs.ai.NaivePlayer;
import bshpvs.model.Player;
import bshpvs.statistics.GameStat;
import bshpvs.statistics.PlayerStat;
import bshpvs.statistics.Statistics;

import java.util.ArrayList;

import static bshpvs.engine.Game.randomPromptShips;

public class AIGame {

    public static GameStat playMultiMatch(ArrayList<Player> players) {
        long startTime = System.nanoTime();

        // Have very player add every other player as an opponent
        for (Player currentPl : players) {
            for (Player pl : players) {
                if (!currentPl.equals(pl)) {
                    currentPl.addOpponent(pl);
                }
            }
        }

        // Have every player setup ships
        for (Player pl : players) {
            randomPromptShips(pl);
        }

        // Calculate survival count
        int survivCount = 0;
        for (Player pl : players) {
            if (!pl.isDefeated()) {
                survivCount++;
            }
        }

        int current = 0;
        int turn = 0;
        while (survivCount > 1) {
            players.get(current).turn();

            // Update survivCount
            survivCount = 0;
            for (Player pl : players) {
                if (!pl.isDefeated()) {
                    survivCount++;
                }
            }

            // Update current Player
            if (current == players.size() - 1) {
                current = 0;
            } else {
                current++;
            }
        }

        long stopTime = System.nanoTime();
        long gameTime = stopTime - startTime;

        ArrayList<PlayerStat> stats = new ArrayList<>();

        PlayerStat winner = null;
        for (Player pl : players) {
            stats.add(pl.getPlayerStat());

            if (!pl.isDefeated()) {
                winner = pl.getPlayerStat();
            }
        }

        return new GameStat(stats, gameTime, winner);
    }

    public static GameStat playMatch(Player alpha, Player beta) {
        long startTime = System.nanoTime();

        alpha.addOpponent(beta);
        beta.addOpponent(alpha);

        randomPromptShips(alpha);
        randomPromptShips(beta);

        Player current = alpha;

        while (!alpha.isDefeated() && !beta.isDefeated()) {
            if (current.equals(alpha)) {
                alpha.turn();
                current = beta;
            } else {
                beta.turn();
                current = alpha;
            }
        }

        long stopTime = System.nanoTime();
        long gameTime = stopTime - startTime;

        ArrayList<PlayerStat> stats = new ArrayList<>();
        stats.add(alpha.getPlayerStat());
        stats.add(beta.getPlayerStat());

        PlayerStat winner = alpha.isDefeated() ? beta.getPlayerStat() : alpha.getPlayerStat();
        return new GameStat(stats, gameTime, winner);
    }

    public static void main(String[] args) {
//        ArrayList<GameStat> gameStats = new ArrayList<>();
//        long start = System.nanoTime();
//
//        for (int i = 0; i < 10000; i++) {
//
//            GameStat round = playMatch(new NaivePlayer(), new HunterPlayer());
//            gameStats.add(round);
//        }
//        long stop = System.nanoTime();
//
//        Statistics stats = new Statistics(gameStats);
//
//        long runTime = stop - start;

        ArrayList<GameStat> gameStats = new ArrayList<>();
        long start = System.nanoTime();

        for (int i = 0; i < 10000; i++) {
            ArrayList<Player> players = new ArrayList<>();
            players.add(new NaivePlayer());
            players.add(new HunterPlayer());

            GameStat round = playMultiMatch(players);
            gameStats.add(round);
        }
        long stop = System.nanoTime();

        Statistics stats = new Statistics(gameStats);

        long runTime = stop - start;

        System.out.println("\n================================= Epoch Statistics =================================");
        System.out.println("Total Run Time: " + (double) runTime / 1000000000.0 + " seconds");
        System.out.println(stats.toString());
        System.out.println("====================================================================================");
        stats.printPlayerStats("HunterPlayer");
        System.out.println("====================================================================================");
        stats.printPlayerStats("NaivePlayer");
        System.out.println("====================================================================================");
    }
}
