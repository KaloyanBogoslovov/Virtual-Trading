package yahoo;

import java.math.BigDecimal;

public class UpdateData {
	private String name;
	private String symbol;
	private BigDecimal bid;
	private BigDecimal ask;

	public UpdateData(){
	}

	public UpdateData(String name, String symbol, String bid, String ask) {
		this.name = name;
		this.symbol = symbol;
		this.bid = new BigDecimal(bid);
		this.ask = new BigDecimal(ask);
	}

	public String getName(){
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public BigDecimal getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid =new BigDecimal(bid);
	}

	public BigDecimal getAsk() {
		return ask;
	}

	public void setAsk(String ask) {
		this.ask = new BigDecimal(ask);
	}
}
