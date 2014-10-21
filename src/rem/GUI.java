package bin.rem;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.prefs.Preferences;

import javax.swing.Icon;
import javax.swing.JButton;
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
import javax.swing.table.DefaultTableModel;
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
	
	//Table
	private String[] columnNames = {"Topic","About","Begin","End", "Status"};
	private Object[][] streams;
	private JTable table = new JTable(new DefaultTableModel(streams,columnNames));
	
	//Preferences
	RemPreference remPref = new RemPreference();
	
	//Files
	private File userfilesDirectory = new File("");
	private File taskFile = new File("userfiles/tasks");
	
	//Other
	private Icon iconWarning = UIManager.getIcon("OptionPane.warningIcon");
	private Icon iconInfo = UIManager.getIcon("OptionPane.informationIcon");
	static Object[] status = {"not_started","in_progress","finished"};
	
	//Debug
	boolean debugMode = false;
	
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
				setToolbar();
				setMainPanel();
				setTable();
				loadTableItemsFromFile();
				setInfoPanel();
				//setLook();
				
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
	public static void centerWindow(JFrame frame) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}

	/**
	 * Set the basic information of the main window.
	 */
	public void setWindow(){
		mainWindow.setTitle("Reminder");
		mainWindow.setSize(new Dimension(mainWindowWidth,mainWindowHeight));
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setMinimumSize(new Dimension(640, mainWindowHeight));
		mainWindow.setLayout(new BorderLayout());
		centerWindow(mainWindow);
	}
	
	/**
	 * setting up the toolbar
	 */
	private void setToolbar(){
		//Diasable the drag functionality of the table header.d
		toolbar.setFloatable(false);
		newButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setAddFrame();
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
		
		settingsButton.addActionListener(new ActionListener(){
			@Override//TODO
			public void actionPerformed(ActionEvent e) {
				setSettingsWindow();
				String lastOutputDir = null;
				try{
					lastOutputDir = remPref.getUserPath();
					if(debugMode){
						System.out.println(Time.getTimeDebug()+" Load preference succesfully");
					}
				}catch(Exception d){
					if(debugMode){
						System.err.println(Time.getTimeDebug()+" Loading error");
					}
				}
				userfilesDirectoryInput.setText(lastOutputDir);
			}
		});
		
		toolbar.add(newButton);
		toolbar.add(removeButton);
		toolbar.add(doneButton);
		toolbar.add(settingsButton);
	}
	
	/**
	 * all settings and configurations of the addFrame
	 */
	private void setAddFrame(){
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
		centerWindow(addFrame);
		addFrame.setVisible(true);
	}
	
	/**
	 * set up the main panel
	 */
	private void setMainPanel(){
		mainWindow.add(toolbar, BorderLayout.NORTH);
		mainWindow.add(scroll, BorderLayout.CENTER);
		mainWindow.add(infoPanel,BorderLayout.SOUTH);
	}
	
	/**
	 * set the info panel with date label.
	 */
	private void setInfoPanel(){
		JLabel info = new JLabel("Date:"+Time.getDate());
		info.setBounds(10, 100, 180,28);
		info.setLocation(10, 40);
		infoPanel.add(info, BorderLayout.CENTER);
	}
	
	/**
	 * Setting up the settings window
	 */
	private void setSettingsWindow(){
		JLabel filePathLabel = new JLabel("Path of the tasks file:");
		JButton saveSettingsButton = new JButton("save settings");
		JButton getPathButton = new JButton("Change");
		
		settingsWindow.setLocationRelativeTo(null);
		settingsWindow.setSize(new Dimension(512, 256));
		settingsWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		settingsPanel.setLayout(null);
		
		getPathButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				getFilePath();
			}
		});
		
		saveSettingsButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//TODO
				try{
					remPref.setUserPath(userfilesDirectory.toString());
					if(debugMode){
						System.out.println(Time.getTimeDebug()+" Save preference via button.");
					}
				}catch(Exception d){
					if(debugMode){
						System.err.println(Time.getTimeDebug()+"Error save via button");
					}
				}
				JOptionPane.showMessageDialog(null, "settings saved");
			}
		});
		
		filePathLabel.setBounds(10, 100, 340,28);
		filePathLabel.setLocation(40,20);
		userfilesDirectoryInput.setBounds(10, 100, 318,28);
		userfilesDirectoryInput.setLocation(40, 40);
		getPathButton.setBounds(10, 100, 100,27);
		getPathButton.setLocation(360, 40);
		
		settingsPanel.add(filePathLabel);
		settingsPanel.add(userfilesDirectoryInput);
		settingsPanel.add(getPathButton);
		settingsWindow.add(settingsPanel, BorderLayout.CENTER);
		settingsWindow.add(saveSettingsButton, BorderLayout.SOUTH);
		settingsWindow.setVisible(true);
	}
	
	private void getFilePath(){
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int response = fc.showOpenDialog(null);
		if(response == JFileChooser.APPROVE_OPTION){
			//TODO
			userfilesDirectory = fc.getSelectedFile();
			userfilesDirectoryInput.setText(userfilesDirectory.toString());
			try{
				remPref.setUserPath(userfilesDirectory.toString());
				if(debugMode){
					System.out.println(Time.getTimeDebug()+" Save preference.");
				}
			}catch(Exception e){
				if(debugMode){
					System.err.println(Time.getTimeDebug()+" Save preference error.");
				}
			}
		}else{
			//Nothing
		}
	}
	
	/**
	 * 
	 */
	private void setLook(){
		try {
			//Nimbus
			//
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
	 * Set a Table
	 */
	private void setTable(){
		table.setFillsViewportHeight(true);
		mainPanel.add(table.getTableHeader(), BorderLayout.PAGE_START);
		mainPanel.add(table, BorderLayout.CENTER);
		table.getTableHeader().setReorderingAllowed(false);
		//TODO

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
		writeTableItemsToFile();
	}

	
	/**
	* Load the items of the table from a userfile
	*/
	private void loadTableItemsFromFile(){
		Scanner sca;
		//open the file
		try{
			sca = new Scanner(taskFile);
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
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "File not found or the file is empty.");
		}
	}
	
	/**
	* Write the items of the table in a userfile
	 * @throws IOException 
	*/
	private void writeTableItemsToFile(){
		try{
		BufferedWriter buffer = new BufferedWriter(new FileWriter(taskFile));
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
		}catch(IOException e){
			JOptionPane.showMessageDialog(null, e.getStackTrace());
		}
	}
	
	/**
	 * 
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
	 * 
	 */
	private void updateTableRow(){
		try{
			int[] row = table.getSelectedRows();
			String value= (String) table.getModel().getValueAt(row[0], 4);
			if(value == status[0]){
				table.getModel().setValueAt(status[1], row[0], 4);
			}else if(value == status[1]){
				table.getModel().setValueAt(status[2], row[0], 4);
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
	 * 
	 */
	private void setTableRowColor(){
		//magic
	}
	
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
