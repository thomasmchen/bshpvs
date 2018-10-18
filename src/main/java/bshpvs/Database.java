package bshpvs;

import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.crypto.Data;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.h2.result.ResultExternal;

import bshpvs.model.Player;
import bshpvs.statistics.GameStat;
import bshpvs.statistics.PlayerStat;


public class Database {

    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:~/test";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";


    /**
     * Adds a user to the database using their GoogleID as their ID
     * 
     * @param GoogleID based on Google Sign-In ID
     * @return true on success, false otherwise
     * 
     * @throws SQLException part of establising connection with DB
     */
    private boolean addUser(String GoogleID, String email) throws SQLException{
        //establish connection
        Connection connection = getDBConnection();

        //create prepared statement object for inserting a user
        PreparedStatement insertUserStatement = null;

        //SQL query for adding a user
        String UserQuery = "INSERT INTO USERS" +
                            "(id, email)" +
                            "values(?,?)";

        try{
            connection.setAutoCommit(false);
            insertUserStatement = connection.prepareStatement(UserQuery);
            insertUserStatement.setString(1, GoogleID);                 //UserID
            insertUserStatement.setString(2, email);                    //User_Email

            insertUserStatement.executeUpdate();
            insertUserStatement.close();

            //commit addition to DB
            connection.commit();

        } catch(SQLException e){
            System.out.println("User Exception Message: " + e.getLocalizedMessage());
            return false;
        }

        return true;
    }


    /**
     * Adds stats from GameStat into the database based on the user's login ID
     * from logging in with Google
     * 
     * @param GoogleID 
     * @param gs GameStat object
     * @param ps PlayerStat object for User
     * @return true on success, false upon failure
     */
    private boolean addGameData(String GoogleID, GameStat gs, PlayerStat ps) throws SQLException{
        Connection connection = getDBConnection(); //connect with H2 DB
        
        PreparedStatement insertPreparedStatement = null;

        String InsertQuery = "INSERT INTO GAMEDATA" + 
                             "(id, user_id, num_players, num_hits, num_misses, total_turns, time, winner, player_types) " + 
                             "values(?,?,?,?,?,?,?,?,?)";

        //get number of games in GameStats Table, use it to determine a unique ID for GameStat being added
        int count = getRowCount(connection);

        try{
            connection.setAutoCommit(false);
            insertPreparedStatement = connection.prepareStatement(InsertQuery);         
            insertPreparedStatement.setInt(1, count+1);                                 //ID
            insertPreparedStatement.setString(2, GoogleID);                             //User_ID [VITAL]
            insertPreparedStatement.setInt(3, gs.getNumPlayers());                      //Num_Players
            insertPreparedStatement.setInt(4, ps.getHits());                            //Num_Hits
            insertPreparedStatement.setInt(5, ps.getMisses());                          //Num_Misses
            insertPreparedStatement.setInt(6, gs.getTotalTurns());                      //Total_Turns
            insertPreparedStatement.setLong(7, gs.getTime());                           //Time
            insertPreparedStatement.setString(8, gs.getWinner().getId().toString());    //Winner
            insertPreparedStatement.setString(9, parsePlayerTypes(gs));                 //Player_Types
        }catch(SQLException e){
            System.out.println("Execption Occurred when adding GameStats, Message: " + e.getLocalizedMessage());
            return false;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

        //close connection
        connection.close();
        return true;
    }

    /**
     * Parses ArrayList of PlayerTypes into a comma-separated list.
     * 
     * "playertype1,playertype2,playertype3,..."
     * 
     * Makes it easier to add to GameStat Table
     * 
     * @param gs GameStat object of interest
     * @return comma-separated list in a single string
     */
    private String parsePlayerTypes(GameStat gs){
        ArrayList<PlayerStat> list = null;
        try{
            list = gs.getPlayerStats();
        } catch(NullPointerException npe){
            System.out.println("No Game Stat was given");
            return null;
        }

        String parsedList = "";
        for(int i = 0; i < list.size(); i++){
            parsedList += list.get(i).getPlayerType();
            if(i + 1 != list.size()){ //i.e. if not the last player in the list
                parsedList += ",";
            }
        }

        return parsedList;
    }

    /**
     * Established connection with H2 Database
     * 
     * MAKE SURE NOT TO BE RUNNING H2 CONSOLE AT THE SAME TIME:
     * 
     * Run the command "SHUTDOWN" in the console before disconnecting,
     * it will say that it has done so already but for some reason running
     * this method will still throw an exception complaining about other
     * connections
     * 
     * @return Connection object representing connection to DB
     */
    private Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbConnection;
    }

