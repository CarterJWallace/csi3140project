import java.sql.*;
public class DatabaseManager {
	private Connection conn;
	private Statement stmt;
	private ResultSet rset;
	
	static final String DB_URL = "";
	static final String USER = "";
	static final String PASS = "";
	
	public DatabaseManager () {
		try {
			Class.forName("org.postgresql.Driver");
		}
		catch (ClassNotFoundException e){
			System.out.println("Failed to load JDBC/ODBC driver.");
			e.printStackTrace();
			return;
		}
		
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			DatabaseMetaData aboutDB = conn.getMetaData();
			String [] tableType = {"TABLE"};
			rset = aboutDB.getTables(null, null, "listings", tableType);
			if(!inspectForTable(rset, "listings")) {
				String [] SQL = initListingsTable();
				for (int i = 0; i < SQL.length; i++) {
					stmt.execute(SQL[i]);
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String [] initListingsTable() {
		String[] SQL = {
				"create table Listings (" +
				"LAST_NAME varchar (16)," +
				"FIRST_NAME varchar (16)," +
				"AREA_CODE varchar (3)," +
				"PREFIX varchar (3)," +
				"SUFFIX varchar (4))",
	            "insert into Listings values ('ANDERSON', 'JOHN',  '314', '825', '1695')",
	            "insert into Listings values ('CABLES',   'WALLY', '212', '434', '9685')",
	            "insert into Listings values ('FRY',      'EDGAR', '415', '542', '5885')",
	            "insert into Listings values ('MARTIN',   'EDGAR', '665', '662', '9001')",
	            "insert into Listings values ('TUCKER',   'JOHN',  '707', '696', '8541')",
		};
		return SQL;
	}
	
	private boolean inspectForTable (ResultSet rs, String tableName) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int numCols = rsmd.getColumnCount();
		
		boolean more = rs.next();
		while(more) {
			for(int i = 1; i <= numCols; i++) {
				//System.out.println(rsmd.getColumnLabel(i));
				if(rsmd.getColumnLabel(i).equals("table_name")){
					//System.out.println(rs.getString(i));
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
	
	public void doInsertQuery(String query) {
		try {
			stmt.executeUpdate(query);
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
				stmt.execute("drop table Listings;");
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

