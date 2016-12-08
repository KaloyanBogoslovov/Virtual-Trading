package tabs.bottom;

import java.sql.ResultSet;
import java.sql.SQLException;

import data.tables.HistoryData;
import data.tables.TradeData;
import data.updating.LoggedUser;
import database.DBConnection;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import tabs.Tabs;

public class HistoryTab implements Tabs{
  private TableView<HistoryData> historyTable;
  TableColumn<HistoryData, String> hCompanyColumn, hSymbolColumn, hOrderColumn, hPurchaseTimeColumn,
      hCloseTimeColumn, hTypeColumn, hVolumeColumn, hPurchasePriceColumn, hClosePriceColumn,
      hProfitColumn;

  @SuppressWarnings({"unchecked", "rawtypes"})
  public HistoryTab() {

    hCompanyColumn = new TableColumn<HistoryData, String>("Company");
    hCompanyColumn.setCellValueFactory(new PropertyValueFactory("company"));
    hSymbolColumn = new TableColumn<HistoryData, String>("Symbol");
    hSymbolColumn.setCellValueFactory(new PropertyValueFactory("symbol"));
    hOrderColumn = new TableColumn<HistoryData, String>("Order ¹");
    hOrderColumn.setCellValueFactory(new PropertyValueFactory("orderNumber"));
    hPurchaseTimeColumn = new TableColumn<HistoryData, String>("Purchase Time");
    hPurchaseTimeColumn.setCellValueFactory(new PropertyValueFactory("orderTime"));
    hCloseTimeColumn = new TableColumn<HistoryData, String>("Close Time");
    hCloseTimeColumn.setCellValueFactory(new PropertyValueFactory("closeTime"));
    hTypeColumn = new TableColumn<HistoryData, String>("Type");
    hTypeColumn.setCellValueFactory(new PropertyValueFactory("orderType"));
    hVolumeColumn = new TableColumn<HistoryData, String>("Volume");
    hVolumeColumn.setCellValueFactory(new PropertyValueFactory("volume"));
    hPurchasePriceColumn = new TableColumn<HistoryData, String>("Purchase Price");
    hPurchasePriceColumn.setCellValueFactory(new PropertyValueFactory("purchasePrice"));
    hClosePriceColumn = new TableColumn<HistoryData, String>("Close Price");
    hClosePriceColumn.setCellValueFactory(new PropertyValueFactory("lastPrice"));
    hProfitColumn = new TableColumn<HistoryData, String>("Profit");
    hProfitColumn.setCellValueFactory(new PropertyValueFactory("profit"));
    historyTable = new TableView<HistoryData>();
    historyTable.getColumns().addAll(hCompanyColumn, hSymbolColumn, hOrderColumn,
        hPurchaseTimeColumn, hCloseTimeColumn, hTypeColumn, hVolumeColumn, hPurchasePriceColumn,
        hClosePriceColumn, hProfitColumn);

    setColumnsSizes();


  }

  public void setColumnsSizes() {
    hCompanyColumn.prefWidthProperty().bind(historyTable.widthProperty().multiply(0.1));
    hSymbolColumn.prefWidthProperty().bind(historyTable.widthProperty().multiply(0.1));
    hOrderColumn.prefWidthProperty().bind(historyTable.widthProperty().multiply(0.1));
    hPurchaseTimeColumn.prefWidthProperty().bind(historyTable.widthProperty().multiply(0.1));
    hCloseTimeColumn.prefWidthProperty().bind(historyTable.widthProperty().multiply(0.1));
    hTypeColumn.prefWidthProperty().bind(historyTable.widthProperty().multiply(0.1));
    hVolumeColumn.prefWidthProperty().bind(historyTable.widthProperty().multiply(0.1));
    hPurchasePriceColumn.prefWidthProperty().bind(historyTable.widthProperty().multiply(0.1));
    hClosePriceColumn.prefWidthProperty().bind(historyTable.widthProperty().multiply(0.1));
    hProfitColumn.prefWidthProperty().bind(historyTable.widthProperty().multiply(0.1));
  }

  @Override
  public TableView getTable() {
    // TODO Auto-generated method stub
    return historyTable;
  }

  @Override
  public void initTableContent() {
    try{
      DBConnection db = new DBConnection();
      db.connectingToDB();
      ResultSet result = db.SelectDB(
          "select company, symbol,ordernumber,tradetime,closetime,tradetype,volume,purchaseprice,lastprice,profit  from trades where username="
              + "'" + LoggedUser.getLoggedUser() + "'" + "and tradestate='closed' ");
      ObservableList<HistoryData> allRows;
      allRows = historyTable.getItems();
      allRows.clear();

      while (result.next()) {
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
        historyTable.getItems().add(historyData);
      }
      db.closeConnectionToDB();
    }catch(SQLException | ClassNotFoundException e){
    }

  }


}
