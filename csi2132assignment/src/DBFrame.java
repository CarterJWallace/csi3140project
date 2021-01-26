import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class DBFrame extends JFrame {
	private static int WIDTH = 577;
	private static int HEIGHT = 466;
	private ListingsTableModel tblModel;
	private JTable table;
	private JScrollPane scrollPane;
	private JTextField lNameField = new JTextField(10);
	private JTextField fNameField = new JTextField(10);
	private JTextField areaCodeField = new JTextField(2);
	private JTextField prefixField = new JTextField(2);
	private JTextField suffixField = new JTextField(3);
	
	private DatabaseManager myDB;
	
	public DBFrame() {
		//String [] info = PasswordDialog.login(this);
		myDB = new DatabaseManager();
		setLocationRelativeTo(null);
		
		JButton getButton = new JButton("Get");
		JButton add = new JButton ("+");
		JButton rem = new JButton("-");
		JLabel space = new JLabel (" ");
		setTitle("DB Frame");
		setSize(WIDTH, HEIGHT);
		getRootPane().setDefaultButton(getButton);
		
		JPanel south = new JPanel();
		south.setLayout(new FlowLayout(FlowLayout.LEFT));
		south.add(new JLabel("Last:"));
        south.add(lNameField);
        south.add(new JLabel(" First:"));
        south.add(fNameField);
        south.add(new JLabel("  Phone:  ("));
        south.add(areaCodeField);
        south.add(new JLabel(") "));
        south.add(prefixField);
        south.add(new JLabel("-"));
        south.add(suffixField);
        south.add(new JLabel("   "));
        south.add(getButton);
        
        JPanel east = new JPanel();
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        east.setLayout(gb);
        add.setFont(new Font("SansSerif", Font.BOLD, 12));
        rem.setFont(new Font("SansSerif", Font.BOLD, 12));
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(add, gbc);
        gb.setConstraints(space, gbc);
        gb.setConstraints(rem, gbc);
        east.add(add);
        east.add(space);
        east.add(rem);
        
        Container contentPane = getContentPane();
        contentPane.add(south, BorderLayout.SOUTH);
        contentPane.add(east, BorderLayout.EAST);
        
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent wEvent) {
        		myDB.close(false);
        	}
        });
        
        areaCodeField.addFocusListener(new PhoneFocusListener());
        areaCodeField.getDocument().addDocumentListener(new PhoneDocumentListener(areaCodeField, 3));
        
        prefixField.addFocusListener(new PhoneFocusListener());
        prefixField.getDocument().addDocumentListener(new PhoneDocumentListener(prefixField, 3));
        
        suffixField.addFocusListener(new PhoneFocusListener());
        suffixField.getDocument().addDocumentListener(new PhoneDocumentListener(suffixField, 3));
        
        add.addActionListener(new AddListingListener(this));
        
        rem.addActionListener(
        		new ActionListener() {
        			public void actionPerformed(ActionEvent aEvent) {
        				try {
        					int selected = table.getSelectedRow();
        					ResultSet rset = myDB.getResultSet();
        					if(selected != -1 && selected < tblModel.getRowCount()) {
        						rset.absolute(table.getSelectedRow() + 1);
        						rset.deleteRow();
        						table.repaint();
        						table.clearSelection();
        					}
        	            	tblModel = new ListingsTableModel(rset);
        	            	table = new JTable(tblModel);
        				}
        				catch (SQLException e) {
        					e.printStackTrace();
        				}
        			}
        		});
        
        getButton.addActionListener(new GetListener());
        getButton.doClick();
        lNameField.requestFocus();
	}
	
	public DatabaseManager getDBManager() {
		return myDB;
	}
	
	class GetListener implements ActionListener {
		public void actionPerformed(ActionEvent aEvent) {
			
			String last = lNameField.getText().trim().toUpperCase();
			String first = fNameField.getText().trim().toUpperCase();
			String ac = areaCodeField.getText().trim().toUpperCase();
			String pre = prefixField.getText().trim().toUpperCase();
			String sfx = suffixField.getText().trim().toUpperCase();
			
            last  = last.replace('\'', ' ');
            first = first.replace('\'', ' ');
            ac    = ac.replace('\'', ' ');
            pre   = pre.replace('\'', ' ');
            sfx   = sfx.replace('\'', ' ');
            
            if(scrollPane != null) {
            	getContentPane().remove(scrollPane);
            }
            
            if(last.length() > 0 || first.length() > 0 || ac.length() > 0 || pre.length() > 0 || sfx.length() > 0) {
            	myDB.doGetQuery(buildQuery(last, first, ac, pre, sfx));
            	ResultSet rset = myDB.getResultSet();
            	tblModel = new ListingsTableModel(rset);
            	table = new JTable(tblModel);
            }
            if(aEvent.getModifiers() == 16) {
            	myDB.doGetQuery(buildQuery("","","","",""));
            	ResultSet rset = myDB.getResultSet();
            	tblModel = new ListingsTableModel(rset);
            	table = new JTable(tblModel);
            }
            else {
            	table = new JTable();
            }
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            scrollPane = new JScrollPane(table);
            getContentPane().add(scrollPane, BorderLayout.CENTER);
            pack();
            doLayout();
		}
		
		public String buildQuery(String last, String first, String ac, String pre, String sfx) {
			if(last.length() == 0 && first.length() == 0 && ac.length() == 0 && pre.length() == 0 && sfx.length() == 0) {
				return "select LAST_NAME, FIRST_NAME, AREA_CODE, PREFIX, SUFFIX from public.listings";
			}
			String whereClause = " where";
			if(last.length() > 0) {
				whereClause += (" LAST_NAME = '" + last + "'");
			}
			if(first.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" FIRST_NAME = '" + first + "'");
			}
			if(ac.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" AREA_CODE = '" + ac + "'");
			}
			if(pre.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" PREFIX = '" + pre + "'");
			}
			if(sfx.length() > 0) {
				if(whereClause.length() > 6) {
					whereClause += " AND";
				}
				whereClause += (" SUFFIX = '" + sfx + "'");
			}
			
			return "select LAST_NAME, FIRST_NAME, AREA_CODE, PREFIX, SUFFIX from Listings" + whereClause;
		}
	}
}
