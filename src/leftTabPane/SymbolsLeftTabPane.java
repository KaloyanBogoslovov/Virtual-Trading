package leftTabPane;

import dataForTable.Data;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SymbolsLeftTabPane {
	public TableView<Data> leftTable;
	TableColumn<Data,String> symbolColumn,bidColumn,askColumn;
	MenuItem delMI;
	public MenuItem newOrderMI;
	public MenuItem detailsMI;
	public MenuItem openChartMI;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SymbolsLeftTabPane(){

		delMI = new MenuItem("Delete");
		newOrderMI= new MenuItem("New Order");
		detailsMI = new MenuItem("Details");
		openChartMI = new MenuItem("Open Chart");


		symbolColumn = new TableColumn<Data,String>("Symbol");
		symbolColumn.setCellValueFactory(new PropertyValueFactory("symbol"));
		bidColumn = new TableColumn<Data,String>("Sell price");
		bidColumn.setCellValueFactory(new PropertyValueFactory("bid"));
		askColumn = new TableColumn<Data,String>("Buy price");
		askColumn.setCellValueFactory(new PropertyValueFactory("ask"));
		leftTable  = new TableView<Data>();
		leftTable.getColumns().addAll(symbolColumn,bidColumn,askColumn);
		leftTable.setContextMenu(new ContextMenu(delMI,newOrderMI,detailsMI,openChartMI));
		setColumnSizing();


		delMI.setOnAction(e->{
			delRow();
		});


	}

	public void setColumnSizing(){
		symbolColumn.prefWidthProperty().bind(leftTable.widthProperty().multiply(0.5));
		bidColumn.prefWidthProperty().bind(leftTable.widthProperty().multiply(0.25));
		askColumn.prefWidthProperty().bind(leftTable.widthProperty().multiply(0.25));

	}

	public void delRow(){
		ObservableList<Data> rowSelected,allRows;
		allRows = leftTable.getItems();
		rowSelected = leftTable.getSelectionModel().getSelectedItems();
		rowSelected.forEach(allRows::remove);

	}

}
