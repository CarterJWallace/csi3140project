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

public class EmployeeAddDialog extends JDialog{
	private JTextField nameField = new JTextField(10);
	private JTextField addressField = new JTextField(10);
	private JTextField ssnField = new JTextField(10);
	private JTextField roleField = new JTextField(10);

	private JButton addButton = new JButton("Add");
	private JButton cancelButton = new JButton("Cancel");
	
	public EmployeeAddDialog (final JFrame owner) {
		super(owner, "Add Employee", true);
		setSize(280, 150);
		setLocationRelativeTo(owner);
		
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(2,2));
		center.add(new JLabel(" Name:"));
		center.add(nameField);
		center.add(new JLabel(" Address:"));
		center.add(addressField);
		center.add(new JLabel(" SSN:"));
		center.add(ssnField);
		center.add(new JLabel(" Role:"));
		center.add(roleField);

		JPanel south = new JPanel();
		addButton.setEnabled(false);
		south.add(addButton);
		south.add(cancelButton);
		
		nameField.getDocument().addDocumentListener(new InputListener());
		ssnField.getDocument().addDocumentListener(new InputListener());
		roleField.getDocument().addDocumentListener(new InputListener());
		addressField.getDocument().addDocumentListener(new InputListener());
		
		addButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent aEvent) {
						CustomerManager ownersDB = ((EmployeeFrame)owner).getCustomerManager();
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
		String addr = addressField.getText().trim().toUpperCase();
		String ssn = ssnField.getText().trim().toUpperCase();
		String role = roleField.getText().trim().toUpperCase();
		
		name = name.replace('\'', ' ');
		addr = addr.replace('\'', ' ');
		ssn = ssn.replace('\'', ' ');
		role = role.replace('\'', ' ');
		
		return new String("insert into \"Lab\".\"Employee\" values ('" + name + "', '" + addr + "', '" + ssn + "', '" + role + "')");
		
	}
	
	class InputListener implements DocumentListener{
		public void insertUpdate(DocumentEvent dEvent) {
			if(nameField.getDocument().getLength() > 0 && addressField.getDocument().getLength() > 0 && ssnField.getDocument().getLength() > 0 && roleField.getDocument().getLength() > 0) {
				addButton.setEnabled(true);
			}
		}
		
		public void removeUpdate(DocumentEvent dEvent) {
			if(nameField.getDocument().getLength() == 0 || addressField.getDocument().getLength() == 0 || ssnField.getDocument().getLength() == 0 || roleField.getDocument().getLength() == 0) {
				addButton.setEnabled(false);
			}
		}
		
		public void changedUpdate(DocumentEvent dEvent) {}
	}
}