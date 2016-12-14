package tabs.bottom;

import java.sql.ResultSet;
import java.sql.SQLException;

import data.tables.BalanceData;
import data.updating.LoggedUser;
import database.Database;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import tabs.Tabs;

public class BalanceTab implements Tabs {
  private TableView<BalanceData> balanceTable;
  private TableColumn<BalanceData, String> balanceColumn, equityColumn, leverageColumn,
      marginColumn, freeMarginColumn, totalProfitColumn;

  @SuppressWarnings({"unchecked", "rawtypes"})
  public BalanceTab() {
    balanceColumn = new TableColumn<BalanceData, String>("Balance");
    balanceColumn.setCellValueFactory(new PropertyValueFactory("balance"));
    equityColumn = new TableColumn<BalanceData, String>("Equity");
    equityColumn.setCellValueFactory(new PropertyValueFactory("equity"));
    leverageColumn = new TableColumn<BalanceData, String>("Leverage");
    leverageColumn.setCellValueFactory(new PropertyValueFactory("leverage"));
    marginColumn = new TableColumn<BalanceData, String>("Margin");
    marginColumn.setCellValueFactory(new PropertyValueFactory("margin"));
    freeMarginColumn = new TableColumn<BalanceData, String>("Free Margin");
    freeMarginColumn.setCellValueFactory(new PropertyValueFactory("freeMargin"));
    totalProfitColumn = new TableColumn<BalanceData, String>("Live Profit");
    totalProfitColumn.setCellValueFactory(new PropertyValueFactory("profit"));

    balanceTable = new TableView<BalanceData>();
    balanceTable.getColumns().addAll(balanceColumn, equityColumn, leverageColumn, marginColumn,
        freeMarginColumn, totalProfitColumn);

    setColumnSizes();
  }

  private void setColumnSizes() {
    balanceColumn.prefWidthProperty().bind(balanceTable.widthProperty().multiply(0.1666));
    equityColumn.prefWidthProperty().bind(balanceTable.widthProperty().multiply(0.1666));
    leverageColumn.prefWidthProperty().bind(balanceTable.widthProperty().multiply(0.1666));
    marginColumn.prefWidthProperty().bind(balanceTable.widthProperty().multiply(0.1666));
    freeMarginColumn.prefWidthProperty().bind(balanceTable.widthProperty().multiply(0.1666));
    totalProfitColumn.prefWidthProperty().bind(balanceTable.widthProperty().multiply(0.1666));
  }

  public TableView getTable() {
    return balanceTable;
  }


  public void initTableContent() {
    try {
      Database.connectingToDB();
      ResultSet result = Database.SelectDB(
          "select balance, equity,leverage,margin,freemargin,totalprofit from users where username='"
              + LoggedUser.getLoggedUser() + "'");
      ObservableList<BalanceData> allRows;
      allRows = balanceTable.getItems();
      allRows.clear();

      while (result.next()) {
        BalanceData balanceData = new BalanceData();
        balanceData.setBalance(result.getString(1));
        balanceData.setEquity(result.getString(2));
        balanceData.setLeverage(result.getString(3));
        balanceData.setMargin(result.getString(4));
        balanceData.setFreeMargin(result.getString(5));
        balanceData.setProfit(result.getString(6));
        balanceTable.getItems().add(balanceData);
      }
      Database.closeConnectionToDB();

    } catch (SQLException | ClassNotFoundException e) {
    }
  }

}
