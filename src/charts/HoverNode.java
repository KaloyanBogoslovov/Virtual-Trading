package charts;

import java.math.BigDecimal;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

class HoverNode extends StackPane {
  HoverNode(BigDecimal priorValue, BigDecimal value) {
    setPrefSize(6, 6);

    final Label label = createDataThresholdLabel(priorValue, value);

    setOnMouseEntered(e -> {
      getChildren().setAll(label);
      setCursor(Cursor.DEFAULT);
      toFront();

    });
    setOnMouseExited(e -> {

      getChildren().clear();
      setCursor(Cursor.DEFAULT);

    });
  }

  private Label createDataThresholdLabel(BigDecimal priorValue, BigDecimal value) {
    final Label label = new Label(value + "");
    label.setTranslateY(-25);
    label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
    label.setStyle("-fx-font-size: 15; -fx-font-weight: bold;");

    label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
    return label;
  }
}
