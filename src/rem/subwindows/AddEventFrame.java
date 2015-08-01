package rem.subwindows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import rem.table.TasksTable;

/**
 * 
 * @author ovae.
 * @version 20150718.
 */
public class AddEventFrame extends JFrame{

	private static final long serialVersionUID = 1L;

	//InputFields
	private JTextField inputFieldTopic;
	private JTextArea inputFieldAbout;
	private JTextField inputFieldBegin;
	//Duration
	private JSlider duration;
	private int durationValue;
	private JTextField durationView;

	//Label and buttons
	private JLabel beginInfoLabel;
	private JLabel endInfoLabel;
	private JButton addButton;
	private JButton resetButton;

	private TasksTable table;
	private JPanel mainPanel;

	/**
	 * 
	 * @param parentFrame
	 */
	public AddEventFrame(final TasksTable table){
		if(table.equals(null)){
			throw new IllegalArgumentException("The table can not be null.");
		}

		inputFieldTopic = new JTextField("");
		inputFieldAbout = new JTextArea("");
		inputFieldBegin = new JTextField("");

		duration = new JSlider(JSlider.HORIZONTAL,1, 365, 1);
		durationValue = 1;
		durationView = new JTextField("");

		beginInfoLabel = new JLabel("");
		endInfoLabel = new JLabel("");
		addButton = new JButton("add");
		resetButton = new JButton("reset");
		mainPanel = new JPanel();
		mainPanel.setLayout(null);

		this.table = table;
		this.setTitle("New Event");
		this.setLocationRelativeTo(null);
		this.setSize(new Dimension(500,340));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setLayout(new BorderLayout());
		createTheContent();
	}

	/**
	 * 
	 */
	private void createTheContent(){
		mainPanel.setBorder(BorderFactory.createTitledBorder("Event"));
		setUpBeginnAndEnd();
		setUpTheInputFields();
		setUpButtonPanel();

		//If the window is closed, the resetInputAddFrame method is used-
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				resetInputsAddFrame();
			}
		});

		//Disable the default visibility.
		this.setVisible(false);
	}

	/**
	 * 
	 */
	private void setUpTheInputFields(){
		JLabel enterTopic = new JLabel("Event ");
		JLabel enterAbout = new JLabel("About ");

		inputFieldAbout.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		//Set the bounds of the Labels
		enterTopic.setBounds(10, 100, 100, 28);
		enterAbout.setBounds(10, 100, 100, 28);

		//Set the bounds of the text fields.
		inputFieldTopic.setBounds(10, 100, 360,28);
		inputFieldAbout.setBounds(10, 100, 360, 128);

		//Set the bounds of the info labels.
		beginInfoLabel.setBounds(10, 100, 180,28);
		endInfoLabel.setBounds(10, 100, 180,28);

		//Set the location of the labels.
		enterTopic.setLocation(40, 30);
		enterAbout.setLocation(40, 60);

		//Set the input locations.
		inputFieldTopic.setLocation(120, 30);
		inputFieldAbout.setLocation(120, 60);

		//Add all labels and text fields to the addPanel.
		mainPanel.add(enterTopic);
		mainPanel.add(enterAbout);
		mainPanel.add(inputFieldTopic);
		mainPanel.add(inputFieldAbout);
	}

	/**
	 * 
	 */
	private void setUpBeginnAndEnd(){
		JLabel beginLable = new JLabel("Begin: ");
		JLabel durationLable = new JLabel("Duration ");

		//Set a input format for begin and end input fields.
		NumberFormat inputFormate = NumberFormat.getNumberInstance(); 
		inputFormate.setMaximumIntegerDigits(8);
		inputFormate.setGroupingUsed(false); 
		inputFieldBegin = new JFormattedTextField(inputFormate);

		//Set the bounds of the text fields.
		inputFieldBegin.setBounds(10, 100, 150,28);

		//Turn on labels at major tick marks.
		duration.setMajorTickSpacing(364);
		duration.setMinorTickSpacing(1);
		duration.setPaintTicks(true);
		duration.setPaintLabels(true);

		//Set the bounds of the Labels
		beginLable.setBounds(10, 100, 100, 28);
		durationLable.setBounds(10, 100, 100, 28);

		//Set the bounds of the text fields.
		beginLable.setLocation(40, 195);
		duration.setBounds(10, 100, 340, 50);
		durationView.setBounds(10, 28, 28, 28);

		//Set the bounds of the info labels.
		beginInfoLabel.setBounds(10, 100, 180,28);
		endInfoLabel.setBounds(10, 100, 180,28);

		//Set the location of the text fields.
		inputFieldBegin.setLocation(120, 195);
		durationLable.setLocation(40, 230);
		duration.setLocation(110, 230);
		durationView.setLocation(450, 230);

		//Set the label locations.
		beginInfoLabel.setLocation(300, 200);
		endInfoLabel.setLocation(300, 230);

		/*
		 * 
		 */
		duration.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider s = (JSlider) e.getSource();
				durationValue = s.getValue();
				durationView.setText(""+s.getValue());
		}});

		//Add all labels and text fields to the addPanel.
		mainPanel.add(beginLable);
		mainPanel.add(durationLable);
		mainPanel.add(duration);
		mainPanel.add(durationView);
		mainPanel.add(beginInfoLabel);
		mainPanel.add(endInfoLabel);
		mainPanel.add(inputFieldBegin);
	}

	/**
	 * 
	 */
	private void setUpButtonPanel(){
		JPanel buttonPanel = new JPanel(new BorderLayout());

		/* The action listener of the addButton.
		 * If the button is pressed, all text field
		 * be controlled. If one of them is empty the new task wont be saved.
		 * If all text fields have a value the task will be add to the task table.
		 */
		addButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//Check if one of the text fields is empty.
				if(inputFieldTopic.getText().trim().isEmpty() || 
						inputFieldAbout.getText().trim().isEmpty() ||
						inputFieldBegin.getText().trim().isEmpty() ){
					JOptionPane.showMessageDialog(null, "At least one inputfeald is empty.");
				}else{
					
					table.addRow(inputFieldTopic.getText(), inputFieldAbout.getText(), inputFieldBegin.getText(), ""+durationValue);
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

		//Add the buttons to the buttonPanel.
		buttonPanel.add(resetButton,BorderLayout.WEST);
		buttonPanel.add(addButton, BorderLayout.CENTER);
		//Add the panels to the addFrame.
		this.add(mainPanel,BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	/**
	 * Resets the all input fields of the addFrame.
	 */
	public void resetInputsAddFrame(){
		inputFieldTopic.setText("");
		inputFieldAbout.setText("");
		inputFieldBegin.setText("");
		duration.resetKeyboardActions();
	}
}
