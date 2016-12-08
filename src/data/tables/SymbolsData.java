package data.tables;

public class SymbolsData {
  String symbol;
  String ask;
  String bid;


  public SymbolsData() {
    symbol = "";
    ask = "";
    bid = "";

  }

  public SymbolsData(String s, String askk, String bidd) {
    symbol = s;
    ask = askk;
    bid = bidd;

  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public String getAsk() {
    return ask;
  }

  public void setAsk(String ask) {
    this.ask = ask;
  }

  public String getBid() {
    return bid;
  }

  public void setBid(String bid) {
    this.bid = bid;
  }
}
