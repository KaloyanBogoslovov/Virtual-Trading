package accounts;

import java.sql.ResultSet;
import java.sql.SQLException;

import database.DBConnection;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CreateAccount {

  private Stage window;
  private Label emptyField;
  private TextField firstNameTF, lastNameTF, userNameTF, balanceTF;
  private PasswordField passTF;
  private ComboBox<String> leverageCB, exchangeCB;
  private DBConnection db = new DBConnection();
  int i;

  public CreateAccount() {
    window = new Stage();
    window.setTitle("Create Account");
    window.initModality(Modality.APPLICATION_MODAL);
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(20, 20, 20, 20));
    grid.setVgap(8);
    grid.setHgap(10);

    Label firstNameLabel = new Label("First Name:");
    GridPane.setConstraints(firstNameLabel, 0, 0);

    firstNameTF = new TextField();
    firstNameTF.setPromptText("First Name");
    GridPane.setConstraints(firstNameTF, 1, 0);

    Label lastNameLabel = new Label("Last Name:");
    GridPane.setConstraints(lastNameLabel, 0, 1);

    lastNameTF = new TextField();
    lastNameTF.setPromptText("Last Name");
    GridPane.setConstraints(lastNameTF, 1, 1);

    Label userNameLabel = new Label("Username:");
    GridPane.setConstraints(userNameLabel, 0, 2);

    userNameTF = new TextField();
    userNameTF.setPromptText("username");
    GridPane.setConstraints(userNameTF, 1, 2);

    Label passLabel = new Label("Password:");
    GridPane.setConstraints(passLabel, 0, 3);

    passTF = new PasswordField();
    passTF.setPromptText("password");
    GridPane.setConstraints(passTF, 1, 3);

    Label balanceLabel = new Label("Balance:");
    GridPane.setConstraints(balanceLabel, 0, 4);

    balanceTF = new TextField();
    balanceTF.setPromptText("100000$");
    GridPane.setConstraints(balanceTF, 1, 4);

    Label leverageLabel = new Label("Leverage:");
    GridPane.setConstraints(leverageLabel, 0, 5);

    leverageCB = new ComboBox<String>();
    leverageCB.getItems().addAll("1:3", "1:4", "1:5", "1:6", "1:7", "1:8", "1:10", "1:20", "1:30",
        "1:40", "1:50", "1:100", "1:200");
    leverageCB.setValue("1:5");
    leverageCB.setPrefWidth(160);
    GridPane.setConstraints(leverageCB, 1, 5);

    Label exchangeLabel = new Label("Stock Exchange:");
    GridPane.setConstraints(exchangeLabel, 0, 6);

    exchangeCB = new ComboBox<String>();
    exchangeCB.getItems().addAll("NYSE,NASDAQ", "Frankfurt SE");
    exchangeCB.setValue("NYSE,NASDAQ");
    exchangeCB.setPrefWidth(160);
    GridPane.setConstraints(exchangeCB, 1, 6);

    Button cancelButton = new Button("Cancel");
    cancelButton.setOnAction(e -> {
      window.close();
    });
    Button signButton = new Button("Create Account");
    signButton.setOnAction(e -> {
      try {
        addNewAccToDB();
      } catch (Exception e1) {
        e1.printStackTrace();
      }

    });
    HBox hbox = new HBox(10);
    hbox.getChildren().addAll(cancelButton, signButton);
    GridPane.setConstraints(hbox, 1, 7);

    emptyField = new Label();
    GridPane.setConstraints(emptyField, 1, 8);

    grid.getChildren().addAll(firstNameLabel, firstNameTF, lastNameLabel, lastNameTF, userNameLabel,
        userNameTF, passLabel, passTF, balanceLabel, balanceTF, leverageLabel, leverageCB,
        exchangeLabel, exchangeCB, hbox, emptyField);

    Scene scene = new Scene(grid, 300, 320);
    window.setScene(scene);
    window.setResizable(false);
    window.getIcons().add(new Image("blue.png"));
    window.show();

  }

  private void addNewAccToDB() throws SQLException, ClassNotFoundException {
    boolean run = checkData();
    if (run) {
      db.connectingToDB();

      String data = "INSERT INTO USERS " + "VALUES(" + "'" + userNameTF.getText() + "'" + ", " + "'"
          + firstNameTF.getText() + "'" + ", " + "'" + lastNameTF.getText() + "'" + ", " + "'"
          + passTF.getText() + "'" + ", " + Long.parseLong(balanceTF.getText()) + ", " + leverage()
          + ", " + 0 + ", " + 0 + ", " + Long.parseLong(balanceTF.getText()) + ", "
          + Long.parseLong(balanceTF.getText()) + ", " + 0 + "," + "'" + stockExchange() + "'"
          + ")";

      db.insertDB(data);
      db.closeConnectionToDB();
      window.close();
    }
  }

  private boolean checkData() throws ClassNotFoundException, SQLException {
    i = 10;
    checkForEmptyFields();
    checkIfUsernameIsFree();
    checkFirstName();
    checkLastName();
    checkBalance();
    if (i == 15)
      return true;
    else {
      return false;
    }
  }

  private void checkForEmptyFields() {
    if (!firstNameTF.getText().equals("") && !lastNameTF.getText().equals("")
        && !balanceTF.getText().equals("") && !userNameTF.getText().equals("")
        && !passTF.getText().equals("")) {
      i += 2;
      emptyField.setText("");
    } else {
      emptyField.setText("Please fill all of the fields");
      emptyField.setTextFill(Color.rgb(210, 39, 30));
      i -= 2;
    }
  }

  private void checkIfUsernameIsFree() throws ClassNotFoundException, SQLException {
    db.connectingToDB();
    ResultSet usernames = db.SelectDB("select username from users");
    while (usernames.next()) {
      if (userNameTF.getText().equalsIgnoreCase(usernames.getString(1))) {
        i--;
        userNameTF.setStyle("-fx-text-inner-color: red;");

      }
    }
    if (i == 8 || i == 12) {
      userNameTF.setStyle("-fx-text-inner-color: black;");
    }
    db.closeConnectionToDB();
  }

  private void checkFirstName() {
    if (firstNameTF.getText().matches("[a-zA-Z]+")) {
      i++;
      firstNameTF.setStyle("-fx-text-inner-color: black;");
    } else {
      firstNameTF.setStyle("-fx-text-inner-color: red;");
      i--;
    }
  }

  private void checkLastName() {
    if (lastNameTF.getText().matches("[a-zA-Z]+")) {
      i++;
      lastNameTF.setStyle("-fx-text-inner-color: black;");
    } else {
      lastNameTF.setStyle("-fx-text-inner-color: red;");
      i--;
    }
  }

  private void checkBalance() {
    if (balanceTF.getText().matches("\\d+")) {
      i++;
      balanceTF.setStyle("-fx-text-inner-color: black;");
    } else {
      balanceTF.setStyle("-fx-text-inner-color: red;");
      i--;
    }
  }

  private int leverage() {
    int a = 5;
    switch (leverageCB.getValue()) {
      case "1:3":
        return 3;
      case "1:4":
        return 4;
      case "1:5":
        return 5;
      case "1:6":
        return 6;
      case "1:7":
        return 7;
      case "1:8":
        return 8;
      case "1:10":
        return 10;
      case "1:20":
        return 20;
      case "1:30":
        return 30;
      case "1:40":
        return 40;
      case "1:50":
        return 50;
      case "1:100":
        return 100;
      case "1:200":
        return 200;
    }
    return a;
  }

  private String stockExchange() {
    String exchange = "";
    switch (exchangeCB.getValue()) {
      case "NYSE,NASDAQ":
        return exchange = "NYSE,NASDAQ";
      case "Frankfurt SE":
        return exchange = "FRANKFURT";
    }

    return exchange;
  }
}
