package yahoo;

import java.math.BigDecimal;

public class EssentialData {

	private String name;
	private String symbol;
	private BigDecimal bid;
	private BigDecimal ask;

	public EssentialData(){

	}

	public String getName() {
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
