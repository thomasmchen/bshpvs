package bshpvs.api.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

import bshpvs.statistics.DBStat;;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatsResponse {
    @JsonProperty("statsList") public DBStat[] statsList;

    public StatsResponse(@JsonProperty("statsList") DBStat[] statsList){
        this.statsList = statsList;
    }

    public DBStat[] getStatsList(){
        return this.statsList;
    }

    public void setStatsList(DBStat[] statsList){
        this.statsList = statsList;
    }
}