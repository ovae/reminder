package rem.subwindows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import rem.MainWindow;
import rem.table.TaskTable;
import rem.util.Util;

/**
 * 
 * @author ovae.
 * @version 20151024.
 */
public class AddTaskFrame extends JFrame{

	private static final long serialVersionUID = 1L;

	private JComboBox<String> topicBox;
	private JTextArea inputFieldAbout;
	private JComboBox<String> status;
	private JTextField inputFieldBegin;
	private JTextField inputFieldEnd;

	private JLabel beginInfoLabel;
	private JLabel endInfoLabel;
	private JButton addButton;
	private JButton resetButton;

	private TaskTable table;
	private JPanel mainPanel;

	private MainWindow window;

	private static Date date = new Date();
	private static SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMdd");

	/**
	 * 
	 * @param window the main window.
	 */
	public AddTaskFrame(final MainWindow window){
		if(window.equals(null)){
			throw new IllegalArgumentException("The table can not be null.");
		}

		this.window = window;

		inputFieldAbout = new JTextArea("");
		status = new JComboBox<String>();
		topicBox = new JComboBox<String>();
		inputFieldBegin = new JTextField("");
		inputFieldEnd = new JTextField("");

		beginInfoLabel = new JLabel("");
		endInfoLabel = new JLabel("");
		addButton = new JButton("add");
		resetButton = new JButton("reset");

		mainPanel = new JPanel();
		mainPanel.setLayout(null);

		this.table = (TaskTable) window.getTaskTable();
		this.setTitle("New Task");
		this.setLocationRelativeTo(null);
		this.setSize(new Dimension(500,330));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setLayout(new BorderLayout());

		createTheContent();
		Util.centerWindow(this);
	}

	/**
	 * 
	 */
	public void createTheContent(){
		mainPanel.setBorder(BorderFactory.createTitledBorder("Task"));
		setUpBeginnAndEnd();
		setUpTheInputFields();
		setUpTheStatusComboBox();
		setUpTheTopicComboBox();
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

	private void setUpTheInputFields(){
		JLabel enterTopic = new JLabel("Topic: ");
		JLabel enterAbout = new JLabel("About: ");

		inputFieldAbout.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		//Set the bounds of the Labels
		enterTopic.setBounds(10, 28, 100, 28);
		enterAbout.setBounds(10, 28, 100, 28);

		//Set the bounds of the text fields.
		inputFieldAbout.setBounds(10, 28, 360, 128);

		//Set the bounds of the info labels.
		beginInfoLabel.setBounds(10, 100, 180,28);
		endInfoLabel.setBounds(10, 100, 180,28);

		//Set the location of the labels.
		enterTopic.setLocation(40, 30);
		enterAbout.setLocation(40, 60);

		//Set the input locations.
		inputFieldAbout.setLocation(120, 60);

		//Add all labels and text fields to the addPanel.
		mainPanel.add(enterTopic);
		mainPanel.add(enterAbout);
		//mainPanel.add(inputFieldTopic);
		mainPanel.add(inputFieldAbout);
	}

	/**
	 * 
	 */
	private void setUpBeginnAndEnd(){
		JLabel beginLable = new JLabel("Begin: ");
		JLabel endLable = new JLabel("End: ");

		//Set a input format for begin and end input fields.
		NumberFormat inputFormate = NumberFormat.getNumberInstance(); 
		inputFormate.setMaximumIntegerDigits(8);
		inputFormate.setGroupingUsed(false); 
		inputFieldBegin = new JFormattedTextField(inputFormate);
		inputFieldEnd = new JFormattedTextField(inputFormate);
		inputFieldBegin.setText(ft.format(date));
		inputFieldEnd.setText(ft.format(date));

		//Set the bounds of the text fields.
		inputFieldBegin.setBounds(10, 100, 150,28);
		inputFieldEnd.setBounds(10, 100, 150,28);

		//Set the bounds of the Labels
		beginLable.setBounds(10, 100, 100, 28);
		endLable.setBounds(10, 100, 100, 28);

		//Set the location of the labels.
		beginLable.setLocation(40, 230);
		endLable.setLocation(290, 230);

		//Set the input locations.
		inputFieldBegin.setLocation(120, 230);
		inputFieldEnd.setLocation(330, 230);

		//Add all labels and text fields to the addPanel.
		mainPanel.add(beginLable);
		mainPanel.add(endLable);

		mainPanel.add(beginInfoLabel);
		mainPanel.add(endInfoLabel);
		mainPanel.add(inputFieldBegin);
		mainPanel.add(inputFieldEnd);
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
				if(String.valueOf(topicBox.getSelectedItem()).trim().isEmpty()  || 
						inputFieldAbout.getText().trim().isEmpty() ||
						inputFieldBegin.getText().trim().isEmpty() ||
						inputFieldEnd.getText().trim().isEmpty() ){
					JOptionPane.showMessageDialog(null, "At least one inputfeald is empty.");
				}else{
					table.addRow(String.valueOf(topicBox.getSelectedItem()), inputFieldAbout.getText(), inputFieldBegin.getText(), inputFieldEnd.getText(), String.valueOf(status.getSelectedItem()));
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
	 * TODO
	 */
	private void setUpTheTopicComboBox(){
		table = (TaskTable) window.getTaskTable();
		List<String> list = new ArrayList<>();
		for(int i=0; i < table.getRowCount(); i++ ){
			String current = (String) table.getValueAt(i,0);
			if(!list.contains(current) && current != null){
				list.add(current);
			}
		}

		String[] out = new String[list.size()];
		for(int i=0;i<list.size();i++){
			out[i] = list.get(i);
		}

		topicBox = new JComboBox<String>(out);
		topicBox.setEditable(true);
		topicBox.setBounds(10, 28, 361,28);
		topicBox.setLocation(120,30);
		mainPanel.add(topicBox);
	}

	/**
	 * 
	 */
	private void setUpTheStatusComboBox(){
		JLabel statusLable = new JLabel("Status ");
		statusLable.setBounds(10, 100, 100, 28);
		statusLable.setLocation(40, 195);

		status = new JComboBox<String>(new String[]{"not_started","started","half-finished", "finished", "delivered"});
		status.setSelectedIndex(0);
		status.setBounds(10, 100, 150, 28);
		status.setLocation(120, 195);

		mainPanel.add(statusLable);
		mainPanel.add(status);
	}

	/**
	 * Resets all input fields of this Frame.
	 */
	public void resetInputsAddFrame(){
		inputFieldAbout.setText("");
		inputFieldBegin.setText(ft.format(date));
		inputFieldEnd.setText(ft.format(date));
		status.setSelectedIndex(0);
	}

	//Setter
	public void setInputFieldAbout(final String text){this.inputFieldAbout.setText(text);}
	public void setInputFieldBegin(final String text){this.inputFieldBegin.setText(text);}
	public void setInputFieldEnd(final String text){this.inputFieldEnd.setText(text);}
	public void setStatus(final int anIndex){this.status.setSelectedIndex(anIndex);}

}