package bshpvs.model;

public class MoveRequest
{
    private String direction;

    private String y;

    private String x;

    private String userID;

    public void setUserID(String userID) {
      this.userID = userID;
    }

    public String getUserID() {
      return userID;
    }

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
        return "MoveRequest [userID = "+userID+", direction = "+direction+", y = "+y+", x = "+x+"]";
    }
}
