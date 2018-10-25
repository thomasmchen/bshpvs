package bshpvs.api.core;

import java.awt.Point;

import bshpvs.api.core.NewGameResponse.Coordinate;

public class MoveResponse
{

    private int shipId;

    Point[] spaces;

    public MoveResponse(int s, Point[] p) {
        this.shipId = s;
        this.spaces = p;

    }
    public int getShipId()
    {
        return this.shipId;
    }

    public void setShipId (int shipId)
    {
        this.shipId = shipId;
    }

   public void setSpaces(Point[] s) {
       this.spaces = s.clone();
   }

   public Point[] getSpaces() {
       return this.spaces;
   }



    @Override
    public String toString()
    {
        return "MoveRequest [shipId = " + this.shipId;
    }
}