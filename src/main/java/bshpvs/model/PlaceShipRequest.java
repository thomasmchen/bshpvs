package bshpvs.model;

public class PlaceShipRequest {

  String start;

  String end;

  String shipType;

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
      return "PlaceShipRequest [start = "+start+", end = "+end+", type = "+shipType+"]";
  }



}
