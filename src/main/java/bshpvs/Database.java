package bshpvs;

import java.sql.*;
import bshpvs.statistics.GameStat;

public class Database {

    private Connection con;

    public Database() throws SQLException {
        con = DriverManager.getConnection("jdbc:h2:~/test", "sa", ""); //may need to move this somewhere else so it can properly be closed
    }

    /**
     * Adds stats from GameStat into the database based on the user's login ID
     * from logging in with Google
     * 
     * @param GoogleID 
     * @param gs GameStat object
     * @return true on success, false upon failure
     */
    private boolean addGameData(String GoogleID, GameStat gs){
        return true;
    }

}