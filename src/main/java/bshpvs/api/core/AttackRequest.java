package bshpvs.api.core;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AttackRequest
{
    @JsonProperty("y") public int y;

    @JsonProperty("x") public int x;

    public AttackRequest(@JsonProperty("y") int y, @JsonProperty("x") int x) {
        this.y = y;
        this.x = x;
    }

    public int getY ()
    {
        return y;
    }

    public void setY (int y)
    {
        this.y = y;
    }

    public int getX ()
    {
        return x;
    }

    public void setX (int x)
    {
        this.x = x;
    }

    @Override
    public String toString()
    {
        return "MoveRequest [y = "+y+", x = "+x+"]";
    }
}