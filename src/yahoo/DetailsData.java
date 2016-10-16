package yahoo;

public class DetailsData {

	private String name;
	private String symbol;
	private String bid;
	private String ask;
	private String price;
	private String open;
	private String dayLow;
	private String dayHigh;
	private String previousClose;
	private String volume;
	private String avgVolumeDaily;
	private String bookValuePerShare;
	private String revenue;
	private String EBITDA;
	public String getEBITDA() {
		return EBITDA;
	}

	public void setEBITDA(String eBITDA) {
		EBITDA = eBITDA;
	}

	private String sharesOutstanding;
	private String marketCap;

	public DetailsData(){}

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

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getAsk() {
		return ask;
	}

	public void setAsk(String ask) {
		this.ask = ask;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getDayLow() {
		return dayLow;
	}

	public void setDayLow(String dayLow) {
		this.dayLow = dayLow;
	}

	public String getDayHigh() {
		return dayHigh;
	}

	public void setDayHigh(String dayHigh) {
		this.dayHigh = dayHigh;
	}

	public String getPreviousClose() {
		return previousClose;
	}

	public void setPreviousClose(String previousClose) {
		this.previousClose = previousClose;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getAvgVolumeDaily() {
		return avgVolumeDaily;
	}

	public void setAvgVolumeDaily(String avgVolumeDaily) {
		this.avgVolumeDaily = avgVolumeDaily;
	}

	public String getBookValuePerShare() {
		return bookValuePerShare;
	}

	public void setBookValuePerShare(String bookValuePerShare) {
		this.bookValuePerShare = bookValuePerShare;
	}

	public String getRevenue() {
		return revenue;
	}

	public void setRevenue(String revenue) {
		this.revenue = revenue;
	}

	public String getSharesOutstanding() {
		return sharesOutstanding;
	}

	public void setSharesOutstanding(String sharesOutstanding) {
		this.sharesOutstanding = sharesOutstanding;
	}

	public String getMarketCap() {
		return marketCap;
	}

	public void setMarketCap(String marketCap) {
		this.marketCap = marketCap;
	}



}
