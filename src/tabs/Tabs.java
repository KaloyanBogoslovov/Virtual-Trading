package tabs;

import javafx.scene.control.TableView;

public interface Tabs {
  public TableView<Object> getTable();
  public void initTableContent();

}
