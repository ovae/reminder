package rem;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import rem.calendar.CalendarPanel;
import rem.constants.Colour;
import rem.constants.Icons;
import rem.constants.Messages;
import rem.files.FileHandler;
import rem.preference.RemPreferences;
import rem.subwindows.AddEventFrame;
import rem.subwindows.AddTaskFrame;
import rem.subwindows.InfoFrame;
import rem.subwindows.SettingsFrame;
import rem.table.EventTable;
import rem.table.RemTable;
import rem.table.TasksTable;
import rem.util.Util;

/**
 * The main window of this program.
 * @author ovae.
 * @version 20150801.
 */
public class MainWindow extends JFrame{

	private static final long serialVersionUID = 1L;

	//Program info
	private String version ="0.5.0";
	
	//Main menu configuration
	private int windowHeight;
	private int windowWidth;

	//JPanels for the basic structure
	private JPanel mainPanel;
	private RemToolBar toolbar;
	private JPanel controlPanel;
	private JPanel contentPanel;
	private InfoPanel infoPanel;

	//Menu
	private JMenuBar menuBar;
	private JMenu menuMenus;
	private JMenu menuHelp;
	private JMenuItem menuItemSave;
	private JMenuItem menuClose;
	private JMenuItem menuItemSettings;
	private JMenuItem menuItemColours;
	private JMenuItem menuItemAbout;
	private JMenuItem menuItemNewTask;
	private JMenuItem menuItemRemoveTask;
	private JMenuItem menuItemChangeTaskStatus;
	private JMenuItem menuItemArchive;
	private JMenuItem menuItemNewEvent;
	private JMenuItem menuItemRemoveEvent;
	private JMenuItem menuItemArchiveEvent;
	private JMenuItem menuItemRestoreEvent;
	private JMenuItem menuItemChangeEventStatus;

	private JMenuItem menuItemRestore;
	private JMenuItem menuItemRemoveArchiveTask;

	//TabbedPane
	private JTabbedPane tabbedPane;
	private JScrollPane tasksScrollPane;
	private JScrollPane archiveScrollPane;
	private JScrollPane eventScrollPane;
	private JScrollPane noteScrollPane;
	private JPanel tasksTab;
	private JPanel calendarTab;
	private JPanel eventTab;
	private JPanel noteTab;

	private JPanel archiveTab;
	private TasksTable taskTable;
	private TasksTable archiveTable;
	private EventTable eventTable;
	private JTextArea noteField;

	//Sub windows
	private AddTaskFrame addTaskFrame;
	private AddEventFrame addEventFrame;
	private InfoFrame infoFrame;
	private SettingsFrame settingsFrame;

	//Files
	private File taskFile;
	private File archiveFile;
	private File eventFile;
	private File noteFile;

	//Preferences
	private RemPreferences preferences;

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
		this.taskTable = new TasksTable(new DefaultTableModel());
		this.archiveTable = new TasksTable(new DefaultTableModel());
		this.eventTable = new EventTable(new DefaultTableModel());
		this.calendarTab = new CalendarPanel(taskTable, archiveTable, eventTable);
		this.noteField = new JTextArea();

		//Menu
		//Initialise all menus and menu items.
		menuBar = new JMenuBar();
		menuMenus = new JMenu("\u2630 Menu");
		menuHelp = new JMenu("Help");
		menuItemSettings = new JMenuItem("Settings");
		menuItemColours = new JMenuItem("Colours");
		menuItemAbout = new JMenuItem("About");

		menuItemNewTask = new JMenuItem("New task");
		menuItemRemoveTask = new JMenuItem("Remove task");
		menuItemChangeTaskStatus = new JMenuItem("Change status");
		menuItemArchive = new JMenuItem("Archive task");

		menuItemRestore = new JMenuItem("Restore task");
		menuItemRemoveArchiveTask = new JMenuItem("Remove");
		menuItemRestoreEvent = new JMenuItem("Restore event");

		menuItemNewEvent = new JMenuItem("New event");
		menuItemRemoveEvent = new JMenuItem("Remove event");
		menuItemChangeEventStatus = new JMenu("Change status");
		menuItemArchiveEvent = new JMenuItem("Archive event");
		menuClose = new JMenuItem("Close");
		menuItemSave = new JMenuItem("Save");

