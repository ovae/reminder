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

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
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
 * 
 * @author ovae
 * @version
 */
public class GUI {
	
	//Main window
	private static int mainWindowWidth = 800;
	private static int mainWindowHeight = 512;
	private static JFrame mainWindow = new JFrame();
	
	//Toolbar
	private JToolBar toolbar = new JToolBar();
	private JButton newButton = new JButton("new");
	private JButton removeButton = new JButton("remove");
	private JButton doneButton = new JButton("done");
	private JButton shiftToArchiv = new JButton("archive");
	private JButton settingsButton = new JButton("settings");
	private JButton infoButton = new JButton("info");
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
	private JScrollPane scroll = new JScrollPane(mainPanel);
	private JPanel infoPanel = new JPanel(new BorderLayout());
	private JLabel infoDateLabel = new JLabel();
	private JLabel saveStateLabel = new JLabel();

	//Setings Window
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
	
	private JTabbedPane tabbedPane = new JTabbedPane();
	//Archiv
	private JPanel archivPanel= new JPanel(new BorderLayout());
	private JScrollPane scrollArchiv = new JScrollPane(archivPanel);
	private String[] columnNamesArchiv = {"Topic","About","Begin","End", "Result"};
	private Object[][] archivList;
	private JTable tableArchiv = new JTable(new DefaultTableModel(archivList,columnNamesArchiv));
	
	//Info Window
	private JFrame infoFrame = new JFrame("Info");
	
	//Table
	private String[] columnNames = {"Topic","About","Begin","End", "Status"};
	private Object[][] streams;
	private JTable table = new JTable(new DefaultTableModel(streams,columnNames));

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
	 * Initialise the main window.
	 */
	public void init(){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				setDebugMode();
				remPref.setPreference();
				
				setWindow();
				checkLook();
				setToolbar();
				setMainPanel();
				initAddTaskFrame();
				setTable();
				//loadTableItemsFromFile();
				loadFromJSON(table,"/tasks.json");
				loadFromJSON(tableArchiv, "/archive.json");
				checkColorBox();
				setInfoPanel();
				
				loadTimeFormatePreferences();
				checkIfTableHasChanged();
				
				setArchivTable();
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
	 * Set a frame centered in the screen
	 * @param frame
	 */
	private static void centerWindow(JFrame frame) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}

	//Main-window**************************************************************************************************************
	/**
	 * Set the basic information of the main window.
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
	 * set up the main panel
	 */
	private void setMainPanel(){
		mainWindow.add(toolbar, BorderLayout.NORTH);
		//mainWindow.add(scroll, BorderLayout.CENTER);
		tabbedPane.addTab("Latest", null, scroll,"Latest tab");
		tabbedPane.addTab("Archive", null, scrollArchiv,"Archive tab");
		mainWindow.add(tabbedPane, BorderLayout.CENTER);
		mainWindow.add(infoPanel,BorderLayout.SOUTH);
	}
	
