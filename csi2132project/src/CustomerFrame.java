import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class CustomerFrame extends JFrame {
	private static int WIDTH = 800;
	private static int HEIGHT = 600;
	private CustomerTableModel customerModel;
	private JTable table;
	private JScrollPane scrollPane;
	private JTextField nameField = new JTextField(10);
	private JTextField addressField = new JTextField(10);
	private JTextField ssnField = new JTextField(10);
	private JTextField dateField = new JTextField(10);
	
	private String tableName = "Customer";	
	private CustomerManager myDB = new CustomerManager(tableName);
	
	public CustomerFrame(MainFrame owner) {
		setLocationRelativeTo(owner);
				
		JButton getButton = new JButton("Get");
		JButton add = new JButton ("+");
		JButton rem = new JButton("-");
		JButton edit = new JButton("edit");
		setTitle("Customer Table");
		setSize(WIDTH, HEIGHT);
		getRootPane().setDefaultButton(getButton);
		
		JPanel south = new JPanel();
		south.setLayout(new FlowLayout(FlowLayout.LEFT));
		south.add(new JLabel("Name:"));
        south.add(nameField);
        south.add(new JLabel(" Address:"));
        south.add(addressField);
        south.add(new JLabel(" SSN:"));
        south.add(ssnField);
        south.add(new JLabel(" Date Joined:"));
        south.add(dateField);
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
        
        add.addActionListener(new CustomerAddListener(this));
        
        rem.addActionListener(
        		new ActionListener() {
        			public void actionPerformed(ActionEvent aEvent) {
        				try {
        					int selected = table.getSelectedRow();
        					ResultSet rset = myDB.getResultSet();
        					if(selected != -1 && selected < customerModel.getRowCount()) {
        						rset.absolute(table.getSelectedRow() + 1);
        						rset.deleteRow();
        						table.repaint();
        						table.clearSelection();
        					}
        	            	customerModel = new CustomerTableModel(rset);
        				}
        				catch (SQLException e) {
        					e.printStackTrace();
        				}
        			}
        		});
        
        edit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent aEvent) {
    			ResultSet rset = myDB.getResultSet();
    			CustomerUpdateDialog updateEmployee = new CustomerUpdateDialog(CustomerFrame.this, rset);
    			updateEmployee.setVisible(true);
    			customerModel = new CustomerTableModel(rset);
        	}
        });
        
        getButton.addActionListener(new GetListener());
        getButton.doClick();
        nameField.requestFocus();
	}
	
	public CustomerManager getCustomerManager() {
		return myDB;
	}
	
	class GetListener implements ActionListener {
		public void actionPerformed(ActionEvent aEvent) {
			
			String name = nameField.getText().trim().toUpperCase();
			String addr = addressField.getText().trim().toUpperCase();
			String ssn = ssnField.getText().trim().toUpperCase();
			String date = dateField.getText().trim();	
			
            name  = name.replace('\'', ' ');
            addr = addr.replace('\'', ' ');
            ssn = ssn.replace('\'', ' ');
            date = date.replace('\'', ' ');
            
            if(scrollPane != null) {
            	getContentPane().remove(scrollPane);
            }
            if(name.length() > 0 || addr.length() > 0 || ssn.length() > 0 || date.length() > 0) {
            	myDB.doGetQuery(buildQuery(name, addr, ssn, date));
            }
            else {
            	myDB.doGetQuery(buildQuery("","","",""));

            }
        	ResultSet rset = myDB.getResultSet();
        	customerModel = new CustomerTableModel(rset);
        	table = new JTable(customerModel);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            scrollPane = new JScrollPane(table);
            getContentPane().add(scrollPane, BorderLayout.CENTER);
            pack();
            doLayout();
		}
		
		public String buildQuery(String name, String addr, String ssn, String date) {
			if(name.length() == 0 && addr.length() == 0 && ssn.length() == 0 && date.length() == 0) {
				return "SELECT * FROM \"Lab\".\"" + tableName + "\"";
			}
			String whereClause = " WHERE";
			if(name.length() > 0) {
				whereClause += (" \"Full_name\" = '" + name + "'");
			}
			if(addr.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Address\" = '" + addr + "'");
			}
			if(ssn.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Customer_SSN\" = '" + ssn + "'");
			}
			if(date.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Date\" = '" + date + "'");
			}
			return "SELECT * FROM \"Lab\".\"" + tableName + "\"" + whereClause;
		}
		
	}
}