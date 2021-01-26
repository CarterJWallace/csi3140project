import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.AbstractTableModel;

public class BookingTableModel extends AbstractTableModel{
	private ResultSet rs;
	
	public BookingTableModel(ResultSet rs) {
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
		return 8;
	}
	
	public String getColumnName(int column) {
		try {
			String colName = rs.getMetaData().getColumnName(column + 1);
			if(colName.equals("UID")) {
				return "ID Number";
			}
			else if(colName.equals("Price")) {
				return "Price";
			}
			else if(colName.equals("Amentities")) {
				return "Amenities";
			}
			else if(colName.equals("View")) {
				return "View";
			}
			else if(colName.equals("Extendable")) {
				return "Extendable?";
			}
			else if(colName.equals("Damage")) {
				return "Damage";
			}
			else if(colName.equals("Hotel_ID")) {
				return "Hotel ID";
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
