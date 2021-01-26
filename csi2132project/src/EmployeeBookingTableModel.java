import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.AbstractTableModel;

public class EmployeeBookingTableModel extends AbstractTableModel{
	private ResultSet rs;
	
	public EmployeeBookingTableModel(ResultSet rs) {
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
		return 9;
	}
	
	public String getColumnName(int column) {
		try {
			String colName = rs.getMetaData().getColumnName(column + 1);
			if(colName.equals("Booking_ref")) {
				return "Booking ID";
			}
			else if(colName.equals("Date_of_booking")) {
				return "Date Booked";
			}
			else if(colName.equals("Date_in")) {
				return "Booking Begins";
			}
			else if(colName.equals("Date_out")) {
				return "Booking Ends";
			}
			else if(colName.equals("Customer_SSN")) {
				return "Customer SSN";
			}
			else if(colName.equals("Customer_name")) {
				return "Customer Name";
			}
			else if(colName.equals("Room_ID")) {
				return "Room ID";
			}
			else if(colName.equals("Hotel_ID")) {
				return "Hotel ID";
			}
			else if(colName.equals("Status")) {
				return "Booking Status";
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
