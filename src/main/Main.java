package main;

import newOrder.NewOrder;
import yahoo.DetailsData;
import yahoo.DetailsDataFromYahoo;
import yahoo.YahooFinance;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import DB.DBConnection;
import about.About;
import accounts.LogIn;
import accounts.LoggedUser;
import accounts.SymbolsData;
import bottomTabs.BHistoryTab;
import bottomTabs.BTradeTab;
import bottomTabs.BalanceTab;
import bottomTabs.ChangeTables;
import charts.MainChart;
import charts.NewChart;
import dataForTable.BalanceData;
import dataForTable.Data;
import dataForTable.HistoryData;
import dataForTable.TradeData;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
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
import leftTabPane.DetailsLeftTabPane;
import leftTabPane.SymbolsLeftTabPane;
import logFile.LogFile;

public class Main extends Application{

	private Stage window;
	private Scene mainScene;
	private MenuBar mainMenuBar;
	private Menu fileMenu,toolsMenu, helpMenu;
	private MenuItem logOutMI,exitMI,orderMI,newChartMI,aboutMI;
	private Separator separator1,separator2;
	private VBox topVBox;
	private Button searchButton,newOrderButton,newChartButton;
	private ComboBox<String> inputCB = new ComboBox<String>();
	private ToolBar topToolBar;
	private TabPane bottomTabPane, leftTabPane;
	private Tab tradeTab,financeTab,historyTab,symbolsTab,detailsTab;
	private Data data;
	private BorderPane borderPane=new BorderPane();
	private BorderPane centerBorderPane = new BorderPane();
	private NewChart chart=	new NewChart();
	private MainChart mainChart = new MainChart();
	private SymbolsLeftTabPane symbolsLeftTabPane = new SymbolsLeftTabPane();
	private LoggedUser loggedUser = new LoggedUser();
	private ChangeTables changeTables = new ChangeTables();
	private BHistoryTab bHistoryTab =new BHistoryTab();
	private BalanceTab balanceTab = new BalanceTab();
	private BTradeTab bTradeTab = new BTradeTab();
	private DataUpdating updating = new DataUpdating();
	Set<String> keys;
	private SymbolsData symbol;

	public static void main(String[] args) {launch(args);}
	 {

	}
	 static{//blockt

	 }


	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		window.setTitle("Virtual Trading");

		fileMenu = new Menu("File");
		toolsMenu = new Menu("Tools");
		helpMenu = new Menu("Help");

		logOutMI= new MenuItem("Logout...");
		exitMI= new MenuItem("Exit");
		fileMenu.getItems().addAll(logOutMI,exitMI);

		orderMI = new MenuItem("New Order...");
		newChartMI = new MenuItem("New Chart...");
		toolsMenu.getItems().addAll(orderMI,newChartMI);

		aboutMI = new MenuItem("About...");
		helpMenu.getItems().addAll(aboutMI);

		topToolBar = new ToolBar();
		searchButton = new Button("Search");

		inputCB.setMinWidth(209);
		inputCB.setMaxWidth(200);
		inputCB.setEditable(true);
		inputCB.getStyleClass().add("combo-box1");

		separator1 = new Separator();
		separator1.setOrientation(Orientation.VERTICAL);
		newOrderButton= new Button("New Order...");
		separator2 = new Separator();
		separator2.setOrientation(Orientation.VERTICAL);

		newChartButton= new Button("New Chart...");
		topToolBar.getItems().addAll(inputCB,searchButton,separator1,newOrderButton,newChartButton,separator2);

		mainMenuBar = new MenuBar();
		mainMenuBar.getMenus().addAll(fileMenu,toolsMenu,helpMenu);

		topVBox = new VBox(5);
		topVBox.getChildren().addAll(mainMenuBar,topToolBar);

		leftTabPane = new TabPane();
		symbolsTab = new Tab("Company symbols");
		detailsTab = new Tab("Company details");

