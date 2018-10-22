package bshpvs.api.core;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class EndGameResponse {
    @JsonProperty("won") public String won;
    @JsonProperty("victoryMessage") public String victoryMessage;

    public EndGameResponse(@JsonProperty("won") String won, @JsonProperty("victoryMessage") String victoryMessage) {
        this.won = won;
        this.victoryMessage = victoryMessage;
    }
 }