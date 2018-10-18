package bshpvs.api.core;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ReceiveUserID
{
    @JsonProperty("id") public String id;

    public ReceiveUserID(@JsonProperty("id") String id) {
        this.id = id;
    }

    public String getID ()
    {
        return id;
    }

    public void setY (String id)
    {
        this.id = id;
    }
    
    @Override
    public String toString()
    {
        return "SendID [userID = "+id+"]";
    }
}