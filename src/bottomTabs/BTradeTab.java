package bottomTabs;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import DB.DBConnection;
import accounts.LoggedUser;
import dataForTable.TradeData;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import yahoo.EssentialData;
import yahoo.EssentialDataFromYahoo;
import yahoo.YahooFinance;


public class BTradeTab {
	public TableView<TradeData> tradeTable;
	TableColumn<TradeData,String>bSymbolColumn,bOrderColumn,bTimeColumn,bTypeColumn,bVolumeColumn,bPriceColumn,bCurrentPriceColumn,bProfitColumn,bCompanyColumn;
	public MenuItem closeTradeMI,showChartMI,showDetailMI;
	private LoggedUser loggedUser = new LoggedUser();
	private ChangeTables changeTables = new ChangeTables();
	private DBConnection db = new DBConnection();
	private BigDecimal profit = new BigDecimal(0);
	private BigDecimal lastPrice = new BigDecimal(0);
	private BigDecimal purchasePrice = new BigDecimal(0);
	private BigDecimal volume = new BigDecimal(0);
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BTradeTab(){
		closeTradeMI = new MenuItem("Close Trade");
		showChartMI = new MenuItem("Open Chart");
		showDetailMI = new MenuItem("Details");
		bCompanyColumn = new TableColumn<TradeData,String>("Company");
		bCompanyColumn.setCellValueFactory(new PropertyValueFactory("company"));
		bSymbolColumn = new TableColumn<TradeData,String>("Symbol");
		bSymbolColumn.setCellValueFactory(new PropertyValueFactory("symbol"));
		bOrderColumn = new TableColumn<TradeData,String>("Order ¹");
		bOrderColumn.setCellValueFactory(new PropertyValueFactory("orderNumber"));
		bTimeColumn = new TableColumn<TradeData,String>("Time");
		bTimeColumn.setCellValueFactory(new PropertyValueFactory("orderTime"));
		bTypeColumn = new TableColumn<TradeData,String>("Type");
		bTypeColumn.setCellValueFactory(new PropertyValueFactory("orderType"));
		bVolumeColumn = new TableColumn<TradeData,String>("Volume");
		bVolumeColumn.setCellValueFactory(new PropertyValueFactory("volume"));
		bPriceColumn = new TableColumn<TradeData,String>("Purchase Price");
		bPriceColumn.setCellValueFactory(new PropertyValueFactory("purchasePrice"));
		bCurrentPriceColumn = new TableColumn<TradeData,String>("Current Price");
		bCurrentPriceColumn.setCellValueFactory(new PropertyValueFactory("currentPrice"));
		bProfitColumn = new TableColumn<TradeData,String>("Profit");
		bProfitColumn.setCellValueFactory(new PropertyValueFactory("profit"));
		tradeTable = new TableView<TradeData>();
		tradeTable.getColumns().addAll(bCompanyColumn,bSymbolColumn,bOrderColumn,bTimeColumn,bTypeColumn,bVolumeColumn,bPriceColumn,bCurrentPriceColumn,bProfitColumn);
		tradeTable.setContextMenu(new ContextMenu(closeTradeMI,showChartMI,showDetailMI));
		setColumnsSizes();

		closeTradeMI.setOnAction(e->{try {
			TradeData closeTrade = tradeTable.getSelectionModel().getSelectedItem();
			closeTrade(closeTrade);
		} catch (Exception e1) {
			e1.printStackTrace();
		}});



	}
	public void setColumnsSizes(){
		bCompanyColumn.prefWidthProperty().bind(tradeTable.widthProperty().multiply(0.2));
		bSymbolColumn.prefWidthProperty().bind(tradeTable.widthProperty().multiply(0.1));
		bOrderColumn.prefWidthProperty().bind(tradeTable.widthProperty().multiply(0.1));
		bTimeColumn.prefWidthProperty().bind(tradeTable.widthProperty().multiply(0.1));
		bTypeColumn.prefWidthProperty().bind(tradeTable.widthProperty().multiply(0.1));
		bVolumeColumn.prefWidthProperty().bind(tradeTable.widthProperty().multiply(0.1));
		bPriceColumn.prefWidthProperty().bind(tradeTable.widthProperty().multiply(0.1));
		bCurrentPriceColumn.prefWidthProperty().bind(tradeTable.widthProperty().multiply(0.1));
		bProfitColumn.prefWidthProperty().bind(tradeTable.widthProperty().multiply(0.1));
	}


