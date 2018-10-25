package bshpvs.api.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatsRequest {
    @JsonProperty("UserID") public String UserID;

    public StatsRequest(@JsonProperty("UserID") String UserID){
        this.UserID = UserID;
    }

    public String getID(){
        return this.UserID;
    }

    @Override
    public String toString(){
        return "StatsRequest [UserID = " + this.UserID + "]";
    }

}