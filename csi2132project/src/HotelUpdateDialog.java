import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HotelUpdateDialog extends JDialog{
	private JTextField uidField = new JTextField(3);
	private JTextField locationField = new JTextField(3);
	private String[] starChoices = {"0","1","2","3","4","5"};
	private JComboBox<String> starsBox = new JComboBox<String>(starChoices);
	private JTextField totalRoomsField = new JTextField(3);
	private JTextField avalRoomsField = new JTextField(3);
	private JTextField addressField = new JTextField(10);
	private JTextField emailField = new JTextField(4);
	private JTextField phoneField = new JTextField(4);
	private String[] employeesChoices;
	private JComboBox<String> employeesBox;
	private String[] chainsChoices;
	private JComboBox<String> chainsBox;
	private JButton updateButton = new JButton("Update");
	private JButton cancelButton = new JButton("Cancel");
	
	public HotelUpdateDialog (HotelFrame owner, final ResultSet rset) {
		super(owner, "Update Hotel", true);
		setSize(280, 150);
		setLocationRelativeTo(owner);
		
		uidField.setEditable(false);
		avalRoomsField.setEditable(false);
		
		chainsChoices = owner.getHotelChains();
		employeesChoices = owner.getEmployeeSSNs();
		
		employeesBox = new JComboBox<String>(employeesChoices);
		chainsBox = new JComboBox<String>(chainsChoices);
		
		try {
			uidField.setText(rset.getString(1));
			locationField.setText(rset.getString(2));
			starsBox.setSelectedItem(rset.getString(3));
			totalRoomsField.setText(rset.getString(4));
			avalRoomsField.setText(rset.getString(5));
			addressField.setText(rset.getString(6));
			emailField.setText(rset.getString(7));
			phoneField.setText(rset.getString(8));
			if(rset.getString(9) != null) {
				employeesBox.setSelectedItem(rset.getString(9));
			}
			if(rset.getString(10) != null) {
				chainsBox.setSelectedItem(rset.getString(10));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(5,2));
		center.add(new JLabel(" UID:"));
		center.add(uidField);
		center.add(new JLabel(" Location:"));
		center.add(locationField);
		center.add(new JLabel(" Stars:"));
		center.add(starsBox);
		center.add(new JLabel(" Total Rooms:"));
		center.add(totalRoomsField);
		center.add(new JLabel(" Available Rooms:"));
		center.add(avalRoomsField);
		center.add(new JLabel(" Address:"));
		center.add(addressField);
		center.add(new JLabel(" Contact Email:"));
		center.add(emailField);
		center.add(new JLabel(" Contact Phone:"));
		center.add(phoneField);
		center.add(new JLabel(" Manager SSN:"));
		center.add(employeesBox);
		center.add(new JLabel(" Chain ID:"));
		center.add(chainsBox);
		
		JPanel south = new JPanel();
		updateButton.setEnabled(false);
		south.add(updateButton);
		south.add(cancelButton);
		
		uidField.getDocument().addDocumentListener(new InputListener());
		locationField.getDocument().addDocumentListener(new InputListener());
		totalRoomsField.getDocument().addDocumentListener(new InputListener());
		avalRoomsField.getDocument().addDocumentListener(new InputListener());
		addressField.getDocument().addDocumentListener(new InputListener());
		emailField.getDocument().addDocumentListener(new InputListener());
		phoneField.getDocument().addDocumentListener(new InputListener());
		
		starsBox.addItemListener(new BoxItemListener());

		updateButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent aEvent) {
						CustomerManager ownersDB = ((HotelFrame)owner).getCustomerManager();
						ownersDB.doInsertQuery(buildQuery());
						dispose();
					}
				});
		cancelButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent aEvent) {
						owner.doClick();
						dispose();
					}
				});
		
		Container contentPane = getContentPane();
		contentPane.add(center,  BorderLayout.CENTER);
		contentPane.add(south, BorderLayout.SOUTH);
		
		pack();
	}
	
	public String buildQuery() {
		String uid = uidField.getText().trim().toUpperCase();
		String loc = locationField.getText().trim();
		String stars = (String)starsBox.getSelectedItem();
		String totRoom = totalRoomsField.getText().trim().toUpperCase();
		String avalRoom = avalRoomsField.getText().trim().toUpperCase();
		String addr = addressField.getText().trim().toUpperCase();
		String email = emailField.getText().trim().toUpperCase();
		String phone = phoneField.getText().trim().toUpperCase();
		String empID = (String)employeesBox.getSelectedItem();
		String chainID = (String)chainsBox.getSelectedItem();
		
		uid = uid.replace('\'', ' ');
		loc = loc.replace('\'', ' ');
		totRoom = totRoom.replace('\'', ' ');
		avalRoom = avalRoom.replace('\'', ' ');
		addr = addr.replace('\'', ' ');
		email = email.replace('\'', ' ');
		phone = phone.replace('\'', ' ');
		
		return new String("update \"Lab\".\"Hotel\" set \"City\" = '" + loc + "', \"Stars\" = '" + stars + "', \"Number_of_rooms\" = '" + totRoom + "', \"Address\" = '" + addr + "', \"Email\" = '" + email + "', \"Phone\" = '" + phone +"', \"Employee_SSN\" ='" + empID + "', \"Chain_ID\" = '" + chainID + "' where \"Hotel_ID\" = '" + uid + "'");
		
	}
	
	class BoxItemListener implements ItemListener{
		public void itemStateChanged(ItemEvent e) {
			updateButton.setEnabled(true);
		}
	}
	
	class InputListener implements DocumentListener{
		public void insertUpdate(DocumentEvent dEvent) {
			if(uidField.getDocument().getLength() > 0 && locationField.getDocument().getLength() > 0 && totalRoomsField.getDocument().getLength() > 0 && addressField.getDocument().getLength() > 0 && emailField.getDocument().getLength() > 0  && phoneField.getDocument().getLength() > 0) {
				updateButton.setEnabled(true);
			}
		}
		
		public void removeUpdate(DocumentEvent dEvent) {
			if(uidField.getDocument().getLength() == 0 || locationField.getDocument().getLength() == 0 || totalRoomsField.getDocument().getLength() == 0 || addressField.getDocument().getLength() == 0 || emailField.getDocument().getLength() == 0 || phoneField.getDocument().getLength() == 0) {
				updateButton.setEnabled(false);
			}
		}
		
		public void changedUpdate(DocumentEvent dEvent) {}
	}
}