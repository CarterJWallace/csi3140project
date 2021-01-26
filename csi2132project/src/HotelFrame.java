import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class HotelFrame extends JFrame {
	private static int WIDTH = 800;
	private static int HEIGHT = 600;
	private HotelTableModel hotelModel;
	private JTable table;
	private JScrollPane scrollPane;
	private JTextField uidField = new JTextField(3);
	private JTextField locationField = new JTextField(4);
	private String[] starChoices = {"", "0","1","2","3","4","5"};
	private JComboBox<String> starsBox = new JComboBox<String>(starChoices);
	private JTextField totalRoomsField = new JTextField(3);
	private JTextField avalRoomsField = new JTextField(3);
	private JTextField addressField = new JTextField(10);
	private JTextField emailField = new JTextField(4);
	private JTextField phoneField = new JTextField(4);
	private JTextField employeeField = new JTextField(4);
	private JTextField chainField = new JTextField(1);
	private String tableName = "Hotel";	
	private CustomerManager myDB = new CustomerManager(tableName);
	private int uid;
	private String[] hotelChains;
	private String[] employeeSSNs;
	private JButton getButton = new JButton("Get");
	
	public HotelFrame(MainFrame owner) {
		setLocationRelativeTo(owner);
		
		uid = myDB.doUIDQuery("Hotel_ID");
		hotelChains = myDB.getKeys("Chain_ID", "Hotel_Chain");
		employeeSSNs = myDB.getKeys("Employee_SSN", "Employee");
				
		JButton add = new JButton ("+");
		JButton rem = new JButton("-");
		JButton edit = new JButton("edit");
		setTitle("Hotel Table");
		setSize(WIDTH, HEIGHT);
		getRootPane().setDefaultButton(getButton);
		
		JPanel south = new JPanel();
		south.setLayout(new FlowLayout(FlowLayout.LEFT));
		south.add(new JLabel("UID:"));
        south.add(uidField);
        south.add(new JLabel(" Location:"));
        south.add(locationField);
        south.add(new JLabel(" Rating:"));
        south.add(starsBox);
        south.add(new JLabel(" Total Rooms:"));
        south.add(totalRoomsField);
        south.add(new JLabel(" Available Rooms:"));
        south.add(avalRoomsField);
        south.add(new JLabel(" Address:"));
        south.add(addressField);
        south.add(new JLabel(" Contact Email:"));
        south.add(emailField);
        south.add(new JLabel(" Contact Phone"));
        south.add(phoneField);
        south.add(new JLabel(" Contact ID"));
        south.add(employeeField);
        south.add(new JLabel(" Chain ID"));
        south.add(chainField);
        south.add(new JLabel("   "));
        south.add(getButton);
        
        JPanel east = new JPanel();
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        east.setLayout(gb);
        add.setFont(new Font("SansSerif", Font.BOLD, 12));
        rem.setFont(new Font("SansSerif", Font.BOLD, 12));
        edit.setFont(new Font("SansSerif", Font.BOLD, 12));
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gb.setConstraints(add, gbc);
        gb.setConstraints(rem, gbc);
        gb.setConstraints(edit, gbc);
        east.add(add);
        east.add(rem);
        east.add(edit);
        
        Container contentPane = getContentPane();
        contentPane.add(south, BorderLayout.SOUTH);
        contentPane.add(east, BorderLayout.EAST);
        
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent wEvent) {
        		owner.setVisible(true);
        		myDB.close(false);
        	}
        });
        
        add.addActionListener(new HotelAddListener(this));
        
        rem.addActionListener(
        		new ActionListener() {
        			public void actionPerformed(ActionEvent aEvent) {
        				try {
        					int selected = table.getSelectedRow();
        					ResultSet rset = myDB.getResultSet();
        					if(selected != -1 && selected < hotelModel.getRowCount()) {
        						rset.absolute(table.getSelectedRow() + 1);
        						rset.deleteRow();
        						table.repaint();
        						table.clearSelection();
        					}
        	            	hotelModel = new HotelTableModel(rset);
        				}
        				catch (SQLException e) {
        					e.printStackTrace();
        				}
        			}
        		});
        
        edit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent aEvent) {
    			ResultSet rset = myDB.getResultSet();
    			HotelUpdateDialog updateHotel = new HotelUpdateDialog(HotelFrame.this, rset);
    			updateHotel.setVisible(true);
    			hotelModel = new HotelTableModel(rset);
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
			
			String uid = uidField.getText().trim().toUpperCase();
			String loc = locationField.getText().trim();
			String stars = (String)starsBox.getSelectedItem();
			String totRoom = totalRoomsField.getText().trim().toUpperCase();
			String avalRoom = avalRoomsField.getText().trim().toUpperCase();
			String addr = addressField.getText().trim().toUpperCase();
			String email = emailField.getText().trim().toUpperCase();
			String phone = phoneField.getText().trim().toUpperCase();
			String empID = employeeField.getText().trim().toUpperCase();
			String chainID = chainField.getText().trim().toUpperCase();		
			
            uid  = uid.replace('\'', ' ');
            loc = loc.replace('\'', ' ');
            stars = stars.replace('\'', ' ');
            totRoom = totRoom.replace('\'', ' ');
            avalRoom = avalRoom.replace('\'', ' ');
            addr = addr.replace('\'', ' ');
            email = email.replace('\'', ' ');            
            phone = phone.replace('\'', ' ');
            empID = empID.replace('\'', ' ');
            chainID = chainID.replace('\'', ' ');
            
            if(scrollPane != null) {
            	getContentPane().remove(scrollPane);
            }
            if(uid.length() > 0 || loc.length() > 0 || stars.length() > 0 || totRoom.length() > 0 || avalRoom.length() > 0 || addr.length() > 0 || email.length() > 0 || phone.length() > 0 || empID.length() > 0 || chainID.length() > 0) {
            	myDB.doGetQuery(buildQuery(uid, loc, stars, totRoom, avalRoom, addr, email, phone, empID, chainID));
            }
            else {
            	myDB.doGetQuery(buildQuery("","","","","","","","","",""));

            }
        	ResultSet rset = myDB.getResultSet();
        	hotelModel = new HotelTableModel(rset);
        	table = new JTable(hotelModel);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            scrollPane = new JScrollPane(table);
            getContentPane().add(scrollPane, BorderLayout.CENTER);
            pack();
            doLayout();
		}
		
		public String buildQuery(String uid, String loc, String stars, String totRoom, String avalRoom, String addr, String email, String phone, String empID, String chainID) {
			if(uid.length() == 0 && loc.length() == 0 && stars.length() == 0 && totRoom.length() == 0 && avalRoom.length() == 0 && addr.length() == 0 && email.length() == 0 && phone.length() == 0 && empID.length() == 0 && chainID.length() == 0) {
				return "SELECT * FROM \"Lab\".\"" + tableName + "\"";
			}
			String whereClause = " WHERE";
			if(uid.length() > 0) {
				whereClause += (" \"Hotel_ID\" = '" + uid + "'");
			}
			if(loc.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"City\" = '" + loc + "'");
			}
			if(stars.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Stars\" = '" + stars + "'");
			}
			if(totRoom.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Number_of_rooms\" = '" + totRoom + "'");
			}
			if(avalRoom.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Available_Rooms\" = '" + avalRoom + "'");
			}
			if(addr.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Address\" = '" + addr + "'");
			}
			if(email.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Email\" = '" + email + "'");
			}
			if(phone.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Phone\" = '" + phone + "'");
			}
			if(empID.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Employee_SSN\" = '" + empID + "'");
			}
			if(chainID.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Chain_ID\" = '" + chainID + "'");
			}
			return "SELECT * FROM \"Lab\".\"" + tableName + "\"" + whereClause;
		}
		
	}
	
	public int getUID() {
		return uid;
	}
	
	public void refreshUID() {
		uid = myDB.doUIDQuery("Hotel_ID");
	}
	
	public String[] getHotelChains() {
		return hotelChains;
	}
	
	public String[] getEmployeeSSNs() {
		return employeeSSNs;
	}
	
	public void doClick() {
		getButton.doClick();
	}
}