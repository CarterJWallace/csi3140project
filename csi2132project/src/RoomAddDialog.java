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

public class RoomAddDialog extends JDialog{
	private JTextField uidField = new JTextField(3);
	private JTextField priceField = new JTextField(3);
	private JTextField amenitiesField = new JTextField(10);
	private String[] capacityChoices = {"Single","Double","Queen","King","Party"};
	private JComboBox<String> capacityBox = new JComboBox<String>(capacityChoices);
	private String[] viewChoices = {"City", "Mountain", "Ocean"};
	private JComboBox<String> viewBox = new JComboBox<String>(viewChoices);
	private String[] extendableChoices = {"false", "true"};
	private JComboBox<String> extendableBox = new JComboBox<String>(extendableChoices);
	private JTextField damageField = new JTextField(10);
	private String[] hotelChoices;
	private JComboBox<String> hotelBox;
	private JButton addButton = new JButton("Add");
	private JButton cancelButton = new JButton("Cancel");
	
	public RoomAddDialog (RoomFrame owner) {
		super(owner, "Add Room", true);
		setSize(280, 150);
		setLocationRelativeTo(owner);
		
		uidField.setEditable(false);
		uidField.setText(Integer.toString(owner.getUID() + 1));
		
		hotelChoices = owner.getHotels();
		
		hotelBox = new JComboBox<String>(hotelChoices);
		
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(4,2));
		center.add(new JLabel(" UID:"));
		center.add(uidField);
		center.add(new JLabel(" Price:"));
		center.add(priceField);
		center.add(new JLabel(" Amenities:"));
		center.add(amenitiesField);
		center.add(new JLabel(" Capacity:"));
		center.add(capacityBox);
		center.add(new JLabel(" View Type:"));
		center.add(viewBox);
		center.add(new JLabel(" Extendable?"));
		center.add(extendableBox);
		center.add(new JLabel(" Damage:"));
		center.add(damageField);
		center.add(new JLabel(" Hotel ID:"));
		center.add(hotelBox);
		
		JPanel south = new JPanel();
		addButton.setEnabled(false);
		south.add(addButton);
		south.add(cancelButton);
		
		uidField.getDocument().addDocumentListener(new InputListener());
		priceField.getDocument().addDocumentListener(new InputListener());
		amenitiesField.getDocument().addDocumentListener(new InputListener());
		damageField.getDocument().addDocumentListener(new InputListener());
		
		addButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent aEvent) {
						CustomerManager ownersDB = ((RoomFrame)owner).getCustomerManager();
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
		String price = priceField.getText().trim().toUpperCase();
		String amen = amenitiesField.getText().trim().toUpperCase();
		String cap = ((String)capacityBox.getSelectedItem()).toUpperCase();
		String view = ((String)viewBox.getSelectedItem()).toUpperCase();
		String ext = ((String)extendableBox.getSelectedItem()).toUpperCase();
		String dmg = damageField.getText().trim().toUpperCase();
		String hID = (String)hotelBox.getSelectedItem();
		
		uid = uid.replace('\'', ' ');
		price = price.replace('\'', ' ');
		amen = amen.replace('\'', ' ');
		dmg = dmg.replace('\'', ' ');
		
		return new String("insert into \"Lab\".\"Room\" values ('" + uid + "', '" + price + "', '" + amen + "', '" + cap + "', '" + view + "', '" + ext + "', '" + dmg + "', '" + hID + "')");
		
	}
	
	class InputListener implements DocumentListener{
		public void insertUpdate(DocumentEvent dEvent) {
			if(uidField.getDocument().getLength() > 0 && priceField.getDocument().getLength() > 0 && amenitiesField.getDocument().getLength() > 0 && damageField.getDocument().getLength() > 0) {
				addButton.setEnabled(true);
			}
		}
		
		public void removeUpdate(DocumentEvent dEvent) {
			if(uidField.getDocument().getLength() == 0 || priceField.getDocument().getLength() == 0 || amenitiesField.getDocument().getLength() == 0 || damageField.getDocument().getLength() == 0) {
				addButton.setEnabled(false);
			}
		}
		
		public void changedUpdate(DocumentEvent dEvent) {}
	}
}