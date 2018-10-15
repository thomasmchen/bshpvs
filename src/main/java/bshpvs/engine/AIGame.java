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

    public static GameStat playMatch(Player alpha, Player beta) {
        long startTime = System.nanoTime();

        randomPromptShips(alpha);
        randomPromptShips(beta);

        Player current = alpha;

        while (!alpha.isDefeated() && !beta.isDefeated()) {
            if (current.equals(alpha)) {
                alpha.move(beta);
                current = beta;
            } else {
                beta.move(alpha);
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

    public static Statistics simulateAIMatch(Player alpha, Player beta, int numMatches) {
        ArrayList<GameStat> gameStats = new ArrayList<>();

        for (int i = 0; i < numMatches; i++) {
            GameStat round = playMatch(alpha, beta);
            gameStats.add(round);
        }

        return new Statistics(gameStats);
    }

    public static void main(String[] args) {

        long start = System.nanoTime();
        Statistics stats = simulateAIMatch(new NaivePlayer(), new HunterPlayer(), 10000);
        long stop = System.nanoTime();
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