    /**
     * Function to get number of rows in GameStats table, which
     * will be used to determine a Unique ID for any GameStat object
     * added to the table 
     * 
     * @param c Connection to H2 DB
     * @return row count of GameStat Table
     */
    private int getRowCount(Connection c){
        Statement s = null;
        int count = 0;
        try {
            s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT COUNT(*)" + " AS rowcount " + "FROM GAMESTATS");
            rs.next();
            count = rs.getInt("rowcount");
            rs.close();
        }catch (SQLException e){
            System.out.println("Issue with getRowCount: " + e.getLocalizedMessage());
        }
        return count;
    }


    /**
     * Function for testing ability to write to the database, will most likely separate into its own test
     * 
     * Change ID field inputs to test multiple users
     * 
     * @return true upon success, false otherwise
     * @throws SQLException part of establishing connection with DB
     */
    private boolean addFakeData() throws SQLException{
        Connection connection = getDBConnection(); //connect with H2 DB
        
        PreparedStatement insertPreparedStatement = null;
        PreparedStatement insertUserStatement = null;

        String InsertQuery = "INSERT INTO GAMESTATS" + 
                             "(id, user_id, num_players, num_hits, num_misses, total_turns, time, winner, player_types) " + 
                             "values(?,?,?,?,?,?,?,?,?)";
        String FakeUserQuery = "INSERT INTO USERS" +
                                "(id, email)" +
                                "values(?,?)";

        try{
            connection.setAutoCommit(false);
            insertUserStatement = connection.prepareStatement(FakeUserQuery);
            insertUserStatement.setString(1, "steve");
            insertUserStatement.setString(2, "fake2@email.com");

            insertUserStatement.executeUpdate();
            insertUserStatement.close();

            connection.commit();
        }catch(SQLException e){
            System.out.println("User Exception Message: " + e.getLocalizedMessage());
        }
        try{
            //get number of rows to give unique ID to GameStats
            int count1 = getRowCount(connection);

            System.out.println("1st count from db: " + count1);

            connection.setAutoCommit(false);
            insertPreparedStatement = connection.prepareStatement(InsertQuery);         
            insertPreparedStatement.setInt(1, count1+1);                                       //id
            insertPreparedStatement.setString(2, "steve");                                //user_id
            insertPreparedStatement.setInt(3, 3);                                       //num_players
            insertPreparedStatement.setInt(4, 14);                                      //num_hits
            insertPreparedStatement.setInt(5, 52);                                      //num_misses
            insertPreparedStatement.setInt(6, 70);                                      //total_turns
            insertPreparedStatement.setLong(7, 10000);                                  //time
            insertPreparedStatement.setString(8, "steve");                       //winner
            insertPreparedStatement.setString(9, "playerType1,playerType2,playerType3");           //playertypes

            insertPreparedStatement.executeUpdate();
            insertPreparedStatement.close();

            connection.commit();

            //get number of rows to give unique ID to GameStats
            int count2 = getRowCount(connection);

            System.out.println("2nd count from db: " + count2);

        }catch(SQLException e){
            System.out.println("Game Stat Execption Message " + e.getLocalizedMessage());
        }catch(Exception e){
            e.printStackTrace();
        }
        
        Statement s = null;
        try{
            connection.setAutoCommit(false);
            s = connection.createStatement();

            ResultSet rs = s.executeQuery("SELECT * FROM USERS");
            System.out.println("H2 Database Users");
            while(rs.next()){
                System.out.println("ID: " + rs.getString("ID") + " email: " + rs.getString("EMAIL"));
            }
            s.close();
            connection.commit();

        }catch(SQLException e){
            System.out.println("Print error Exception Message " + e.getLocalizedMessage());
        }
        
        return true;
    }
/*
    
    public static void main(String[] args){
        Database db = new Database();
        try{
            db.addFakeData();
        }catch (SQLException e){
            System.out.println("OOF Exception Message: " + e.getLocalizedMessage());
        }

        System.out.println("Done.");
    }
    */
}
