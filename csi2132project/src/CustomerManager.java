import java.sql.*;
import java.util.ArrayList;

public class CustomerManager {
	private Connection conn;
	private Statement stmt;
	private ResultSet rset;
	private String tableName;
	
	static final String DB_URL = "jdbc:postgresql://web0.site.uottawa.ca:15432/aman070";
	static final String USER = "aman070";
	static final String PASS = "Mansw12010-";
	
	public CustomerManager(String tableName) {
		try {
			Class.forName("org.postgresql.Driver");
		}
		catch (ClassNotFoundException e) {
			System.out.println("Failed to load driver JDBC Driver.");
			e.printStackTrace();
			return;
		}
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			DatabaseMetaData aboutDB = conn.getMetaData();
			String [] tableType = {"TABLE"};
			rset = aboutDB.getTables(null, null, tableName, null);
			if(!inspectForTable(rset, tableName)) {
				System.out.println("Table not found.");
			}
			this.tableName = tableName;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private boolean inspectForTable (ResultSet rs, String tableName) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int numCols = rsmd.getColumnCount();
		
		boolean more = rs.next();
		while(more) {
			for(int i = 1; i <= numCols; i++) {
				if(rsmd.getColumnLabel(i).equals("table_name")){
					if(rs.getString(i).equals(tableName)) {
						System.out.println("Found one that equals " + rs.getString(i));
						return true;
					}
				}
			}
			System.out.println("Did not find");
			more = rs.next();
		}
		return false;
	}
	
	public void doGetQuery(String query) {
		try {
			rset = stmt.executeQuery(query);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int doUIDQuery(String uidColName) {
		try {
			ResultSet uid = stmt.executeQuery("SELECT * FROM \"Lab\".\"" + tableName + "\" ORDER BY \"" + uidColName + "\" DESC LIMIT 1");
			uid.next();
			return uid.getInt(1);
		}
		catch(SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public int doUIDQuery2(String uidColName, String uidTblName) {
		try {
			ResultSet uid = stmt.executeQuery("SELECT * FROM \"Lab\".\"" + uidTblName + "\" ORDER BY \"" + uidColName + "\" DESC LIMIT 1");
			uid.next();
			return uid.getInt(1);
		}
		catch(SQLException e) {
			return 0;
		}
	}
	
	public String doCustomerQuery(String custUID) {
		try {
			ResultSet name = stmt.executeQuery("SELECT * FROM \"Lab\".\"Customer\" WHERE \"Customer_SSN\" = '" + custUID + "';");
			name.next();
			return name.getString(1);
		}
		catch(SQLException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String[] getKeys (String keyColName, String keyTblName){
		ArrayList<String> output = new ArrayList<String>();
		try {
			ResultSet test = stmt.executeQuery("SELECT \"" + keyColName + "\" FROM \"Lab\".\"" + keyTblName + "\"");
			while(test.next()) {
				output.add(test.getString(1));
			}
			return output.toArray(new String[0]);
		}
		catch(SQLException e) {
			e.printStackTrace();
			return output.toArray(new String[0]);
		}
	}
	
	public void doInsertQuery(String query) {
		try {
			stmt.executeUpdate(query);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void doUpdateQuery(String query) {
		try {
			stmt.execute(query);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet getResultSet() {
		return rset;
	}
	
	public void close(boolean remove) {
		try {
			if(remove) {
				stmt.execute("drop table " + tableName + ";");
			}
			rset.close();
			stmt.close();
			conn.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
