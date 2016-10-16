package dataForTable;

import javafx.beans.property.SimpleStringProperty;

public class BalanceData {
    private SimpleStringProperty balance;
    private SimpleStringProperty equity;
    private SimpleStringProperty leverage;
    private SimpleStringProperty margin;
    private SimpleStringProperty freeMargin;
    private SimpleStringProperty profit ;

    public BalanceData(){

    	balance = new SimpleStringProperty();
	    equity = new SimpleStringProperty();
	    leverage = new SimpleStringProperty();
	    margin = new SimpleStringProperty();
	    freeMargin = new SimpleStringProperty();
	    profit = new SimpleStringProperty();
    }
	public String getBalance() {
		return balance.get();
	}
	public void setBalance(String balancee) {
		balance.set(balancee);
	}
	public String getEquity() {
		return equity.get();
	}
	public void setEquity(String equityy) {
		equity.set(equityy);
	}
	public String getLeverage(){
		return leverage.get();
	}
	public void setLeverage(String leveragee){
		leverage.set(leveragee);
	}
	public String getMargin() {
		return margin.get();
	}
	public void setMargin(String marginn) {
		margin.set(marginn);
	}
	public String getFreeMargin() {
		return freeMargin.get();
	}
	public void setFreeMargin(String freeMarginn) {
		freeMargin.set(freeMarginn);
	}
	public String getProfit() {
		return profit.get();
	}
	public void setProfit(String profitt) {
		profit.set(profitt);
	}
}
