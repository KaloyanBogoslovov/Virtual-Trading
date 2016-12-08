package tabs.left;

import java.io.IOException;

import accounts.LogIn;
import accounts.Symbols;
import charts.MainChart;
import charts.NewChart;
import data.tables.SymbolsData;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Main;
import newtradingposition.NewPosition;
import tabs.Tabs;
import yahoo.DetailsData;
import yahoo.DetailsDataFromYahoo;
import yahoo.YahooFinance;

public class SymbolsTab implements Tabs{
  private TableView<SymbolsData> leftTable;


  @SuppressWarnings({"unchecked", "rawtypes"})
  public SymbolsTab() {
    MenuItem delMI = new MenuItem("Delete");
    MenuItem newOrderMI = new MenuItem("New Order");
    MenuItem detailsMI = new MenuItem("Details");
    MenuItem openChartMI = new MenuItem("Open Chart");

    TableColumn<SymbolsData, String> symbolColumn = new TableColumn<SymbolsData, String>("Symbol");
    symbolColumn.setCellValueFactory(new PropertyValueFactory("symbol"));
    TableColumn<SymbolsData, String> bidColumn = new TableColumn<SymbolsData, String>("Sell price");
    bidColumn.setCellValueFactory(new PropertyValueFactory("bid"));
    TableColumn<SymbolsData, String> askColumn = new TableColumn<SymbolsData, String>("Buy price");
    askColumn.setCellValueFactory(new PropertyValueFactory("ask"));
    leftTable = new TableView<SymbolsData>();
    leftTable.getColumns().addAll(symbolColumn, bidColumn, askColumn);
    leftTable.setContextMenu(new ContextMenu(delMI, newOrderMI, detailsMI, openChartMI));

    symbolColumn.prefWidthProperty().bind(leftTable.widthProperty().multiply(0.5));
    bidColumn.prefWidthProperty().bind(leftTable.widthProperty().multiply(0.25));
    askColumn.prefWidthProperty().bind(leftTable.widthProperty().multiply(0.25));

    detailsMI.setOnAction(e -> {
      detailsMI();
    });

    openChartMI.setOnAction(e -> {
      openChartMI();
    });

    delMI.setOnAction(e -> {
      delRow();
    });

    newOrderMI.setOnAction(e -> {
      SymbolsData symbol = leftTable.getSelectionModel().getSelectedItem();
      NewPosition newOrder = new NewPosition(LogIn.symbols);
      newOrder.symbolTF.setText(symbol.getSymbol());
    });

  }

  private void detailsMI() {
    SymbolsData symbol = leftTable.getSelectionModel().getSelectedItem();
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

  private void openChartMI() {
    SymbolsData symbol = leftTable.getSelectionModel().getSelectedItem();
    try {
      NewChart.lastCompanyChart = symbol.getSymbol();
      Main.centerBorderPane.setCenter(new MainChart("empty").drawMainChart(symbol.getSymbol()));
    } catch (Exception e1) {
      System.out.println("Unlucky Exception");
    }
  }

  private void delRow() {
    ObservableList<SymbolsData> rowSelected, allRows;
    allRows = leftTable.getItems();
    rowSelected = leftTable.getSelectionModel().getSelectedItems();
    rowSelected.forEach(allRows::remove);
  }

  @Override
  public TableView getTable() {
    return leftTable;
  }

  @Override
  public void initTableContent() {
    try {
      Symbols symbol = LogIn.mapData.get(Main.inputCB.getEditor().getText());
      String company = symbol.symbol;
      NewChart.lastCompanyChart = company;
      YahooFinance yahoo = new DetailsDataFromYahoo(symbol.getSymbol());
      DetailsData info = (DetailsData) yahoo.getData();

      SymbolsData data = new SymbolsData();
      data.setSymbol(info.getSymbol());
      data.setAsk(info.getAsk());
      data.setBid(info.getBid());
      leftTable.getItems().add(data);
      DetailsTab details = new DetailsTab(info);
      Main.detailsTab.setContent(details.getDetails());
      Main.leftTabPane.getSelectionModel().select(Main.detailsTab);
      Main.centerBorderPane.setCenter(new MainChart("empty").drawMainChart(company));

    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }

}
