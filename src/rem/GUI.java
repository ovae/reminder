package bin.rem;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class GUI {
	
	//Main window
	static int mainWindowWidth = 800;
	static int mainWindowHeight = 512;
	static JFrame mainWindow = new JFrame();
	
	//Toolbar
	JToolBar toolbar = new JToolBar();
	JButton newButton = new JButton("new");
	JButton removeButton = new JButton("remove");
	JButton doneButton = new JButton("done");
	
	//Main Panel
	JPanel mainPanel = new JPanel(new BorderLayout());
	
	//Table
	String[] columnNames = {"Topic","About","Begin","End", "Status"};
	Object[][] streams;
	JTable table = new JTable(new DefaultTableModel(streams,columnNames));
	
	//Files
	File podcastData = new File("userfiles/tasks");
	
	//Other
	static Object[] status = {"not started","in process","finished"};
	
	/**
	 * Initialise the main window.
	 */
	public void init(){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				setWindow();
				setToolbar();
				setMainPanel();
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
	
	private void setToolbar(){
		newButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				addTableRow("mathematics","work","20141010","20141024");
			}
		});
		
		
		toolbar.add(newButton);
		toolbar.add(removeButton);
		toolbar.add(doneButton);
		mainWindow.add(toolbar, BorderLayout.NORTH);
	}
	
	private void setMainPanel(){
		
		mainWindow.add(mainPanel, BorderLayout.CENTER);
		setTable();
	}
	
	/**
	 * Set a Table
	 */
	private void setTable(){
		table.setFillsViewportHeight(true);
		mainPanel.add(table.getTableHeader(), BorderLayout.PAGE_START);
		mainPanel.add(table, BorderLayout.CENTER);
	}
	/**
	 * Function to add a new row to the Table.
	 * @param podcast
	 * @param url
	 * "Topic","About","Begin","End", "Status"
	 */
	private void addTableRow(String topic, String about, String begin, String end){
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.addRow(new Object[]{topic, about, begin, end, status[0]});
	}
	
	
	/**
	* Load the items of the table from a userfile
	*/
	private void loadTableItemsFromFile(){
		//magic TODO
	}
	
	/**
	* Write the items of the table in a userfile
	 * @throws IOException 
	*/
	private void writeTableItemsToFile(){
		//magic TODO

	}
}
