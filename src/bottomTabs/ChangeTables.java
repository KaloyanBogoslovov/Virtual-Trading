package bottomTabs;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ChangeTables {

	private static  IntegerProperty changeTable = new SimpleIntegerProperty();
	public IntegerProperty changeTableProperty(){
		return changeTable;
	}

	public int getChangeTable() {
		return changeTable.get();
	}

	@SuppressWarnings("static-access")
	public void setChangeTable(int change) {
		this.changeTable.set(change);
	}
}
