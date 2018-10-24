package bshpvs.api.stub;

import bshpvs.api.core.AttackRequest;
import bshpvs.api.core.AttackResponse;
import bshpvs.api.core.NewGameRequest;
import bshpvs.engine.Game;
import bshpvs.api.core.NewGameResponse;
import bshpvs.api.core.MoveResponse;
import bshpvs.api.core.MoveRequest;
import bshpvs.api.core.EndGameResponse;
import bshpvs.api.core.NewGameRequest.UserShip;
import bshpvs.api.core.NewGameRequest._Point;
import bshpvs.api.core.NewGameResponse.Coordinate;
import bshpvs.api.core.NewGameResponse.ShipObject;
import bshpvs.database.Database;
import bshpvs.model.Player;
import bshpvs.ai.HunterPlayer;
import bshpvs.ai.NaivePlayer;
import bshpvs.model.Ship;
import bshpvs.api.core.ReceiveUserID;
import bshpvs.api.core.StatsResponse;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.awt.Point;
import java.sql.SQLException;

import com.fasterxml.jackson.databind.ObjectMapper;


@CrossOrigin
@Controller
public class EngineController {


    public Player playerOne;
    public Player playerTwo;
    public Game game;

    NewGameRequest newGameRequest;
    ReceiveUserID receiveUserID;

    @CrossOrigin
    @MessageMapping("/placeShips")
    @SendTo("/topic/confirmPlacement")
    public NewGameRequest newGame(String json) throws Exception {
        this.reset();
        ObjectMapper objectMapper = new ObjectMapper();
        NewGameRequest req = objectMapper.readValue(json, NewGameRequest.class);
        req.convertShips();
        System.out.println(req.toString());
        newGameRequest = req;
        return req;
    }

    @CrossOrigin
    @MessageMapping("/id")
    @SendTo("/topic/getID")
    public ReceiveUserID id(String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        receiveUserID = mapper.readValue(json, ReceiveUserID.class);

        Database db = new Database();
        //check H2
        if(!db.checkH2Exists()){
            System.out.println("H2 DB does not exist, creating...");
            db.createH2Database();
        }
        else{
            System.out.println("H2 DB already exists, continuing...");
        }

        //check User
        if(!db.doesUserExist(receiveUserID.getID())){
            System.out.println("User does not exist in DB, creating new user...");
            db.addUser(receiveUserID.getID());
        }
        else{
            System.out.println("User already exists, continuing...");
        }
        //System.out.println(receiveUserID.getID());
        return receiveUserID;
    }

    @CrossOrigin
    @MessageMapping("/windowInit")
    @SendTo("/topic/windowInitResponse")
    public String gameInit() throws Exception {
        this.initializeGame();
        String userId = newGameRequest.getUserId();
        String userName = newGameRequest.getUserName();
        String victoryMessage = newGameRequest.getVictoryMessage();
        ShipObject[] ships = new ShipObject[newGameRequest.getShips().length];


        for (int i = 0; i < newGameRequest.getShips().length; i++) {
            Ship s = newGameRequest.getShips()[i];
            System.out.println("st.x: " + s.st.x + " st.y " + s.st.y);
            Point[] points = s.getPoints();
            Coordinate[] pts = new Coordinate[points.length];
            for (int j = 0; j < points.length; j++) {
                pts[j] = new Coordinate(points[j].x, points[j].y);
            }
        int shipId = 0;
        switch (s.getType()) {
            case CARRIER:
            shipId = 0;
            break;
            case BATTLESHIP:
            shipId = 1;
            break;
            case CRUISER:
            shipId = 2;
            break;
            case SUBMARINE:
            shipId = 3;
            break;
            case DESTROYER:
            shipId = 4;
            break;
        }
        ShipObject obj = new ShipObject(shipId, pts.length, pts);
            ships[i] = obj;

        }
        ObjectMapper mapper = new ObjectMapper();

        NewGameResponse response = new NewGameResponse(userId, userName, victoryMessage, ships);
        String json = mapper.writeValueAsString(response);
        return json;
    }  

