package main;

import about.About;
import accounts.LogIn;
import charts.MainChart;
import charts.NewChart;
import data.updating.DataUpdating;
import data.updating.LoggedUser;
import data.updating.UpdateTables;
import database.DBConnection;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import logfile.LogFile;
import newtradingposition.NewPosition;
import tabs.Tabs;
import tabs.TabsFactory;

public class Main extends Application {
  public static TabPane leftTabPane;
  public static Tab detailsTab;
  public static BorderPane centerBorderPane = new BorderPane();
  public static ComboBox<String> inputCB;
  private Tabs symbolTable;


  public static void main(String[] args) {
    launch(args);
  }

  public void start(Stage primaryStage) throws Exception {
    Stage window = primaryStage;
    window.setTitle("Virtual Trading");

    Menu fileMenu = new Menu("File");
    Menu toolsMenu = new Menu("Tools");
    Menu helpMenu = new Menu("Help");

    Tabs balanceTable = TabsFactory.createElement(0);
    Tabs tradeTable = TabsFactory.createElement(1);
    Tabs historyTable = TabsFactory.createElement(2);
    symbolTable = TabsFactory.createElement(3);
    new MainChart();

    MenuItem logOutMI = new MenuItem("Logout...");
    MenuItem exitMI = new MenuItem("Exit");
    fileMenu.getItems().addAll(logOutMI, exitMI);

    MenuItem orderMI = new MenuItem("New Order...");
    MenuItem newChartMI = new MenuItem("New Chart...");
    toolsMenu.getItems().addAll(orderMI, newChartMI);

    MenuItem aboutMI = new MenuItem("About...");
    helpMenu.getItems().addAll(aboutMI);

    inputCB = new ComboBox<String>();
    inputCB.setMinWidth(209);
    inputCB.setMaxWidth(200);
    inputCB.setEditable(true);
    inputCB.getStyleClass().add("combo-box1");

    Button searchButton = new Button("Search");
    Separator separator1 = new Separator();
    separator1.setOrientation(Orientation.VERTICAL);
    Button newOrderButton = new Button("New Order...");
    Separator separator2 = new Separator();
    separator2.setOrientation(Orientation.VERTICAL);
    Button newChartButton = new Button("New Chart...");

    ToolBar topToolBar = new ToolBar();
    topToolBar.getItems().addAll(inputCB, searchButton, separator1, newOrderButton, newChartButton,
        separator2);

    MenuBar mainMenuBar = new MenuBar();
    mainMenuBar.getMenus().addAll(fileMenu, toolsMenu, helpMenu);

    VBox topVBox = new VBox(5);
    topVBox.getChildren().addAll(mainMenuBar, topToolBar);

    leftTabPane = new TabPane();
    Tab symbolsTab = new Tab("Company symbols");
    detailsTab = new Tab("Company details");
    symbolsTab.setContent(symbolTable.getTable());
    leftTabPane.getTabs().addAll(symbolsTab, detailsTab);
    leftTabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
    leftTabPane.setSide(Side.BOTTOM);

    TabPane bottomTabPane = new TabPane();
    Tab tradeTab = new Tab("Trades");
    Tab balanceTab = new Tab("Balance");
    Tab historyTab = new Tab("History");
    bottomTabPane.getTabs().addAll(tradeTab, balanceTab, historyTab);
    bottomTabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
    bottomTabPane.setSide(Side.BOTTOM);

    tradeTab.setContent(tradeTable.getTable());
    historyTab.setContent(historyTable.getTable());
    balanceTab.setContent(balanceTable.getTable());

    inputCB.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
      public void handle(KeyEvent e) {
        inputCompanyListener(e);
      }
    });

    UpdateTables.updateTableProperty().addListener((v, oldvalue, newvalue) -> {
      initTables(tradeTable, balanceTable, historyTable);
    });

    LoggedUser.loggedUserProperty().addListener((v, oldvalue, newvalue) -> {
      initTables(tradeTable, balanceTable, historyTable);
    });

    searchButton.setOnAction(e -> {
      symbolTable.initTableContent();
    });

    exitMI.setOnAction(e -> {
      window.close();
      System.exit(0);
    });

    newOrderButton.setOnAction(e -> {
      new NewPosition(LogIn.symbols);
    });

    logOutMI.setOnAction(e -> {
      LoggedUser.setLoggedUser("");
      new LogIn();
    });

    orderMI.setOnAction(e -> {
      new NewPosition(LogIn.symbols);
    });

    aboutMI.setOnAction(e -> {
      new About();
      System.out.println("153756");
    });

    newChartButton.setOnAction(e -> {
      new NewChart().window.show();
    });

    newChartMI.setOnAction(e -> {
      new NewChart().window.show();
    });

    window.setOnCloseRequest(e -> {
      System.exit(0);
    });

    BorderPane borderPane = new BorderPane();
    borderPane.setTop(topVBox);
    borderPane.setLeft(leftTabPane);
    borderPane.setBottom(bottomTabPane);
    borderPane.setCenter(centerBorderPane);
    centerBorderPane.setBottom(MainChart.centerHbox);
    Rectangle2D a = Screen.getPrimary().getVisualBounds(); // making object with the screens
                                                           // properties
    Scene mainScene = new Scene(borderPane, (a.getMaxX()) * 0.9, (a.getMaxY()) * 0.9);
    mainScene.getStylesheets().add("comboStyles.css");

    leftTabPane.prefWidthProperty().bind(mainScene.widthProperty().multiply(0.325));
    bottomTabPane.prefHeightProperty().bind(mainScene.heightProperty().multiply(0.3));
    bottomTabPane.prefWidthProperty().bind(mainScene.widthProperty());

    window.getIcons().add(new Image("blue.png"));
    window.setScene(mainScene);
    window.show();

    DBConnection.createDB();
    new DataUpdating().dataUpdate();
    LogFile.initPathLocationAndCreateFile();
    new LogIn();
  }

  private void initTables(Tabs tradeTable, Tabs balanceTable, Tabs historyTable) {
    try {
      tradeTable.initTableContent();
      balanceTable.initTableContent();
      historyTable.initTableContent();
    } catch (Exception e1) {
      e1.printStackTrace();
    }
  }

  private void inputCompanyListener(KeyEvent e) {
    KeyCode key = e.getCode();
    if ((key != KeyCode.ENTER) && (key != KeyCode.UP) && (key != KeyCode.DOWN)) {
      searchCompany();
    }
    if (!inputCB.getEditor().getText().isEmpty() && e.getCode() == KeyCode.ENTER) {
      symbolTable.initTableContent();
    }
  }

  /*
   * check for company names that contain the inputed chars in the combobox and display them as
   * hints
   */
  private void searchCompany() {
    inputCB.hide();
    inputCB.getItems().clear();
    String s_typed = inputCB.getEditor().getText();

    for (String key : LogIn.keys) {
      if (s_typed.length() > 1) {
        boolean toadd =
            s_typed.length() == 0 ? true : key.toLowerCase().contains(s_typed.toLowerCase());
        if (toadd)
          inputCB.getItems().add(key);
      }
    }
    boolean showlist = s_typed.length() > 1;
    if (showlist)
      inputCB.show();
  }
}
