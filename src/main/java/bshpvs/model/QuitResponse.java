package bshpvs.model;

public class QuitResponse {

  private String userId;

  private String userName;

  private String quitMessage;

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

  public String getQuitMessage() {
    return quitMessage;
  }

  public void setQuitMessage(String quitMessage) {
    this.quitMessage = quitMessage;
  }
  @Override
  public String toString()
  {
      return "QuitResponse [userId = "+userId+", userName = "+userName+", quitMessage = "+quitMessage+"]";
  }
}
