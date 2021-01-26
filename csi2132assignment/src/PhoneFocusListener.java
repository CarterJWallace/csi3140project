import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;

public class PhoneFocusListener implements FocusListener{

		public void focusGained(FocusEvent fEvent) {
			javax.swing.JTextField tf = (JTextField)fEvent.getSource();
			tf.setText("");
		}
		
		public void focusLost(FocusEvent fEvent) {}
}
