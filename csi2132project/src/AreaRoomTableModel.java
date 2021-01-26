import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.AbstractTableModel;

public class AreaRoomTableModel extends AbstractTableModel{
	private ResultSet rs;

	public AreaRoomTableModel(ResultSet rs){
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
		return 2;
	}
	
	public String getColumnName(int column) {
		try {
			String colName = rs.getMetaData().getColumnName(column + 1);
			if(colName.equals("City")) {
				return "Location";
			}
			else if(colName.equals("sum")) {
				return "Available Rooms in Area";
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
