import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
public class EmployeeAddListener implements ActionListener{
	EmployeeFrame ef;
	
	public EmployeeAddListener(EmployeeFrame ef) {
		this.ef = ef;
	}
	
	public void actionPerformed(ActionEvent aEvent) {
		EmployeeAddDialog addEmployee = new EmployeeAddDialog(ef);
		addEmployee.setVisible(true);
	}
}
