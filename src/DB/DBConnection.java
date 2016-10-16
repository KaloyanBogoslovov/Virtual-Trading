package DB;
import java.sql.*;
public class DBConnection {

		public static final String DBURL = "jdbc:h2:tcp://localhost/~/test";
		public static final String DBUSER = "Kaloyan_Bogoslovov";
		public static final String DBPASS = "qwerty";
		Connection con;

		public void connectingToDB() throws ClassNotFoundException, SQLException{
			Class.forName("org.h2.Driver");
			//con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
			 con = DriverManager.getConnection("jdbc:h2:~/test", "Kaloyan_Bogoslovov", "qwerty" );
		}

		public ResultSet SelectDB(String data) throws SQLException{
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(data);
			return rs;
		}
		public void insertDB(String data) throws SQLException{
			Statement st = con.createStatement();
			st.executeUpdate(data);

		}
		public void closeConnectionToDB() throws SQLException{
			con.close();
		}

}
