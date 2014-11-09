package bin.rem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.swing.AbstractListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * @author ovae
 * @version alpha 0.0.8
 */
public class GUI {
	
	//Main window
	private static int mainWindowWidth = 800;
	private static int mainWindowHeight = 512;
	private static JFrame mainWindow = new JFrame();
	
	//Toolbar
	private JToolBar toolbar = new JToolBar();
	//Button to add a new tasks to the task table.
	private JButton newButton = new JButton("new");
	//Button to remove selected rows from the task or archive table.
	private JButton removeButton = new JButton("remove");
	//Button to change the state of the tasks.
	private JButton doneButton = new JButton("done");
	//Button to archive older tasks in the archive table.
	private JButton shiftToArchiv = new JButton("archive");
	//Button to open the settings.
	private JButton settingsButton = new JButton("settings");
	//Button to open the info window.
	private JButton infoButton = new JButton("info");
	//Button to save the program.
	private JButton saveAll = new JButton("save");
	
	//AddFrame
	private JFrame addFrame = new JFrame("New");
	private JTextField inputTopic = new JTextField("");
	private JTextField inputAbout = new JTextField("");
	
	private JFormattedTextField inputBegin;
	private JTextField inputEnd = new JTextField("");
	private JLabel beginInfoLabel = new JLabel("");
	private JLabel endInfoLabel = new JLabel("");
	private JButton addButton = new JButton("add");
	private JButton resetButton = new JButton("reset");
	
	//Main Panel
	private JPanel mainPanel = new JPanel(new BorderLayout());
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JScrollPane scroll = new JScrollPane(mainPanel);
	private JPanel infoPanel = new JPanel(new BorderLayout());
	private JLabel infoDateLabel = new JLabel();
	private JLabel saveStateLabel = new JLabel();
	
	//Info Window
	private JFrame infoFrame = new JFrame("Info");
	
	//Settings Window
	private JFrame settingsWindow = new JFrame("Settings");
	private JTextField userfilesDirectoryInput = new JTextField();
	private JPanel settingsPanel = new JPanel(new BorderLayout());
	private JCheckBox lookBox = new JCheckBox("Nimbus");
	private JCheckBox useColorsBox = new JCheckBox("Color");
	private JCheckBox useTimeFormateA = new JCheckBox();
	private JCheckBox useTimeFormateB = new JCheckBox();
	private JCheckBox useTimeFormateC = new JCheckBox();
	private char selectedTimeFormate = 'a';
	private boolean isLookBox = false;
	private boolean isColorBox = false;
	private JLabel selectedLook = new JLabel("Error");
	private boolean haveColorCheckBoxChanged = false;

	//Archive Pane
	private JPanel archivePanel= new JPanel(new BorderLayout());
	private JScrollPane scrollArchive = new JScrollPane(archivePanel);
	private String[] columnNamesArchive = {"Topic","About","Begin","End", "Result"};
	private Object[][] archiveList;
	private JTable tableArchive = new JTable(new DefaultTableModel(archiveList,columnNamesArchive));
		
	//Task Table
	private String[] columnNames = {"Topic","About","Begin","End", "Status"};
	private Object[][] streams;
	private JTable tableTasks = new JTable(new DefaultTableModel(streams,columnNames));

	//Preferences
	private RemPreference remPref = new RemPreference();
	private String tempUserPrefsPath = "";
	
	//Files
	private File userfilesDirectory = new File("");
	
	//Other, Icons
	private Icon iconWarning = UIManager.getIcon("OptionPane.warningIcon");
	private Icon iconInfo = UIManager.getIcon("OptionPane.informationIcon");
	static Object[] status = {"not_started","started","half-finished","finished","delivered"};
	
	//Debug
	private boolean debugMode = false;
	
	/**
	 * Constructor
	 */
	public GUI(){
	}
	
