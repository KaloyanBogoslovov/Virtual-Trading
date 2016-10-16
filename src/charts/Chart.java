package charts;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import yahoo.ChartData;
import yahoo.ChartDataFromYahoo;
import yahoo.YahooFinance;

public class Chart {

	protected double startValue;
    protected double endValue;
    LineChart<String,Number> lineChart;

    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected void makeChart(String comp, String period,String periodCB,String interval){
    	  XYChart.Series series = new XYChart.Series();
          List<ChartData> stocks=getChartData(comp,period,periodCB,interval);
         for(int i=stocks.size()-1;i>=0;i--){
      	   findStartAndEnd(stocks,i);
      	   XYChart.Data data = setData(stocks,i);
      	   series.getData().add(data);
          }
         final NumberAxis yAxis = new NumberAxis(startValue,endValue*1.04,(endValue-startValue)/20);
         final CategoryAxis xAxis = new CategoryAxis();
         lineChart = new LineChart<String,Number>(xAxis,yAxis);
         lineChart.setTitle(comp);
         lineChart.getData().addAll(series);
         lineChart.setLegendVisible(false);

    }



	protected List<ChartData> getChartData(String comp, String period,String periodCB,String interval){
        int time=1;
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        if(!period.equals("")){
        	time=Integer.parseInt(period);
        }
        from.add(period(periodCB),-time); // from 5 years ago

        SimpleDateFormat format1 = new SimpleDateFormat("MM-dd-yyyy");
        String fromDate = format1.format(from.getTime());
        String toDate = format1.format(to.getTime());
        System.out.println("Create chart for:"+comp+", from:"+fromDate+" to:"+toDate);
        YahooFinance yahoo = new ChartDataFromYahoo(comp,fromDate,toDate,interval(interval));
        List<ChartData> stocks =(List<ChartData>) yahoo.getData();
        startValue = 10000d;
        endValue = 0d;
        return stocks;
	}

	protected void findStartAndEnd(List<ChartData> stocks, int i){

		   if(startValue>stocks.get(i).getLow().doubleValue()){
	 		   startValue=stocks.get(i).getLow().doubleValue();
	 		   }
	 	   if(endValue<stocks.get(i).getHigh().doubleValue()){
	 		   endValue = stocks.get(i).getHigh().doubleValue();
	 	   }
		}

		protected XYChart.Data setData(List<ChartData> stocks, int i){
			BigDecimal empty = new BigDecimal(0);
			String date = stocks.get(i).getDate();
			BigDecimal closePrice = stocks.get(i).getClose().setScale(2, BigDecimal.ROUND_HALF_EVEN);
			XYChart.Data data= 	new XYChart.Data(date,closePrice);
			if(i>0)data.setNode(new HoverNode(stocks.get(i-1).getClose().setScale(2, BigDecimal.ROUND_HALF_EVEN),stocks.get(i).getClose().setScale(2, BigDecimal.ROUND_HALF_EVEN)));
			if(i==0)data.setNode(new HoverNode(empty,stocks.get(i).getClose().setScale(2, BigDecimal.ROUND_HALF_EVEN)));
			return data;
		}


		public int period(String period){
			int a=Calendar.YEAR;//default value = year;
			if(period!=null){
			switch(period){
			case "Days":return Calendar.DAY_OF_MONTH;
			case "Weeks": return Calendar.WEEK_OF_MONTH;
			case "Months": return Calendar.MONTH;
			case "Years": return Calendar.YEAR;
			}}

			return a ;

		}
		public String interval(String interval){
			String a = "w";//default value weekly
			if(interval!=null){
			switch(interval){
			case "Days":return a="d";
			case "Weeks": return a="w";
			case "Months": return a="m";
			}}

			return a ;
		}
}
