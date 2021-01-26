import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
public class CustomerAddListener implements ActionListener{
	CustomerFrame cf;
	
	public CustomerAddListener(CustomerFrame cf) {
		this.cf = cf;
	}
	
	public void actionPerformed(ActionEvent aEvent) {
		CustomerAddDialog addCustomer = new CustomerAddDialog(cf);
		addCustomer.setVisible(true);
	}
}
