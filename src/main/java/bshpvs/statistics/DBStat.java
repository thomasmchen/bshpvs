package bshpvs.statistics;

public class DBStat {
    private int numPlayers;
    private double hitPerc;
    private double missPerc;
    private int numTurns;
    private long time;
    private String winner;
    private String playerTypes;

    /**
     * Special Stat Object for grouping data from the Database together
     * 
     * @param numPlayers
     * @param hitPerc
     * @param missPerc
     * @param numTurns
     * @param time
     * @param winner
     * @param playerTypes
     */
    public DBStat(int numPlayers, double hitPerc, double missPerc, int numTurns, long time, String winner, String playerTypes){
        this.numPlayers = numPlayers;
        this.hitPerc = hitPerc;
        this.missPerc = missPerc;
        this.numTurns = numTurns;
        this.time = time;
        this.winner = winner;
        this.playerTypes = playerTypes;
    } 

    public int getNumPlayer(){
        return this.numPlayers;
    }

    public double getHitPerc(){
        return this.hitPerc;
    }

    public double getMissPerc(){
        return this.missPerc;
    }

    public int getNumTurns(){
        return this.numTurns;
    }

    public long getTime(){
        return this.time;
    }

    public String getWinner(){
        return this.winner;
    }

    public String getPlayerTypes(){
        return this.playerTypes;
    }

    @Override
    public String toString(){
        return "# of players: " + this.numPlayers +
                "hit percentage: " + this.hitPerc +
                "miss percentage: " + this.missPerc +
                "# of turns: " + this.numTurns +
                "time: " + this.time + 
                "winner: " + this.winner +
                "Player Types: " + this.playerTypes;
    }

}