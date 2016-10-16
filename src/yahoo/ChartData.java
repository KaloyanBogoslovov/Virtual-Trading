package yahoo;

import java.math.BigDecimal;

public class ChartData {
    private String date;
    private BigDecimal open;
    private BigDecimal low;
    private BigDecimal high;
    private BigDecimal close;
    private BigDecimal adjClose;
    private Long volume;

	public ChartData( String date, String open, String low, String high,
			String close, String adjClose, Long volume) {

		this.date = date;
		this.open =new BigDecimal(open);
		this.low = new BigDecimal(low);
		this.high = new BigDecimal(high);
		this.close = new BigDecimal(close);
		this.adjClose = new BigDecimal(adjClose);
		this.volume=volume;
	}

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public BigDecimal getOpen() {
		return open;
	}
	public void setOpen(BigDecimal open) {
		this.open = open;
	}
	public BigDecimal getLow() {
		return low;
	}
	public void setLow(BigDecimal low) {
		this.low = low;
	}
	public BigDecimal getHigh() {
		return high;
	}
	public void setHigh(BigDecimal high) {
		this.high = high;
	}
	public BigDecimal getClose() {
		return close;
	}
	public void setClose(BigDecimal close) {
		this.close = close;
	}
	public BigDecimal getAdjClose(){
		return adjClose;
	}
	public void setAdjClose(BigDecimal adjClose) {
		this.adjClose = adjClose;
	}

	public Long getVolume() {
		return volume;
	}

	public void setVolume(Long volume) {
		this.volume = volume;
	}

}
