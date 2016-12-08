package database;

import java.sql.*;

public class DBConnection {

  private static final String DBURL = "jdbc:h2:~/test";
  private static final String DBUSER = "Kaloyan_Bogoslovov";
  private static final String DBPASS = "qwerty";
  private Connection connection;

  public void connectingToDB() throws ClassNotFoundException, SQLException {
    Class.forName("org.h2.Driver");
    connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
  }

  public static void createDB() throws ClassNotFoundException, SQLException {
    Class.forName("org.h2.Driver");
    Connection con = DriverManager.getConnection("jdbc:h2:~/test", "Kaloyan_Bogoslovov", "qwerty");
    Statement st = con.createStatement();
    String createUsersTable =
        "CREATE TABLE IF NOT EXISTS USERS(USERNAME VARCHAR2(20) primary key,FIRSTNAME VARCHAR2,lastname varchar2,password varchar2 ,balance number,leverage integer,margin NUMBER,totalprofit NUMBER,equity NUMBER,freemargin number,ordernumber integer,stockexchange varchar2);";
    String createTradesTable =
        "CREATE TABLE IF NOT EXISTS TRADES(USERNAME VARCHAR2(20) references users(username),SYMBOL VARCHAR2(8),ORDERNUMBER INTEGER,TRADETIME DATETIME ,TRADETYPE VARCHAR2(8),VOLUME NUMBER,PURCHASEPRICE NUMBER,CURRENTPRICE NUMBER,PROFIT NUMBER,TRADESTATE VARCHAR2,LASTPRICE NUMBER,COMPANY varchar2,Closetime datetime);";
    st.executeUpdate(createUsersTable);
    st.executeUpdate(createTradesTable);
    con.close();
  }

  public ResultSet SelectDB(String data) throws SQLException {
    Statement st = connection.createStatement();
    ResultSet rs = st.executeQuery(data);
    return rs;
  }

  public void insertDB(String data) throws SQLException {
    Statement st = connection.createStatement();
    st.executeUpdate(data);
  }

  public void closeConnectionToDB() throws SQLException {
    connection.close();
  }

}