	/**
	 * Initialize the main thread.
	 */
	public void init(){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				//Set the debug mode if its on.
				setDebugMode();

				//initiate the preference instance.
				remPref.setPreference();

				//initiate the main window with all its components.
				setWindow();
				setToolbar();
				setMainPanel();
				initAddTaskFrame();
				setTable();
				setArchiveTable();
				setInfoPanel();

				//load the table contents.
				loadFromJSON(tableTasks,"/tasks.json");
				loadFromJSON(tableArchive, "/archive.json");

				//load the preferences.
				loadTimeFormatePreferences();

				//check methods
				checkIfTableHasChanged();
				checkLook();
				checkColorBox();
			}
		});
	}
	
	/**
	 * Set the main window to visible
	 */
	public void start(){
		mainWindow.setVisible(true);
	}
	
	/**
	 * Hide the main window
	 */
	public void stop(){
		mainWindow.setVisible(false);
	}
	
	/**
	 * Sets the frame parameter centered in the screen
	 * @param frame
	 */
	private static void centerWindow(JFrame frame) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}

	//Main-Window**************************************************************************************************************
	/**
	 * Set the basic settings of the main window.
	 */
	private void setWindow(){
		mainWindow.setTitle("Reminder");
		mainWindow.setSize(new Dimension(mainWindowWidth,mainWindowHeight));
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setMinimumSize(new Dimension(640, mainWindowHeight));
		mainWindow.setLayout(new BorderLayout());
		centerWindow(mainWindow);
	}
	
	/**
	 * Set up the main panel
	 * and the position of the containers
	 */
	private void setMainPanel(){
		mainWindow.add(toolbar, BorderLayout.NORTH);
		tabbedPane.addTab("Latest", null, scroll,"Latest tab");
		tabbedPane.addTab("Archive", null, scrollArchive,"Archive tab");
		mainWindow.add(tabbedPane, BorderLayout.CENTER);
		mainWindow.add(infoPanel,BorderLayout.SOUTH);
	}

	//Toolbar******************************************************************************************************************
	/**
	 * Setting up the toolbar and all of its compounds and action listeners.
	 */
	private void setToolbar(){
		//Disable the drag functionality of the table header.
		toolbar.setFloatable(false);

		/* The action listener of the newButton.
		 * If the newButton is pressed:
		 * the required preferences for the AddFrame are loaded,
		 * by default all the inputs are reset.
		 */
		newButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				loadAddFramePreference();
				resetInputsAddFrame();
				setAddFrameVisible();
			}
		});

		/* The action listener of the removeButton.
		 * If the removeButton is pressed and the user has select at least one table row,
		 * the selected rows will be removed and the task and archive tables are saved.
		 */
		removeButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int p =JOptionPane.showConfirmDialog(null, "Do you want to remove it.","Select an Option",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(p==0){
					removeTableRow();
					saveTableToJSON(tableTasks, columnNames, "/tasks.json");
					saveTableToJSON(tableArchive, columnNamesArchive,"/archive.json");
				}
			}
		});

		/* The action listener of the doneButton.
		 * If the doneButton is pressed the status cell of the selected
		 * row will be changed in its state.
		 * After that the task and archive table are saved.
		 */
		doneButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				updateTableRow();
				saveTableToJSON(tableTasks, columnNames, "/tasks.json");
				saveTableToJSON(tableArchive, columnNamesArchive,"/archive.json");
			}
		});

		/* The action listener of the saveAll button.
		 * If the saveAll button is pressed the content of the task and
		 * archive table are saved. And the saveStatusLabel is set, if 
		 * the save process was successfully to [saved] or other wise to [saving failed].
		 */
		saveAll.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				try{
					saveTableToJSON(tableTasks, columnNames, "/tasks.json");
					saveTableToJSON(tableArchive, columnNamesArchive,"/archive.json");
					if(debugMode){
						System.out.println(Time.getTimeDebug()+" Saving successfully [SaveButton_toolbar].");
					}
					saveStateLabel.setText("[saved]");
				}catch(Exception f){
					if(debugMode){
						System.out.println(Time.getTimeDebug()+" Saveing failed [SaveButton_toolbar].");
					}
					saveStateLabel.setText("[saving failed]");
				}
			}
			
		});

		/* The action listener of the shiftToArchive button.
		 * Copies the selected rows of the task table in the archive table
		 * and removes the selected rows from the task table.
		 */
		shiftToArchiv.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				shiftTableItemsinArchive();
			}
		});

		/* The action listener of the settingsButton.
		 * If the button is pressed the settings window will be appear on the screen.
		 */
		settingsButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				setSettingsWindow();
				String lastOutputDir = null;
				try{
					//get the userPath from the preference instance
					lastOutputDir = remPref.getUserPath();
					//set the help variables
					isLookBox = remPref.getLookCheckBox();
					isColorBox = remPref.getColorCheckBox();
					//If the checkbox isLookBox is activated 
					// 'Nimbus' is written in the selectedLook label
					//or otherwise 'Default'.
					if(isLookBox){
						selectedLook.setText("Nimbus");
					}else{
						selectedLook.setText("Default");
					}
					//if the Nimbus look is set in the preferences the checkbox will be set to selected.
					lookBox.setSelected(isLookBox);
					//if the color for the tables are set in the preferences, the checkbox will be set to selected.
					useColorsBox.setSelected(isColorBox);
					if(debugMode){
						System.out.println(Time.getTimeDebug()+" Load preference succesfully.");
					}
				}catch(Exception d){
					if(debugMode){
						System.err.println(Time.getTimeDebug()+" Loading preference error.");
					}
				}
				//get the user file path from the preferences and set the label to it.
				userfilesDirectoryInput.setText(lastOutputDir);
			}
		});
		
		/* The action listener of the infoButton.
		 * If the button is pressed the Info window will be appear.
		 */
		infoButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				setInfoFrame();
			}
		});
		
		//Add all compounds to the toolbar.
		toolbar.add(newButton);
		toolbar.add(removeButton);
		toolbar.add(doneButton);
		toolbar.add(saveAll);
		toolbar.add(shiftToArchiv);
		toolbar.addSeparator(new Dimension(3, 10));
		toolbar.add(settingsButton);
		toolbar.add(infoButton);
	}
	//Info-Frame***************************************************************************************************************
	/**
	 * Setting up the info window
	 */
	private void setInfoFrame(){
		//set the basic setting for the frame.
		infoFrame.setLocationRelativeTo(null);
		infoFrame.setSize(new Dimension(350,290));
		infoFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		infoFrame.setLayout(new BorderLayout());
		
		//Declare all needed compounds
		JPanel infoPanel = new JPanel();
		JLabel head = new JLabel("Declaration on the choice of color");
		JTextField green = new JTextField();
		JTextField gray = new JTextField();
		JTextField red = new JTextField();
		JTextField orange = new JTextField();
		JTextField yellow = new JTextField();
		JTextField blue = new JTextField();
		JTextField white = new JTextField();
		JLabel greenLabel = new JLabel("More then 2 days time");
		JLabel grayLabel = new JLabel("Delivered");
		JLabel redLabel = new JLabel("Delivery day");
		JLabel orangeLabel = new JLabel("One day left");
		JLabel yellowLabel = new JLabel("Two days left");
		JLabel blueLabel = new JLabel("Selected row");
		JLabel whiteLabel = new JLabel("Default value");
		
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

		//Set the bounds of the heading label
		head.setBounds(10, 100, 240,28);
		head.setLocation(20, 20);
		
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
		infoPanel.add(head);
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
		infoFrame.add(infoPanel);
		infoFrame.setVisible(true);
		//center the info window on the screen.
		centerWindow(infoFrame);
	}

	//Add-task-window**********************************************************************************************************
	/**
	 * all settings and configurations of the addFrame
	 * you can add new tasks over this window.
	 */
	private void initAddTaskFrame(){
		//set the basic setting for the frame.
		addFrame.setLocationRelativeTo(null);
		addFrame.setSize(new Dimension(400,256));
		addFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		addFrame.setLayout(new BorderLayout());
		//FormattedTextField Begin
		NumberFormat inputBeginFormate = NumberFormat.getNumberInstance(); 
		inputBeginFormate.setMaximumIntegerDigits(8);
		inputBeginFormate.setGroupingUsed(false); 
		inputBegin = new JFormattedTextField(inputBeginFormate);

		//Declare all needed compounds.
		JPanel addPanel = new JPanel();
		JPanel buttonPanel = new JPanel(new BorderLayout());
		JLabel enterTopic = new JLabel("Topic: ");
		JLabel enterAbout = new JLabel("About: ");
		JLabel enterBegin = new JLabel("Begin: ");
		JLabel enterEnd = new JLabel("End: ");

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
				//Get the values of the text fields.
				String topic = inputTopic.getText();
				String about = inputAbout.getText();
				String begin = inputBegin.getText();
				String end = inputEnd.getText();

				//Check if one of the text fields is empty.
				if(inputTopic.getText().equals("") || 
						inputAbout.getText().equals("") ||
						inputBegin.getText().equals("") ||
						inputEnd.getText().equals("") ){
					JOptionPane.showMessageDialog(null, "At least one inputfeald is empty.");
				}else{
					//if(checkInputBeginEnd()){
						addTableRow(topic,about,begin,end);
					//}
				}
				//checkInputBeginEnd();
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
		//Add the buttons to the buttonPanel.
		buttonPanel.add(resetButton,BorderLayout.WEST);
		buttonPanel.add(addButton, BorderLayout.CENTER);
		//Add the panels to the addFrame.
		addFrame.add(addPanel,BorderLayout.CENTER);
		addFrame.add(buttonPanel, BorderLayout.SOUTH);
		//Disable the default visibility.
		addFrame.setVisible(false);
		//Center the add Frame on the screen.
		centerWindow(addFrame);
	}

	/**
	 * Sets the addFrame to visible.
	 */
	private void setAddFrameVisible(){
		addFrame.setVisible(true);
	}

	/**
	 * Resets the all input fields of the addFrame.
	 */
	private void resetInputsAddFrame(){
		inputTopic.setText("");
		inputAbout.setText("");
		inputBegin.setText("");
		inputEnd.setText("");
	}

	/**
	 * Loads the time format preference.
	 */
	private void loadAddFramePreference(){
		int currentFormate = remPref.getTimeFormate();
		String beginOutput = "[ ";
		String endOutput = "[ ";

		//Check if Begin and End have the length of 8.
		if(currentFormate == 'a'){
			//time format A
			beginOutput += "yyyyMMdd";
			endOutput += "yyyyMMdd";
		}else if(currentFormate == 'b'){
			//time format B
			beginOutput += "MMddyyyy";
			endOutput += "MMddyyyy";
		}else{
			//time format C
			beginOutput += "ddMMyyyy";
			endOutput += "ddMMyyyy";
		}
		beginOutput += " ]";
		endOutput += " ]";

		beginInfoLabel.setText(beginOutput);
		endInfoLabel.setText(endOutput);
	}

	/**
	 * 
	 */
	private boolean checkInputBeginEnd(){
		boolean ret=false;
		String tempTextBegin = inputBegin.getText();
		String tempTextEnd = inputEnd.getText();
		String month = Time.getCurrentMonth();
		String year = Time.getCurrentYear();
		
		String subAEndY = tempTextEnd.substring(0,4);
		String subAEndM = tempTextEnd.substring(4,6);
		String subBEndM = tempTextEnd.substring(0,2);
		String subBEndY = tempTextEnd.substring(4,8);
		String subCEndM = tempTextEnd.substring(4,6);
		String subCEndY= tempTextEnd.substring(4,8);
		int currentFormate = remPref.getTimeFormate();

		String beginOutput = "[ ";
		String endOutput = "[ ";

		//Check if Begin and End have the length of 8.
		if(tempTextBegin.length()==8 && tempTextBegin.length()==8){
			if(currentFormate == 'a'){
				//timeformate A
				beginOutput += "yyyyMMdd";
				endOutput += "yyyyMMdd";
			}else if(currentFormate == 'b'){
				//timeformate B
				beginOutput += "MMddyyyy";
				endOutput += "MMddyyyy";
			}else{
				//timeformate C
				beginOutput += "ddMMyyyy";
				endOutput += "ddMMyyyy";
			}
			ret=true;
		}else{
			beginOutput += " Error";
			endOutput += " Error";
			ret=false;
		}
		beginOutput += " ]";
		endOutput += " ]";

		beginInfoLabel.setText(beginOutput);
		endInfoLabel.setText(endOutput);

		return ret;
		/*
		if(subAEndY.equals(year) && subAEndM.equals(month) ){
			//timeformate A
			endInfoLabel.setText("[A Ok]");
		}else if(subBEndY.equals(year) && subBEndM.equals(month) ){
			//timeformate B
			endInfoLabel.setText("[B Ok]");
		}else if(subCEndY.equals(year) && subCEndM.equals(month) ){
			//timeformate C
			endInfoLabel.setText("[C Ok]");
		}else{
			//set lables to error
			endInfoLabel.setText(endOutput+" Error]");
		}
		*/
	}

	//Info-panel***************************************************************************************************************
	/**
	 * Set the info panel with date label.
	 */
	private void setInfoPanel(){
		infoDateLabel.setText("Date: "+Time.getDate("yyyy.MM.dd"));
		saveStateLabel.setText("[saved]");

		infoPanel.add(infoDateLabel, BorderLayout.CENTER);
		infoPanel.add(saveStateLabel, BorderLayout.EAST);
	}

	/**
	 * Set the time format labels of the addFrame.
	 */
	private void setDateLable(){
		if(useTimeFormateA.isSelected()){
			infoDateLabel.setText("Date: "+Time.getDate("yyyy.MM.dd"));
		}else if(useTimeFormateB.isSelected()){
			infoDateLabel.setText("Date: "+Time.getDate("MM.dd.yyyy"));
		}else if(useTimeFormateC.isSelected()){
			infoDateLabel.setText("Date: "+Time.getDate("dd.MM.yyyy"));
		}else{
			infoDateLabel.setText("Date: "+Time.getDate()+"[default]");
		}
	}

	//Settings-window**********************************************************************************************************
	/**
	 * Setting up the settings window
	 */
	private void setSettingsWindow(){
		//Declare all compounds for the settings window.
		JLabel filePathLabel = new JLabel("Path of the tasks file:");
		JButton saveSettingsButton = new JButton("save settings");
		JButton getPathButton = new JButton("Change");
		JLabel lookLabel = new JLabel("Use an alternative look:");
		JLabel selectedLookLabel = new JLabel("Selected look:");
		JLabel selectedColorLabel = new JLabel("Use colors:");
		JLabel timeFormatLabel = new JLabel("Select your favorite time format:");
		JLabel timeCheckBoxA = new JLabel("yyyyMMdd");
		JLabel timeCheckBoxB = new JLabel("MMddyyyy");
		JLabel timeCheckBoxC = new JLabel("ddMMyyyy");

		//Set the basic settings for the settings window.
		settingsWindow.setLocationRelativeTo(null);
		settingsWindow.setSize(new Dimension(512, 350));
		settingsWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		settingsPanel.setLayout(null);

		//Try to get the user file directory from the preference instance.
		try{
			tempUserPrefsPath =remPref.getUserPath();
			loadTimeFormatePreferences();
		}catch(Exception e){
			if(debugMode){
				System.err.println("\n"+Time.getTimeDebug()+" Loading tempUserPrefsPath error. \n");
			}
		}

		//Set the bounds of the labels and text fields.
		filePathLabel.setBounds(10, 100, 340,28);
		filePathLabel.setLocation(40,20);
		userfilesDirectoryInput.setBounds(10, 100, 318,28);
		userfilesDirectoryInput.setLocation(40, 40);
		getPathButton.setBounds(10, 100, 100,27);
		getPathButton.setLocation(360, 40);

		lookLabel.setBounds(10, 100, 340, 28);
		lookLabel.setLocation(40,80);
		//Checkbox gui look
		lookBox.setBounds(10, 10, 20, 28);
		lookBox.setLocation(210,80);
		//Chekbox gui look label
		selectedLookLabel.setBounds(10, 100, 150, 28);
		selectedLookLabel.setLocation(240,80);
		//Checkbox gui look, label that changes
		selectedLook.setBounds(10, 100, 340, 28);
		selectedLook.setLocation(350,80);

		//CheckBox time format
		useTimeFormateA.setBounds(10, 100, 340, 28);
		useTimeFormateB.setBounds(10, 100, 340, 28);
		useTimeFormateC.setBounds(10, 100, 340, 28);
		useTimeFormateA.setLocation(210,160);
		useTimeFormateB.setLocation(210,180);
		useTimeFormateC.setLocation(210,200);

		//CheckBox time format label
		timeFormatLabel.setBounds(10,100,300,28);
		timeFormatLabel.setLocation(40,140);
		timeCheckBoxA.setBounds(10,100,100,28);
		timeCheckBoxB.setBounds(10,100,100,28);
		timeCheckBoxC.setBounds(10,100,100,28);
		timeCheckBoxA.setLocation(40,160);
		timeCheckBoxB.setLocation(40,180);
		timeCheckBoxC.setLocation(40,200);

		//Color checkbox label
		selectedColorLabel.setBounds(10,100,150,28);
		selectedColorLabel.setLocation(40,100);
		//Color checkbox
		useColorsBox.setBounds(10, 10, 20, 28);
		useColorsBox.setLocation(210,100);

		/* The action listener of the getPathButton.
		 * If the button is pressed a new window appear.
		 * The user must select a directory where the user files
		 * are stored.
		 */
		getPathButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				getFilePath();
				//saveSettings();
			}
		});

		/* The action listener of the lookBox checkbox.
		 * If the box is selected or deselected and the saveSettings button is pressed the program checks if the
		 * look checkbox is set to selected. If it is selected the GUI-look will be Nimbus.
		 * If it is not selected the GUI-look will be the default java swing look.
		 * The look changes appear after a program restart.
		 */
		lookBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(isLookBox){
					isLookBox = false;
					selectedLook.setText("Nimbus");
					haveColorCheckBoxChanged=true;
					JOptionPane.showMessageDialog(null, "After a restart is the default look activated.");
				}else{
					isLookBox = true;
					selectedLook.setText("Default");
					haveColorCheckBoxChanged=true;
					JOptionPane.showMessageDialog(null, "After a restart is the Nimbus look activated.");
				}
				//saveSettings();
			}
		});

		/* The action listener of the useColorBox checkbox.
		 * If the box is selected or deselected and the saveSettings button is pressed the program checks if the
		 * color checkbox is selected or not. If it is selected the tables row will be colored. Otherwise they were
		 * set to white.
		 */
		useColorsBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(isColorBox){
					isColorBox = false;
					if(debugMode){
						System.out.println(Time.getTimeDebug()+" Disable Colors.");
					}
				}else{
					isColorBox = true;
					if(debugMode){
						System.out.println(Time.getTimeDebug()+" Enable Colors.");
					}
				}
			}
		});

		/* The action listener of the saveSettingsButton button.
		 * If the button is pressed the preferences will be saved.
		 * If the directory of the user files has changed and no files
		 * where found to load, the tables where cleaned.
		 * Also the settings will be saved and the date label of the main window is set set.
		 */
		saveSettingsButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//If the userPath have changed to nothing:
				if(tempUserPrefsPath.equals( userfilesDirectoryInput.getText() )){
					if(debugMode){
						System.out.println(Time.getTimeDebug()+" Userpath dont changed [loadTable].");
					}
				}else{
					//Else do this:
					//loadTableItemsFromFile();
					loadFromJSON(tableTasks,"/tasks.json");
					loadFromJSON(tableArchive, "/archive.json");
					if(debugMode){
						System.out.println(Time.getTimeDebug()+" Userpath have changed [loadTable].");
					}
				}
				saveSettings();
				setDateLable();
				loadAddFramePreference();
				JOptionPane.showMessageDialog(null, "settings saved");
			}
		});

		/* The action listener of the useTimeFormateA checkbox.
		 * If this checkbox is set the other to formats are set
		 * to false.
		 */
		useTimeFormateA.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				useTimeFormateB.setSelected(false);
				useTimeFormateC.setSelected(false);
				selectedTimeFormate = 'a';
			}
		});

		/* The action listener of the useTimeFormateB checkbox.
		 * If this checkbox is set the other to formats are set
		 * to false.
		 */
		useTimeFormateB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				useTimeFormateA.setSelected(false);
				useTimeFormateC.setSelected(false);
				selectedTimeFormate = 'b';
			}
		});

		/* The action listener of the useTimeFormateC checkbox.
		 * If this checkbox is set the other to formats are set
		 * to false.
		 */
		useTimeFormateC.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				useTimeFormateA.setSelected(false);
				useTimeFormateB.setSelected(false);
				selectedTimeFormate = 'c';
			}
		});

		//Add all elements to the settingsPanel
		settingsPanel.add(filePathLabel);
		settingsPanel.add(userfilesDirectoryInput);
		settingsPanel.add(getPathButton);
		settingsPanel.add(lookLabel);
		settingsPanel.add(lookBox);
		settingsPanel.add(selectedLookLabel);
		settingsPanel.add(selectedLook);
		settingsPanel.add(selectedColorLabel);
		settingsPanel.add(useColorsBox);

		settingsPanel.add(timeFormatLabel);
		settingsPanel.add(timeCheckBoxA);
		settingsPanel.add(timeCheckBoxB);
		settingsPanel.add(timeCheckBoxC);
		settingsPanel.add(useTimeFormateA);
		settingsPanel.add(useTimeFormateB);
		settingsPanel.add(useTimeFormateC);
		//Add the settingsPanel to the settingsWindow
		settingsWindow.add(settingsPanel, BorderLayout.CENTER);
		settingsWindow.add(saveSettingsButton, BorderLayout.SOUTH);
		//Make the settingsWindow visible by default.
		settingsWindow.setVisible(true);
		//Center the window on the screen.
		centerWindow(settingsWindow);
	}
	
	/**
	 * Load the time format preference for the settingsFrame.
	 */
	private void loadTimeFormatePreferences(){
		int tempTimeForamte = 0;
		try{
			tempTimeForamte = remPref.getTimeFormate();
			if(tempTimeForamte == 'a'){
				useTimeFormateA.setSelected(true);
			}else if(tempTimeForamte == 'b'){
				useTimeFormateB.setSelected(true);
			}else{
				useTimeFormateC.setSelected(true);
			}
			setDateLable();
		}catch(Exception e){
			if(debugMode){
				System.err.println(Time.getTimeDebug()+" Loading time formate error");
			}
		}

	}
	
	/**
	 * Method to get the filepath from an filechooser.
	 */
	private void getFilePath(){
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int response = fc.showOpenDialog(null);
		if(response == JFileChooser.APPROVE_OPTION){
			userfilesDirectory = fc.getSelectedFile();
			userfilesDirectoryInput.setText(userfilesDirectory.toString());
		}
	}
	
	/**
	 * Save the settings.
	 */
	private void saveSettings(){
		try{
			remPref.setLookCheckBox(isLookBox);
			remPref.setColorCheckBox(isColorBox);
			remPref.setTimeFormate(selectedTimeFormate);
			//TODO
			//If the userPath havened change do nothing
			if(tempUserPrefsPath.equals( userfilesDirectoryInput.getText() )){
				if(debugMode){
					System.out.println(Time.getTimeDebug()+" Userpath dont changed. [saveTable]");
				}
			}else{
				remPref.setUserPath(userfilesDirectory.toString());
				if(debugMode){
					System.out.println(Time.getTimeDebug()+" Userpath have changed. [saveTable]");
				}
			}
			
			//If the color checkbox have changed and the savesSteiings butten were presed the Table resets its items.
			if(haveColorCheckBoxChanged=true){
				haveColorCheckBoxChanged=false;
				resetTable();
				if(debugMode){
					System.out.println(Time.getTimeDebug()+" resetTable.");
				}
			}

			if(debugMode){
				System.out.println(Time.getTimeDebug()+" Save preference.");
			}
			saveStateLabel.setText("[saved]");
		}catch(Exception e){
			if(debugMode){
				System.err.println(Time.getTimeDebug()+" Save preference error.");
			}
			saveStateLabel.setText("[changed]");
		}
	}
	
	//Gui-look*****************************************************************************************************************
	/**
	 * set an other look of the java gui.
	 */
	private void setLook(){
		try {
			//Nimbus
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look and feel.
		}
	}
	
	/**
	 * Check if the user has set an alternative look of the java GUI
	 */
	private void checkLook(){
		if(remPref.getLookCheckBox() == true){
			setLook();
		}
	}
	
	//Table********************************************************************************************************************
	/**
	 * Set a Table
	 */
	private void setTable(){
		tableTasks.setFillsViewportHeight(true);
		mainPanel.add(tableTasks.getTableHeader(), BorderLayout.PAGE_START);
		mainPanel.add(tableTasks, BorderLayout.CENTER);
		tableTasks.getTableHeader().setReorderingAllowed(false);
	}
	
	/**
	 * Function to add a new row to the Table.
	 * @param topic, about, begin, end
	 * @param url
	 * "Topic","About","Begin","End", "Status"
	 * This method is only yoused in the addNewTaskFrame.
	 */
	private void addTableRow(String topic, String about, String begin, String end){
		DefaultTableModel model = (DefaultTableModel) tableTasks.getModel();
		model.addRow(new Object[]{topic, about, begin, end, status[0]});
		//writeTableItemsToFile();
		saveTableToJSON(tableTasks, columnNames, "/tasks.json");
	}
	
	/**
	 * Function to add a new row to the Table.
	 * @param topic, about, begin, end, status
	 * @param url
	 * "Topic","About","Begin","End", "Status"
	 */
	private void addTableRow(String topic, String about, String begin, String end, String status){
		DefaultTableModel model = (DefaultTableModel) tableTasks.getModel();
		model.addRow(new Object[]{topic, about, begin, end, status});
	}
	
	private void addTableRow(JTable tempTable, String topic, String about, String begin, String end, String status){
			DefaultTableModel model = (DefaultTableModel) tempTable.getModel();
			model.addRow(new Object[]{topic, about, begin, end, status});
	}

	

	/**
	 * Method to remove selected items from the table.
	 */
	private void removeTableRow(){
		int[] rows = tableTasks.getSelectedRows();
		TableModel tm= tableTasks.getModel();
		while(rows.length>0){
			((DefaultTableModel)tm).removeRow(tableTasks.convertRowIndexToModel(rows[0]));
			rows = tableTasks.getSelectedRows();
		}
		tableTasks.clearSelection();
	}

	/**
	 * removes all elements of the table.
	 */
	private void emptyTable(){
		try{
			DefaultTableModel dm = (DefaultTableModel) tableTasks.getModel();
			int rowCount = dm.getRowCount();
			//Remove rows one by one from the end of the table
			for (int i = rowCount - 1; i >= 0; i--) {
				dm.removeRow(i);
			}
			
			if(debugMode){
				System.out.println(Time.getTimeDebug()+" Empty table successfully.");
			}
		}catch(Exception e){
			if(debugMode){
				System.err.println(Time.getTimeDebug()+" Empty table error.");
			}
		}
	}

	/**
	 * Method to add items to the table and update the table.
	 */
	private void updateTableRow(){
		try{
			int[] row = tableTasks.getSelectedRows();
			String value= (String) tableTasks.getModel().getValueAt(row[0], 4);
			if(value == status[0]){
				tableTasks.getModel().setValueAt(status[1], row[0], 4);
			}else if(value == status[1]){
				tableTasks.getModel().setValueAt(status[2], row[0], 4);
			}else if(value == status[2]){
				tableTasks.getModel().setValueAt(status[3], row[0], 4);
			}else if(value == status[3]){
				tableTasks.getModel().setValueAt(status[4], row[0], 4);
			}else{
				tableTasks.getModel().setValueAt(status[0], row[0], 4);
			}
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "You have to select a row.");
		}
	}

	
	/**
	 * Emptys the table and loads it again.
	 */
	private void resetTable(){
		emptyTable();
		checkColorBox();
		if(remPref.getColorCheckBox() == false){
			setTableRowWhite();
		}
		//loadTableItemsFromFile(); TODO
		loadFromJSON(tableTasks,"/tasks.json");
		loadFromJSON(tableArchive, "/archive.json");
	}

	/**
	 * Sort the Table by the end date.
	 */
	private void sortTable(){
		//get the table values
		int tableRows = tableTasks.getRowCount();
		String[][] tableValuesUnSort= new String[tableRows][5];
		for(int i = 0 ; i < tableTasks.getRowCount(); i++){
			for(int j = 0 ; j < 5;j++){
				tableValuesUnSort[i][j] =(String) tableTasks.getValueAt(i,j);
			}
		}
		
		//sort the elements
		int tempA = 0;
		int tempB = 0;
		int tempChange =0;
		boolean changedCompletly;
		do{
			changedCompletly = true;
			for(int i = 0 ; i < tableTasks.getRowCount()-1; i++){
				tempA = Integer.parseInt(tableValuesUnSort[i][3]);
				tempB = Integer.parseInt(tableValuesUnSort[(i+1)][3]);
				if(tempA > tempB){
					tempChange = tempA;
					tableValuesUnSort[i][3] =""+tempB+"";
					tableValuesUnSort[(i+1)][3] = ""+tempChange+"";
					 changedCompletly = false;
				}
			}
		}while(!changedCompletly);
		
		//Empty the table
		emptyTable();
		//add the sorted elements back into the table.
		for(int i = 0 ; i < tableTasks.getRowCount(); i++){
			addTableRow(tableValuesUnSort[i][0],
						tableValuesUnSort[i][1],
						tableValuesUnSort[i][2],
						tableValuesUnSort[i][3],
						tableValuesUnSort[i][4]);
		}
		
	}

	/**
	 * Colorize table rows dependent on the 'end' value
	 */
	private void setTableRowColor(){
		tableTasks.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
				final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				String tableValue = (String)table.getModel().getValueAt(row, 3);
				//String tableStatus = (String) table.getModel().getValueAt(row, 4);
				//get the date of today
				Date date = new Date();
				SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMdd");
				String today =ft.format(date);
				
				int todayParse = Integer.parseInt(""+today+"");
				int valueParse = Integer.parseInt(tableValue);
				
				if((valueParse-todayParse)==0){
					//the delivery day is today
					c.setBackground(new Color(240, 88, 88));//red
				}else if((valueParse-todayParse)<0){
					//the task ist deliverd or you failed the dilevery day.
					c.setBackground(Color.LIGHT_GRAY);
				}else if((valueParse-todayParse)==1){
					//you have one day time to finish your task
					c.setBackground(new Color(255,149,88));//orange
				}else if((valueParse-todayParse)==2){
					c.setBackground(new Color(255,210,120));//yellow
				}else if((valueParse-todayParse)>2){
					//you have more then 2 day time to finish your task
					c.setBackground(new Color(126, 207, 88));//green
				}else{
					c.setBackground(Color.WHITE);
				}
				
				//If you select a row and the row gets blue.
				if(isSelected){
					c.setBackground(new Color(160,166,207));//blue
				}
				
				return c;
			}
		});
	}

	/**
	 * Resets the colored table rows.
	 */
	private void setTableRowWhite(){
		tableTasks.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
			final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				c.setBackground(Color.WHITE);
			return c;
		}
		});
	}
	
	/**
	 * Check if any item of the Table has changed.
	 */
	private void checkIfTableHasChanged(){
		tableTasks.getModel().addTableModelListener(new TableModelListener(){
			public void tableChanged(TableModelEvent tme){
				saveStateLabel.setText("[changed]");
			}
			
		});
	}
	
	/**
	 * Check if the color check box is set in the preferences.
	 */
	private void checkColorBox(){
		if(remPref.getColorCheckBox() == true){
			setTableRowColor();
			setArchiveTableRowColor();
		}
	}
	//Archive******************************************************************************************************************
	/**
	 * Set a archive table
	 */
	private void setArchiveTable(){
		//scrollArchiv.setRowHeaderView(tableArchiv.getTableHeader());
		//scrollArchiv.setColumnHeaderView(new JTable(streams,columnNames));
		tableArchive .setFillsViewportHeight(true);
		archivePanel.add(tableArchive .getTableHeader(), BorderLayout.PAGE_START);
		archivePanel.add(tableArchive , BorderLayout.CENTER);
		tableArchive .getTableHeader().setReorderingAllowed(false);
	}
	
	/**
	 *Shift the selected rows of the Tasks table in the archive table.
	 */
	private void shiftTableItemsinArchive(){
		int[] rows = tableTasks.getSelectedRows();
		TableModel tm= tableTasks.getModel();
		for(int i = 0 ; i < rows.length; i++){
			addTableRow(tableArchive, (String) tableTasks.getValueAt(i,0),
									(String) tableTasks.getValueAt(i,1),
									(String) tableTasks.getValueAt(i,2),
									(String) tableTasks.getValueAt(i,3),
									(String) tableTasks.getValueAt(i,4));
		}
		
		while(rows.length>0){
			((DefaultTableModel)tm).removeRow(tableTasks.convertRowIndexToModel(rows[0]));
			rows = tableTasks.getSelectedRows();
		}
		
		//add the remove function TODO
		
		tableTasks.clearSelection();
	}
	
	/**
	 * The colored table rows dependent on the 'end' value.
	 */
	private void setArchiveTableRowColor(){
		tableArchive.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
				final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				//If you select a row and the row gets blue.
				c.setBackground(Color.LIGHT_GRAY);
				if(isSelected){
					c.setBackground(new Color(160,166,207));//blue
				}
				return c;
			}
		});
	}
	
	//JSON*********************************************************************************************************************
	/**
	 * Save all items of the JTable in an JSON file.
	 * 
	 * @param tempTable
	 * @param tableHead
	 * @param path
	 */
	private void saveTableToJSON(JTable tempTable, String[] tableHead ,String path){
		File tempFile = new File(remPref.getUserPath().toString()+path);
		RemJSON rjson = new RemJSON();
		for(int i=0; i<tempTable.getRowCount(); i++){
			rjson.setTask(tableHead,
					(String) tempTable.getValueAt(i,0),
					(String) tempTable.getValueAt(i,1),
					(String) tempTable.getValueAt(i,2),
					(String) tempTable.getValueAt(i,3),
					(String) tempTable.getValueAt(i,4));
		}
		rjson.writeJsonToFile(tempFile);
	}
	
	
	/**
	 * Load all items for a JTable from an JSON file.
	 * @param tempTable
	 * @param path
	 */
	private void loadFromJSON(JTable tempTable, String path){
		FileReader reader;
		File tempFile = new File(remPref.getUserPath().toString()+path);
		try {
			reader = new FileReader(tempFile);
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
			JSONArray lang= (JSONArray) jsonObject.get("Tasks");
			
			for(int i=0; i<lang.size(); i++){
				//System.out.println("The " + i + " element of the array: "+lang.get(i));
			}
			Iterator i = lang.iterator();

			// take each value from the json array separately
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				addTableRow(tempTable, (String) innerObj.get("Topic"),
						(String) innerObj.get("About"),
						(String) innerObj.get("Begin"),
						(String) innerObj.get("End"),
						(String) innerObj.get("Status"));
			}
		} catch (FileNotFoundException | ParseException e) {
			// Auto-generated catch block
			//e.printStackTrace();
		}catch ( IOException f){
			f.printStackTrace();
		}
	}

	
	//Debug********************************************************************************************************************
	/**
	 * Prints out a heading in a terminal if the debug mode is activated.
	 */
	private void setDebugMode(){
		if(debugMode){
			System.out.println("Reminder-DebugMode");
			System.out.println("==================");
		}
	}
}
