package bshpvs.api.core;

public class MoveRequest
{
    private String direction;

    private String y;

    private String x;

    public String getDirection ()
    {
        return direction;
    }

    public void setDirection (String direction)
    {
        this.direction = direction;
    }

    public String getY ()
    {
        return y;
    }

    public void setY (String y)
    {
        this.y = y;
    }

    public String getX ()
    {
        return x;
    }

    public void setX (String x)
    {
        this.x = x;
    }

    @Override
    public String toString()
    {
        return "MoveRequest [direction = "+direction+", y = "+y+", x = "+x+"]";
    }
}