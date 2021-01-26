import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class MainFrame extends JFrame{
	private static int WIDTH = 800;
	private static int HEIGHT = 600;
	
	private JButton areaRoomsButton = new JButton("Rooms per Area");
	private JButton hotelCapacityButton = new JButton("Capacity of Hotels");
	private JButton bookingButton = new JButton("Book a Room");
	private JButton customerButton = new JButton("Customer Table");
	private JButton employeeButton = new JButton("Employee Table");
	private JButton hotelButton = new JButton("Hotel Table");
	private JButton roomButton = new JButton("Room Table");
	private JButton employeeBookingButton = new JButton("Employee Booking Table");
	
	private String[] roleChoices = {"Employee", "Customer"};
	private JComboBox<String> roleBox = new JComboBox<String>(roleChoices);
	
	private String roleSelection = "Employee";

	public MainFrame() {
		setLocationRelativeTo(null);
		setTitle("Main DB Window");
		setSize(WIDTH, HEIGHT);
		
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(4, 2, 10, 10));
		center.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		center.add(customerButton);
		center.add(employeeButton);
		center.add(hotelButton);
		center.add(roomButton);
		center.add(bookingButton);
		center.add(employeeBookingButton);
		center.add(areaRoomsButton);
		center.add(hotelCapacityButton);
		
		if(roleSelection.equals("Customer")) {
			employeeBookingButton.setEnabled(false);
		}
		
		JPanel south = new JPanel();
		south.add(roleBox);
		
		roleBox.addItemListener(new BoxItemListener());
		
		bookingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent aEvent) {
				BookingFrame bookingFrame = new BookingFrame(MainFrame.this);
				bookingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				bookingFrame.setVisible(true);
				setVisible(false);
			}
		});
		
		employeeBookingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent aEvent) {
				EmployeeBookingFrame employeeBookingFrame = new EmployeeBookingFrame(MainFrame.this);
				employeeBookingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				employeeBookingFrame.setVisible(true);
				setVisible(false);
			}
		});
		
		employeeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent aEvent) {
				EmployeeFrame employeeFrame = new EmployeeFrame(MainFrame.this);
				employeeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				employeeFrame.setVisible(true);
				setVisible(false);
			}
		});
		
		customerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent aEvent) {
				CustomerFrame customerFrame = new CustomerFrame(MainFrame.this);
				customerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				customerFrame.setVisible(true);
				setVisible(false);
			}
		});
		
		hotelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent aEvent) {
				HotelFrame hotelFrame = new HotelFrame(MainFrame.this);
				hotelFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				hotelFrame.setVisible(true);
				setVisible(false);
			}
		});
		
		roomButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent aEvent) {
				RoomFrame roomFrame = new RoomFrame(MainFrame.this);
				roomFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				roomFrame.setVisible(true);
				setVisible(false);
			}
		});
		
		areaRoomsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent aEvent) {
				AreaRoomsFrame areaRoomsFrame = new AreaRoomsFrame(MainFrame.this);
				areaRoomsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				areaRoomsFrame.setVisible(true);
				setVisible(false);
			}
		});
		
		hotelCapacityButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent aEvent) {
				HotelCapacityFrame hotelCapacityFrame = new HotelCapacityFrame(MainFrame.this);
				hotelCapacityFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				hotelCapacityFrame.setVisible(true);
				setVisible(false);
			}
		});
		
		Container contentPane = getContentPane();
		contentPane.add(center, BorderLayout.CENTER);
		contentPane.add(south, BorderLayout.SOUTH);
		
		pack();
	}
	
	class BoxItemListener implements ItemListener{
		public void itemStateChanged(ItemEvent e) {
			roleSelection = (String)roleBox.getSelectedItem();
			if(roleSelection.equals("Employee")) {
				employeeBookingButton.setEnabled(true);
			}
			else if(roleSelection.equals("Customer")) {
				employeeBookingButton.setEnabled(false);
			}
		}
	}
	
	public String getRole() {
		return roleSelection;
	}
}