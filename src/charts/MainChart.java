package charts;

import java.io.IOException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;


public class MainChart extends Chart{
	public TextField periodTF;
	public ComboBox<String> periodCB, intervalCB;
	public HBox centerHbox = new HBox(10);
	public Button adjustChartButton;
	public MainChart(){

		Label periodLabel = new Label("Period:");
		periodTF = new TextField();
		periodTF.setPrefWidth(50);
		periodTF.setText("1");
		periodCB = new ComboBox<String>();
		periodCB.getItems().addAll("Days","Weeks","Months","Years");
		periodCB.setPromptText("Select a period:");
		periodCB.setValue("Years");
		Label intervalLabel = new Label("Interval:");
		intervalCB = new ComboBox<String>();
		intervalCB.getItems().addAll("Days","Weeks","Months");
		intervalCB.setPromptText("Select an interval:");
		intervalCB.setValue("Weeks");
		adjustChartButton = new Button("Adjust Chart");

		centerHbox.getChildren().addAll(periodLabel,periodTF,periodCB,intervalLabel,intervalCB,adjustChartButton);
		centerHbox.setAlignment(Pos.BASELINE_CENTER);
		centerHbox.setPadding(new Insets(0,0,10,0));
		centerHbox.setVisible(false);
	}


	public LineChart<String,Number> drawChart(String comp) throws IOException{
	   String period = periodCB.getValue();
       String interval  = intervalCB.getValue();
       makeChart(comp,periodTF.getText(),period,interval);
       centerHbox.setVisible(true);
       return lineChart;
	}


}
