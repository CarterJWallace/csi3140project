import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.AbstractTableModel;

public class HotelCapacityTableModel extends AbstractTableModel{
	private ResultSet rs;

	public HotelCapacityTableModel(ResultSet rs){
		this.rs = rs;
	}
	
	public int getRowCount() {
		try {
			rs.last();
			return rs.getRow();
		}
		catch(SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public int getColumnCount() {
		return 4;
	}
	
	public String getColumnName(int column) {
		try {
			String colName = rs.getMetaData().getColumnName(column + 1);
			if(colName.equals("Hotel_ID")) {
				return "Hotel ID";
			}
			else if(colName.equals("Guest Capacity")) {
				return "Total Capacity";
			}
			else if(colName.equals("City")) {
				return "Location";
			}
			else if(colName.equals("Branch_name")) {
				return "Name";
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
