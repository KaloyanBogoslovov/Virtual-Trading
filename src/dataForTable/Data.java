package dataForTable;

public class Data {
	String symbol;
	String ask;
	String bid;


	public Data(){
		symbol="";
		ask = "";
		bid = "";

	}
	public Data(String s,String askk,String bidd){
		symbol = s;
		ask=askk;
		bid = bidd;

	}
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getAsk() {
		return ask;
	}
	public void setAsk(String ask) {
		this.ask = ask;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
}
