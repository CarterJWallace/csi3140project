import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class BookingAddDialog extends JDialog{
	private BookingFrame myDB;
	private String role;
	
	private JTextField uidField = new JTextField(3);
	private JTextField dateField = new JTextField(5);
	private JTextField checkInField = new JTextField(5);
	private JTextField checkOutField = new JTextField(5);
	private JComboBox<String> customerBox = new JComboBox<String>();
	private String[] statusChoices = {"Booked", "Paid"};
	private JComboBox<String> statusBox = new JComboBox<String>(statusChoices);
	private JTextField statusField = new JTextField(5);
	private String[] customerChoices;
    private JTextField nameField = new JTextField(5);
	private JTextField roomIDField = new JTextField(3);
	private JTextField hotelIDField = new JTextField(3);
	
	private Calendar today = Calendar.getInstance();
	private LocalDate localDate = today.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	private String todayYear;
	private String todayMonth;
	private String todayDay;
	
	private JButton addButton = new JButton("Add");
	private JButton cancelButton = new JButton("Cancel");
	
	public BookingAddDialog (BookingFrame owner, ResultSet rset, int bookingUID) {
		super(owner, "Add Booking", true);
		setSize(280, 150);
		setLocationRelativeTo(owner);
		
		todayYear = Integer.toString(localDate.getYear());
		todayMonth = Integer.toString(localDate.getMonthValue());
		todayDay = Integer.toString(localDate.getDayOfMonth());
		
		myDB = owner;
		role = myDB.getOwner().getRole();
		
		uidField.setEditable(false);
		nameField.setEditable(false);
		roomIDField.setEditable(false);
		hotelIDField.setEditable(false);
		dateField.setEditable(false);
		
		uidField.setText(Integer.toString(bookingUID));
		dateField.setText(todayYear + "-" + todayMonth + "-" + todayDay);
		
		customerChoices = (owner).getCustomers();
		customerBox = new JComboBox<String>(customerChoices);
		
		try {
			roomIDField.setText(rset.getString(1));
			hotelIDField.setText(rset.getString(8));
			nameField.setText(myDB.getCustomerName((String)customerBox.getSelectedItem()));
		}
		catch(SQLException e) {
			e.printStackTrace();
		}		
		
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(3,4));
		center.add(new JLabel(" Booking ID:"));
		center.add(uidField);
		center.add(new JLabel(" Room ID:"));
		center.add(roomIDField);
		center.add(new JLabel(" Hotel ID:"));
		center.add(hotelIDField);
		center.add(new JLabel(" Date:"));
		center.add(dateField);
		center.add(new JLabel(" Check In Date:"));
		center.add(checkInField);
		center.add(new JLabel(" Check Out Date:"));
		center.add(checkOutField);
		center.add(new JLabel(" Customer SSN:"));
		center.add(customerBox);
		center.add(new JLabel(" Customer Name:"));
		center.add(nameField);
		center.add(new JLabel(" Booking Status:"));
		
		if(role.equals("Customer")) {
			statusField.setText("Booked");
			statusField.setEditable(false);
			center.add(statusField);
		}
		else {
			center.add(statusBox);
		}
		
		JPanel south = new JPanel();
		addButton.setEnabled(false);
		south.add(addButton);
		south.add(cancelButton);
		
		uidField.getDocument().addDocumentListener(new InputListener());
		dateField.getDocument().addDocumentListener(new InputListener());
		checkInField.getDocument().addDocumentListener(new InputListener());
		checkOutField.getDocument().addDocumentListener(new InputListener());

		customerBox.addItemListener(new BoxItemListener());
		
		addButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent aEvent) {
						CustomerManager ownersDB = ((BookingFrame)owner).getCustomerManager();
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
		String uid = uidField.getText();
		String date = dateField.getText();
		String checkIn = checkInField.getText();
		String checkOut = checkOutField.getText();
		String custID = ((String)customerBox.getSelectedItem()).toUpperCase();
		String custName = nameField.getText().toUpperCase();
		String roomID = roomIDField.getText();
		String hotelID = hotelIDField.getText();
		String status = "";
		if(role.equals("Customer")) {
			status = "Booked";
		}
		else{
			status = ((String)statusBox.getSelectedItem()).toUpperCase();
		}
		
		date = date.replace('\'', ' ');
		checkIn = checkIn.replace('\'', ' ');
		checkOut = checkOut.replace('\'', ' ');
		
		return new String("insert into \"Lab\".\"Booking\" values ('" + uid + "', '" + date + "', '" + checkIn + "', '" + checkOut + "', '" + custID + "', '" + custName + "', '" + roomID + "', '" + hotelID + "', '" + status + "')");
		
	}
	
	class BoxItemListener implements ItemListener{
		public void itemStateChanged(ItemEvent e) {
			nameField.setText(myDB.getCustomerName((String)customerBox.getSelectedItem()));
		}
	}
	
	class InputListener implements DocumentListener{
		public void insertUpdate(DocumentEvent dEvent) {
			if(dateField.getDocument().getLength() > 0 && checkInField.getDocument().getLength() > 0 && checkOutField.getDocument().getLength() > 0) {
				addButton.setEnabled(true);
			}
		}
		
		public void removeUpdate(DocumentEvent dEvent) {
			if(dateField.getDocument().getLength() == 0 || checkInField.getDocument().getLength() == 0 || checkOutField.getDocument().getLength() == 0){
				addButton.setEnabled(false);
			}
		}
		
		public void changedUpdate(DocumentEvent dEvent) {}
	}
}