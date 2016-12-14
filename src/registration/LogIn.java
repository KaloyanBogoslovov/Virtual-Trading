package registration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import data.updating.LoggedUser;
import database.Database;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LogIn {

  public static HashMap<String, Symbols> mapData;
  public static Set<String> symbols;
  public static Set<String> keys;
  private TextField nameTF;
  private PasswordField passTF;
  private Label error;
  private Stage window;

  public LogIn() {
    window = new Stage();
    window.setTitle("Login");
    window.initModality(Modality.APPLICATION_MODAL);

    GridPane grid = new GridPane();
    grid.setPadding(new Insets(20, 20, 20, 20));
    grid.setVgap(8);
    grid.setHgap(10);

    Label nameLabel = new Label("Username:");
    GridPane.setConstraints(nameLabel, 0, 0);

    nameTF = new TextField();
    nameTF.setPromptText("username");
    GridPane.setConstraints(nameTF, 1, 0);
    nameTF.setPrefWidth(220);
    Label passLabel = new Label("Password:");
    GridPane.setConstraints(passLabel, 0, 1);

    passTF = new PasswordField();
    passTF.setPromptText("password");
    GridPane.setConstraints(passTF, 1, 1);
    passTF.setPrefWidth(220);

    nameTF.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER) {
        try {
          logging();
        } catch (ClassNotFoundException | SQLException e1) {
          e1.printStackTrace();
        }
      }
    });
    passTF.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER) {
        try {
          logging();
        } catch (ClassNotFoundException | SQLException e1) {
          e1.printStackTrace();
        }
      }
    });

    Button loginButton = new Button("Log In");
    loginButton.setOnAction(e -> {
      try {
        logging();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    });
    loginButton.setPrefWidth(110);
    Button signButton = new Button("Create Account");
    signButton.setOnAction(e -> {
      new CreateAccount();
    });
    signButton.setPrefWidth(110);
    HBox hbox = new HBox(10);
    hbox.getChildren().addAll(signButton, loginButton);
    GridPane.setConstraints(hbox, 1, 2);

    error = new Label();
    GridPane.setConstraints(error, 1, 3);

    grid.getChildren().addAll(nameLabel, nameTF, passLabel, passTF, hbox, error);

    Scene scene = new Scene(grid, 330, 160);
    window.setScene(scene);
    window.setResizable(false);
    window.setOnCloseRequest(e -> {
      System.exit(0);
    });
    window.getIcons().add(new Image("blue.png"));
    window.show();

  }

  private void logging() throws ClassNotFoundException, SQLException {
    Database.connectingToDB();
    String input = nameTF.getText() + " " + passTF.getText();
    ResultSet result = Database.SelectDB("Select username,password,stockexchange from USERS");
    while (result.next()) {
      if (input.equals(result.getString(1) + " " + result.getString(2))) {
        LoggedUser.setLoggedUser(nameTF.getText());
        String exchange = getExchange(result.getString(3));
        initData(exchange);
        initSymbols(exchange);
        window.close();
      }
      error.setText("Your username or password is incorrect!");
      error.setTextFill(Color.rgb(210, 39, 30));
    }
    Database.closeConnectionToDB();
  }

  private String getExchange(String exchangeFromDB) {
    String exchange = "";
    System.out.println("Exchange from Database:" + exchangeFromDB);
    switch (exchangeFromDB) {
      case "FRANKFURT":
        return exchange = "DeCompanies.csv";
      // case "FRANKFURT":return exchange="FrankfurtStockExchange.csv";
      case "NYSE,NASDAQ":
        return exchange = "NYSE,NASDAQ.csv";
    }

    return exchange;
  }

  private void initData(String exchange) {
    mapData = new HashMap<String, Symbols>();
    try {
      BufferedReader br = getCompanies(exchange);
      String line = br.readLine();
      while (line != null) {
        String[] data = line.split(",");
        mapData.put(data[1], new Symbols(data[0]));
        line = br.readLine();
      }
      keys = mapData.keySet();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // add all company symbols to a hashset object
  private void initSymbols(String exchange) {
    symbols = new HashSet<String>();
    try {
      BufferedReader br = getCompanies(exchange);
      String line = br.readLine();
      while (line != null) {
        String[] data = line.split(",");
        symbols.add(data[0]);
        line = br.readLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private BufferedReader getCompanies(String exchange) throws IOException {
    InputStream is = getClass().getResourceAsStream(exchange);
    InputStreamReader isr = new InputStreamReader(is);
    BufferedReader br = new BufferedReader(isr);
    br.readLine();
    return br;
  }


}
