package rem;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import rem.calendar.CalendarPanel;
import rem.files.FileHandler;
import rem.preference.RemPreferences;
import rem.subWindows.AddTaskFrame;
import rem.subWindows.InfoFrame;
import rem.subWindows.SettingsFrame;

/**
 * The main window of this program.
 * @author ovae.
 * @version 20150408.
 */
public class MainWindow extends JFrame{

	private static final long serialVersionUID = 1L;

	//Program info
	private String version ="0.3.0";
	
	//Main menu configuration
	private int windowHeight;
	private int windowWidth;

	//JPanels for the basic structure
	private JPanel mainPanel;
	private JToolBar toolbar;
	private JPanel controlPanel;
	private JPanel contentPanel;
	private InfoPanel infoPanel;

	//Menu
	private JMenuBar menuBar;
	private JMenu menuFiles;
	private JMenu menuTask;
	private JMenu menuHelp;
	private JMenuItem menuItemSave;
	private JMenuItem menuClose;
	private JMenuItem menuItemSettings;
	private JMenuItem menuItemColours;
	private JMenuItem menuItemAbout;
	private JMenuItem menuItemNewTask;
	private JMenuItem menuItemRemoveTask;
	private JMenuItem menuItemChangeStatus;
	private JMenuItem menuItemArchive;

	//TabbedPane
	private JTabbedPane tabbedPane;
	private JScrollPane tasksScrollPane;
	private JScrollPane archiveScrollPane;
	private JPanel tasksTab;
	private JPanel calendarTab;

	private JPanel archiveTab;
	private TasksTable taskTable;
	private TasksTable archiveTable;

	//Toolbar buttons
	private JButton newTaskButton;
	private JButton removeButton;
	private JButton doneButton;
	private JButton saveButton;
	private JButton archiveButton;

	//Sub windows
	private AddTaskFrame addTaskFrame;
	private InfoFrame infoFrame;
	private SettingsFrame settingsFrame;

	//Files
	private File taskFile;
	private File archiveFile;

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
		this.toolbar = new JToolBar();
		//toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.Y_AXIS));
		this.controlPanel = new JPanel(new BorderLayout());
		this.contentPanel = new JPanel(new BorderLayout());
		this.infoPanel = new InfoPanel();

		//JPanels
		this.tabbedPane = new JTabbedPane();
		this.tasksTab = new JPanel(new BorderLayout());
		this.archiveTab = new JPanel(new BorderLayout());
		this.tasksScrollPane = new JScrollPane(tasksTab);
		this.archiveScrollPane = new JScrollPane(archiveTab);

		//TabbedPane elements
		this.taskTable = new TasksTable(new DefaultTableModel());
		this.archiveTable = new TasksTable(new DefaultTableModel());
		this.calendarTab = new CalendarPanel(taskTable);

		//Menu
		//Initialise all menus and menu items.
		menuBar = new JMenuBar();
		menuFiles = new JMenu("Files");
		menuHelp = new JMenu("Help");
		menuTask = new JMenu("Task");
		menuItemSave = new JMenuItem("Save");
		menuClose = new JMenuItem("Close");
		menuItemSettings = new JMenuItem("Settings");
		menuItemColours = new JMenuItem("Colours");
		menuItemAbout = new JMenuItem("About");
		menuItemNewTask = new JMenuItem("New task");
		menuItemRemoveTask = new JMenuItem("Remove task");
		menuItemChangeStatus = new JMenuItem("Change status");
		menuItemArchive = new JMenuItem("Archive Task");

		//Toolbar
		this.newTaskButton = new JButton(new ImageIcon(getClass().getResource("/icons/add.png")));
		this.removeButton = new JButton(new ImageIcon(getClass().getResource("/icons/remove.png")));
		this.doneButton = new JButton(new ImageIcon(getClass().getResource("/icons/done.png")));
		this.saveButton = new JButton(new ImageIcon(getClass().getResource("/icons/Save.png")));
		this.archiveButton = new JButton(new ImageIcon(getClass().getResource("/icons/archive.png")));

		//Files
		//System.out.println(System.getProperty("user.dir")+"/userfiles/tasks.txt");
		taskFile = new File(System.getProperty("user.dir")+"/userfiles/tasks.txt");
		archiveFile = new File(System.getProperty("user.dir")+"/userfiles/archive.txt");

		//sub menus
		this.addTaskFrame = new AddTaskFrame(this);
		this.infoFrame = new InfoFrame();
		this.settingsFrame = new SettingsFrame(this);
		centerWindow(infoFrame);
		centerWindow(addTaskFrame);
		centerWindow(settingsFrame);

		//Initialise the preferences
		preferences = new RemPreferences();

