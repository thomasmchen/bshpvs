package bshpvs.api.core;

public class MoveRequest
{
    private String direction;

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


    @Override
    public String toString()
    {
        return "MoveRequest [direction = "+direction+", shipId = " + this.shipId;
    }
}