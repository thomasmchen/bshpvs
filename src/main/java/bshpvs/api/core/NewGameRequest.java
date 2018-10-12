package bshpvs.api.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class NewGameRequest
{
    private String userId;

    private String userName;

    private UserShip[] ships;

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
        return ships;
    }

    public void setShips (UserShip[] ships)
    {
        this.ships = ships;
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
        @JsonProperty("firstSpace") Point firstSpace;
        @JsonProperty("lastSpace") Point lastSpace;
        @JsonProperty("numSpaces") int numSpaces;
        @JsonProperty("identifier") int identifier;
        public UserShip(@JsonProperty("firstSpace") Point firstSpace, @JsonProperty("lastSpace") Point lastSpace, @JsonProperty("identifier") int identifier, @JsonProperty("numSpaces") int numSpaces) {
            this.firstSpace = firstSpace;
            this.lastSpace = lastSpace;
            this.identifier = identifier;
            this.numSpaces = numSpaces;
        }
        
    }

    public static class Point {
        @JsonProperty("x") public int x;
        @JsonProperty("y") public int y;
        public Point(@JsonProperty("x") int x, @JsonProperty("Y") int y) {
            this.x = x;
            this.y = y;

        }
    }
}