package bottomTabs;

import dataForTable.BalanceData;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class BalanceTab {
	public TableView<BalanceData> balanceTable;
	TableColumn<BalanceData,String> balanceColumn,equityColumn,leverageColumn,marginColumn,freeMarginColumn, totalProfitColumn;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BalanceTab(){
		balanceColumn = new TableColumn<BalanceData,String>("Balance");
		balanceColumn.setCellValueFactory(new PropertyValueFactory("balance"));
		equityColumn = new TableColumn<BalanceData,String>("Equity");
		equityColumn.setCellValueFactory(new PropertyValueFactory("equity"));
		leverageColumn = new TableColumn<BalanceData,String>("Leverage");
		leverageColumn.setCellValueFactory(new PropertyValueFactory("leverage"));
		marginColumn = new TableColumn<BalanceData,String>("Margin");
		marginColumn.setCellValueFactory(new PropertyValueFactory("margin"));
		freeMarginColumn = new TableColumn<BalanceData,String>("Free Margin");
		freeMarginColumn.setCellValueFactory(new PropertyValueFactory("freeMargin"));
		totalProfitColumn = new TableColumn<BalanceData,String>("Live Profit");
		totalProfitColumn.setCellValueFactory(new PropertyValueFactory("profit"));

		balanceTable = new TableView<BalanceData>();
		balanceTable.getColumns().addAll(balanceColumn,equityColumn,leverageColumn,marginColumn,freeMarginColumn, totalProfitColumn);

		setColumnSizes();
	}

	public void setColumnSizes(){
		balanceColumn.prefWidthProperty().bind(balanceTable.widthProperty().multiply(0.1666));
		equityColumn.prefWidthProperty().bind(balanceTable.widthProperty().multiply(0.1666));
		leverageColumn.prefWidthProperty().bind(balanceTable.widthProperty().multiply(0.1666));
		marginColumn.prefWidthProperty().bind(balanceTable.widthProperty().multiply(0.1666));
		freeMarginColumn.prefWidthProperty().bind(balanceTable.widthProperty().multiply(0.1666));
		totalProfitColumn.prefWidthProperty().bind(balanceTable.widthProperty().multiply(0.1666));
	}

}
