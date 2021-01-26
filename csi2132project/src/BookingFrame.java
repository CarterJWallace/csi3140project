import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class BookingFrame extends JFrame {
	private static int WIDTH = 800;
	private static int HEIGHT = 600;
	private BookingTableModel bookingModel;
	private JTable table;
	private JScrollPane scrollPane;
	private JTextField uidField = new JTextField(3);
	private JTextField priceField = new JTextField(3);
	private JTextField amenitiesField = new JTextField(10);
	private String[] capacityChoices = {"","Single","Double","Queen","King","Party"};
	private JComboBox<String> capacityBox = new JComboBox<String>(capacityChoices);
	private String[] viewChoices = {"","City", "Mountain", "Ocean"};
	private JComboBox<String> viewBox = new JComboBox<String>(viewChoices);
	private String[] extendableChoices = {"","false", "true"};
	private JComboBox<String> extendableBox = new JComboBox<String>(extendableChoices);
	private JTextField damageField = new JTextField(10);
	private JTextField hotelField = new JTextField(3);
	private String tableName = "Room";	
	private CustomerManager myDB = new CustomerManager(tableName);
	private int uid;
	private int bookingUID;
	private String[] hotels;
	private String[] customers;
	private MainFrame owner;
	private JButton getButton = new JButton("Get");
	
	public BookingFrame(MainFrame owner) {
		setLocationRelativeTo(owner);
		this.owner = owner;
		
		uid = myDB.doUIDQuery("Room_ID");
		bookingUID = (myDB.doUIDQuery2("Booking_ref", "Booking")) + 1;
		hotels = myDB.getKeys("Hotel_ID", "Hotel");
		customers = myDB.getKeys("Customer_SSN", "Customer");
				
		JButton add = new JButton ("Book");
		setTitle("Booking Table");
		setSize(WIDTH, HEIGHT);
		getRootPane().setDefaultButton(getButton);
		
		JPanel south = new JPanel();
		south.setLayout(new FlowLayout(FlowLayout.LEFT));
		south.add(new JLabel("UID:"));
        south.add(uidField);
        south.add(new JLabel(" Price:"));
        south.add(priceField);
        south.add(new JLabel(" Amenities:"));
        south.add(amenitiesField);
        south.add(new JLabel(" Capacity:"));
        south.add(capacityBox);
        south.add(new JLabel(" View Type:"));
        south.add(viewBox);
        south.add(new JLabel(" Extendable?"));
        south.add(extendableBox);
        south.add(new JLabel(" Damage:"));
        south.add(damageField);
        south.add(new JLabel(" Hotel ID:"));
        south.add(hotelField);
        south.add(new JLabel("   "));
        south.add(getButton);
        
        JPanel east = new JPanel();
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        east.setLayout(gb);
        add.setFont(new Font("SansSerif", Font.BOLD, 12));
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gb.setConstraints(add, gbc);
        east.add(add);
        
        Container contentPane = getContentPane();
        contentPane.add(south, BorderLayout.SOUTH);
        contentPane.add(east, BorderLayout.EAST);
        
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent wEvent) {
        		owner.setVisible(true);
        		myDB.close(false);
        	}
        });
        
        add.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent aEvent) {
    			ResultSet rset = myDB.getResultSet();
    			BookingAddDialog bookingDialog = new BookingAddDialog(BookingFrame.this, rset, bookingUID);
    			bookingDialog.setVisible(true);
    			bookingModel = new BookingTableModel(rset);
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
			String price = priceField.getText().trim();
			String amen = amenitiesField.getText().trim();
			String cap = ((String)capacityBox.getSelectedItem());
			String view = ((String)viewBox.getSelectedItem());
			String ext = ((String)extendableBox.getSelectedItem());
			String dmg = damageField.getText().trim();
			String hID = hotelField.getText().trim();
			
			uid = uid.replace('\'', ' ');
			price = price.replace('\'', ' ');
			amen = amen.replace('\'', ' ');
			dmg = dmg.replace('\'', ' ');
            
            if(scrollPane != null) {
            	getContentPane().remove(scrollPane);
            }
            if(uid.length() > 0 || price.length() > 0 || amen.length() > 0 || cap.length() > 0 || view.length() > 0 || ext.length() > 0 || dmg.length() > 0 || hID.length() > 0) {
            	myDB.doGetQuery(buildQuery(uid, price, amen, cap, view, ext, dmg, hID));
            }
            else {
            	myDB.doGetQuery(buildQuery("","","","","","","",""));

            }
        	ResultSet rset = myDB.getResultSet();
        	bookingModel = new BookingTableModel(rset);
        	table = new JTable(bookingModel);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            scrollPane = new JScrollPane(table);
            getContentPane().add(scrollPane, BorderLayout.CENTER);
            pack();
            doLayout();
		}
		
		public String buildQuery(String uid, String price, String amen, String cap, String view, String ext, String dmg, String hID) {
			if(uid.length() == 0 && price.length() == 0 && amen.length() == 0 && cap.length() == 0 && view.length() == 0 && ext.length() == 0 && dmg.length() == 0 && hID.length() == 0) {
				return "SELECT * FROM \"Lab\".\"" + tableName + "\"";
			}
			String whereClause = " WHERE";
			if(uid.length() > 0) {
				whereClause += (" \"Room_ID\" = '" + uid + "'");
			}
			if(price.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Price\" = '" + price + "'");
			}
			if(amen.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Amentities\" = '" + amen + "'");
			}
			if(cap.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Capacity\" = '" + cap + "'");
			}
			if(view.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"View\" = '" + view + "'");
			}
			if(ext.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Extendable\" = '" + ext + "'");
			}
			if(dmg.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Damage\" = '" + dmg + "'");
			}
			if(hID.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Hotel_ID\" = '" + hID + "'");
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
	
	public String[] getHotels() {
		return hotels;
	}
	
	public String[] getCustomers() {
		return customers;
	}
	
	public String getCustomerName(String customerUID) {
		return myDB.doCustomerQuery(customerUID);
	}
	
	public MainFrame getOwner() {
		return owner;
	}
	
	public void doClick() {
		getButton.doClick();
	}
}