		//Files
		taskFile = new File(System.getProperty("user.dir")+"/userfiles/tasks.txt");
		archiveFile = new File(System.getProperty("user.dir")+"/userfiles/archive.txt");
		eventFile = new File(System.getProperty("user.dir")+"/userfiles/event.txt");
		noteFile = new File(System.getProperty("user.dir")+"/userfiles/note.txt");

		//sub menus
		this.addTaskFrame = new AddTaskFrame(taskTable);
		this.addEventFrame = new AddEventFrame(eventTable);
		this.infoFrame = new InfoFrame();
		this.settingsFrame = new SettingsFrame(this);
		Util.centerWindow(infoFrame);
		Util.centerWindow(addTaskFrame);
		Util.centerWindow(addEventFrame);
		Util.centerWindow(settingsFrame);

		//ToolBar
		toolbar = new RemToolBar(tabbedPane, archiveTable, archiveTable, eventTable, addTaskFrame, addEventFrame);

		//Initialise the preferences
		preferences = new RemPreferences();

		//Basic menu structure.
		windowStructure();
	}

	public void run(){
		this.setVisible(true);
	}

	/**
	 * Set all basic window settings.
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
				settingUpTheMenu();
				settingUpTheToolbar();
				settingUpTheContentPanel();
				settingUpTheNoteTab();

				loadTableContent();
				//Refresh the calendarTab.
				((CalendarPanel) calendarTab).refreshCalendar();

			}
		});
	}

	/**
	 * Sets up all menu components.
	 */
	private void settingUpTheMenu(){
		controlPanel.add(menuBar, BorderLayout.WEST);
		//this.setJMenuBar(menuBar);
		menuBar.add(menuMenus);
			//menuMenus.add(menuOpenFiles);
				menuMenus.add(menuItemNewTask);
				menuMenus.add(menuItemRemoveTask);
				menuMenus.add(menuItemChangeTaskStatus);
				menuMenus.add(menuItemArchive);
			menuMenus.addSeparator();
				menuMenus.add(menuItemNewEvent);
				menuMenus.add(menuItemRemoveEvent);
				menuMenus.add(menuItemChangeEventStatus);
				menuMenus.add(menuItemArchiveEvent);
			menuMenus.addSeparator();
				menuMenus.add(menuItemRestore);
				menuMenus.add(menuItemRemoveArchiveTask);
				menuMenus.add(menuItemRestoreEvent);
			menuMenus.addSeparator();

			menuMenus.add(menuItemSave);
			menuMenus.add(menuClose);

		menuBar.add(menuHelp);
			menuHelp.add(menuItemColours);
			//menuHelp.add(menuItemSettings);
			menuHelp.add(menuItemAbout);

		menuItemNewTask.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				addTaskFrame.setVisible(true);
			}
		});

		menuItemRemoveTask.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				int[] rows = taskTable.getSelectedRows();
				if(rows.length < 1){
					JOptionPane.showMessageDialog(null, Messages.NO_SELECTED_ROW.getMsg());
					return;
				}else{
					taskTable.removeRows();
				}
			}
		});

		menuItemChangeTaskStatus.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				taskTable.updateTableRow();
			}
		});

		menuItemArchive.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				taskTable.shiftTableItemsinOtherTable(archiveTable);
			}
		});

		menuItemSettings.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				settingsFrame.setVisible(true);
			}
		});

		menuItemColours.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				infoFrame.setVisible(true);
			}
		});

		menuItemAbout.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				JOptionPane.showMessageDialog(null, "Reminder "+version,"Program Info", 1);
			}
		});

		menuItemSave.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				try {
					FileHandler.writeFile(taskTable.getTableContent(),taskFile);
					FileHandler.writeFile(archiveTable.getTableContent(), archiveFile);
					FileHandler.writeFile(eventTable.getTableContent(), eventFile);
					FileHandler.writeNoteFile(noteFile, noteField.getText());
					infoPanel.setStateSaved();
				} catch (IOException e1) {
					System.err.println(Messages.FAILED_TO_SAVE.getMsg());
				}
			}
		});

		menuClose.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});

		menuItemRestore.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				archiveTable.shiftTableItemsinOtherTable(taskTable);
			}
		});

		menuItemRemoveArchiveTask.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] rows = archiveTable.getSelectedRows();
				if(rows.length < 1){
					JOptionPane.showMessageDialog(null, Messages.NO_SELECTED_ROW.getMsg());
					return;
				}else{
					archiveTable.removeRows();
				}
			}
		});

		menuItemNewEvent.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				addEventFrame.setVisible(true);
			}
		});

		menuItemRemoveEvent.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				eventTable.removeRow();
			}
		});

		menuItemArchiveEvent.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				eventTable.shiftTableItemsinOtherTable(archiveTable);
			}
		});

		menuItemRestoreEvent.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				archiveTable.shiftTableItemsinOtherTable(eventTable);
			}
		});

		menuItemChangeEventStatus.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				eventTable.updateTableRow();
			}
		});

		//Set icon for the menu items
		menuItemSave.setIcon(Icons.SAVE_ICON.getIcon());
		menuClose.setIcon(Icons.EXIT_ICON.getIcon());
		menuItemAbout.setIcon(Icons.ABOUT_ICON.getIcon());
		menuItemSettings.setIcon(Icons.SETTINGS_ICON.getIcon());
		menuItemColours.setIcon(Icons.COLOUR_ICON.getIcon());

		menuItemNewTask.setIcon(Icons.ADD_TASK_ICON.getIcon());
		menuItemRemoveTask.setIcon(Icons.REMOVE_TASK_ICON.getIcon());
		menuItemChangeTaskStatus.setIcon(Icons.CHANGE_TASK_STATE_ICON.getIcon());
		menuItemArchive.setIcon(Icons.ARCHIVE_ICON.getIcon());

		menuItemNewEvent.setIcon(Icons.ADD_EVENT_ICON.getIcon());
		menuItemRemoveEvent.setIcon(Icons.REMOVE_EVENT_ICON.getIcon());
		menuItemArchiveEvent.setIcon(Icons.ARCHIVE_EVENT_ICON.getIcon());
		menuItemChangeEventStatus.setIcon(Icons.CHANGE_EVENT_STATE_ICON.getIcon());

		menuItemRestore.setIcon(Icons.RESTORE_ICON.getIcon());
		menuItemRemoveArchiveTask.setIcon(Icons.ARCHIVE_REMOVE_ICON.getIcon());
		menuItemRestoreEvent.setIcon(Icons.RESTORE_EVENT_ICON.getIcon());

		menuItemNewTask.setEnabled(true);
		menuItemRemoveTask.setEnabled(true);
		menuItemChangeTaskStatus.setEnabled(true);
		menuItemArchive.setEnabled(true);

		menuItemNewEvent.setEnabled(true);
		menuItemRemoveEvent.setEnabled(false);
		menuItemArchiveEvent.setEnabled(false);
		menuItemChangeEventStatus.setEnabled(false);

		menuItemRestore.setEnabled(false);
		menuItemRemoveArchiveTask.setEnabled(false);
		menuItemRestoreEvent.setEnabled(false);

		//Set short cuts.
		final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		//menuOpenFiles.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, SHORTCUT_MASK));
		menuClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, SHORTCUT_MASK));
		menuItemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, SHORTCUT_MASK));
		menuItemNewTask.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, SHORTCUT_MASK));
		menuItemRemoveTask.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, SHORTCUT_MASK));
		menuItemArchive.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, SHORTCUT_MASK));
		menuItemChangeTaskStatus.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, SHORTCUT_MASK));
		menuItemRestore.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, SHORTCUT_MASK));
		menuItemRemoveArchiveTask.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, SHORTCUT_MASK));

		//Set mnemonics
		menuMenus.setMnemonic(KeyEvent.VK_M);
		//menuTask.setMnemonic(KeyEvent.VK_T);
		menuHelp.setMnemonic(KeyEvent.VK_H);
	}

	/**
	 * Sets up all toolbar components.
	 */
	private void settingUpTheToolbar(){

		controlPanel.add(toolbar, BorderLayout.CENTER);
	}

	//Setting up the basic window panel structure.
	private void settingUpTheContentPanel(){
		settingUpTaskTable();
		tabbedPane.addTab("Latest", tasksScrollPane);
		tabbedPane.addTab("Event", eventScrollPane);
		tabbedPane.addTab("Archive", archiveScrollPane);
		tabbedPane.addTab("Note", noteScrollPane);
		tabbedPane.addTab("Calendar", calendarTab);

		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				switch(tabbedPane.getSelectedIndex()){
					case 0:
						//Latest tab
						menuItemNewTask.setEnabled(true);
						menuItemRemoveTask.setEnabled(true);
						menuItemChangeTaskStatus.setEnabled(true);
						menuItemArchive.setEnabled(true);
						menuItemRestore.setEnabled(false);
						menuItemRemoveArchiveTask.setEnabled(false);
						menuItemNewEvent.setEnabled(true);
						menuItemRemoveEvent.setEnabled(false);
						menuItemArchiveEvent.setEnabled(false);
						menuItemRestoreEvent.setEnabled(false);
						menuItemChangeEventStatus.setEnabled(false);
						break;
					case 1:
						//Event tab
						menuItemNewTask.setEnabled(true);
						menuItemRemoveTask.setEnabled(false);
						menuItemChangeTaskStatus.setEnabled(false);
						menuItemArchive.setEnabled(false);
						menuItemRestore.setEnabled(false);
						menuItemRemoveArchiveTask.setEnabled(false);
						menuItemNewEvent.setEnabled(true);
						menuItemRemoveEvent.setEnabled(true);
						menuItemArchiveEvent.setEnabled(true);
						menuItemRestoreEvent.setEnabled(false);
						menuItemChangeEventStatus.setEnabled(true);
						break;
					case 2:
						//Archive tab
						menuItemNewTask.setEnabled(true);
						menuItemRemoveTask.setEnabled(false);
						menuItemChangeTaskStatus.setEnabled(false);
						menuItemArchive.setEnabled(false);
						menuItemRestore.setEnabled(true);
						menuItemRemoveArchiveTask.setEnabled(true);
						menuItemNewEvent.setEnabled(true);
						menuItemRemoveEvent.setEnabled(false);
						menuItemArchiveEvent.setEnabled(false);
						menuItemRestoreEvent.setEnabled(true);
						menuItemChangeEventStatus.setEnabled(false);
						break;
					case 3:
						//Node tab
						menuItemNewTask.setEnabled(true);
						menuItemRemoveTask.setEnabled(false);
						menuItemChangeTaskStatus.setEnabled(false);
						menuItemArchive.setEnabled(false);
						menuItemRestore.setEnabled(false);
						menuItemRemoveArchiveTask.setEnabled(false);
						menuItemNewEvent.setEnabled(true);
						menuItemRemoveEvent.setEnabled(false);
						menuItemArchiveEvent.setEnabled(false);
						menuItemRestoreEvent.setEnabled(false);
						menuItemChangeEventStatus.setEnabled(false);
						break;
					case 4:
						//Calendar tab
						menuItemNewTask.setEnabled(true);
						menuItemRemoveTask.setEnabled(false);
						menuItemChangeTaskStatus.setEnabled(false);
						menuItemArchive.setEnabled(false);
						menuItemRestore.setEnabled(false);
						menuItemRemoveArchiveTask.setEnabled(false);
						menuItemNewEvent.setEnabled(true);
						menuItemRemoveEvent.setEnabled(false);
						menuItemArchiveEvent.setEnabled(false);
						menuItemRestoreEvent.setEnabled(false);
						menuItemChangeEventStatus.setEnabled(false);
						break;
					default:
						//Error
				}
			}
		});

		contentPanel.add(tabbedPane, BorderLayout.CENTER);

		mainPanel.add(controlPanel, BorderLayout.NORTH);
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		mainPanel.add(infoPanel, BorderLayout.SOUTH);
		//TODO
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
	 * 
	 */
	private void settingUpTheNoteTab() {
		noteField.setBackground(Colour.CALENDAR_DAY.getColor());
		noteField.setMargin(new Insets(10,10,10,10));
		noteTab.add(noteField, BorderLayout.CENTER);
		noteField.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				infoPanel.setStateChanged();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				infoPanel.setStateChanged();
			}

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
	 * Load the table content from files in the table.
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
	}

	/**
	 * Get the task table Object.
	 * @return
	 */
	public RemTable getTaskTable(){
		return taskTable;
	}

	/**
	 * Get the event table Object.
	 * @return
	 */
	public RemTable getEventTable(){
		return eventTable;
	}

	/**
	 * 
	 * @param file
	 */
	public void setTaskFilePath(final File file){
		this.taskFile = file;
	}

	/**
	 * 
	 * @param file
	 */
	public void setArchiveFilePath(final File file){
		this.archiveFile = file;
	}

	/**
	 * 
	 * @return
	 */
	public RemPreferences getPreferences(){
		return preferences;
	}
}
