package charts;

import java.io.IOException;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class NewChart extends Chart{
	ComboBox<String> periodCB,intervalCB;
	public Stage window= new Stage();
	TextField periodTF = new TextField();
	BorderPane borderPane;
	public TextField companyTF = new TextField();
	public Button adjustButton= new Button("New Chart");
	public String lastCompanyChart="";


	public   NewChart(){
		super();// calls the constructor of the extended class
		window.setTitle("New Chart");
		window.initModality(Modality.APPLICATION_MODAL);
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(20,20,0,20));
		grid.setVgap(8);
		grid.setHgap(10);

		Label companyLabel = new Label("Company:");
		GridPane.setConstraints(companyLabel, 0, 0);


		companyTF.setPromptText("company");
		GridPane.setConstraints(companyTF, 1, 0);

		Label periodLabel = new Label("Period:");
		GridPane.setConstraints(periodLabel, 0, 1);

		HBox hbox = new HBox(10);
		periodCB = new ComboBox<String>();
		periodCB.getItems().addAll("Days","Weeks","Months","Years");
		periodCB.setPromptText("Select a period:");

		periodTF.setPromptText("period");
		periodTF.setPrefWidth(60);
		hbox.getChildren().addAll(periodTF,periodCB);
		GridPane.setConstraints(hbox, 1, 1);

		Label intervalLabel = new Label("Interval:");
		GridPane.setConstraints(intervalLabel, 0, 2);
		intervalCB = new ComboBox<String>();
		intervalCB.getItems().addAll("Days","Weeks","Months");
		intervalCB.setPromptText("Select an interval:");
		GridPane.setConstraints(intervalCB, 1, 2);
		intervalCB.setPrefWidth(194);

		adjustButton.setPrefWidth(250);
		grid.getChildren().addAll(companyLabel,companyTF,periodLabel,hbox,intervalLabel,intervalCB);
		borderPane = new BorderPane();
		borderPane.setTop(grid);
		borderPane.setCenter(adjustButton);
		Scene scene = new Scene(borderPane,290,180);
		window.setScene(scene);
		window.setResizable(false);
		window.getIcons().add(new Image("blue.png"));

	}

	public void chart() throws IOException{
		       lastCompanyChart=companyTF.getText();
		       String period = periodCB.getValue();
		       String interval  = intervalCB.getValue();
		       makeChart(lastCompanyChart,periodTF.getText(),period,interval);
		       setLineChart(lineChart);
		       window.close();
	}

	public LineChart<String,Number> getLineChart() {
		return lineChart;
	}
	public void setLineChart(LineChart<String,Number> line ) {
		lineChart = line;
	}


}
