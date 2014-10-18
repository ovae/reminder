package bin.rem;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

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
	
	//AddFrame
	JFrame addFrame = new JFrame("New");
	JTextField inputTopic = new JTextField("");
	JTextField inputAbout = new JTextField("");
	JTextField inputBegin = new JTextField("");
	JTextField inputEnd = new JTextField("");
	JButton addButton = new JButton("add");
	JButton resetButton = new JButton("reset");
	
	//Main Panel
	JPanel mainPanel = new JPanel(new BorderLayout());
	JScrollPane scroll = new JScrollPane(mainPanel);
	
	//Table
	String[] columnNames = {"Topic","About","Begin","End", "Status"};
	Object[][] streams;
	JTable table = new JTable(new DefaultTableModel(streams,columnNames));
	
	//Files
	File taskFile = new File("userfiles/tasks");
	
	//Other
	static Object[] status = {"not_started","in_process","finished"};
	
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
				setAddFrame();
			}
		});
		
		removeButton.addActionListener(new ActionListener(){
			@Override//TODO
			public void actionPerformed(ActionEvent e) {
				removeTableRow();
				writeTableItemsToFile();
			}
		});
		
		
		doneButton.addActionListener(new ActionListener(){
			@Override//TODO
			public void actionPerformed(ActionEvent e) {
				updateTableRow();
				writeTableItemsToFile();
			}
		});
		
		toolbar.add(newButton);
		toolbar.add(removeButton);
		toolbar.add(doneButton);
		mainWindow.add(toolbar, BorderLayout.NORTH);
	}
	
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
		//JLabel space = new JLabel("");
		
		enterTopic.setBounds(10, 100, 100, 28);
		enterAbout.setBounds(10, 100, 100, 28);
		enterBegin.setBounds(10, 100, 100, 28);
		enterEnd.setBounds(10, 100, 100, 28);
		//space.setBounds(10, 100, 100, 28);
		
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
					/*
					inputTopic.setText("");
					inputAbout.setText("");
					inputBegin.setText("");
					inputEnd.setText("");
					*/
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
	 * 
	 */
	private void setMainPanel(){
		mainWindow.add(scroll, BorderLayout.CENTER);
		setTable();
		loadTableItemsFromFile();
	}
	
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
		writeTableItemsToFile();
	}

	
	/**
	* Load the items of the table from a userfile
	*/
	private void loadTableItemsFromFile(){
		//magic TODO
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
		//magic TODO
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

	private void setTableRowColor(){
		
	}

}
