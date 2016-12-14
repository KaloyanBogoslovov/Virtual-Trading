package charts;

import java.io.IOException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;



public class MainChart extends Chart {
  private static TextField periodTF;
  private static ComboBox<String> periodCB, intervalCB;
  public static HBox centerHbox = new HBox(10);

  public MainChart(){
  }

  public MainChart(BorderPane centerBorderPane) {
    Label periodLabel = new Label("Period:");
    periodTF = new TextField();
    periodTF.setPrefWidth(50);
    periodTF.setText("1");
    periodCB = new ComboBox<String>();
    periodCB.getItems().addAll("Days", "Weeks", "Months", "Years");
    periodCB.setPromptText("Select a period:");
    periodCB.setValue("Years");
    Label intervalLabel = new Label("Interval:");
    intervalCB = new ComboBox<String>();
    intervalCB.getItems().addAll("Days", "Weeks", "Months");
    intervalCB.setPromptText("Select an interval:");
    intervalCB.setValue("Weeks");
    Button adjustChartButton = new Button("Adjust Chart");

    centerHbox.getChildren().addAll(periodLabel, periodTF, periodCB, intervalLabel, intervalCB,
        adjustChartButton);
    centerHbox.setAlignment(Pos.BASELINE_CENTER);
    centerHbox.setPadding(new Insets(0, 0, 10, 0));
    centerHbox.setVisible(false);

    adjustChartButton.setOnAction(e -> {
      try {
        centerBorderPane.setCenter(drawMainChart(NewChart.lastCompanyChart));
      } catch (Exception e1) {
        System.out.println("adjustChartButton throws unlucky exception" + e1.getStackTrace());
      }
    });
  }

  public LineChart<String, Number> drawMainChart(String company) throws IOException {
    String period = periodCB.getValue();
    String interval = intervalCB.getValue();
    makeChart(company, periodTF.getText(), period, interval);
    centerHbox.setVisible(true);
    return lineChart;
  }

}
