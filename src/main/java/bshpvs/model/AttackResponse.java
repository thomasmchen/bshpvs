package bshpvs.model;

public class AttackResponse
{
    private String enemy;

    private String y;

    private String x;

    private String userID;

    public void setUserID(String userID) {
      this.userID = userID;
    }

    public String getUserID() {
      return userID;
    }

    public String getEnemy ()
    {
        return enemy;
    }

    public void setEnemy (String enemy)
    {
        this.enemy = enemy;
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
        return "AttackResponse [userID = "+userID+", enemy = "+enemy+", y = "+y+", x = "+x+"]";
    }
}
