package bin.rem;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

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
	private JButton settingsButton = new JButton("settings");
	private JButton infoButton = new JButton("info");
	private JButton saveAll = new JButton("save");
	
	//AddFrame
	private JFrame addFrame = new JFrame("New");
	private JTextField inputTopic = new JTextField("");
	private JTextField inputAbout = new JTextField("");
	private JTextField inputBegin = new JTextField("");
	private JTextField inputEnd = new JTextField("");
	private JButton addButton = new JButton("add");
	private JButton resetButton = new JButton("reset");
	
	//Main Panel
	private JPanel mainPanel = new JPanel(new BorderLayout());
	private JScrollPane scroll = new JScrollPane(mainPanel);
	private JPanel infoPanel = new JPanel(new BorderLayout());
	
	//Setings Window
	private JFrame settingsWindow = new JFrame("Settings");
	private JTextField userfilesDirectoryInput = new JTextField();
	private JPanel settingsPanel = new JPanel(new BorderLayout());
	private JCheckBox lookBox = new JCheckBox("Nimbus");
	private JCheckBox useColorsBox = new JCheckBox("Color");
	private JCheckBox useTimeFormateA = new JCheckBox();
	private JCheckBox useTimeFormateB = new JCheckBox();
	private JCheckBox useTimeFormateC = new JCheckBox();
	private boolean isLookBox = false;
	private boolean isColorox = false;
	private JLabel selectedLook = new JLabel("Error");
	
	//Info Window
	private JFrame infoFrame = new JFrame("Info");
	
	//Table
	private String[] columnNames = {"Topic","About","Begin","End", "Status"};
	private Object[][] streams;
	private JTable table = new JTable(new DefaultTableModel(streams,columnNames));
	
	//Preferences
	RemPreference remPref = new RemPreference();
	
	//Files
	private File userfilesDirectory = new File("");
	private File taskFileBack;
	
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
				setTable();
				loadTableItemsFromFile();
				setTableRowColor();
				setInfoPanel();
				//taskFileBack =new File(""+remPref.getUserPath().toString()+"/tasks");
				
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
	
	//Toolbar******************************************************************************************************************
	/**
	 * setting up the toolbar
	 */
	private void setToolbar(){
		//Diasable the drag functionality of the table header.d
		toolbar.setFloatable(false);
		newButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setAddTaskFrame();
			}
		});
		
		removeButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int p =JOptionPane.showConfirmDialog(null, "Do you want to remove it.","Select an Option",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(p==0){
					removeTableRow();
					writeTableItemsToFile();
				}
			}
		});
		
		
		doneButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				updateTableRow();
				writeTableItemsToFile();
			}
		});
		
		saveAll.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					writeTableItemsToFile();
					JOptionPane.showMessageDialog(null, "Tasks saved");
				}catch(Exception f){
					JOptionPane.showMessageDialog(null, "Saving failed");
				}
			}
			
		});
		
		settingsButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				setSettingsWindow();
				setTimeFormatBoxes();
				String lastOutputDir = null;
				try{
					lastOutputDir = remPref.getUserPath();
					isLookBox = remPref.getLookCheckBox();
					//If the checkox isLookBox is activated 
					// 'Nimbus' is written in the selectedLook label
					//or otherwise 'Default'.
					if(isLookBox){
						selectedLook.setText("Nimbus");
					}else{
						selectedLook.setText("Default");
					}
					lookBox.setSelected(isLookBox);
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
			@Override//TODO
			public void actionPerformed(ActionEvent e) {
				setInfoFrame();
			}
		});
		
		toolbar.add(newButton);
		toolbar.add(removeButton);
		toolbar.add(doneButton);
		toolbar.add(saveAll);
		toolbar.add(settingsButton);
		toolbar.add(infoButton);
	}
	//Info-Frame***************************************************************************************************************
	/**
	 * Setting up the info window
	 */
	//TODO
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
		JLabel blueLabel = new JLabel("Selected a row");
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
	private void setAddTaskFrame(){
		addFrame.setLocationRelativeTo(null);
		addFrame.setSize(new Dimension(350,256));
		addFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		addFrame.setLayout(new BorderLayout());
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
		
		enterTopic.setLocation(40, 40);
		enterAbout.setLocation(40, 70);
		enterBegin.setLocation(40, 100);
		enterEnd.setLocation(40, 130);
		
		inputTopic.setLocation(120, 40);
		inputAbout.setLocation(120, 70);
		inputBegin.setLocation(120, 100);
		inputEnd.setLocation(120, 130);
		
		addPanel.setLayout(null);
		addPanel.add(enterTopic);
		addPanel.add(enterAbout);
		addPanel.add(enterBegin);
		addPanel.add(enterEnd);
		addPanel.add(inputTopic);
		addPanel.add(inputAbout);
		addPanel.add(inputBegin);
		addPanel.add(inputEnd);
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
					addTableRow(topic,about,begin,end);
				}
			}
		});
		
		resetButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				inputTopic.setText("");
				inputAbout.setText("");
				inputBegin.setText("");
				inputEnd.setText("");
			}
		});
		
		addFrame.add(addPanel,BorderLayout.CENTER);
		addFrame.add(buttonPanel, BorderLayout.SOUTH);
		addFrame.setVisible(true);
		centerWindow(addFrame);
	}
	
	//Main-window**************************************************************************************************************
	/**
	 * set up the main panel
	 */
	private void setMainPanel(){
		mainWindow.add(toolbar, BorderLayout.NORTH);
		mainWindow.add(scroll, BorderLayout.CENTER);
		mainWindow.add(infoPanel,BorderLayout.SOUTH);
	}
	
	//Info-panel***************************************************************************************************************
	/**
	 * set the info panel with date label.
	 */
	private void setInfoPanel(){
		JLabel info = new JLabel("Date:"+Time.getDate());
		info.setBounds(10, 100, 180,28);
		info.setLocation(10, 40);
		infoPanel.add(info, BorderLayout.CENTER);
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
					JOptionPane.showMessageDialog(null, "After a restart is the default look activated.");
				}else{
					isLookBox = true;
					selectedLook.setText("Default");
					JOptionPane.showMessageDialog(null, "After a restart is the Nimbus look activated.");
				}
				//saveSettings();
			}
		});
		
		useColorsBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//magic TODO
			}
		});
		
		
		saveSettingsButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				saveSettings();
				loadTableItemsFromFile();
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
		
		/*
		useTimeFormateA.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setTimeFormatBoxes();
			}
		});
		
		useTimeFormateB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setTimeFormatBoxes();
			}
		});
		
		useTimeFormateC.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setTimeFormatBoxes();
			}
		});
		*/
		
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
	 * 
	 */
	private void setTimeFormatBoxes(){
		//TODO
		if(useTimeFormateA.isSelected()){
			useTimeFormateB.setSelected(false);
			useTimeFormateC.setSelected(false);
		}else if(useTimeFormateB.isSelected()){
			useTimeFormateA.setSelected(false);
			useTimeFormateC.setSelected(false);
		}else if(useTimeFormateC.isSelected()){
			useTimeFormateA.setSelected(false);
			useTimeFormateB.setSelected(false);
		}else{
			useTimeFormateA.setSelected(true);
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
			remPref.setUserPath(userfilesDirectory.toString());
			remPref.setLookCheckBox(isLookBox);
			if(debugMode){
				System.out.println(Time.getTimeDebug()+" Save preference.");
			}
		}catch(Exception e){
			if(debugMode){
				System.err.println(Time.getTimeDebug()+" Save preference error.");
			}
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
	 */
	private void addTableRow(String topic, String about, String begin, String end){
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.addRow(new Object[]{topic, about, begin, end, status[0]});
		writeTableItemsToFile();
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
		//writeTableItemsToFile();
	}

	
	/**
	* Load the items of the table from a userfile
	*/
	private void loadTableItemsFromFile(){
		Scanner sca;
		File tempFile = new File(remPref.getUserPath().toString()+"/tasks");
		//open the file
		try{
			sca = new Scanner(tempFile);
			//read from the file
			while(sca.hasNext()){
				String topic = sca.next();
				String about = sca.next();
				String begin = sca.next();
				String end = sca.next();
				String status = sca.next();
				//add to the table
				addTableRow(topic, about, begin, end, status);
			}
			if(debugMode){
				System.out.println(Time.getTimeDebug()+" Load from file successfully.");
			}
		}catch(Exception e){
			if(debugMode){
				System.err.println("\n"+Time.getTimeDebug()+" Loading table items error. \n");
			}
			//TODO
			/*
			if(tempFile != taskFileBack){
				emptyTable();
			}
			*/
			JOptionPane.showMessageDialog(null, "File not found or the file is empty.");
		}
	}
	
	/**
	* Write the items of the table in a userfile
	 * @throws IOException 
	*/
	private void writeTableItemsToFile(){
		File tempFile = new File(remPref.getUserPath().toString()+"/tasks");
		try{
		BufferedWriter buffer = new BufferedWriter(new FileWriter(tempFile));
			for(int i = 0 ; i < table.getRowCount(); i++){
				buffer.newLine();
				for(int j = 0 ; j < 5;j++){
					String test =(String) table.getValueAt(i,j);
					test = test.replaceAll("\\s", "_");
					buffer.write(test);
					buffer.write("\t");
				}
			}
			buffer.close();
			if(debugMode){
				System.out.println(Time.getTimeDebug()+" Write to file successfully.");
			}
		}catch(IOException e){
			if(debugMode){
				System.err.println(Time.getTimeDebug()+" Writing to file error.");
			}
			JOptionPane.showMessageDialog(null, e.getStackTrace());
		}
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
	 * 
	 */
	private void sortTableItems(){
		//magic
	}
	
	/**
	 * Colorize table rows dependent on the 'end' value
	 */
	private void setTableRowColor(){
		//magic
		
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
				final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				String tableValue = (String)table.getModel().getValueAt(row, 3);
				String tableStatus = (String) table.getModel().getValueAt(row, 4);
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
