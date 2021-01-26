import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoomAddListener implements ActionListener{
	RoomFrame rf;
	
	public RoomAddListener(RoomFrame rf) {
		this.rf = rf;
	}
	
	public void actionPerformed(ActionEvent aEvent) {
		rf.refreshUID();
		RoomAddDialog addRoom = new RoomAddDialog(rf);
		addRoom.setVisible(true);
	}
}
