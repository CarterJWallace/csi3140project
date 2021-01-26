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

public class RoomUpdateDialog extends JDialog{
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
	private JButton updateButton = new JButton("Update");
	private JButton cancelButton = new JButton("Cancel");
	
	public RoomUpdateDialog (RoomFrame owner, final ResultSet rset) {
		super(owner, "Update Room", true);
		setSize(280, 150);
		setLocationRelativeTo(owner);
		
		uidField.setEditable(false);
		
		hotelChoices = owner.getHotels();
		
		hotelBox = new JComboBox<String>(hotelChoices);
		
		try {
			uidField.setText(rset.getString(1));
			priceField.setText(rset.getString(2));
			amenitiesField.setText(rset.getString(3));
			if(rset.getString(4) != null) {
				capacityBox.setSelectedItem(rset.getString(4));
			}
			if(rset.getString(5) != null) {
				viewBox.setSelectedItem(rset.getString(5));
			}
			if(rset.getString(6) != null) {
				extendableBox.setSelectedItem(rset.getString(6));
			}
			damageField.setText(rset.getString(7));
			if(rset.getString(8) != null) {
				hotelBox.setSelectedItem(rset.getString(8));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
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
		updateButton.setEnabled(false);
		south.add(updateButton);
		south.add(cancelButton);
		
		uidField.getDocument().addDocumentListener(new InputListener());
		priceField.getDocument().addDocumentListener(new InputListener());
		amenitiesField.getDocument().addDocumentListener(new InputListener());
		damageField.getDocument().addDocumentListener(new InputListener());
		
		capacityBox.addItemListener(new BoxItemListener());
		viewBox.addItemListener(new BoxItemListener());
		extendableBox.addItemListener(new BoxItemListener());
		hotelBox.addItemListener(new BoxItemListener());

		updateButton.addActionListener(
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
		
		return new String("update \"Lab\".\"Room\" set \"Price\" = '" + price + "', \"Amentities\" = '" + amen + "', \"Capacity\" = '" + cap + "', \"View\" = '" + view + "', \"Extendable\" = '" + ext + "', \"Damage\" = '" + dmg +"', \"Hotel_ID\" ='" + hID + "' where \"Room_ID\" = '" + uid + "'");
		
	}
	
	class BoxItemListener implements ItemListener{
		public void itemStateChanged(ItemEvent e) {
			updateButton.setEnabled(true);
		}
	}
	
	class InputListener implements DocumentListener{
		public void insertUpdate(DocumentEvent dEvent) {
			if(uidField.getDocument().getLength() > 0 && priceField.getDocument().getLength() > 0 && amenitiesField.getDocument().getLength() > 0 && damageField.getDocument().getLength() > 0) {
				updateButton.setEnabled(true);
			}
		}
		
		public void removeUpdate(DocumentEvent dEvent) {
			if(uidField.getDocument().getLength() == 0 || priceField.getDocument().getLength() == 0 || amenitiesField.getDocument().getLength() == 0 || damageField.getDocument().getLength() == 0) {
				updateButton.setEnabled(false);
			}
		}
		
		public void changedUpdate(DocumentEvent dEvent) {}
	}
}