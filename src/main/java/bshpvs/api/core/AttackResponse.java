package bshpvs.api.core;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AttackResponse
{
    @JsonProperty public String yourMove;
    @JsonProperty public String theirMove;
    @JsonProperty public int y;
    @JsonProperty public int x;
    public AttackResponse(@JsonProperty  int y, @JsonProperty  int x, @JsonProperty String yourMove, @JsonProperty String theirMove) {
        this.y = y;
        this.x = x;
        this.yourMove = yourMove;
        this.theirMove = theirMove;
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