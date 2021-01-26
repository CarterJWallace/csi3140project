import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class HotelCapacityFrame extends JFrame {
	private static int WIDTH = 800;
	private static int HEIGHT = 600;
	private HotelCapacityTableModel hotelCapacityTableModel;
	private JTable table;
	private JScrollPane scrollPane;
	private JTextField idField = new JTextField(3);
	private JTextField capacityField = new JTextField(3);
	private JTextField locationField = new JTextField(10);
	private JTextField nameField = new JTextField(10);
	private String tableName = "hotel_capacity";	
	private CustomerManager myDB = new CustomerManager(tableName);
	
	public HotelCapacityFrame(MainFrame owner) {
		setLocationRelativeTo(owner);
				
		JButton getButton = new JButton("Get");
		setTitle("Capacity of Hotel Table");
		setSize(WIDTH, HEIGHT);
		getRootPane().setDefaultButton(getButton);
		
		JPanel south = new JPanel();
		south.setLayout(new FlowLayout(FlowLayout.LEFT));
		south.add(new JLabel("Hotel ID:"));
        south.add(idField);
        south.add(new JLabel(" Capacity:"));
        south.add(capacityField);
        south.add(new JLabel(" Location:"));
        south.add(locationField);
        south.add(new JLabel(" Name:"));
        south.add(nameField);
        south.add(new JLabel("   "));
        south.add(getButton);
        
        Container contentPane = getContentPane();
        contentPane.add(south, BorderLayout.SOUTH);
        
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent wEvent) {
        		owner.setVisible(true);
        		myDB.close(false);
        	}
        });
        
        getButton.addActionListener(new GetListener());
        getButton.doClick();
        idField.requestFocus();
	}
	
	public CustomerManager getCustomerManager() {
		return myDB;
	}
	
	class GetListener implements ActionListener {
		public void actionPerformed(ActionEvent aEvent) {
			
			String id = idField.getText().trim();
			String cap = capacityField.getText().trim();
			String loc = locationField.getText().trim();
			String name = nameField.getText().trim();
			
			loc = loc.replace('\'', ' ');
			id = id.replace('\'', ' ');
			cap = cap.replace('\'', ' ');
			name = name.replace('\'', ' ');
            
            if(scrollPane != null) {
            	getContentPane().remove(scrollPane);
            }
            if(id.length() > 0 || cap.length() > 0 || loc.length() > 0 || name.length() > 0) {
            	myDB.doGetQuery(buildQuery(id, cap, loc, name));
            }
            else {
            	myDB.doGetQuery(buildQuery("","","",""));

            }
        	ResultSet rset = myDB.getResultSet();
        	hotelCapacityTableModel = new HotelCapacityTableModel(rset);
        	table = new JTable(hotelCapacityTableModel);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            scrollPane = new JScrollPane(table);
            getContentPane().add(scrollPane, BorderLayout.CENTER);
            pack();
            doLayout();
		}
		
		public String buildQuery(String id, String cap, String loc, String name) {
			if(id.length() == 0 && cap.length() == 0 && loc.length() == 0 && name.length() == 0) {
				return "SELECT * FROM \"Lab\".\"" + tableName + "\"";
			}
			String whereClause = " WHERE";
			if(id.length() > 0) {
				whereClause += (" \"Hotel_ID\" = '" + id + "'");
			}
			if(cap.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Guest Capacity\" = '" + cap + "'");
			}
			if(loc.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"City\" = '" + loc + "'");
			}
			if(name.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Branch_name\" = '" + name + "'");
			}
			return "SELECT * FROM \"Lab\".\"" + tableName + "\"" + whereClause;
		}
		
	}
}