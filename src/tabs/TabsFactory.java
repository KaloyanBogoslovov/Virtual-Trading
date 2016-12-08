package tabs;

import tabs.bottom.BalanceTab;
import tabs.bottom.HistoryTab;
import tabs.bottom.TradeTab;
import tabs.left.SymbolsTab;

public class TabsFactory {

  public static Tabs createElement(int e){
    Tabs element=null;
    switch(e){
      case 0: return element = new BalanceTab();
      case 1: return element = new TradeTab();
      case 2: return element = new HistoryTab();
      case 3: return element = new SymbolsTab();
    }
        return element;
}

}
