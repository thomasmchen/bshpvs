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
import bshpvs.model.Player;
import bshpvs.ai.HunterPlayer;
import bshpvs.ai.NaivePlayer;
import bshpvs.model.Ship;

import org.jboss.logging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.awt.Point;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;


@CrossOrigin
@Controller
public class EngineController {


    public Player playerOne;
    public ArrayList<Player> opponents = new ArrayList<Player>();
    public Game game;

    NewGameRequest newGameRequest;


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
    @MessageMapping("/windowInit")
    @SendTo("/topic/windowInitResponse")
    public String gameInit() throws Exception {
        this.initializeGame();
        int userId = 0;
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
        int numOpponents = newGameRequest.getNumOpponents();
        NewGameResponse response = new NewGameResponse(userId, userName, victoryMessage, ships, numOpponents);
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
        System.out.println("Attack request " + req.playerPos + " x: " + req.x + "y: " + req.y);
        AttackResponse response = this.game.turn(p, req.playerPos);
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
    

    public void reset() {
        this.game = null;
        this.newGameRequest = null;
        this.playerOne = null;
        this.opponents = new ArrayList<Player>();
    }

    public void initializeUserPlayer() {
        this.playerOne = new Player(10, this.opponents);
        for (int i = 0; i < newGameRequest.getShips().length; i++) {
            this.playerOne.addShip(newGameRequest.getShips()[i]);
        }

        // Tell all the bots that the user is an enemy to them
        for (int i = 0; i < this.opponents.size(); i++) {
            Player p = this.opponents.get(i);
            p.addOpponent(this.playerOne);
        }
    }

    public void initializeEnemies() {
        // Check which AI is selected, then generate the appropriate amount of bots
        if (this.newGameRequest.getSelectedAI().equalsIgnoreCase("normal")) {
            for (int i = 0; i < this.newGameRequest.getNumOpponents(); i++) {
                Player p = new NaivePlayer();
                opponents.add(p);
            }
        } else if (this.newGameRequest.getSelectedAI().equalsIgnoreCase("hunter")) {
            for (int i = 0; i < this.newGameRequest.getNumOpponents(); i++) {
                Player p = new HunterPlayer();
                opponents.add(p);
            }
        } else {
            for (int i = 0; i < this.newGameRequest.getNumOpponents(); i++) {
                Player p = new NaivePlayer();
                opponents.add(p);
            }
        }

        // Tell all the bots that they are enemies of each other
        for (int i = 0; i < this.opponents.size() - 1; i++) {
            Player p = this.opponents.get(i);
            for (int j = i + 1; j < this.opponents.size(); j++) {
                Player e = this.opponents.get(j);
                p.addOpponent(e);
                e.addOpponent(p);
            }
        }
    }

    // initialize the battleship game model
    public void initializeGame() {
        this.initializeEnemies();
        this.initializeUserPlayer();
        Player[] players = new Player[this.opponents.size()];
        for (int i = 0; i < players.length; i++) {
            players[i] = this.opponents.get(i);
        }
        game = new Game(this.playerOne, players);
   }
    
}