	//Toolbar******************************************************************************************************************
	/**
	 * setting up the toolbar
	 */
	private void setToolbar(){
		//Diasable the drag functionality of the table header.d
		toolbar.setFloatable(false);
		newButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				loadAddFramePreference();
				resetInputsAddFrame();
				setAddFrameVisible();
			}
		});
		
		removeButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int p =JOptionPane.showConfirmDialog(null, "Do you want to remove it.","Select an Option",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(p==0){
					removeTableRow();
					saveTableToJSON(table, columnNames, "/tasks.json");
					saveTableToJSON(tableArchiv, columnNamesArchiv,"/archive.json");
				}
			}
		});
		
		
		doneButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				updateTableRow();
				saveTableToJSON(table, columnNames, "/tasks.json");
				saveTableToJSON(tableArchiv, columnNamesArchiv,"/archive.json");
			}
		});
		
		saveAll.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					//TODO
					//writeTableItemsToFile();
					saveTableToJSON(table, columnNames, "/tasks.json");
					saveTableToJSON(tableArchiv, columnNamesArchiv,"/archive.json");
					if(debugMode){
						System.out.println(Time.getTimeDebug()+" Saving successfully [SaveButton_toolbar].");
					}
					saveStateLabel.setText("[saved]");
					//JOptionPane.showMessageDialog(null, "Tasks saved");
				}catch(Exception f){
					//JOptionPane.showMessageDialog(null, "Saving failed");
					if(debugMode){
						System.out.println(Time.getTimeDebug()+" Saveing failed [SaveButton_toolbar].");
					}
					saveStateLabel.setText("[Saving failed]");
				}
			}
			
		});
		
		shiftToArchiv.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				shiftTableItemsinArchiv();
			}
		});
		
		settingsButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				setSettingsWindow();
				String lastOutputDir = null;
				try{
					lastOutputDir = remPref.getUserPath();
					isLookBox = remPref.getLookCheckBox();
					isColorBox = remPref.getColorCheckBox();
					//If the checkox isLookBox is activated 
					// 'Nimbus' is written in the selectedLook label
					//or otherwise 'Default'.
					if(isLookBox){
						selectedLook.setText("Nimbus");
					}else{
						selectedLook.setText("Default");
					}
					lookBox.setSelected(isLookBox);
					useColorsBox.setSelected(isColorBox);
					if(debugMode){
						System.out.println(Time.getTimeDebug()+" Load preference succesfully.");
					}
				}catch(Exception d){
					if(debugMode){
						System.err.println(Time.getTimeDebug()+" Loading preference error.");
					}
				}
				userfilesDirectoryInput.setText(lastOutputDir);
			}
		});
		
		infoButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				setInfoFrame();
			}
		});
		
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
		infoFrame.setLocationRelativeTo(null);
		infoFrame.setSize(new Dimension(350,290));
		infoFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		infoFrame.setLayout(new BorderLayout());
		
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
		
		green.setBackground(new Color(126, 207, 88));
		gray.setBackground(Color.LIGHT_GRAY);
		red.setBackground(new Color(240, 88, 88));
		orange.setBackground(new Color(255,149,88));
		yellow.setBackground(new Color(255,210,120));
		blue.setBackground(new Color(160,166,207));
		white.setBackground(Color.WHITE);
		
		green.setEditable(false);
		gray.setEditable(false);
		red.setEditable(false);
		orange.setEditable(false);
		yellow.setEditable(false);
		blue.setEditable(false);
		white.setEditable(false);

		head.setBounds(10, 100, 240,28);
		head.setLocation(20, 20);
		
		green.setBounds(10, 100, 100,28);
		gray.setBounds(10, 100, 100,28);
		red.setBounds(10, 100, 100,28);
		orange.setBounds(10, 100, 100,28);
		yellow.setBounds(10, 100, 100,28);
		blue.setBounds(10, 100, 100,28);
		white.setBounds(10, 100, 100,28);
		
		greenLabel.setBounds(10, 100, 300,28);
		grayLabel.setBounds(10, 100, 300,28);
		redLabel.setBounds(10, 100, 300,28);
		orangeLabel.setBounds(10, 100, 300,28);
		yellowLabel.setBounds(10, 100, 300,28);
		blueLabel.setBounds(10, 100, 300,28);
		whiteLabel.setBounds(10, 100, 300,28);

		gray.setLocation(20, 45);
		green.setLocation(20, 73);
		yellow.setLocation(20, 101);
		orange.setLocation(20, 129);
		red.setLocation(20, 157);
		blue.setLocation(20, 185);
		white.setLocation(20, 213);
		
		grayLabel.setLocation(130, 45);
		greenLabel.setLocation(130, 73);
		yellowLabel.setLocation(130, 101);
		orangeLabel.setLocation(130, 129);
		redLabel.setLocation(130, 157);
		blueLabel.setLocation(130, 185);
		whiteLabel.setLocation(130, 213);
		
		infoPanel.setLayout(null);
		infoPanel.add(head);
		infoPanel.add(gray);
		infoPanel.add(green);
		infoPanel.add(yellow);
		infoPanel.add(orange);
		infoPanel.add(red);
		infoPanel.add(blue);
		infoPanel.add(white);
		
		infoPanel.add(grayLabel);
		infoPanel.add(greenLabel);
		infoPanel.add(yellowLabel);
		infoPanel.add(orangeLabel);
		infoPanel.add(redLabel);
		infoPanel.add(blueLabel);
		infoPanel.add(whiteLabel);
		
		infoFrame.add(infoPanel);
		infoFrame.setVisible(true);
		centerWindow(infoFrame);
	}
	
	//Add-task-window**********************************************************************************************************
	/**
	 * all settings and configurations of the addFrame
	 * you can add new tasks over this window.
	 */
	private void initAddTaskFrame(){
		addFrame.setLocationRelativeTo(null);
		addFrame.setSize(new Dimension(400,256));
		addFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		addFrame.setLayout(new BorderLayout());
		//FormattedTextField Begin
		NumberFormat inputBeginFormate = NumberFormat.getNumberInstance(); 
		inputBeginFormate.setMaximumIntegerDigits(8);
		inputBeginFormate.setGroupingUsed(false); 
		inputBegin = new JFormattedTextField(inputBeginFormate);
		
		JPanel addPanel = new JPanel();
		JPanel buttonPanel = new JPanel(new BorderLayout());

		JLabel enterTopic = new JLabel("Topic: ");
		JLabel enterAbout = new JLabel("About: ");
		JLabel enterBegin = new JLabel("Begin: ");
		JLabel enterEnd = new JLabel("End: ");
		
		enterTopic.setBounds(10, 100, 100, 28);
		enterAbout.setBounds(10, 100, 100, 28);
		enterBegin.setBounds(10, 100, 100, 28);
		enterEnd.setBounds(10, 100, 100, 28);
		
		inputTopic.setBounds(10, 100, 180,28);
		inputAbout.setBounds(10, 100, 180,28);
		inputBegin.setBounds(10, 100, 180,28);
		inputEnd.setBounds(10, 100, 180,28);
		
		beginInfoLabel.setBounds(10, 100, 180,28);
		endInfoLabel.setBounds(10, 100, 180,28);
		
		enterTopic.setLocation(40, 40);
		enterAbout.setLocation(40, 70);
		enterBegin.setLocation(40, 100);
		enterEnd.setLocation(40, 130);
		
		inputTopic.setLocation(120, 40);
		inputAbout.setLocation(120, 70);
		inputBegin.setLocation(120, 100);
		inputEnd.setLocation(120, 130);
		beginInfoLabel.setLocation(300, 100);
		endInfoLabel.setLocation(300, 130);
		
		addPanel.setLayout(null);
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
		buttonPanel.add(resetButton,BorderLayout.WEST);
		buttonPanel.add(addButton, BorderLayout.CENTER);
		
		addButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String topic = inputTopic.getText();
				String about = inputAbout.getText();
				String begin = inputBegin.getText();
				String end = inputEnd.getText();

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
		
		resetButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				resetInputsAddFrame();
			}
		});
		
		addFrame.add(addPanel,BorderLayout.CENTER);
		addFrame.add(buttonPanel, BorderLayout.SOUTH);
		addFrame.setVisible(false);
		centerWindow(addFrame);
	}
	
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
	 * set the info panel with date label.
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

		settingsWindow.setLocationRelativeTo(null);
		settingsWindow.setSize(new Dimension(512, 350));
		settingsWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		settingsPanel.setLayout(null);

		try{
			tempUserPrefsPath =remPref.getUserPath();
			loadTimeFormatePreferences();
		}catch(Exception e){
			if(debugMode){
				System.err.println("\n"+Time.getTimeDebug()+" Loading tempUserPrefsPath error. \n");
			}
		}

		getPathButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				getFilePath();
				//saveSettings();
			}
		});

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
					loadFromJSON(table,"/tasks.json");
					loadFromJSON(tableArchiv, "/archive.json");
					//TODO
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
		
		
		useTimeFormateA.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				useTimeFormateB.setSelected(false);
				useTimeFormateC.setSelected(false);
				selectedTimeFormate = 'a';
			}
		});
		
		useTimeFormateB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				useTimeFormateA.setSelected(false);
				useTimeFormateC.setSelected(false);
				selectedTimeFormate = 'b';
			}
		});
		
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
		settingsWindow.add(settingsPanel, BorderLayout.CENTER);
		settingsWindow.add(saveSettingsButton, BorderLayout.SOUTH);
		settingsWindow.setVisible(true);
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
		table.setFillsViewportHeight(true);
		mainPanel.add(table.getTableHeader(), BorderLayout.PAGE_START);
		mainPanel.add(table, BorderLayout.CENTER);
		table.getTableHeader().setReorderingAllowed(false);
	}
	
	/**
	 * Function to add a new row to the Table.
	 * @param topic, about, begin, end
	 * @param url
	 * "Topic","About","Begin","End", "Status"
	 * This method is only yoused in the addNewTaskFrame.
	 */
	private void addTableRow(String topic, String about, String begin, String end){
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.addRow(new Object[]{topic, about, begin, end, status[0]});
		//writeTableItemsToFile();
		saveTableToJSON(table, columnNames, "/tasks.json");
	}
	
	/**
	 * Function to add a new row to the Table.
	 * @param topic, about, begin, end, status
	 * @param url
	 * "Topic","About","Begin","End", "Status"
	 */
	private void addTableRow(String topic, String about, String begin, String end, String status){
		DefaultTableModel model = (DefaultTableModel) table.getModel();
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
		int[] rows = table.getSelectedRows();
		TableModel tm= table.getModel();
		while(rows.length>0){
			((DefaultTableModel)tm).removeRow(table.convertRowIndexToModel(rows[0]));
			rows = table.getSelectedRows();
		}
		table.clearSelection();
	}

	/**
	 * removes all elements of the table.
	 */
	private void emptyTable(){
		try{
			DefaultTableModel dm = (DefaultTableModel) table.getModel();
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
			int[] row = table.getSelectedRows();
			String value= (String) table.getModel().getValueAt(row[0], 4);
			if(value == status[0]){
				table.getModel().setValueAt(status[1], row[0], 4);
			}else if(value == status[1]){
				table.getModel().setValueAt(status[2], row[0], 4);
			}else if(value == status[2]){
				table.getModel().setValueAt(status[3], row[0], 4);
			}else if(value == status[3]){
				table.getModel().setValueAt(status[4], row[0], 4);
			}else{
				table.getModel().setValueAt(status[0], row[0], 4);
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
		loadFromJSON(table,"/tasks.json");
		loadFromJSON(tableArchiv, "/archive.json");
	}

	/**
	 * Sort the Table by the end date.
	 */
	private void sortTable(){
		//get the table values
		int tableRows = table.getRowCount();
		String[][] tableValuesUnSort= new String[tableRows][5];
		for(int i = 0 ; i < table.getRowCount(); i++){
			for(int j = 0 ; j < 5;j++){
				tableValuesUnSort[i][j] =(String) table.getValueAt(i,j);
			}
		}
		
		//sort the elements
		int tempA = 0;
		int tempB = 0;
		int tempChange =0;
		boolean changedCompletly;
		do{
			changedCompletly = true;
			for(int i = 0 ; i < table.getRowCount()-1; i++){
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
		for(int i = 0 ; i < table.getRowCount(); i++){
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
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
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
				
				//Check the status
				/*
				if(tableStatus.equals(status[4])){
					c.setBackground(Color.LIGHT_GRAY);
				}
				*/
				
				return c;
			}
		});
	}

	/**
	 * Resets the colorized table rows.
	 */
	private void setTableRowWhite(){
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
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
		table.getModel().addTableModelListener(new TableModelListener(){
			public void tableChanged(TableModelEvent tme){
				saveStateLabel.setText("[changed]");
			}
			
		});
	}
	
	/**
	 * Check if the color checkbox is set in the preferences.
	 */
	private void checkColorBox(){
		if(remPref.getColorCheckBox() == true){
			setTableRowColor();
		}
	}
	//Archiv*******************************************************************************************************************
	/**
	 * Set a archiv table
	 */
	private void setArchivTable(){
		tableArchiv .setFillsViewportHeight(true);
		archivPanel.add(tableArchiv .getTableHeader(), BorderLayout.PAGE_START);
		archivPanel.add(tableArchiv , BorderLayout.CENTER);
		tableArchiv .getTableHeader().setReorderingAllowed(false);
	}
	
	/**
	 *Shift the selected rows of the Tasks table in the archive table.
	 */
	private void shiftTableItemsinArchiv(){
		int[] rows = table.getSelectedRows();
		TableModel tm= table.getModel();
		for(int i = 0 ; i < rows.length; i++){
			addTableRow(tableArchiv, (String) table.getValueAt(i,0),
									(String) table.getValueAt(i,1),
									(String) table.getValueAt(i,2),
									(String) table.getValueAt(i,3),
									(String) table.getValueAt(i,4));
		}
		
		while(rows.length>0){
			((DefaultTableModel)tm).removeRow(table.convertRowIndexToModel(rows[0]));
			rows = table.getSelectedRows();
		}
		
		//add the remove function
		
		table.clearSelection();
	}
	//JSON*********************************************************************************************************************
	/**
	 * 
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
	 * prints out a heading in a terminal if the debugmode is activated 
	 */
	private void setDebugMode(){
		if(debugMode){
			System.out.println("Reminder-DebugMode");
			System.out.println("==================");
		}
	}
}
