package bshpvs.api.core;

import java.awt.geom.Point2D;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.awt.Point;


import bshpvs.model.Ship;
import bshpvs.model.CellType;

public class NewGameRequest
{
    private String userId;

    private String userName;

    @JsonProperty("ships") UserShip[] _ships;
    private Ship[] ships;

    private String victoryMessage;

    public String getUserId ()
    {
        return userId;
    }

    public void setUserId (String userId)
    {
        this.userId = userId;
    }

    public String getUserName ()
    {
        return userName;
    }

    public void setUserName (String userName)
    {
        this.userName = userName;
    }

    public UserShip[] getShips ()
    {
        return _ships;
    }

    public void setShips (UserShip[] ships)
    {
        this._ships = ships;
    }

    public void convertShips () {
        ships = new Ship[this._ships.length];
        for (int i = 0; i < this._ships.length; i++) {
            UserShip currShip = this._ships[i];
             Point p1 = new Point(currShip.firstSpace.x, currShip.firstSpace.y);
             Point p2 = new Point(currShip.lastSpace.x, currShip.lastSpace.y);
             int identifier = currShip.identifier;
             CellType type;
             switch(identifier) {
                case 0:
                    type = CellType.CARRIER;
                    break;
                case 1:
                    type = CellType.BATTLESHIP;
                    break;
                case 2:
                    type = CellType.CRUISER;
                    break;
                case 3:
                    type = CellType.SUBMARINE;
                    break;
                case 4:
                    type = CellType.DESTROYER;
                    break;
                default:
                    type = CellType.CARRIER;

             }

             Ship s = new Ship(p1, p2, type);
             ships[i] = s;
        }
    }

    public String getVictoryMessage ()
    {
        return victoryMessage;
    }

    public void setVictoryMessage (String victoryMessage)
    {
        this.victoryMessage = victoryMessage;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [userId = "+userId+", userName = "+userName+", ships = "+ships+", victoryMessage = "+victoryMessage+"]";
    }

    public static class UserShip {
        @JsonProperty("firstSpace") _Point firstSpace;
        @JsonProperty("lastSpace") _Point lastSpace;
        @JsonProperty("numSpaces") int numSpaces;
        @JsonProperty("identifier") int identifier;
        public UserShip(@JsonProperty("firstSpace") _Point firstSpace, @JsonProperty("lastSpace") _Point lastSpace, @JsonProperty("identifier") int identifier, @JsonProperty("numSpaces") int numSpaces) {
            this.firstSpace = firstSpace;
            this.lastSpace = lastSpace;
            this.identifier = identifier;
            this.numSpaces = numSpaces;
        }
        
    }

    public static class _Point {
        @JsonProperty("x") public int x;
        @JsonProperty("y") public int y;
        public _Point(@JsonProperty("x") int x, @JsonProperty("Y") int y) {
            this.x = x;
            this.y = y;

        }
    }
}