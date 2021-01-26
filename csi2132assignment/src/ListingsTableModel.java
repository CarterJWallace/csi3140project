import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.AbstractTableModel;

public class ListingsTableModel extends AbstractTableModel{
	private ResultSet rs;
	
	public ListingsTableModel(ResultSet rs) {
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
		return 3;
	}
	
	public String getColumnName(int column) {
		try {
			String colName = rs.getMetaData().getColumnName(column + 1);
			if(colName.equals("LAST_NAME")) {
				return "Last Name";
			}
			else if(colName.equals("FIRST_NAME")) {
				return "First Name";
			}
			else if(colName.equals("AREA_CODE")) {
				return "Phone Number";
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
			if(column == 2) {
				return "(" + rs.getObject(column + 1) + ") " + rs.getObject(column + 2) + "-" + rs.getObject(column + 3);
			}
			else {
				return rs.getObject(column + 1);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
