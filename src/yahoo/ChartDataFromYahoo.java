package yahoo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class ChartDataFromYahoo extends YahooFinance {
  private String company;
  private String from;
  private String to;
  private String interval;
  private List<ChartData> result;

  public ChartDataFromYahoo(String company, String from, String to, String interval) {
    this.company = company;
    this.from = from;
    this.to = to;
    this.interval = interval;

  }

  public Object getData() {
    String url = makeURL();
    URLConnection connection = setConnectionWithYahoo(url);
    getInformationFromYahoo(connection);
    return result;
  }

  public String makeURL() {
    int fr = Integer.parseInt(from.substring(0, 2)) - 1;
    int t = Integer.parseInt(to.substring(0, 2)) - 1;

    String fromTo = "&a=" + fr + "&b=" + from.charAt(3) + from.charAt(4) + "&c=" + from.charAt(6)
        + from.charAt(7) + from.charAt(8) + from.charAt(9) + "&d=" + t + "&e=" + to.charAt(3)
        + to.charAt(4) + "&f=" + to.charAt(6) + to.charAt(7) + to.charAt(8) + to.charAt(9) + "&g=";

    String url =
        "http://ichart.yahoo.com/table.csv?s=" + company + fromTo + interval + "&ignore=.csv";
    return url;
  }

  private void getInformationFromYahoo(URLConnection connection) {
    try {
      InputStreamReader is = new InputStreamReader(connection.getInputStream());
      BufferedReader br = new BufferedReader(is);
      br.readLine();
      String line = br.readLine();
      addChartDataObjectsToList(line, br);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void addChartDataObjectsToList(String line, BufferedReader br) throws IOException {
    result = new ArrayList<ChartData>();
    while (line != null) {
      System.out.println("Historical data parsing:" + line);
      ChartData quote = parseCSVAndSetChartData(line);
      result.add(quote);
      line = br.readLine();
    }
    br.close();
  }

  private ChartData parseCSVAndSetChartData(String line) {
    String[] data = line.split(",");
    return new ChartData(data[0], data[1], data[3], data[2], data[4], data[6],
        Long.parseLong(data[5]));
  }



}
