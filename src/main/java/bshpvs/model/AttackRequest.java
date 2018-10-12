package bshpvs.model;

public class AttackRequest
{
    private String enemy;

    private String y;

    private String x;

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
        return "MoveRequest [enemy = "+enemy+", y = "+y+", x = "+x+"]";
    }
}