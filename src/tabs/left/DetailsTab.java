package tabs.left;

import java.io.IOException;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import yahoo.DetailsData;


public class DetailsTab {

  DetailsData info = null;

  public DetailsTab() {}

  public DetailsTab(DetailsData data) {
    info = data;
  }

  public GridPane getDetails() throws IOException, NullPointerException {
    GridPane detailsGrid = new GridPane();
    detailsGrid.setPadding(new Insets(10, 10, 10, 10));
    detailsGrid.setVgap(8);
    detailsGrid.setHgap(10);

    Label companyName = new Label("Company Name:");
    GridPane.setConstraints(companyName, 0, 0);
    Label companyNameInfo = new Label(info.getName());
    GridPane.setConstraints(companyNameInfo, 2, 0);

    Label companySymbol = new Label("Symbol:");
    GridPane.setConstraints(companySymbol, 0, 1);
    Label companySymbolInfo = new Label(info.getSymbol());
    GridPane.setConstraints(companySymbolInfo, 1, 1);

    Label companyBid = new Label("Sell price:");
    GridPane.setConstraints(companyBid, 0, 2);
    Label companyBidInfoLabel = new Label();
    companyBidInfoLabel.setText(info.getBid());
    GridPane.setConstraints(companyBidInfoLabel, 1, 2);

    Label companyAsk = new Label("Buy price:");
    GridPane.setConstraints(companyAsk, 0, 3);
    Label companyAskInfo = new Label();
    companyAskInfo.setText(info.getAsk());
    GridPane.setConstraints(companyAskInfo, 1, 3);

    Label companyPrice = new Label("Last trade price:");
    GridPane.setConstraints(companyPrice, 0, 4);
    Label companyPriceInfo = new Label();
    companyPriceInfo.setText(info.getPrice());
    GridPane.setConstraints(companyPriceInfo, 1, 4);

    Label companyOpen = new Label("Day open:");
    GridPane.setConstraints(companyOpen, 2, 1);
    Label companyOpenInfo = new Label();
    companyOpenInfo.setText(info.getOpen());
    GridPane.setConstraints(companyOpenInfo, 3, 1);

    Label companyDayl = new Label("Day low:");
    GridPane.setConstraints(companyDayl, 2, 2);
    Label companyDaylInfo = new Label();
    companyDaylInfo.setText(info.getDayLow());
    GridPane.setConstraints(companyDaylInfo, 3, 2);

    Label companyDayh = new Label("Day high:");
    GridPane.setConstraints(companyDayh, 2, 3);
    Label companyDayhInfo = new Label();
    companyDayhInfo.setText(info.getDayHigh());
    GridPane.setConstraints(companyDayhInfo, 3, 3);

    Label companyClose = new Label("Close price:");
    GridPane.setConstraints(companyClose, 2, 4);
    Label companyCloseInfo = new Label();
    companyCloseInfo.setText(info.getPreviousClose());
    GridPane.setConstraints(companyCloseInfo, 3, 4);

    Label companyVolume = new Label("Volume:");
    GridPane.setConstraints(companyVolume, 0, 5);
    Label companyVolumeInfo = new Label();
    companyVolumeInfo.setText(info.getVolume());
    GridPane.setConstraints(companyVolumeInfo, 1, 5);

    Label companyVolumeA = new Label("Avg volume(3m):");
    GridPane.setConstraints(companyVolumeA, 2, 5);
    Label companyVolumeAInfo = new Label();
    companyVolumeAInfo.setText(info.getAvgVolumeDaily());
    GridPane.setConstraints(companyVolumeAInfo, 3, 5);

    Label companyStats = new Label("Company Stats:");
    GridPane.setConstraints(companyStats, 0, 6);

    Label companyBVPS = new Label("Book value per share:");
    GridPane.setConstraints(companyBVPS, 0, 7);
    Label companyBVPSInfo = new Label();
    companyBVPSInfo.setText(info.getBookValuePerShare());
    GridPane.setConstraints(companyBVPSInfo, 1, 7);

    Label companyRevenue = new Label("Revenue:");
    GridPane.setConstraints(companyRevenue, 0, 8);
    Label companyRevenueInfo = new Label();
    companyRevenueInfo.setText(info.getRevenue());
    GridPane.setConstraints(companyRevenueInfo, 1, 8);

    Label companyEBITDA = new Label("EBITDA:");
    GridPane.setConstraints(companyEBITDA, 0, 9);
    Label companyEBITDAInfo = new Label();
    companyEBITDAInfo.setText(info.getEBITDA());
    GridPane.setConstraints(companyEBITDAInfo, 1, 9);

    Label companyOutstandingShares = new Label("Outstanding shares:");
    GridPane.setConstraints(companyOutstandingShares, 0, 10);
    Label companyOutstandingSharesInfo = new Label();
    companyOutstandingSharesInfo.setText(info.getSharesOutstanding());
    GridPane.setConstraints(companyOutstandingSharesInfo, 1, 10);

    Label companyMarketCap = new Label("Market Capitalization:");
    GridPane.setConstraints(companyMarketCap, 0, 11);
    Label companyMarketCapInfo = new Label();
    companyMarketCapInfo.setText(info.getMarketCap());
    GridPane.setConstraints(companyMarketCapInfo, 1, 11);

    detailsGrid.getChildren().addAll(companyName, companyNameInfo, companySymbol, companySymbolInfo,
        companyBid, companyBidInfoLabel, companyAsk, companyAskInfo, companyPrice, companyPriceInfo,
        companyOpen, companyOpenInfo, companyDayl, companyDaylInfo, companyDayh, companyDayhInfo,
        companyClose, companyCloseInfo, companyVolume, companyVolumeInfo, companyVolumeA,
        companyVolumeAInfo, companyStats, companyBVPS, companyBVPSInfo, companyRevenue,
        companyRevenueInfo, companyEBITDA, companyEBITDAInfo, companyOutstandingShares,
        companyOutstandingSharesInfo, companyMarketCap, companyMarketCapInfo);
    return detailsGrid;

  }

}
