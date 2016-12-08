package data.updating;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class UpdateTables {

  private static IntegerProperty updateTables = new SimpleIntegerProperty();

  public static IntegerProperty updateTableProperty() {
    return updateTables;
  }

  public static int getUpdateTable() {
    return updateTables.get();
  }


  public static void setUpdateTable(int change) {
    updateTables.set(change);
  }
}
