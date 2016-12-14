package yahoo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import data.updating.DataUpdating;
import logfile.LogFile;

public class UpdateDataFromYahoo extends YahooFinance {
  private String[] companies;
  private List<UpdateData> result = new ArrayList<UpdateData>();
  String[] data;

  public UpdateDataFromYahoo(String[] companies) {
    this.companies = companies;
  }

  public Object getData() {
    String url = makeURL();
    URLConnection connection = setConnectionWithYahoo(url);
    getInformationFromYahoo(connection);
    return result;
  }

  private String makeURL() {
    String company = "";
    for (int i = 0; i < companies.length - 1; i++) {
      company = company + companies[i] + "%2C";
    }
    company = company + companies[companies.length - 1];
    String url =
        "http://finance.yahoo.com/d/quotes.csv?s=" + company + "&f=nsbal1oghpva2b4s6j4j2j1&e=.csv";
    LogFile.saveDataToLogFile("URL:" + url);
    return url;
  }

  private void getInformationFromYahoo(URLConnection connection) {
    try {
      InputStreamReader is = new InputStreamReader(connection.getInputStream());
      BufferedReader br = new BufferedReader(is);
      String line = br.readLine();
      addChartDataObjectsToList(line, br);
    } catch (final java.net.SocketTimeoutException e) {
      System.out.println("SocketTimeoutException");
      LogFile.saveDataToLogFile("Exception: SocketTimeoutException");
      new DataUpdating().dataUpdate();
    } catch (java.io.IOException e) {
      LogFile.saveDataToLogFile("Exception: IOException");
      System.out.println("Exception: IOException");
      e.printStackTrace();
      new DataUpdating().dataUpdate();
    }
  }

  private void addChartDataObjectsToList(String line, BufferedReader br) throws IOException {
    result = new ArrayList<UpdateData>();
    while (line != null) {
      System.out.println("Parsing:" + line);
      LogFile.saveDataToLogFile("Parsing:" + line);
      UpdateData quote = parseCSVLine(line);
      result.add(quote);
      line = br.readLine();
    }
    br.close();
  }

  private UpdateData parseCSVLine(String line) {
    // removing the first element '"' to be able to get the name of the company
    line = line.substring(1);
    int iend = line.indexOf('"');
    // getting the company name
    String companyName = line.substring(0, iend);
    line = line.substring(iend);
    data = line.split(",");
    data[0] = companyName;
    // if there isnt available data for sell/buy price set the price at 0
    checkDataAvailability();
    return new UpdateData(data[0], data[1], data[2], data[3]);
  }

  private void checkDataAvailability() {
    if (!data[2].matches("[0-9.]*")) {
      data[2] = "0";
    }
    if (!data[3].matches("[0-9.]*")) {
      data[3] = "0";
    }
  }


}


