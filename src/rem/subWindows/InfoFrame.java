package rem.subWindows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 
 * @author ovae.
 * @version 20150308.
 */
public class InfoFrame extends JFrame{

	private static final long serialVersionUID = 1L;

	//Declare all needed compounds
	JPanel infoPanel;
	JLabel head;
	JTextField green;
	JTextField gray;
	JTextField red;
	JTextField orange;
	JTextField yellow;
	JTextField blue;
	JTextField white;
	JLabel greenLabel;
	JLabel grayLabel;
	JLabel redLabel;
	JLabel orangeLabel;
	JLabel yellowLabel;
	JLabel blueLabel;
	JLabel whiteLabel;

	/**
	 * 
	 */
	public InfoFrame(){
		infoPanel = new JPanel();
		green = new JTextField();
		gray = new JTextField();
		red = new JTextField();
		orange = new JTextField();
		yellow = new JTextField();
		blue = new JTextField();
		white = new JTextField();
		greenLabel = new JLabel("More then 2 days time");
		grayLabel = new JLabel("Delivered");
		redLabel = new JLabel("Delivery day");
		orangeLabel = new JLabel("One day left");
		yellowLabel = new JLabel("Two days left");
		blueLabel = new JLabel("Selected row");
		whiteLabel = new JLabel("Default value");
		settingUpInfoFrame();
		infoPanel.setBorder(BorderFactory.createTitledBorder("Declaration on the choice of colour"));
		this.setVisible(false);
	}

	/**
	 * 
	 */
	private void settingUpInfoFrame(){
		//set the basic setting for the frame.
		this.setLocationRelativeTo(null);
		this.setSize(new Dimension(350,290));
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setLayout(new BorderLayout());

		//Set the background color for every text field.
		green.setBackground(new Color(126, 207, 88));
		gray.setBackground(Color.LIGHT_GRAY);
		red.setBackground(new Color(240, 88, 88));
		orange.setBackground(new Color(255,149,88));
		yellow.setBackground(new Color(255,210,120));
		blue.setBackground(new Color(160,166,207));
		white.setBackground(Color.WHITE);

		//Disable the ability to edit the text fields.
		green.setEditable(false);
		gray.setEditable(false);
		red.setEditable(false);
		orange.setEditable(false);
		yellow.setEditable(false);
		blue.setEditable(false);
		white.setEditable(false);

		//Set the bounds for every text field.
		green.setBounds(10, 100, 100,28);
		gray.setBounds(10, 100, 100,28);
		red.setBounds(10, 100, 100,28);
		orange.setBounds(10, 100, 100,28);
		yellow.setBounds(10, 100, 100,28);
		blue.setBounds(10, 100, 100,28);
		white.setBounds(10, 100, 100,28);

		//Set the bounds for every text field label.
		greenLabel.setBounds(10, 100, 300,28);
		grayLabel.setBounds(10, 100, 300,28);
		redLabel.setBounds(10, 100, 300,28);
		orangeLabel.setBounds(10, 100, 300,28);
		yellowLabel.setBounds(10, 100, 300,28);
		blueLabel.setBounds(10, 100, 300,28);
		whiteLabel.setBounds(10, 100, 300,28);

		//Set the location for every text field.
		gray.setLocation(20, 45);
		green.setLocation(20, 73);
		yellow.setLocation(20, 101);
		orange.setLocation(20, 129);
		red.setLocation(20, 157);
		blue.setLocation(20, 185);
		white.setLocation(20, 213);

		//Set the location for every text field label.
		grayLabel.setLocation(130, 45);
		greenLabel.setLocation(130, 73);
		yellowLabel.setLocation(130, 101);
		orangeLabel.setLocation(130, 129);
		redLabel.setLocation(130, 157);
		blueLabel.setLocation(130, 185);
		whiteLabel.setLocation(130, 213);

		//Disable the layout of the infoPanel.
		infoPanel.setLayout(null);
		//Add all text field to the infoPanel.
		infoPanel.add(gray);
		infoPanel.add(green);
		infoPanel.add(yellow);
		infoPanel.add(orange);
		infoPanel.add(red);
		infoPanel.add(blue);
		infoPanel.add(white);

		//Add all text field labels to the infoPanel.
		infoPanel.add(grayLabel);
		infoPanel.add(greenLabel);
		infoPanel.add(yellowLabel);
		infoPanel.add(orangeLabel);
		infoPanel.add(redLabel);
		infoPanel.add(blueLabel);
		infoPanel.add(whiteLabel);
		
		//Add the infoPanel to the infoFrame and set it to visible.
		this.add(infoPanel);
	}

}
