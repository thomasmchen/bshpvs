package bshpvs.api.stub;

import bshpvs.api.core.AttackRequest;
import bshpvs.api.core.AttackResponse;
import bshpvs.api.core.NewGameRequest;
import bshpvs.api.core.NewGameResponse;
import bshpvs.api.core.NewGameRequest.UserShip;
import bshpvs.api.core.NewGameRequest._Point;
import bshpvs.api.core.NewGameResponse.Coordinate;
import bshpvs.api.core.NewGameResponse.ShipObject;
import bshpvs.model.Game;
import bshpvs.model.Player;
import bshpvs.model.Ship;

import org.jboss.logging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.awt.Point;

import com.fasterxml.jackson.databind.ObjectMapper;


@CrossOrigin
@Controller
public class EngineController {


    public Player playerOne;
    public Player playerTwo;
    public Game game;

    NewGameRequest newGameRequest;


    @CrossOrigin
    @MessageMapping("/placeShips")
    @SendTo("/topic/confirmPlacement")
    public NewGameRequest newGame(String json) throws Exception {
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
        this.initializePlayers();
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

            ShipObject obj = new ShipObject(s.getType().getValue(), pts.length, pts);
            ships[i] = obj;

        }
        ObjectMapper mapper = new ObjectMapper();

        NewGameResponse response = new NewGameResponse(userId, userName, victoryMessage, ships);
        String json = mapper.writeValueAsString(response);
        return json;
    }  

    @CrossOrigin
    @MessageMapping("/turn")
    @SendTo("/topic/turnResponse")
    public AttackResponse turn(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        AttackRequest req = objectMapper.readValue(json, AttackRequest.class);
        Point p = new Point(req.x, req.y);
        return this.turn(p);
    }



    public void initializePlayers() {
        this.playerOne = new Player(10);
        this.playerTwo = new Player(10);
    }

    public void initializeGame() {
        game = new Game(this.playerOne, this.playerTwo, 10);
        game.initAi(1);

        for (int i = 0; i < newGameRequest.getShips().length; i++) {
            this.playerOne.addShip(newGameRequest.getShips()[i]);
        }
    }

    public AttackResponse turn(Point p) {
        return this.game.turn(p);
    }
}
