import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.AbstractTableModel;

public class HotelTableModel extends AbstractTableModel{
	private ResultSet rs;
	
	public HotelTableModel(ResultSet rs) {
		this.rs = rs;
	}
	
	public int getRowCount() {
		try {
			rs.last();
			return rs.getRow();
		}
		catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public int getColumnCount() {
		return 10;
	}
	
	public String getColumnName(int column) {
		try {
			String colName = rs.getMetaData().getColumnName(column + 1);
			if(colName.equals("Hotel_ID")) {
				return "Hotel ID";
			}
			else if(colName.equals("City")) {
				return "Location";
			}
			else if(colName.equals("Stars")) {
				return "Rating";
			}
			else if(colName.equals("Number_of_rooms")) {
				return "Total Rooms";
			}
			else if(colName.equals("Available_Rooms")) {
				return "Rooms Available";
			}
			else if(colName.equals("Address")) {
				return "Address";
			}
			else if(colName.equals("Email")) {
				return "Contact Email";
			}
			else if(colName.equals("Phone")) {
				return "Contact Phone Number";
			}
			else if(colName.equals("Employee_SSN")) {
				return "Manager Employee SSN";
			}
			else if(colName.equals("Chain_ID")) {
				return "Chain ID";
			}
			else {
				return colName;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public Object getValueAt(int row, int column) {
		try {
			rs.absolute(row + 1);
			return rs.getObject(column + 1);
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}