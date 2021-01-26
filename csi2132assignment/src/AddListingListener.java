import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddListingListener implements ActionListener{
	DBFrame dbf;
	
	public AddListingListener(DBFrame dbf) {
		this.dbf = dbf;
	}
	
	public void actionPerformed(ActionEvent aEvent) {
		AddListingDialog addDialog = new AddListingDialog(dbf);
		addDialog.setVisible(true);
	}
}
