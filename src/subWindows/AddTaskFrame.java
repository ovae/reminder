package subWindows;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import rem.MainWindow;
import rem.files.FileHandler;

/**
 * 
 * @author ovae.
 * @version 20150308.
 */
public class AddTaskFrame extends JFrame{

	private JTextField inputTopic = new JTextField("");
	private JTextField inputAbout = new JTextField("");

	private JTextField inputBegin = new JTextField("");
	private JTextField inputEnd = new JTextField("");
	private JLabel beginInfoLabel = new JLabel("");
	private JLabel endInfoLabel = new JLabel("");
	private JButton addButton = new JButton("add");
	private JButton resetButton = new JButton("reset");

	private MainWindow parentFrame;

	/**
	 * 
	 * @param parentFrame
	 */
	public AddTaskFrame(final MainWindow parentFrame){
		if(parentFrame.equals(null)){
			throw new IllegalArgumentException("The paarent frame can not be null.");
		}
		this.parentFrame = parentFrame;
		this.setTitle("New Task");
		this.setLocationRelativeTo(null);
		this.setSize(new Dimension(400,256));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setLayout(new BorderLayout());
		init();
	}

	/**
	 * 
	 */
	public void init(){
		//set the basic setting for the frame.
		//Declare all needed compounds.
		JPanel addPanel = new JPanel();
		JPanel buttonPanel = new JPanel(new BorderLayout());
		JLabel enterTopic = new JLabel("Topic: ");
		JLabel enterAbout = new JLabel("About: ");
		JLabel enterBegin = new JLabel("Begin: ");
		JLabel enterEnd = new JLabel("End: ");

		//Set a input format for begin and end input fields.
		NumberFormat inputFormate = NumberFormat.getNumberInstance(); 
		inputFormate.setMaximumIntegerDigits(8);
		inputFormate.setGroupingUsed(false); 
		inputBegin = new JFormattedTextField(inputFormate);

		inputFormate.setMaximumIntegerDigits(8);
		inputFormate.setGroupingUsed(false); 
		inputEnd = new JFormattedTextField(inputFormate);

		//Set the bounds of the Labels
		enterTopic.setBounds(10, 100, 100, 28);
		enterAbout.setBounds(10, 100, 100, 28);
		enterBegin.setBounds(10, 100, 100, 28);
		enterEnd.setBounds(10, 100, 100, 28);

		//Set the bounds of the text fields.
		inputTopic.setBounds(10, 100, 180,28);
		inputAbout.setBounds(10, 100, 180,28);
		inputBegin.setBounds(10, 100, 180,28);
		inputEnd.setBounds(10, 100, 180,28);

		//Set the bounds of the info lables.
		beginInfoLabel.setBounds(10, 100, 180,28);
		endInfoLabel.setBounds(10, 100, 180,28);

		//Set the location of the text fields.
		enterTopic.setLocation(40, 40);
		enterAbout.setLocation(40, 70);
		enterBegin.setLocation(40, 100);
		enterEnd.setLocation(40, 130);

		//Set the label locations.
		inputTopic.setLocation(120, 40);
		inputAbout.setLocation(120, 70);
		inputBegin.setLocation(120, 100);
		inputEnd.setLocation(120, 130);
		beginInfoLabel.setLocation(300, 100);
		endInfoLabel.setLocation(300, 130);

		/* The action listener of the addButton.
		 * If the button is pressed, all text field
		 * be controlled. If one of them is empty the new task wont be saved.
		 * If all text fields have a value the task will be add to the task table.
		 */
		addButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//Check if one of the text fields is empty.
				if(inputTopic.getText().trim().isEmpty() || 
						inputAbout.getText().trim().isEmpty() ||
						inputBegin.getText().trim().isEmpty() ||
						inputEnd.getText().trim().isEmpty() ){
					JOptionPane.showMessageDialog(null, "At least one inputfeald is empty.");
				}else{
					
					parentFrame.getTaskTable().addRow(inputTopic.getText(), inputAbout.getText(), inputBegin.getText(), inputEnd.getText());
				}
			}
		});

		/* The action listener of the resetButton.
		 * If the button is pressed all the text fields of the window
		 * will be set to an empty string value.
		 */
		resetButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				resetInputsAddFrame();
			}
		});

		//Disable the layout of the addPanel.
		addPanel.setLayout(null);
		//Add all labels and text fields to the addPanel.
		addPanel.add(enterTopic);
		addPanel.add(enterAbout);
		addPanel.add(enterBegin);
		addPanel.add(enterEnd);
		addPanel.add(inputTopic);
		addPanel.add(inputAbout);
		addPanel.add(inputBegin);
		addPanel.add(inputEnd);
		addPanel.add(beginInfoLabel);
		addPanel.add(endInfoLabel);
		addPanel.setBorder(BorderFactory.createTitledBorder("New Task"));
		//Add the buttons to the buttonPanel.
		buttonPanel.add(resetButton,BorderLayout.WEST);
		buttonPanel.add(addButton, BorderLayout.CENTER);
		//Add the panels to the addFrame.
		this.add(addPanel,BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		//Disable the default visibility.
		this.setVisible(false);
	}

	//Sets the addFrame to visible.
	public void setVisible(Boolean visible){
		this.setVisible(visible);
	}

	//Resets the all input fields of the addFrame.
	public void resetInputsAddFrame(){
		inputTopic.setText("");
		inputAbout.setText("");
		inputBegin.setText("");
		inputEnd.setText("");
	}
}