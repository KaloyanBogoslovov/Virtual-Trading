package yahoo;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public abstract class YahooFinance {
	public static final int CONNECTION_TIMEOUT = Integer.parseInt(System.getProperty("yahoofinance.connection.timeout", "10000"));
	public abstract Object getData();

	public URLConnection setConnectionWithYahoo(String url){
		URLConnection connection = null;
		try {
			System.out.println("URL:"+url);
			URL request = new URL(url);
			connection = request.openConnection();
			connection.setConnectTimeout(CONNECTION_TIMEOUT);
			connection.setReadTimeout(CONNECTION_TIMEOUT);
			} catch (IOException e) {
				e.printStackTrace();
			}
		return connection;
	}




}
