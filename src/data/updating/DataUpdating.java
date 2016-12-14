package data.updating;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import database.Database;
import logfile.LogFile;
import yahoo.UpdateData;
import yahoo.UpdateDataFromYahoo;
import yahoo.YahooFinance;


public class DataUpdating {
  private BigDecimal zero = new BigDecimal(0);
  private int i = 0;
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
  private BigDecimal liveProfit = new BigDecimal(0);


  public void dataUpdate() {
    final Runnable beeper = new Runnable() {
      public void run() {
        if (userLogedIn()) {
          try {
            Database.connectingToDB();
            String[] companies = getCompanies();
            System.out.println("companies");
            if (companies.length != 0) {
              LogFile.saveDataToLogFile(
                  "Update all active positions of user: " + LoggedUser.getLoggedUser());
              System.out.println("tring to update");
              updateCompanies(companies);
            } else {
              System.out.println("No company has been traded yet");
            }
          } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
          }
        } else {
          System.out.println("please logIn");
        }
      }
    };
    final ScheduledFuture<?> beeperHandle =
        scheduler.scheduleWithFixedDelay(beeper, 10, 10, SECONDS);
    scheduler.schedule(new Runnable() {
      public void run() {
        beeperHandle.cancel(true);
      }
    }, 60 * 60, SECONDS);


  }

  private boolean userLogedIn() {
    return !LoggedUser.getLoggedUser().equals("");
  }

  private String[] getCompanies() throws SQLException {
    ResultSet companySymbols = Database.SelectDB("select symbol  from trades where username='"
        + LoggedUser.getLoggedUser() + "' and tradestate='active'");
    ArrayList<String> rowValues = new ArrayList<String>();
    while (companySymbols.next()) {
      rowValues.add(companySymbols.getString(1));
    }
    String[] companies = (String[]) rowValues.toArray(new String[rowValues.size()]);
    return companies;
  }

  private void updateCompanies(String[] companies) throws SQLException {
    YahooFinance yahoo = new UpdateDataFromYahoo(companies);
    @SuppressWarnings("unchecked")
    List<UpdateData> stocks = (List<UpdateData>) yahoo.getData();

    calculateAndUpdateProfits(companies, stocks);
    updateUsersTotalProfitAndEquity();
    repaintTables();
    Database.closeConnectionToDB();
  }

  private void calculateAndUpdateProfits(String[] companies, List<UpdateData> stocks)
      throws SQLException {
    BigDecimal currentPrice = new BigDecimal(0);
    BigDecimal profit = new BigDecimal(0);
    BigDecimal purchasePrice = new BigDecimal(0);
    BigDecimal volume = new BigDecimal(0);
    int orderNumber = 0;

    ResultSet companyData = Database.SelectDB(
        "select symbol,tradetype,purchaseprice,volume,ordernumber  from trades where username='"
            + LoggedUser.getLoggedUser() + "' and tradestate='active'");
    // calculate profits
    for (int i = 0; i <= companies.length - 1; i++) {
      companyData.absolute(i + 1);
      purchasePrice = companyData.getBigDecimal(3);
      volume = companyData.getBigDecimal(4);
      orderNumber = companyData.getInt(5);
      UpdateData companyPrice = stocks.get(i);

      if (companyData.getString(2).equals("buy")) {
        if (!companyPrice.getBid().equals(zero)) {
          currentPrice = companyPrice.getBid();
          profit = (currentPrice.subtract(purchasePrice)).multiply(volume);
        }
      } else if (companyData.getString(2).equals("sell")) {
        if (!companyPrice.getAsk().equals(zero)) {
          currentPrice = companyPrice.getAsk();
          profit = (purchasePrice.subtract(currentPrice)).multiply(volume);
        }
      }
      currentPrice = currentPrice.setScale(2, BigDecimal.ROUND_HALF_EVEN);
      profit = profit.setScale(2, BigDecimal.ROUND_HALF_EVEN);

      Database.insertDB("update trades set profit=" + profit + ", currentPrice=" + currentPrice
          + "where username='" + LoggedUser.getLoggedUser() + "'" + "and ordernumber="
          + orderNumber);
      liveProfit = (liveProfit.add(profit));
    }
  }

  private void updateUsersTotalProfitAndEquity() throws SQLException {
    BigDecimal equity = new BigDecimal(0);
    ResultSet balanceData = Database
        .SelectDB("select balance from users where username='" + LoggedUser.getLoggedUser() + "'");
    balanceData.absolute(1);
    liveProfit = liveProfit.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    equity = (balanceData.getBigDecimal(1).add(liveProfit)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    equity = equity.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    Database.insertDB("update users set totalprofit=" + liveProfit + ", equity=" + equity
        + "where username='" + LoggedUser.getLoggedUser() + "'");
    liveProfit = new BigDecimal(0);
  }

  private void repaintTables() {
    int update = UpdateTables.getUpdateTable();
    update++;
    UpdateTables.setUpdateTable(update);
    i++;
    System.out.println("number of iterations:" + i);
  }
}
