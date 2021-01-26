import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class EmployeeBookingFrame extends JFrame {
	private static int WIDTH = 800;
	private static int HEIGHT = 600;
	private EmployeeBookingTableModel employeeBookingModel;
	private JTable table;
	private JScrollPane scrollPane;
	private JTextField uidField = new JTextField(3);
	private JTextField dateField = new JTextField(5);
	private JTextField inDateField = new JTextField(5);
	private JTextField outDateField = new JTextField(5);
	private JTextField customerSSNField = new JTextField(5);
	private JTextField customerNameField = new JTextField(5);
	private JTextField roomIDField = new JTextField(3);
	private JTextField hotelIDField = new JTextField(3);
	private String[] statusChoices = {"","Booked","Paid"};
	private JComboBox<String> statusBox = new JComboBox<String>(statusChoices);

	private String tableName = "Booking";	
	private CustomerManager myDB = new CustomerManager(tableName);
	private int uid;
	private MainFrame owner;
	private JButton getButton = new JButton("Get");
	
	public EmployeeBookingFrame(MainFrame owner) {
		setLocationRelativeTo(owner);
		this.owner = owner;
				
		JButton change = new JButton ("Change Status to Paid");
		setTitle("Booking Table");
		setSize(WIDTH, HEIGHT);
		getRootPane().setDefaultButton(getButton);
		
		JPanel south = new JPanel();
		south.setLayout(new FlowLayout(FlowLayout.LEFT));
		south.add(new JLabel("UID:"));
        south.add(uidField);
        south.add(new JLabel(" Date Booked:"));
        south.add(dateField);
        south.add(new JLabel(" Booking Begins:"));
        south.add(inDateField);
        south.add(new JLabel(" Booking Ends:"));
        south.add(outDateField);
        south.add(new JLabel(" Customer SSN:"));
        south.add(customerSSNField);
        south.add(new JLabel(" Customer Name:"));
        south.add(customerNameField);
        south.add(new JLabel(" Room:"));
        south.add(roomIDField);
        south.add(new JLabel(" Hotel ID:"));
        south.add(hotelIDField);
        south.add(new JLabel(" Booking Status:"));
        south.add(statusBox);
        south.add(new JLabel("   "));
        south.add(getButton);
        
        JPanel east = new JPanel();
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        east.setLayout(gb);
        change.setFont(new Font("SansSerif", Font.BOLD, 12));
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gb.setConstraints(change, gbc);
        east.add(change);
        
        Container contentPane = getContentPane();
        contentPane.add(south, BorderLayout.SOUTH);
        contentPane.add(east, BorderLayout.EAST);
        
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent wEvent) {
        		owner.setVisible(true);
        		myDB.close(false);
        	}
        });
        
        change.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent aEvent) {
    			try {
    				int selected = table.getSelectedRow();
    				ResultSet resultSet = myDB.getResultSet();
    				if(selected != -1 && selected < employeeBookingModel.getRowCount()) {
    					resultSet.absolute(table.getSelectedRow()+1);
    					resultSet.updateString(9, "Paid");
    					resultSet.updateRow();
    					table.repaint();
    					table.clearSelection();
    				}
    			}
    			catch(SQLException e) {
    				e.printStackTrace();
    			}
        	}
        });
        
        getButton.addActionListener(new GetListener());
        getButton.doClick();
        uidField.requestFocus();
	}
	
	public CustomerManager getCustomerManager() {
		return myDB;
	}
	
	class GetListener implements ActionListener {
		public void actionPerformed(ActionEvent aEvent) {
			
			String uid = uidField.getText().trim();
			String date = dateField.getText().trim();
			String in = inDateField.getText().trim();
			String out = outDateField.getText().trim();
			String custID = customerSSNField.getText().trim();
			String custName = customerNameField.getText().trim();
			String roomID = roomIDField.getText().trim();
			String hotelID = hotelIDField.getText().trim();
			String status = (String)statusBox.getSelectedItem();
			
			uid = uid.replace('\'', ' ');
			date = date.replace('\'', ' ');
			in = in.replace('\'', ' ');
			out = out.replace('\'', ' ');
			custID = custID.replace('\'', ' ');
			custName = custName.replace('\'', ' ');
			roomID = roomID.replace('\'', ' ');
			hotelID = hotelID.replace('\'', ' ');
            
            if(scrollPane != null) {
            	getContentPane().remove(scrollPane);
            }
            if(uid.length() > 0 || date.length() > 0 || in.length() > 0 || out.length() > 0 || custID.length() > 0 || custName.length() > 0 || roomID.length() > 0 || hotelID.length() > 0) {
            	myDB.doGetQuery(buildQuery(uid, date, in, out, custID, custName, roomID, hotelID, status));
            }
            else {
            	myDB.doGetQuery(buildQuery("","","","","","","","",""));

            }
        	ResultSet rset = myDB.getResultSet();
        	employeeBookingModel = new EmployeeBookingTableModel(rset);
        	table = new JTable(employeeBookingModel);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            scrollPane = new JScrollPane(table);
            getContentPane().add(scrollPane, BorderLayout.CENTER);
            pack();
            doLayout();
		}
		
		public String buildQuery(String uid, String date, String in, String out, String custID, String custName, String roomID, String hotelID, String status) {
			if(uid.length() == 0 && date.length() == 0 && in.length() == 0 && out.length() == 0 && custID.length() == 0 && custName.length() == 0 && roomID.length() == 0 && hotelID.length() == 0 && status.length() == 0) {
				return "SELECT * FROM \"Lab\".\"" + tableName + "\"";
			}
			String whereClause = " WHERE";
			if(uid.length() > 0) {
				whereClause += (" \"Booking_ref\" = '" + uid + "'");
			}
			if(date.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Date_of_booking\" = '" + date + "'");
			}
			if(in.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Date_in\" = '" + in + "'");
			}
			if(out.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Date_out\" = '" + out + "'");
			}
			if(custID.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Customer_SSN\" = '" + custID + "'");
			}
			if(custName.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Customer_name\" = '" + custName + "'");
			}
			if(roomID.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Room_ID\" = '" + roomID + "'");
			}
			if(hotelID.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Hotel_ID\" = '" + hotelID + "'");
			}
			if(status.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Status\" = '" + status + "'");
			}
			return "SELECT * FROM \"Lab\".\"" + tableName + "\"" + whereClause;
		}
		
	}
}