    @CrossOrigin
    @MessageMapping("/attackTurn")
    @SendTo("/topic/turnResponse")
    public AttackResponse turn(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        AttackRequest req = objectMapper.readValue(json, AttackRequest.class);
        Point p = new Point(req.x, req.y);
        AttackResponse response = this.game.turn(p);
        return response;
    }

    @CrossOrigin
    @MessageMapping("/moveTurn")
    @SendTo("/topic/moveResponse")
    public MoveResponse moveTurn(String json) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        MoveRequest req = objectMapper.readValue(json, MoveRequest.class);
        int shipId = req.getShipId();
        String direction = req.getDirection();
        MoveResponse response = this.game.moveTurn(shipId, direction);
        return response;
    }
    

    @CrossOrigin
    @MessageMapping("/checkWin")
    @SendTo("/topic/endGame")
    public EndGameResponse endGame() throws Exception {
        EndGameResponse egr = null;
        System.out.println("Here");
        if (this.game.secondPlayer.isDefeated()) {
            String message = "Congrats " + newGameRequest.getUserName() + " you won!";
            egr = new EndGameResponse(message, this.newGameRequest.getVictoryMessage());
            
            //add game data to database for that users game data
            Database db = new Database();
            db.addGameData(newGameRequest.getUserId(), this.game.gameStat, this.playerOne.getPlayerStat()); 
        } else if (this.game.firstPlayer.isDefeated()) {
            String message = newGameRequest.getUserName() + " lost";
            egr = new EndGameResponse(message, "Good fight!");
            
            //add game data to database for that users game data
            Database db = new Database();
            db.addGameData(newGameRequest.getUserId(), this.game.gameStat, this.playerOne.getPlayerStat()); 
        }
        
        return egr;

    }

    @CrossOrigin
    @MessageMapping("/stats")
    @SendTo("/topic/getStats")
    public StatsResponse getGameStats() {
        System.out.println("Attempting to retreive GameStats...");
        StatsResponse statsRes = null;
        Database db = new Database();

        if(this.receiveUserID == null){
            System.out.println("recieveUserID object was null");
            return null;
        }

        try{
            statsRes = new StatsResponse(db.pullGameData(this.receiveUserID.getID()));
        }catch(SQLException e){
            System.out.println("ERROR could not retreive GameStats, Message: " + e.getLocalizedMessage());
            return null;
        }
        System.out.println("GameStats Retreived.");

        return statsRes;
    }
    

    public void reset() {
        this.game = null;
        this.newGameRequest = null;
        this.playerOne = null;
        this.playerTwo = null;
    }

    public void initializeUserPlayer() {
        this.playerOne = new Player(10, playerTwo);
        for (int i = 0; i < newGameRequest.getShips().length; i++) {
            this.playerOne.addShip(newGameRequest.getShips()[i]);
        }
    }

    public void initializeEnemy() {
        if (this.newGameRequest.getSelectedAI().equalsIgnoreCase("normal")) {
            System.out.println("Normal ai");
            playerTwo = new NaivePlayer();
        } else if (this.newGameRequest.getSelectedAI().equalsIgnoreCase("hunter")) {
            System.out.println("Hunter ai");
            playerTwo = new HunterPlayer();
        } else {
            playerTwo = new NaivePlayer();
        }
    }

    public void setOpponents() {
        this.playerOne.addOpponent(this.playerTwo);
        this.playerTwo.addOpponent(this.playerOne);
    }

    public void initializeGame() {
        this.initializeUserPlayer();
        this.initializeEnemy();
        this.setOpponents();
        game = new Game(this.playerOne, this.playerTwo);
      


   }

    public AttackResponse turn(Point p) {
        return this.game.turn(p);
    }
    
}
