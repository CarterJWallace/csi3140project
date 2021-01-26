import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class EmployeeFrame extends JFrame {
	private static int WIDTH = 800;
	private static int HEIGHT = 600;
	private EmployeeTableModel employeeModel;
	private JTable table;
	private JScrollPane scrollPane;
	private JTextField nameField = new JTextField(10);
	private JTextField addressField = new JTextField(10);
	private JTextField ssnField = new JTextField(10);
	private JTextField roleField = new JTextField(10);
	
	private String tableName = "Employee";	
	private CustomerManager myDB = new CustomerManager(tableName);
	
	public EmployeeFrame(MainFrame owner) {
		setLocationRelativeTo(owner);
				
		JButton getButton = new JButton("Get");
		JButton add = new JButton ("+");
		JButton rem = new JButton("-");
		JButton edit = new JButton("edit");
		setTitle("Employee Table");
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
        south.add(new JLabel(" Role:"));
        south.add(roleField);
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
        
        add.addActionListener(new EmployeeAddListener(this));
        
        rem.addActionListener(
        		new ActionListener() {
        			public void actionPerformed(ActionEvent aEvent) {
        				try {
        					int selected = table.getSelectedRow();
        					ResultSet rset = myDB.getResultSet();
        					if(selected != -1 && selected < employeeModel.getRowCount()) {
        						rset.absolute(table.getSelectedRow() + 1);
        						rset.deleteRow();
        						table.repaint();
        						table.clearSelection();
        					}
        	            	employeeModel = new EmployeeTableModel(rset);
        				}
        				catch (SQLException e) {
        					e.printStackTrace();
        				}
        			}
        		});
        
        edit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent aEvent) {
    			ResultSet rset = myDB.getResultSet();
    			EmployeeUpdateDialog updateEmployee = new EmployeeUpdateDialog(EmployeeFrame.this, rset);
    			updateEmployee.setVisible(true);
    			employeeModel = new EmployeeTableModel(rset);
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
			
			String name = nameField.getText().trim();
			String addr = addressField.getText().trim();
			String ssn = ssnField.getText().trim().toUpperCase();
			String role = roleField.getText().trim();	
			
            name  = name.replace('\'', ' ');
            addr = addr.replace('\'', ' ');
            ssn = ssn.replace('\'', ' ');
            role = role.replace('\'', ' ');
            
            if(scrollPane != null) {
            	getContentPane().remove(scrollPane);
            }
            if(name.length() > 0 || addr.length() > 0 || ssn.length() > 0 || role.length() > 0) {
            	myDB.doGetQuery(buildQuery(name, addr, ssn, role));
            }
            else {
            	myDB.doGetQuery(buildQuery("","","",""));

            }
        	ResultSet rset = myDB.getResultSet();
        	employeeModel = new EmployeeTableModel(rset);
        	table = new JTable(employeeModel);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            scrollPane = new JScrollPane(table);
            getContentPane().add(scrollPane, BorderLayout.CENTER);
            pack();
            doLayout();
		}
		
		public String buildQuery(String name, String addr, String ssn, String role) {
			if(name.length() == 0 && addr.length() == 0 && ssn.length() == 0 && role.length() == 0) {
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
				whereClause += (" \"Employee_SSN\" = '" + ssn + "'");
			}
			if(role.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" \"Role\" = '" + role + "'");
			}
			return "SELECT * FROM \"Lab\".\"" + tableName + "\"" + whereClause;
		}
		
	}
}