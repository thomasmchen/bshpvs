package bshpvs.model;

public class PlaceShipRequest {

  private String start;

  private String end;

  private String shipType;

  private String userID;

  public void setUserID(String userID) {
    this.userID = userID;
  }

  public String getUserID() {
    return userID;
  }

  public void setStart(String start) {
    this.start = start;
  }

  public String getStart() {
    return start;

  }

  public void setEnd(String end) {
    this.end = end;
  }

  public String getEnd() {
    return end;
  }

  public void setShipType(String shipType) {
    this.shipType = shipType;
  }

  public String getShipType() {
    return shipType;
  }
  @Override
  public String toString()
  {
      return "PlaceShipRequest [userID = "+userID+", start = "+start+", end = "+end+", type = "+shipType+"]";
  }



}
