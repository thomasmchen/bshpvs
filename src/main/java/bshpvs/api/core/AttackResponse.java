package bshpvs.api.core;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AttackResponse
{
    @JsonProperty public String yourMove;
    @JsonProperty public String theirMove;
    @JsonProperty public String message;
    @JsonProperty public CoordinateWithInfo[] coors;

    public AttackResponse(@JsonProperty String yourMove, @JsonProperty String theirMove, @JsonProperty String message, @JsonProperty CoordinateWithInfo[] coors) {
        this.yourMove = yourMove;
        this.theirMove = theirMove;
        this.message = message;
        this.coors = coors;
    }

    @Override
    public String toString()
    {
        return "AttackResponse";
    }

    public static class CoordinateWithInfo {
        @JsonProperty public int x;
        @JsonProperty public int y;
        @JsonProperty public int playerPos;
        @JsonProperty public String info;
        public CoordinateWithInfo(@JsonProperty int x, @JsonProperty int y, @JsonProperty int playerPos, @JsonProperty String info) {
            this.x = x;
            this.y = y;
            this.playerPos = playerPos;
            this.info = info;
        }
    }
}