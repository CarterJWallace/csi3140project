import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HotelAddListener implements ActionListener{
	HotelFrame hf;
	
	public HotelAddListener(HotelFrame hf) {
		this.hf = hf;
	}
	
	public void actionPerformed(ActionEvent aEvent) {
		hf.refreshUID();
		HotelAddDialog addHotel = new HotelAddDialog(hf);
		addHotel.setVisible(true);
	}
}
