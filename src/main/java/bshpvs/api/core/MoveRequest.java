package bshpvs.api.core;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MoveRequest
{
    @JsonProperty("direction")
    private String direction;

    @JsonProperty("shipId")
    private int shipId;

    public String getDirection ()
    {
        return direction;
    }

    public void setDirection (String direction)
    {
        this.direction = direction;
    }

    public void setShipId(int shipId) {
        this.shipId = shipId;
    }

    
    public int getShipId() {
        return shipId;
    }


    @Override
    public String toString()
    {
        return "MoveRequest [direction = "+direction+", shipId = " + this.shipId;
    }
}