import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class AreaRoomsFrame extends JFrame {
	private static int WIDTH = 800;
	private static int HEIGHT = 600;
	private AreaRoomTableModel areaRoomTableModel;
	private JTable table;
	private JScrollPane scrollPane;
	private JTextField locationField = new JTextField(5);
	private JTextField roomsField = new JTextField(3);
	private String tableName = "room_in_location";	
	private CustomerManager myDB = new CustomerManager(tableName);
	
	public AreaRoomsFrame(MainFrame owner) {
		setLocationRelativeTo(owner);
				
		JButton getButton = new JButton("Get");
		setTitle("Rooms per Area Table");
		setSize(WIDTH, HEIGHT);
		getRootPane().setDefaultButton(getButton);
		
		JPanel south = new JPanel();
		south.setLayout(new FlowLayout(FlowLayout.LEFT));
		south.add(new JLabel("Location:"));
        south.add(locationField);
        south.add(new JLabel(" Number of Rooms:"));
        south.add(roomsField);
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
        locationField.requestFocus();
	}
	
	public CustomerManager getCustomerManager() {
		return myDB;
	}
	
	class GetListener implements ActionListener {
		public void actionPerformed(ActionEvent aEvent) {
			
			String loc = locationField.getText().trim();
			String rooms = roomsField.getText().trim();
			
			loc = loc.replace('\'', ' ');
			rooms = rooms.replace('\'', ' ');
            
            if(scrollPane != null) {
            	getContentPane().remove(scrollPane);
            }
            if(loc.length() > 0 || rooms.length() > 0) {
            	myDB.doGetQuery(buildQuery(loc, rooms));
            }
            else {
            	myDB.doGetQuery(buildQuery("",""));

            }
        	ResultSet rset = myDB.getResultSet();
        	areaRoomTableModel = new AreaRoomTableModel(rset);
        	table = new JTable(areaRoomTableModel);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            scrollPane = new JScrollPane(table);
            getContentPane().add(scrollPane, BorderLayout.CENTER);
            pack();
            doLayout();
		}
		
		public String buildQuery(String loc, String rooms) {
			if(loc.length() == 0 && rooms.length() == 0) {
				return "SELECT * FROM \"Lab\".\"" + tableName + "\"";
			}
			String whereClause = " WHERE";
			if(loc.length() > 0) {
				whereClause += (" \"City\" = '" + loc + "'");
			}
			if(rooms.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"sum\" = '" + rooms + "'");
			}
			return "SELECT * FROM \"Lab\".\"" + tableName + "\"" + whereClause;
		}
		
	}
}