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

public class AddListingDialog extends JDialog{
	private JTextField lNameField = new JTextField(16);
	private JTextField fNameField = new JTextField(16);
	private JTextField areaCodeField = new JTextField(2);
	private JTextField prefixField = new JTextField(2);
	private JTextField suffixField = new JTextField(3);
	private JButton addButton = new JButton("Add");
	private JButton cancelButton = new JButton("Cancel");
	
	public AddListingDialog (final JFrame owner) {
		super(owner, "Add Listing", true);
		setSize(280, 150);
		setLocationRelativeTo(owner);
		
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(3,2));
		center.add(new JLabel(" Last Name:"));
		center.add(lNameField);
		center.add(new JLabel(" First Name:"));
		center.add(fNameField);
		
		JPanel pnPanel = new JPanel();
		pnPanel.add(new JLabel("("));
		pnPanel.add(areaCodeField);
		pnPanel.add(new JLabel(") "));
		pnPanel.add(prefixField);
		pnPanel.add(new JLabel("-"));
		pnPanel.add(suffixField);
		
		center.add(new JLabel(" Phone Number:"));
		center.add(pnPanel);
		
		JPanel south = new JPanel();
		addButton.setEnabled(false);
		south.add(addButton);
		south.add(cancelButton);
		
		lNameField.getDocument().addDocumentListener(new InputListener());
		fNameField.getDocument().addDocumentListener(new InputListener());
		areaCodeField.getDocument().addDocumentListener(new InputListener());
		prefixField.getDocument().addDocumentListener(new InputListener());
		suffixField.getDocument().addDocumentListener(new InputListener());
		
		areaCodeField.getDocument().addDocumentListener(new PhoneDocumentListener(areaCodeField, 3));		
		prefixField.getDocument().addDocumentListener(new PhoneDocumentListener(prefixField, 3));	
		suffixField.getDocument().addDocumentListener(new PhoneDocumentListener(suffixField, 4));	
		
		areaCodeField.addFocusListener(new PhoneFocusListener());
		prefixField.addFocusListener(new PhoneFocusListener());
		suffixField.addFocusListener(new PhoneFocusListener());
		
		addButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent aEvent) {
						//((DBFrame)owner).doInsertQuery(buildQuery());
						DatabaseManager ownersDB = ((DBFrame)owner).getDBManager();
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
		String last = lNameField.getText().trim().toUpperCase();
		String first = fNameField.getText().trim().toUpperCase();
		String ac = areaCodeField.getText().trim().toUpperCase();
		String pre = prefixField.getText().trim().toUpperCase();
		String sfx = suffixField.getText().trim().toUpperCase();
		
		last = last.replace('\'', ' ');
		first = first.replace('\'', ' ');
		ac = ac.replace('\'', ' ');
		pre = pre.replace('\'', ' ');
		sfx = sfx.replace('\'', ' ');
		
		return new String("insert into Listings values ('" + last + "', '" + first + "', '" + ac + "', '" + pre + "', '" + sfx + "')");
	}
	
	class InputListener implements DocumentListener{
		public void insertUpdate(DocumentEvent dEvent) {
			if(lNameField.getDocument().getLength() > 0 && fNameField.getDocument().getLength() > 0 && areaCodeField.getDocument().getLength() == 3 && prefixField.getDocument().getLength() == 3 && suffixField.getDocument().getLength() == 4) {
				addButton.setEnabled(true);
				if(dEvent.getDocument() == suffixField.getDocument()) {
					addButton.requestFocus();
					getRootPane().setDefaultButton(addButton);
				}
			}
		}
		
		public void removeUpdate(DocumentEvent dEvent) {
			if(lNameField.getDocument().getLength() == 0 || fNameField.getDocument().getLength() == 0 || areaCodeField.getDocument().getLength() < 3 || prefixField.getDocument().getLength() < 3 || suffixField.getDocument().getLength() < 4) {
				addButton.setEnabled(false);
			}
		}
		
		public void changedUpdate(DocumentEvent dEvent) {}
	}
}
