package about;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class About {

  public About() {
    Stage window = new Stage();
    window.setTitle("About");

    Label author = new Label("Creator: Kaloyan Evgeniev Bogoslovov");
    Label title = new Label("Virtual Trading 1.0");
    Image image = new Image("blue.png");

    ImageView imageView = new ImageView();
    imageView.setImage(image);

    VBox vbox = new VBox(10);
    vbox.getChildren().addAll(title, author, imageView);
    vbox.setAlignment(Pos.CENTER);

    window.getIcons().add(new Image("blue.png"));
    Scene scene = new Scene(vbox, 220, 220);
    window.setScene(scene);
    window.show();
  }

}