		//Basic menu structure.
		windowStructure();
		//this.pack();
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
		this.setIconImage(new ImageIcon(getClass().getResource("/icons/main.png")).getImage());
		centerWindow(this);
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
		menuBar.add(menuFiles);
			//menuFiles.add(menuOpenFiles);
			menuFiles.add(menuItemSave);
			menuFiles.add(menuClose);
		menuBar.add(menuTask);
			menuTask.add(menuItemNewTask);
			menuTask.add(menuItemRemoveTask);
			menuTask.add(menuItemChangeStatus);
			menuTask.add(menuItemArchive);
		menuBar.add(menuHelp);
			//menuHelp.add(menuItemSettings);
			menuHelp.add(menuItemColours);
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
				int p =JOptionPane.showConfirmDialog(null, "Do you want to remove it.","Select an Option",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(p==0){
					taskTable.removeRow();
				}
			}
		});

		menuItemChangeStatus.addActionListener(new ActionListener(){
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
					infoPanel.setStateSaved();
				} catch (IOException e1) {
					System.err.println("Failed to write to file.");
				}
			}
		});

		menuClose.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});

		//Set icon for the menu items
		menuItemSave.setIcon(new ImageIcon(getClass().getResource("/icons/Save.png")));
		menuClose.setIcon(new ImageIcon(getClass().getResource("/icons/Exit.png")));
		menuItemNewTask.setIcon(new ImageIcon(getClass().getResource("/icons/add.png")));
		menuItemRemoveTask.setIcon(new ImageIcon(getClass().getResource("/icons/remove.png")));
		menuItemChangeStatus.setIcon(new ImageIcon(getClass().getResource("/icons/done.png")));
		menuItemArchive.setIcon(new ImageIcon(getClass().getResource("/icons/archive.png")));
		menuItemAbout.setIcon(new ImageIcon(getClass().getResource("/icons/info.png")));
		menuItemSettings.setIcon(new ImageIcon(getClass().getResource("/icons/Settings.png")));
		menuItemColours.setIcon(new ImageIcon(getClass().getResource("/icons/Colour.png")));

		//Set short cuts.
		final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		//menuOpenFiles.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, SHORTCUT_MASK));
		menuClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, SHORTCUT_MASK));
		menuItemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, SHORTCUT_MASK));
		menuItemNewTask.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, SHORTCUT_MASK));
		menuItemRemoveTask.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, SHORTCUT_MASK));
		menuItemArchive.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, SHORTCUT_MASK));
		menuItemChangeStatus.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, SHORTCUT_MASK));

		//Set mnemonics
		menuFiles.setMnemonic(KeyEvent.VK_F);
		menuTask.setMnemonic(KeyEvent.VK_T);
		menuHelp.setMnemonic(KeyEvent.VK_H);
	}

	/**
	 * Sets up all toolbar components.
	 */
	private void settingUpTheToolbar(){
		/* The action listener of the newTaskButton.
		 * If the newButton is pressed:
		 * the required preferences for the AddTaskFrame are loaded,
		 * by default all the inputs are reseted.
		 */
		newTaskButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				addTaskFrame.setVisible(true);
			}
		});

		/* The action listener of the removeButton.
		 * If the removeButton is pressed and the user has select at least one table row,
		 * the selected rows will be removed.
		 */
		removeButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				taskTable.removeRow();
			}
		});

		/* The action listener of the doneButton.
		 * If the doneButton is pressed the status cell of the selected
		 * row will be changed in its state.
		 */
		doneButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				taskTable.updateTableRow();
			}
		});

		/* The action listener of the shiftToArchive button.
		 * Copies the selected rows of the task table in the archive table
		 * and removes the selected rows from the task table.
		 */
		archiveButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				taskTable.shiftTableItemsinOtherTable(archiveTable);
			}
		});

		//Saves the task and archive table.
		saveButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				try {
					FileHandler.writeFile(taskTable.getTableContent(),taskFile);
					FileHandler.writeFile(archiveTable.getTableContent(), archiveFile);
					infoPanel.setStateSaved();
				} catch (IOException e1) {
					System.err.println("Failed to write to file.");
				}
			}
		});

		//Add all components to the toolbar.
		newTaskButton.setMargin(new java.awt.Insets(-2, -2, -2, -2));
		removeButton.setMargin(new java.awt.Insets(-2, -2, -2, -2));
		doneButton.setMargin(new java.awt.Insets(-2, -2, -2, -2));
		archiveButton.setMargin(new java.awt.Insets(-2, -2, -2, -2));

		newTaskButton.setToolTipText("new task");
		removeButton.setToolTipText("remove");
		doneButton.setToolTipText("change status");
		archiveButton.setToolTipText("archive");

		newTaskButton.setBorder(null);
		removeButton.setBorder(null);
		doneButton.setBorder(null);
		archiveButton.setBorder(null);

		toolbar.add(newTaskButton);
		toolbar.add(removeButton);
		toolbar.add(doneButton);
		toolbar.add(archiveButton);
		this.toolbar.addSeparator(new Dimension(3, 10));
		toolbar.setFloatable(false);
		controlPanel.add(toolbar, BorderLayout.CENTER);
	}

	//Setting up the basic window panel structure.
	private void settingUpTheContentPanel(){
		settingUpTaskTable();
		tabbedPane.addTab("Latest", tasksScrollPane);
		tabbedPane.addTab("Archive", archiveScrollPane);
		tabbedPane.addTab("Calendar", calendarTab);
		contentPanel.add(tabbedPane, BorderLayout.CENTER);

		mainPanel.add(controlPanel, BorderLayout.NORTH);
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		mainPanel.add(infoPanel, BorderLayout.SOUTH);
	}

	//Setting up the taskTables.
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

		archiveTab.add(archiveTable, BorderLayout.CENTER);
		archiveTab.add(archiveTable.getTableHeader(), BorderLayout.PAGE_START);
	}

	//Load the table content from files in the table.
	private void loadTableContent(){
		try {
			FileHandler.loadFile(taskTable, taskFile);
			FileHandler.loadFile(archiveTable, archiveFile);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Unable to load files.");
		}finally{
			infoPanel.setStateSaved();
		}
	}

	//Get the task table Object.
	public RemTable getTaskTable(){
		return taskTable;
	}

	/**
	 * Sets the frame parameter centred in the screen
	 * @param frame
	 */
	private static void centerWindow(JFrame frame) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}

	public void setTaskFilePath(final File file){
		this.taskFile = file;
	}

	public void setArchiveFilePath(final File file){
		this.archiveFile = file;
	}

	public RemPreferences getPreferences(){
		return preferences;
	}
}
