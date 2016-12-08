package data.updating;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoggedUser {

  private static StringProperty loggedUser = new SimpleStringProperty("");

  public static StringProperty loggedUserProperty() {
    return loggedUser;
  }

  public static String getLoggedUser() {
    return loggedUser.get();
  }


  public static void setLoggedUser(String currentLoggedUser) {
    loggedUser.set(currentLoggedUser);
  }

}