		symbolsTab.setContent(symbolsLeftTabPane.leftTable);
		leftTabPane.getTabs().addAll(symbolsTab,detailsTab);
		leftTabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		leftTabPane.setSide(Side.BOTTOM);

		bottomTabPane = new TabPane();
		tradeTab = new Tab("Trades");
		financeTab = new Tab("Balance");
		historyTab = new Tab("History");
		bottomTabPane.getTabs().addAll(tradeTab,financeTab,historyTab);
		bottomTabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		bottomTabPane.setSide(Side.BOTTOM);
		tradeTab.setContent(bTradeTab.tradeTable);
		historyTab.setContent(bHistoryTab.historyTable);
		financeTab.setContent(balanceTab.balanceTable);

		inputCB.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
	        public void handle(KeyEvent e) {
				KeyCode key = e.getCode();
				if ( (key != KeyCode.ENTER) && (key != KeyCode.UP)  && (key != KeyCode.DOWN)) {
					searchCompany();
				}
	            if (!inputCB.getEditor().getText().isEmpty()&&e.getCode() == KeyCode.ENTER) {
	            	addMarketDataTable();
	            }
	        }
	 });

		changeTables.changeTableProperty().addListener((v,oldvalue,newvalue)->{
			try {
				initialTable();
				initialHistoryTable();
				balanceTable();

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		loggedUser.loggedUserProperty().addListener((v,oldvalue,newvalue)->{
			try {
				initialTable();
				initialHistoryTable();
				balanceTable();


			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		searchButton.setOnAction(e->{
			addMarketDataTable();
		});
		symbolsLeftTabPane.newOrderMI.setOnAction(e->{
			Data symbol = symbolsLeftTabPane.leftTable.getSelectionModel().getSelectedItem();
			NewOrder newOrder = new NewOrder(LogIn.symbols);
			newOrder.symbolTF.setText(symbol.getSymbol());
		});

		newChartButton.setOnAction(e->{
			chart.window.show();
		chart.adjustButton.setOnAction(e1->{
			mainChart.centerHbox.setVisible(true);
			try {
				chart.chart();
			} catch (IOException e2) {e2.printStackTrace();}
			LineChart<String,Number> lchart =chart.getLineChart();
			centerBorderPane.setCenter(lchart);
		});
		});

		exitMI.setOnAction(e->{
			window.close();
			System.exit(0);
		});
		newOrderButton.setOnAction(e->{
			 new NewOrder(LogIn.symbols);
		});
		logOutMI.setOnAction(e->{
			loggedUser.setLoggedUser("");
			new LogIn();
		});
		orderMI.setOnAction(e->{
			new NewOrder(LogIn.symbols);
		});
		newChartMI.setOnAction(e->{
			chart.window.show();
		chart.adjustButton.setOnAction(e1->{
			mainChart.centerHbox.setVisible(true);
			try {
				chart.chart();
			} catch (IOException e2) {e2.printStackTrace();}
			LineChart<String,Number> lchart =chart.getLineChart();
			centerBorderPane.setCenter(lchart);
		});
		});
		aboutMI.setOnAction(e->{
			new About();
			System.out.println("153756");
		});
		bTradeTab.showChartMI.setOnAction(e->{
			showChartMI();
		});
		bTradeTab.showDetailMI.setOnAction(e->{
			showDetailsMI();
		});
		symbolsLeftTabPane.detailsMI.setOnAction(e->{
			detailsMI();
		});
		symbolsLeftTabPane.openChartMI.setOnAction(e->{
			openChartMI();
		});
		mainChart.adjustChartButton.setOnAction(e->{
			try {centerBorderPane.setCenter(mainChart.drawChart(chart.lastCompanyChart));
			} catch (Exception e1) {System.out.println("adjustChartButton throws unlucky exception"+e1.getStackTrace());}
		});
		window.setOnCloseRequest(e->{
			System.exit(0);
		});


		borderPane.setTop(topVBox);
		borderPane.setLeft(leftTabPane);
		borderPane.setBottom(bottomTabPane);
		borderPane.setCenter(centerBorderPane);
		centerBorderPane.setBottom(mainChart.centerHbox);
		Rectangle2D a =Screen.getPrimary().getVisualBounds(); //making object with the screens properties
		mainScene = new Scene(borderPane,(a.getMaxX())*0.9,(a.getMaxY())*0.9);
		mainScene.getStylesheets().add("comboStyles.css");
		//LEFTTABPATE SIZING
		leftTabPane.prefWidthProperty().bind(mainScene.widthProperty().multiply(0.325));

		//BOTTOMTABPANE SIZING
		bottomTabPane.prefHeightProperty().bind(mainScene.heightProperty().multiply(0.3));
		bottomTabPane.prefWidthProperty().bind(mainScene.widthProperty());
		window.getIcons().add(new Image("blue.png"));
		window.setScene(mainScene);
		window.show();
		DBConnection.createDB();
		new LogIn();
		updating.dataUpdate();
		LogFile.initPathLocationAndCreateFile();
	}


	public void addMarketDataTable(){
		try {
		symbol=LogIn.mapData.get(inputCB.getEditor().getText());
		String company = symbol.symbol;
		chart.lastCompanyChart = company;
		YahooFinance yahoo= new DetailsDataFromYahoo(symbol.getSymbol());
		DetailsData info = (DetailsData) yahoo.getData();

		data = new Data();
		data.setSymbol(info.getSymbol());//
		data.setAsk(info.getAsk());
		data.setBid(info.getBid());
		symbolsLeftTabPane.leftTable.getItems().add(data);
		DetailsLeftTabPane 	details = new DetailsLeftTabPane(info);
		detailsTab.setContent(details.getDetails());
		leftTabPane.getSelectionModel().select(detailsTab);
		centerBorderPane.setCenter(mainChart.drawChart(company));

		} catch ( IOException e1) {e1.printStackTrace();}

	}
	public void initialTable() throws SQLException, ClassNotFoundException{

		DBConnection db = new DBConnection();
		db.connectingToDB();
		ResultSet result = db.SelectDB("select company, symbol,ordernumber,tradetime,tradetype,volume,purchaseprice,currentprice,profit  from trades where username="+"'"+loggedUser.getLoggedUser()+"'"+"and tradestate='active' ");

		ObservableList<TradeData> allRows;
		allRows = bTradeTab.tradeTable.getItems();
		allRows.clear();

		while(result.next()){
		TradeData tradeData = new TradeData();
		tradeData.setCompany(result.getString(1));
		tradeData.setSymbol(result.getString(2));
		tradeData.setOrderNumber(result.getInt(3));
		tradeData.setOrderTime(result.getString(4));
		tradeData.setOrderType(result.getString(5));
		tradeData.setVolume(result.getInt(6));
		tradeData.setPurchasePrice(result.getBigDecimal(7));
//		if(result.getString(8).equals("0.00")){
//			tradeData.setCurrentPrice("No data");
//			tradeData.setProfit("N/A");

//		}else{
			tradeData.setCurrentPrice(result.getBigDecimal(8));
			tradeData.setProfit(result.getBigDecimal(9));
//		}
		bTradeTab.tradeTable.getItems().add(tradeData);
		}
		 db.closeConnectionToDB();
	}
	public void initialHistoryTable()throws SQLException, ClassNotFoundException{

		DBConnection db = new DBConnection();
		db.connectingToDB();
		ResultSet result = db.SelectDB("select company, symbol,ordernumber,tradetime,closetime,tradetype,volume,purchaseprice,lastprice,profit  from trades where username="+"'"+loggedUser.getLoggedUser()+"'"+"and tradestate='closed' ");
		ObservableList<HistoryData> allRows;
		allRows = bHistoryTab.historyTable.getItems();
		allRows.clear();

		while(result.next()){
		HistoryData historyData = new HistoryData();
		historyData.setCompany(result.getString(1));
		historyData.setSymbol(result.getString(2));
		historyData.setOrderNumber(result.getInt(3));
		historyData.setOrderTime(result.getString(4));
		historyData.setCloseTime(result.getString(5));
		historyData.setOrderType(result.getString(6));
		historyData.setVolume(result.getInt(7));
		historyData.setPurchasePrice(result.getBigDecimal(8));
		historyData.setLastPrice(result.getBigDecimal(9));
		historyData.setProfit(result.getBigDecimal(10));
		bHistoryTab.historyTable.getItems().add(historyData);


		}
		 db.closeConnectionToDB();
	}
	public void balanceTable() throws ClassNotFoundException, SQLException{
		DBConnection db = new DBConnection();
		db.connectingToDB();
		ResultSet result = db.SelectDB("select balance, equity,leverage,margin,freemargin,totalprofit from users where username='"+loggedUser.getLoggedUser()+"'");
		ObservableList<BalanceData> allRows;
		allRows = balanceTab.balanceTable.getItems();
		allRows.clear();

		while(result.next()){
			BalanceData balanceData = new BalanceData();
			balanceData.setBalance(result.getString(1));
			balanceData.setEquity(result.getString(2));
			balanceData.setLeverage(result.getString(3));
			balanceData.setMargin(result.getString(4));
			balanceData.setFreeMargin(result.getString(5));
			balanceData.setProfit(result.getString(6));
			balanceTab.balanceTable.getItems().add(balanceData);
		}
		db.closeConnectionToDB();
	}

	public void detailsMI(){
		Data symbol = symbolsLeftTabPane.leftTable.getSelectionModel().getSelectedItem();
		try {
			YahooFinance yahoo= new DetailsDataFromYahoo(symbol.getSymbol());
			DetailsData info = (DetailsData) yahoo.getData();
			DetailsLeftTabPane 	details = new DetailsLeftTabPane(info);
			detailsTab.setContent(details.getDetails());
			leftTabPane.getSelectionModel().select(detailsTab);
		} catch (Exception e1) {System.out.println("Unlucky Exception");}
	}

	public void openChartMI(){
		Data symbol = symbolsLeftTabPane.leftTable.getSelectionModel().getSelectedItem();
		try {
			chart.lastCompanyChart = symbol.getSymbol();
			centerBorderPane.setCenter(mainChart.drawChart(symbol.getSymbol()));
		} catch (Exception e1) {System.out.println("Unlucky Exception");}
	}

	public void showDetailsMI(){
		TradeData symbol = bTradeTab.tradeTable.getSelectionModel().getSelectedItem();
		try {
			YahooFinance yahoo= new DetailsDataFromYahoo(symbol.getSymbol());
			DetailsData info = (DetailsData) yahoo.getData();
			DetailsLeftTabPane 	details = new DetailsLeftTabPane(info);
			detailsTab.setContent(details.getDetails());
			leftTabPane.getSelectionModel().select(detailsTab);
		} catch (Exception e1) {System.out.println("Unlucky Exception");}
	}

	public void showChartMI(){
		TradeData symbol = bTradeTab.tradeTable.getSelectionModel().getSelectedItem();
		try {
			chart.lastCompanyChart = symbol.getSymbol();
			centerBorderPane.setCenter(mainChart.drawChart(symbol.getSymbol()));
		} catch (Exception e1) {System.out.println("Unlucky Exception");}
	}
	//check for company names that contain the inputed chars in the combobox and display them as hints
	public void searchCompany(){
		inputCB.hide();
		inputCB.getItems().clear();
		String s_typed = inputCB.getEditor().getText();

		for ( String key : LogIn.keys ){
			if(s_typed.length()>1){
			boolean toadd = s_typed.length() == 0 ? true : key.toLowerCase().contains(s_typed.toLowerCase());
			if (toadd) inputCB.getItems().add(key);

			}
		}
		boolean showlist = s_typed.length() > 1;
		if (showlist) inputCB.show();
	}

}