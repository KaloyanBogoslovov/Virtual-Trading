package yahoo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;

public class EssentialDataFromYahoo extends YahooFinance {
  private String company = "";
  private String[] data;
  EssentialData result = null;

  public EssentialDataFromYahoo(String company) {
    this.company = company;
  }

  public Object getData() {
    String url = makeURL();
    URLConnection connection = setConnectionWithYahoo(url);
    getInformationFromYahoo(connection);
    setDataToEssentialData();
    return result;
  }

  private String makeURL() {
    String url = "http://finance.yahoo.com/d/quotes.csv?s=" + company + "&f=nsba&e=.csv";
    return url;
  }

  private void getInformationFromYahoo(URLConnection connection) {
    try {
      InputStreamReader is = new InputStreamReader(connection.getInputStream());
      BufferedReader br = new BufferedReader(is);
      String line = br.readLine();
      br.close();
      System.out.println("Essential data parsing:" + line);
      parseTheInformation(line);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("getInformationFromYahoo exception");
    }
  }

  private void parseTheInformation(String line) {
    // removing the first element '"' to be able to get the name of the company
    line = line.substring(1);
    int iend = line.indexOf('"');
    // getting the company name
    String companyName = line.substring(0, iend);
    line = line.substring(iend);
    data = line.split(",");
    data[0] = companyName;
    data[1] = data[1].substring(1, data[1].length() - 1);
  }

  private void setDataToEssentialData() {
    result = new EssentialData();
    try {
      result.setName(data[0]);
      result.setSymbol(data[1]);
      result.setBid(data[2]);
      result.setAsk(data[3]);
    } catch (NumberFormatException e) {
      System.out.println("no data exception");
    }
  }


}
