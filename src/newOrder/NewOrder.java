package newOrder;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;

import DB.DBConnection;
import accounts.LoggedUser;
import bottomTabs.ChangeTables;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import yahoo.EssentialData;
import yahoo.EssentialDataFromYahoo;
import yahoo.YahooFinance;

public class NewOrder {

	Stage window;
	public TextField symbolTF,volumeTF;
	private LoggedUser loggedUser = new LoggedUser();
	private DBConnection db = new DBConnection();
	private Label error;
	private Set<String> companySymbols=null;
	private String company;
	private BigDecimal purchaseStockPrice;
	private BigDecimal closeStockPrice;
	private EssentialData stock;
	public NewOrder(Set<String> symbols){
		companySymbols=symbols;
		window = new Stage();
		window.setTitle("New Order");
		window.initModality(Modality.APPLICATION_MODAL);
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(20,20,20,20));
		grid.setVgap(8);
		grid.setHgap(10);

		//symbol label
		Label symbolLabel = new Label("Symbol:");
		GridPane.setConstraints(symbolLabel, 0, 0);

		//symbol textfield
		symbolTF =new TextField();
		symbolTF.setPrefWidth(150);
		GridPane.setConstraints(symbolTF, 1, 0);

		Label volumeLabel = new Label("Volume:");
		GridPane.setConstraints(volumeLabel, 0, 1);

		volumeTF = new TextField();
		volumeTF.setPrefWidth(150);
		GridPane.setConstraints(volumeTF, 1, 1);

		Button sellButton = new Button("Sell Short");
		sellButton.setPrefWidth(75);
		Button buyButton = new Button("Buy Long");
		buyButton.setPrefWidth(75);
		HBox buttonHBox = new HBox(10);
		buttonHBox.getChildren().addAll(sellButton,buyButton);
		GridPane.setConstraints(buttonHBox, 1, 2);
		error = new Label();
		GridPane.setConstraints(error, 1, 3);

		sellButton.setOnAction(e->{
			try {
				sellTrade();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		buyButton.setOnAction(e-> {
		try {
			buyTrade();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		});


		grid.getChildren().addAll(symbolLabel,symbolTF,volumeLabel,volumeTF,buttonHBox,error);

		Scene mainScene  = new Scene(grid,250,150);
		window.setScene(mainScene);
		window.setResizable(false);
		window.getIcons().add(new Image("blue.png"));
		window.show();
	}

	private void buyTrade() throws SQLException{

		if(symbolAndVolumeInputNotEmpty()){
			if(companySymbolExists()){
				YahooFinance yahoo = new EssentialDataFromYahoo(company);
				stock =(EssentialData) yahoo.getData();
				purchaseStockPrice = stock.getAsk();
				closeStockPrice = stock.getBid();
				if(!checkPurchaseStockPrice())return;
				saveTransaction("buy");
				}else{
					wrongSymbol();

			}
		}
	}

	private void sellTrade() throws ClassNotFoundException, SQLException{

		if(symbolAndVolumeInputNotEmpty()){
			if(companySymbolExists()){
				YahooFinance yahoo = new EssentialDataFromYahoo(company);
				stock =(EssentialData) yahoo.getData();
				purchaseStockPrice = stock.getBid();
				closeStockPrice = stock.getAsk();
				if(!checkPurchaseStockPrice())return;
				saveTransaction("sell");
				}else{
					wrongSymbol();

			}
		}
	}

	private boolean symbolAndVolumeInputNotEmpty(){
		return !symbolTF.getText().equals("")&&!volumeTF.getText().equals("")&&volumeTF.getText().matches("\\d+");
	}

	private boolean companySymbolExists(){
		company =symbolTF.getText();
		boolean run = false;
		//check if the inputed symbol is in the data
		for(String symbol:companySymbols){
			if(company.equalsIgnoreCase(symbol)){
				run=true;
				break;
			}
		}
		return run;

	}

	private boolean checkPurchaseStockPrice() throws SQLException{
		boolean run= true;
		if(purchaseStockPrice==null){
			run=false;
			System.out.println("No data!");
			error.setText("No data!");
            error.setTextFill(Color.rgb(210, 39, 30));
			db.closeConnectionToDB();
		}
		return run;
	}

	private ResultSet userInformationFromDB(){
		ResultSet rt = null;
		try {
			db.connectingToDB();
			rt = db.SelectDB("Select balance,leverage,margin,totalprofit,ordernumber,freemargin from users where username ="+"'"+loggedUser.getLoggedUser()+"'");
			rt.absolute(1);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return rt;
	}

	private void saveTransaction(String type) throws SQLException{
		ResultSet rt=userInformationFromDB();
		BigDecimal balance = rt.getBigDecimal(1);
		BigDecimal leverage = rt.getBigDecimal(2);
		BigDecimal accMargin = rt.getBigDecimal(3);
		BigDecimal freeMargin = rt.getBigDecimal(6);
		int orderNumber=rt.getInt(5);
		BigDecimal volume = new BigDecimal(volumeTF.getText());
		purchaseStockPrice=purchaseStockPrice.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		BigDecimal margin  = volume.multiply(purchaseStockPrice).divide(leverage); //devided by the leverage

		if(freeMargin.compareTo(margin)==1){
		accMargin=(accMargin.add(margin)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
		freeMargin = (balance.subtract(accMargin)).setScale(2, BigDecimal.ROUND_HALF_EVEN);

		orderNumber++;
		String state = "active";
		//saving to db
		db.insertDB("UPDATE users set margin ="+accMargin+", freemargin="+freeMargin+", ordernumber="+orderNumber+"where username="+"'"+loggedUser.getLoggedUser()+"'");
		stock.getName();
		db.insertDB("insert into trades values("+
		"'"+loggedUser.getLoggedUser()+"'"+", "+
		"'"+stock.getSymbol()+"'"+","+
		+orderNumber +","+
		"'"+getDate()+"'"+","+
		"'"+type +"'"+","
		+volume +","+
		purchaseStockPrice +","+
		closeStockPrice +","+
		0+","+
		"'"+state +"'"+", "+
		0+", "+
		"'"+stock.getName() +"'"+","+"'"+getDate()+"'"+
		" )");
		System.out.println("Opened long position in:"+stock.getName()+" at price:"+ purchaseStockPrice);

		repaintTables();
		db.closeConnectionToDB();
		window.close();

		}else{
	System.out.println("not enough margin!");
	error.setText("Not enough margin!");
    error.setTextFill(Color.rgb(210, 39, 30));
	db.closeConnectionToDB();
	}
	}

	private StringBuilder getDate(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		StringBuilder date = new StringBuilder(dateFormat.format(cal.getTime()));
		return date;
	}
	private void repaintTables(){
		ChangeTables changeTables = new ChangeTables();
		int change = changeTables.getChangeTable();
		change++;
		changeTables.setChangeTable(change);
	}

	private void wrongSymbol() throws SQLException{
		error.setText("Symbol is wrong!");
        error.setTextFill(Color.rgb(210, 39, 30));

	}


}


