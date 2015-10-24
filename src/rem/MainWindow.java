package rem;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import rem.calendar.CalendarPanel;
import rem.constants.Colour;
import rem.constants.Icons;
import rem.constants.Messages;
import rem.files.FileHandler;
import rem.panels.InfoPanel;
import rem.subwindows.AddEventFrame;
import rem.subwindows.AddTaskFrame;
import rem.table.EventTable;
import rem.table.RemTable;
import rem.table.TaskTable;
import rem.util.Util;

/**
 * This is the main window of the reminder program.
 * @author ovae.
 * @version 20151024.
 */
public class MainWindow extends JFrame{

	private static final long serialVersionUID = 1L;

	/**
	 * Program info
	 */
	public static final String version ="1.5.6_20151024";

	/**
	 * If {@code True} then developer mode is on
	 * else {@code False} mode is off.
	 */
	public static final Boolean devMode = false;

	/**
	 * The height of the main window.
	 */
	private int windowHeight;

	/**
	 * the weight of the main window.
	 */
	private int windowWidth;

	//JPanels for the basic structure
	private JPanel mainPanel;
	private JPanel controlPanel;
	private JPanel contentPanel;
	private InfoPanel infoPanel;

	//Menu and ToolBar
	private RemToolBar toolbar;
	private RemMenuBar remMenu;

	//TabbedPane
	private JTabbedPane tabbedPane;
	private JScrollPane tasksScrollPane;
	private JScrollPane archiveScrollPane;
	private JScrollPane eventScrollPane;
	private JScrollPane noteScrollPane;

	//Tab's
	private JPanel tasksTab;
	private JPanel eventTab;
	private JPanel archiveTab;
	private JPanel noteTab;
	private JPanel calendarTab;

	//Tables
	private TaskTable taskTable;
	private TaskTable archiveTable;
	private EventTable eventTable;
	private JTextArea noteField;

	//Sub windows
	private AddTaskFrame addTaskFrame;
	private AddEventFrame addEventFrame;

	//Files
	private File taskFile;
	private File archiveFile;
	private File eventFile;
	private File noteFile;

	/**
	 * Creates a new MainWindow.
	 */
	public MainWindow(){
		//Initialise all attributes.
		this.windowHeight = 512;
		this.windowWidth = 800;
		this.mainPanel = new JPanel(new BorderLayout());
		this.controlPanel = new JPanel(new BorderLayout());
		this.contentPanel = new JPanel(new BorderLayout());
		this.infoPanel = new InfoPanel();

		//JPanels
		this.tabbedPane = new JTabbedPane();
		this.tasksTab = new JPanel(new BorderLayout());
		this.archiveTab = new JPanel(new BorderLayout());
		this.eventTab = new JPanel(new BorderLayout());
		this.noteTab = new JPanel(new BorderLayout());
		this.tasksScrollPane = new JScrollPane(tasksTab);
		this.archiveScrollPane = new JScrollPane(archiveTab);
		this.eventScrollPane = new JScrollPane(eventTab);
		this.noteScrollPane = new JScrollPane(noteTab);

		//TabbedPane elements
		this.taskTable = new TaskTable(new DefaultTableModel());
		this.archiveTable = new TaskTable(new DefaultTableModel());
		this.eventTable = new EventTable(new DefaultTableModel());
		this.calendarTab = new CalendarPanel(taskTable, archiveTable, eventTable);
		this.noteField = new JTextArea();

		//Files
		taskFile = new File(System.getProperty("user.dir")+"/userfiles/tasks.txt");
		archiveFile = new File(System.getProperty("user.dir")+"/userfiles/archive.txt");
		eventFile = new File(System.getProperty("user.dir")+"/userfiles/event.txt");
		noteFile = new File(System.getProperty("user.dir")+"/userfiles/note.txt");

		//Basic menu structure.
		windowStructure();
	}

	/**
	 * Sets the window to visible.
	 */
	public void run(){
		this.setVisible(true);
	}

