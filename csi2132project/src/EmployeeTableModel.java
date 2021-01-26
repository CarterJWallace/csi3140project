import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.AbstractTableModel;

public class EmployeeTableModel extends AbstractTableModel{
	private ResultSet rs;
	
	public EmployeeTableModel(ResultSet rs) {
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
		return 4;
	}
	
	public String getColumnName(int column) {
		try {
			String colName = rs.getMetaData().getColumnName(column + 1);
			if(colName.equals("Full_name")) {
				return "Full Name";
			}
			else if(colName.equals("Address")) {
				return "Address";
			}
			else if(colName.equals("Employee_SSN")) {
				return "Employee SSN";
			}
			else if(colName.equals("Role")) {
				return "Role";
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
