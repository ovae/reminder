package rem.panels;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import rem.constants.Colour;

/**
 * 
 * @author ovae.
 * @version 20150822.
 */
public class ColourPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ColourPanel(){
		this.setLayout(null);
		setUpContent();
	}

	/**
	 * 
	 */
	private void setUpContent(){
		JTextField deliveredField = new JTextField();
		JLabel deliveredLabel = new JLabel("Delivered");

		JTextField morethanTwoDaysField = new JTextField();
		JLabel morethanTwoDaysLabel = new JLabel("More than 2 days time");

		JTextField twoDaysLeftField = new JTextField();
		JLabel twoDaysLeftLabel = new JLabel("Two days left");

		JTextField oneDayLeftField = new JTextField();
		JLabel oneDayLeftLabel = new JLabel("One day left");

		JTextField deliveryDayField = new JTextField();
		JLabel deliveryDayLabel = new JLabel("Delivery day");

		JTextField selectedRowField = new JTextField();
		JLabel selectedRowLabel = new JLabel("Selected row");

		JTextField defaultField = new JTextField();
		JLabel defaultLabel = new JLabel("Default value");

		//Set the background color for every text field.
		deliveredField.setBackground(Colour.TABLE_DELIVERED.getColor());
		morethanTwoDaysField.setBackground(Colour.TABLE_MORE_THAN_TO_DAYS.getColor());
		twoDaysLeftField.setBackground(Colour.TABLE_TWO_DAYS_LEFT.getColor());
		oneDayLeftField.setBackground(Colour.TABLE_ONE_DAY_LEFT.getColor());
		deliveryDayField.setBackground(Colour.TABLE_DELIVERY_DAY.getColor());
		selectedRowField.setBackground(Colour.TABLE_SELECTED_ROW.getColor());
		defaultField.setBackground(Colour.TABLE_DEFAULT.getColor());

		//Disable the ability to edit the text fields.
		deliveredField.setEditable(false);
		morethanTwoDaysField.setEditable(false);
		twoDaysLeftField.setEditable(false);
		oneDayLeftField.setEditable(false);
		deliveryDayField.setEditable(false);
		selectedRowField.setEditable(false);
		defaultField.setEditable(false);

		//Set the bounds for every text field.
		deliveredField.setBounds(10, 100, 100,28);
		morethanTwoDaysField.setBounds(10, 100, 100,28);
		twoDaysLeftField.setBounds(10, 100, 100,28);
		oneDayLeftField.setBounds(10, 100, 100,28);
		deliveryDayField.setBounds(10, 100, 100,28);
		selectedRowField.setBounds(10, 100, 100,28);
		defaultField.setBounds(10, 100, 100,28);

		//Set the bounds for every text field label.
		deliveredLabel.setBounds(10, 100, 300,28);
		morethanTwoDaysLabel.setBounds(10, 100, 300,28);
		twoDaysLeftLabel.setBounds(10, 100, 300,28);
		oneDayLeftLabel.setBounds(10, 100, 300,28);
		deliveryDayLabel.setBounds(10, 100, 300,28);
		selectedRowLabel.setBounds(10, 100, 300,28);
		defaultLabel.setBounds(10, 100, 300,28);

		//Set the location for every text field.
		deliveredField.setLocation(20, 20);
		morethanTwoDaysField.setLocation(20, 50);
		twoDaysLeftField.setLocation(20, 80);
		oneDayLeftField.setLocation(20, 110);
		deliveryDayField.setLocation(20, 140);
		selectedRowField.setLocation(20, 170);
		defaultField.setLocation(20, 200);

		//Set the location for every text field label.
		deliveredLabel.setLocation(130, 20);
		morethanTwoDaysLabel.setLocation(130, 50);
		twoDaysLeftLabel.setLocation(130, 80);
		oneDayLeftLabel.setLocation(130, 110);
		deliveryDayLabel.setLocation(130, 140);
		selectedRowLabel.setLocation(130, 170);
		defaultLabel.setLocation(130, 200);

		this.setLayout(null);
		this.add(deliveredField);
		this.add(deliveredLabel);
		this.add(morethanTwoDaysField);
		this.add(morethanTwoDaysLabel);
		this.add(twoDaysLeftField);
		this.add(twoDaysLeftLabel);
		this.add(oneDayLeftField);
		this.add(oneDayLeftLabel);
		this.add(deliveryDayField);
		this.add(deliveryDayLabel);
		this.add(selectedRowField);
		this.add(selectedRowLabel);
		this.add(defaultField);
		this.add(defaultLabel);
	}
}