	/**
	 * Set up all basic window settings.
	 */
	private void windowSettings(){
		this.setSize(new Dimension(windowWidth,windowHeight));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(640, windowHeight));
		this.setLayout(new BorderLayout());
		this.setContentPane(mainPanel);
		this.setTitle("Reminder");
		this.setIconImage(Icons.MAIN_ICON.getIcon().getImage());
		Util.centerWindow(this);
	}

	/**
	 * The window structure. All components are used in separated threads.
	 */
	private void windowStructure(){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				windowSettings();
				settingUpTheContentPanel();
				settingUpTheNoteTab();

				loadTableContent();
				settingUpComponents();
				//Refresh the calendarTab.
				((CalendarPanel) calendarTab).refreshCalendar();
			}
		});
	}

	/**
	 * Sets up the menu and the toolBar.
	 */
	private void settingUpComponents(){
		controlPanel.add(remMenu, BorderLayout.WEST);
		controlPanel.add(toolbar, BorderLayout.CENTER);
	}

	/**
	 * Setting up the basic window panel structure.
	 */
	private void settingUpTheContentPanel(){
		settingUpTaskTable();
		tabbedPane.addTab("Latest", tasksScrollPane);
		tabbedPane.addTab("Event", eventScrollPane);
		tabbedPane.addTab("Archive", archiveScrollPane);
		tabbedPane.addTab("Note", noteScrollPane);
		tabbedPane.addTab("Calendar", calendarTab);
		contentPanel.add(tabbedPane, BorderLayout.CENTER);

		mainPanel.add(controlPanel, BorderLayout.NORTH);
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		mainPanel.add(infoPanel, BorderLayout.SOUTH);
		try {
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			//SwingUtilities.updateComponentTreeUI(mainPanel);
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look and feel.
		}
	}

	/**
	 * Sets up the note tab.
	 */
	private void settingUpTheNoteTab() {
		noteField.setBackground(Colour.CALENDAR_DAY.getColor());
		noteField.setMargin(new Insets(10,10,10,10));
		noteTab.add(noteField, BorderLayout.CENTER);
		noteField.getDocument().addDocumentListener(new DocumentListener(){

			/**
			 * 
			 */
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				infoPanel.setStateChanged();
			}

			/**
			 * 
			 */
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				infoPanel.setStateChanged();
			}

			/**
			 * 
			 */
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				infoPanel.setStateChanged();
			}
			
		});
	}


	/**
	 * Setting up the taskTables.
	 */
	private void settingUpTaskTable(){
		//Task tab
		String[] columnNames = {"Topic", "About", "Begin", "End", "Status"};
		taskTable.setTableHeader(columnNames);
		taskTable.setTableModel();
		taskTable.setTableRowColor();

		tasksTab.add(taskTable, BorderLayout.CENTER);
		tasksTab.add(taskTable.getTableHeader(), BorderLayout.PAGE_START);
		taskTable.checkIfTableHasChanged(infoPanel);

		//Archive tab
		String[] columnNamesArchive = {"Topic", "About", "Begin", "End", "Result"};
		archiveTable.setTableHeader(columnNamesArchive);
		archiveTable.setTableModel();
		archiveTable.setTableRowColor();
		archiveTable.checkIfTableHasChanged(infoPanel);

		archiveTab.add(archiveTable, BorderLayout.CENTER);
		archiveTab.add(archiveTable.getTableHeader(), BorderLayout.PAGE_START);

		//Event tab
		String[] columnNamesEvent = {"Event", "About", "Begin", "End", "Status"};
		eventTable.setTableHeader(columnNamesEvent);
		eventTable.setTableModel();
		eventTable.setTableRowColor();
		eventTable.checkIfTableHasChanged(infoPanel);

		eventTab.add(eventTable, BorderLayout.CENTER);
		eventTab.add(eventTable.getTableHeader(), BorderLayout.PAGE_START);
	}


	/**
	 * Load the table content from files in the tables.
	 */
	private void loadTableContent(){
		try {
			FileHandler.loadFile(taskTable, taskFile);
			FileHandler.loadFile(archiveTable, archiveFile);
			FileHandler.loadFile(eventTable, eventFile);
			FileHandler.loadNoteFile(noteField,noteFile);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, Messages.UNABLE_TO_LOAD.getMsg());
		}finally{
			infoPanel.setStateSaved();
		}
		addTaskFrame = new AddTaskFrame(this);
		addEventFrame = new AddEventFrame(eventTable);
		toolbar = new RemToolBar(this, addTaskFrame, addEventFrame);
		remMenu = new RemMenuBar(this, addTaskFrame, addEventFrame, noteField);
	}

	//Setter
	public void setTaskFilePath(final File file){this.taskFile = file;}
	public void setArchiveFilePath(final File file){this.archiveFile = file;}

	//Getter
	public RemTable getTaskTable(){return taskTable;}
	public RemTable getEventTable(){return eventTable;}
	public TaskTable getArchiveTable() {return archiveTable;}
	public JTabbedPane getTabbedPane() {return tabbedPane;}
	public InfoPanel getInfoPanel() {return infoPanel;}
	public File getTaskFile() {return taskFile;}
	public File getEventFile() {return eventFile;}
	public File getArchiveFile() {return archiveFile;}
	public File getNoteFile() {return noteFile;}

}
