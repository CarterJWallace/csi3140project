import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PhoneDocumentListener implements DocumentListener{
	private JTextField txtField;
	private int numsAllowed;
	
	public PhoneDocumentListener(JTextField tf, int numsAllowed) {
		txtField = tf;
		this.numsAllowed = numsAllowed;
	}
	
	public void insertUpdate(DocumentEvent dEvent) {
		if(dEvent.getDocument().getLength() == numsAllowed) {
			txtField.transferFocus();
		}
	}
	
	public void removeUpdate(DocumentEvent dEvent) {}
	
	public void changedUpdate(DocumentEvent dEvent) {}
	
}
