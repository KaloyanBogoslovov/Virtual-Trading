package tabs.bottom;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import charts.MainChart;
import charts.NewChart;
import data.tables.TradeData;
import data.updating.LoggedUser;
import data.updating.UpdateTables;
import database.Database;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Main;
import tabs.Tabs;
import tabs.left.DetailsTab;
import yahoo.DetailsData;
import yahoo.DetailsDataFromYahoo;
import yahoo.EssentialData;
import yahoo.EssentialDataFromYahoo;
import yahoo.YahooFinance;


public class TradeTab implements Tabs {
  private TableView<TradeData> tradeTable;
  TableColumn<TradeData, String> symbolColumn, orderColumn, timeColumn, typeColumn, volumeColumn,
      priceColumn, currentPriceColumn, profitColumn, companyColumn;
  BigDecimal profit = new BigDecimal(0);
  BigDecimal lastPrice = new BigDecimal(0);
  BigDecimal purchasePrice = new BigDecimal(0);
  BigDecimal volume = new BigDecimal(0);

  @SuppressWarnings({"unchecked", "rawtypes"})
  public TradeTab() {
    MenuItem closeTradeMI = new MenuItem("Close Trade");
    MenuItem showChartMI = new MenuItem("Open Chart");
    MenuItem showDetailMI = new MenuItem("Details");
    companyColumn = new TableColumn<TradeData, String>("Company");
    companyColumn.setCellValueFactory(new PropertyValueFactory("company"));
    symbolColumn = new TableColumn<TradeData, String>("Symbol");
    symbolColumn.setCellValueFactory(new PropertyValueFactory("symbol"));
    orderColumn = new TableColumn<TradeData, String>("Order ¹");
    orderColumn.setCellValueFactory(new PropertyValueFactory("orderNumber"));
    timeColumn = new TableColumn<TradeData, String>("Time");
    timeColumn.setCellValueFactory(new PropertyValueFactory("orderTime"));
    typeColumn = new TableColumn<TradeData, String>("Type");
    typeColumn.setCellValueFactory(new PropertyValueFactory("orderType"));
    volumeColumn = new TableColumn<TradeData, String>("Volume");
    volumeColumn.setCellValueFactory(new PropertyValueFactory("volume"));
    priceColumn = new TableColumn<TradeData, String>("Purchase Price");
    priceColumn.setCellValueFactory(new PropertyValueFactory("purchasePrice"));
    currentPriceColumn = new TableColumn<TradeData, String>("Current Price");
    currentPriceColumn.setCellValueFactory(new PropertyValueFactory("currentPrice"));
    profitColumn = new TableColumn<TradeData, String>("Profit");
    profitColumn.setCellValueFactory(new PropertyValueFactory("profit"));
    tradeTable = new TableView<TradeData>();
    tradeTable.getColumns().addAll(companyColumn, symbolColumn, orderColumn, timeColumn, typeColumn,
        volumeColumn, priceColumn, currentPriceColumn, profitColumn);
    tradeTable.setContextMenu(new ContextMenu(closeTradeMI, showChartMI, showDetailMI));
    setColumnsSizes();

    showChartMI.setOnAction(e -> {
      showChartMI();
    });

    showDetailMI.setOnAction(e -> {
      showDetailsMI();
    });

    closeTradeMI.setOnAction(e -> {
      try {
        TradeData closeTrade = tradeTable.getSelectionModel().getSelectedItem();
        closeTrade(closeTrade);
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    });
  }

  private void setColumnsSizes() {
    companyColumn.prefWidthProperty().bind(tradeTable.widthProperty().multiply(0.2));
    symbolColumn.prefWidthProperty().bind(tradeTable.widthProperty().multiply(0.1));
    orderColumn.prefWidthProperty().bind(tradeTable.widthProperty().multiply(0.1));
    timeColumn.prefWidthProperty().bind(tradeTable.widthProperty().multiply(0.1));
    typeColumn.prefWidthProperty().bind(tradeTable.widthProperty().multiply(0.1));
    volumeColumn.prefWidthProperty().bind(tradeTable.widthProperty().multiply(0.1));
    priceColumn.prefWidthProperty().bind(tradeTable.widthProperty().multiply(0.1));
    currentPriceColumn.prefWidthProperty().bind(tradeTable.widthProperty().multiply(0.1));
    profitColumn.prefWidthProperty().bind(tradeTable.widthProperty().multiply(0.1));
  }


  private void closeTrade(TradeData closeTrade)
      throws ClassNotFoundException, SQLException, IOException {

    getDataFromDB(closeTrade);
    YahooFinance yahoo = new EssentialDataFromYahoo(closeTrade.getSymbol());
    EssentialData stock = (EssentialData) yahoo.getData();
    if (closeTrade.getOrderType().equals("buy")) {
      lastPrice = stock.getBid();
      if (lastPrice == null) {
        showAlertMessage();
        return;
      }
      profit = (lastPrice.subtract(purchasePrice)).multiply(volume);
    } else if (closeTrade.getOrderType().equals("sell")) {
      lastPrice = stock.getAsk();
      if (lastPrice == null) {
        showAlertMessage();
        return;
      }
      profit = (purchasePrice.subtract(lastPrice)).multiply(volume);
    }

    profit = profit.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    saveDataToDB(closeTrade);
  }

  private void getDataFromDB(TradeData closeTrade) throws ClassNotFoundException, SQLException {
    Database.connectingToDB();
    ResultSet userTrade =
        Database.SelectDB("select volume,purchaseprice,symbol from trades where username='"
            + LoggedUser.getLoggedUser() + "'" + "and ordernumber=" + closeTrade.getOrderNumber()
            + "and tradetime='" + closeTrade.getOrderTime() + "'");
    userTrade.first();
    purchasePrice = userTrade.getBigDecimal(2);
    volume = userTrade.getBigDecimal(1);
  }

  private void saveDataToDB(TradeData closeTrade) throws SQLException {
    ResultSet userResult = Database.SelectDB("select balance,leverage,margin from users where username='"
        + LoggedUser.getLoggedUser() + "'");
    userResult.first();
    BigDecimal leverage = userResult.getBigDecimal(2);
    BigDecimal balance =
        (userResult.getBigDecimal(1).add(profit)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    BigDecimal stockMargin =
        (volume.multiply(purchasePrice).divide(leverage)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    BigDecimal accMargin =
        (userResult.getBigDecimal(3).subtract(stockMargin)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    BigDecimal freeMargin = (balance.subtract(accMargin)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    String tradestate = "closed";
    Database.insertDB("update trades set profit=" + profit + ", tradestate='" + tradestate
        + "',lastprice=" + lastPrice + ",closetime='" + getDate() + "' where username='"
        + LoggedUser.getLoggedUser() + "'" + "and ordernumber=" + closeTrade.getOrderNumber()
        + "and tradetime='" + closeTrade.getOrderTime() + "'");
    Database.insertDB("update users set balance=" + balance + ", margin=" + accMargin + ", freemargin="
        + freeMargin + "where username='" + LoggedUser.getLoggedUser() + "'");
    System.out.println("Closed position in:" + closeTrade.getSymbol() + ", at price:" + lastPrice
        + ", at profit:" + profit);
    repaintTables();

    Database.closeConnectionToDB();
  }

  private void showAlertMessage() {
    Alert alert = new Alert(AlertType.ERROR,
        "You can not close a position, when there isn't data from Yahoo");
    alert.setHeaderText("No data from Yahoo");
    alert.showAndWait();
  }

  private StringBuilder getDate() {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar cal = Calendar.getInstance();
    StringBuilder date = new StringBuilder(dateFormat.format(cal.getTime()));
    return date;
  }

  private void repaintTables() {
    int update = UpdateTables.getUpdateTable();
    update++;
    UpdateTables.setUpdateTable(update);
  }


  private void showDetailsMI() {
    TradeData symbol = tradeTable.getSelectionModel().getSelectedItem();
    try {
      YahooFinance yahoo = new DetailsDataFromYahoo(symbol.getSymbol());
      DetailsData info = (DetailsData) yahoo.getData();
      DetailsTab details = new DetailsTab(info);
      Main.detailsTab.setContent(details.getDetails());
      Main.leftTabPane.getSelectionModel().select(Main.detailsTab);
    } catch (Exception e1) {
      System.out.println("Unlucky Exception");
    }
  }

  private void showChartMI() {
    TradeData symbol = tradeTable.getSelectionModel().getSelectedItem();
    try {
      NewChart.lastCompanyChart = symbol.getSymbol();
      Main.centerBorderPane.setCenter(new MainChart().drawMainChart(symbol.getSymbol()));
    } catch (Exception e1) {
      System.out.println("Unlucky Exceptionrrrr");
    }
  }

  @Override
  public TableView getTable() {
    // TODO Auto-generated method stub
    return tradeTable;
  }

  @Override
  public void initTableContent() {
    try {
      Database.connectingToDB();
      ResultSet result = Database.SelectDB(
          "select company, symbol,ordernumber,tradetime,tradetype,volume,purchaseprice,currentprice,profit  from trades where username="
              + "'" + LoggedUser.getLoggedUser() + "'" + "and tradestate='active' ");

      ObservableList<TradeData> allRows;
      allRows = tradeTable.getItems();
      allRows.clear();

      while (result.next()) {
        TradeData tradeData = new TradeData();
        tradeData.setCompany(result.getString(1));
        tradeData.setSymbol(result.getString(2));
        tradeData.setOrderNumber(result.getInt(3));
        tradeData.setOrderTime(result.getString(4));
        tradeData.setOrderType(result.getString(5));
        tradeData.setVolume(result.getInt(6));
        tradeData.setPurchasePrice(result.getBigDecimal(7));
        // if(result.getString(8).equals("0.00")){
        // tradeData.setCurrentPrice("No data");
        // tradeData.setProfit("N/A");
        // }else{
        tradeData.setCurrentPrice(result.getBigDecimal(8));
        tradeData.setProfit(result.getBigDecimal(9));
        // }
        tradeTable.getItems().add(tradeData);
      }
      Database.closeConnectionToDB();
    } catch (SQLException | ClassNotFoundException e) {
    }
  }
}
