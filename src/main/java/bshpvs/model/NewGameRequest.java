package bshpvs.model;

public class NewGameRequest
{
    private String userId;

    private String userName;

    private Ship[] ships;

    private String victoryMessage;

    public String getUserId ()
    {
        return userId;
    }

    public void setUserId (String userId)
    {
        this.userId = userId;
    }

    public String getUserName ()
    {
        return userName;
    }

    public void setUserName (String userName)
    {
        this.userName = userName;
    }

    public Ship[] getShips ()
    {
        return ships;
    }

    public void setShips (Ship[] ships)
    {
        this.ships = ships;
    }

    public String getVictoryMessage ()
    {
        return victoryMessage;
    }

    public void setVictoryMessage (String victoryMessage)
    {
        this.victoryMessage = victoryMessage;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [userId = "+userId+", userName = "+userName+", ships = "+ships+", victoryMessage = "+victoryMessage+"]";
    }
}