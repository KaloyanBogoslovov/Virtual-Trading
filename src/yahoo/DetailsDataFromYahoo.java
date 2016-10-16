package yahoo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DetailsDataFromYahoo extends YahooFinance{
	String company="";
	String[] data;
	DetailsData result=null;
	public DetailsDataFromYahoo(String company){
		this.company=company;
	}

	@Override
	public Object getData() {
		String url = makeURL();
		URLConnection connection = setConnectionWithYahoo(url);
		getInformationFromYahoo(connection);
		setDataToDetailsData();
		return result;
	}

	private String makeURL(){
		String url = "http://finance.yahoo.com/d/quotes.csv?s="+company+"&f=nsbal1oghpva2b4s6j4j2j1&e=.csv";
		return url;
	}

	public void getInformationFromYahoo(URLConnection connection){
		try{
        InputStreamReader is = new InputStreamReader(connection.getInputStream());
        BufferedReader br = new BufferedReader(is);
        String line = br.readLine();
        System.out.println("Details parsing:"+line);
        parseTheInformation(line);
		}catch(IOException e){
			System.out.println();
		}
	}

	public void parseTheInformation(String line){
        //removing the first element '"' to be able to get the name of the company
        line = line.substring(1);
        int iend = line.indexOf('"');
        //getting the company name
        String companyName = line.substring(0 , iend);
        //getting the remaining data
        line=line.substring(iend);
        data = line.split(",");
        data[0]= companyName;
        data[1]=data[1].substring(1, data[1].length()-1);
        System.out.println(data[0]+data[1]);
	}

	public void setDataToDetailsData(){
        result= new DetailsData();
        result.setName(data[0]);
        result.setSymbol(data[1]);
        result.setBid(data[2]);
        result.setAsk(data[3]);
        result.setPrice(data[4]);
        result.setOpen(data[5]);
        result.setDayLow(data[6]);
        result.setDayHigh(data[7]);
        result.setPreviousClose(data[8]);
        result.setVolume(data[9]);
        result.setAvgVolumeDaily(data[10]);
        result.setBookValuePerShare(data[11]);
        result.setRevenue(data[12]);
        result.setEBITDA(data[13]);
        result.setSharesOutstanding(data[14]);
        try{
        result.setMarketCap(data[15]);
        }catch(ArrayIndexOutOfBoundsException a){
			Alert alert = new Alert(AlertType.ERROR,"Currently there isn't data, for the company that you requested. Sorry for the inconvenience.");
			alert.setHeaderText("No data from Yahoo");
			alert.showAndWait();
        }
	}

}