	public void closeTrade(TradeData closeTrade) throws ClassNotFoundException, SQLException, IOException{
		getDataFromDB(closeTrade);

		YahooFinance yahoo = new EssentialDataFromYahoo(closeTrade.getSymbol());
		EssentialData stock = (EssentialData) yahoo.getData();
		if(closeTrade.getOrderType().equals("buy")){
			lastPrice=stock.getBid();
			if(lastPrice==null){
				showAlertMessage();
				return;
				}
			profit=(lastPrice.subtract(purchasePrice)).multiply(volume);
		}else if (closeTrade.getOrderType().equals("sell")){
			lastPrice= stock.getAsk();
			if(lastPrice==null){
				showAlertMessage();
				return;
				}
			profit=(purchasePrice.subtract(lastPrice)).multiply(volume);
		}

		profit=profit.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		saveDataToDB(closeTrade);
	}

	private void getDataFromDB(TradeData closeTrade) throws ClassNotFoundException, SQLException{
		db.connectingToDB();
		ResultSet userTrade=db.SelectDB("select volume,purchaseprice,symbol from trades where username='"+loggedUser.getLoggedUser()+"'"+"and ordernumber="+closeTrade.getOrderNumber()+"and tradetime='"+closeTrade.getOrderTime()+"'" );
		userTrade.first();
		purchasePrice=userTrade.getBigDecimal(2);
		volume=userTrade.getBigDecimal(1);
	}

	private void saveDataToDB(TradeData closeTrade) throws SQLException{
		ResultSet userResult=db.SelectDB("select balance,leverage,margin from users where username='"+loggedUser.getLoggedUser()+"'");
		userResult.first();
		BigDecimal leverage = userResult.getBigDecimal(2);
		BigDecimal balance = (userResult.getBigDecimal(1).add(profit)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
		BigDecimal stockMargin = (volume.multiply(purchasePrice).divide(leverage)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
		BigDecimal accMargin = (userResult.getBigDecimal(3).subtract(stockMargin)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
		BigDecimal freeMargin= (balance.subtract(accMargin)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
		String tradestate ="closed";
		db.insertDB("update trades set profit="+profit+", tradestate='"+tradestate+"',lastprice="+lastPrice+",closetime='"+getDate()+"' where username='"+loggedUser.getLoggedUser()+"'"+"and ordernumber="+closeTrade.getOrderNumber()+"and tradetime='"+closeTrade.getOrderTime()+"'");
		db.insertDB("update users set balance="+balance+", margin="+accMargin+", freemargin="+freeMargin+"where username='"+loggedUser.getLoggedUser()+"'");
		System.out.println("Closed position in:"+closeTrade.getSymbol()+", at price:"+lastPrice+", at profit:"+profit);
		repaintTables();

		db.closeConnectionToDB();
	}

	private void showAlertMessage(){
		Alert alert = new Alert(AlertType.ERROR,"You can not close a position, when there isn't data from Yahoo");
		alert.setHeaderText("No data from Yahoo");
		alert.showAndWait();
	}

	private StringBuilder getDate(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		StringBuilder date = new StringBuilder(dateFormat.format(cal.getTime()));
		return date;
	}

	private void repaintTables(){
		int change = changeTables.getChangeTable();
		change++;
		changeTables.setChangeTable(change);
	}
}
