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
import javax.swing.JComboBox;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class HotelAddDialog extends JDialog{
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
	private JButton addButton = new JButton("Add");
	private JButton cancelButton = new JButton("Cancel");
	
	public HotelAddDialog (HotelFrame owner) {
		super(owner, "Add Hotel", true);
		setSize(280, 150);
		setLocationRelativeTo(owner);
		
		uidField.setEditable(false);
		uidField.setText(Integer.toString(owner.getUID() + 1));
		avalRoomsField.setEditable(false);
		
		chainsChoices = owner.getHotelChains();
		employeesChoices = owner.getEmployeeSSNs();
		
		employeesBox = new JComboBox<String>(employeesChoices);
		chainsBox = new JComboBox<String>(chainsChoices);
		
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
		center.add(new JLabel(" Employee SIN:"));
		center.add(employeesBox);
		center.add(new JLabel(" Chain ID:"));
		center.add(chainsBox);
		
		JPanel south = new JPanel();
		addButton.setEnabled(false);
		south.add(addButton);
		south.add(cancelButton);
		
		uidField.getDocument().addDocumentListener(new InputListener());
		locationField.getDocument().addDocumentListener(new InputListener());
		totalRoomsField.getDocument().addDocumentListener(new InputListener());
		avalRoomsField.getDocument().addDocumentListener(new InputListener());
		addressField.getDocument().addDocumentListener(new InputListener());
		emailField.getDocument().addDocumentListener(new InputListener());
		phoneField.getDocument().addDocumentListener(new InputListener());
		
		addButton.addActionListener(
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
		
		return new String("insert into \"Lab\".\"Hotel\" values ('" + uid + "', '" + loc + "', '" + stars + "', '" + totRoom + "', '" + "-1" + "', '" + addr + "', '" + email + "', '" + phone + "', '" + empID + "', '" + chainID + "')");
		
	}
	
	class InputListener implements DocumentListener{
		public void insertUpdate(DocumentEvent dEvent) {
			if(uidField.getDocument().getLength() > 0 && locationField.getDocument().getLength() > 0 && totalRoomsField.getDocument().getLength() > 0 && addressField.getDocument().getLength() > 0 && emailField.getDocument().getLength() > 0  && phoneField.getDocument().getLength() > 0) {
				addButton.setEnabled(true);
			}
		}
		
		public void removeUpdate(DocumentEvent dEvent) {
			if(uidField.getDocument().getLength() == 0 || locationField.getDocument().getLength() == 0 || totalRoomsField.getDocument().getLength() == 0 || addressField.getDocument().getLength() == 0 || emailField.getDocument().getLength() == 0 || phoneField.getDocument().getLength() == 0) {
				addButton.setEnabled(false);
			}
		}
		
		public void changedUpdate(DocumentEvent dEvent) {}
	}
}