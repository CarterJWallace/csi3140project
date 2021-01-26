import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerUpdateDialog extends JDialog{
	private JTextField nameField = new JTextField(10);
	private JTextField addressField = new JTextField(10);
	private JTextField ssnField = new JTextField(10);
	private JTextField dateField = new JTextField(10);
	
	private JButton updateButton = new JButton("Update");
	private JButton cancelButton = new JButton("Cancel");
	
	public CustomerUpdateDialog (final JFrame owner, final ResultSet rset) {
		super(owner, "Update Customer", true);
		setSize(280, 150);
		setLocationRelativeTo(owner);
		
		try {
			nameField.setText(rset.getString(1));
			addressField.setText(rset.getString(2));
			ssnField.setText(rset.getString(3));
			dateField.setText(rset.getString(4));
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		ssnField.setEditable(false);
		
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(2,2));
		center.add(new JLabel(" Name:"));
		center.add(nameField);
		center.add(new JLabel(" Address:"));
		center.add(addressField);
		center.add(new JLabel(" SSN:"));
		center.add(ssnField);
		center.add(new JLabel(" Role:"));
		center.add(dateField);
		
		JPanel south = new JPanel();
		updateButton.setEnabled(false);
		south.add(updateButton);
		south.add(cancelButton);
		
		nameField.getDocument().addDocumentListener(new InputListener());
		addressField.getDocument().addDocumentListener(new InputListener());
		ssnField.getDocument().addDocumentListener(new InputListener());
		dateField.getDocument().addDocumentListener(new InputListener());

		updateButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent aEvent) {
						CustomerManager ownersDB = ((CustomerFrame)owner).getCustomerManager();
						ownersDB.doInsertQuery(buildQuery());
						dispose();
					}
				});
		cancelButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent aEvent) {
						dispose();
					}
				});
		
		Container contentPane = getContentPane();
		contentPane.add(center,  BorderLayout.CENTER);
		contentPane.add(south, BorderLayout.SOUTH);
		
		pack();
	}
	
	public String buildQuery() {
		String name = nameField.getText().trim().toUpperCase();
		String addr= addressField.getText().trim().toUpperCase();
		String ssn = ssnField.getText().trim().toUpperCase();
		String date = dateField.getText().trim().toUpperCase();
		
		name = name.replace('\'', ' ');
		addr = addr.replace('\'', ' ');
		ssn = ssn.replace('\'', ' ');
		date = date.replace('\'', ' ');
		
		return new String("update \"Lab\".\"Employee\" set \"Full_name\" = '" + name + "', \"Address\" = '" + addr + "', \"Date\" = '" + date + "' where \"Customer_SSN\" = '" + ssn + "'");
		
	}
	
	class InputListener implements DocumentListener{
		public void insertUpdate(DocumentEvent dEvent) {
			if(nameField.getDocument().getLength() > 0 && addressField.getDocument().getLength() > 0 && ssnField.getDocument().getLength() > 0 && dateField.getDocument().getLength() > 0) {
				updateButton.setEnabled(true);
			}
		}
		
		public void removeUpdate(DocumentEvent dEvent) {
			if(nameField.getDocument().getLength() == 0 || addressField.getDocument().getLength() == 0 || ssnField.getDocument().getLength() == 0 || dateField.getDocument().getLength() == 0) {
				updateButton.setEnabled(false);
			}
		}
		
		public void changedUpdate(DocumentEvent dEvent) {}
	}
}