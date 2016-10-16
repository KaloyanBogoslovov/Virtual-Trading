package Accounts;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoggedUser {

	private static  StringProperty loggedUser = new SimpleStringProperty("");
	public StringProperty loggedUserProperty(){
		return loggedUser;
	}
	public String getLoggedUser() {
		return loggedUser.get();
	}

	@SuppressWarnings("static-access")
	public void setLoggedUser(String loggedUser) {
		this.loggedUser.set(loggedUser);
	}

}
