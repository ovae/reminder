package rem;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import rem.constants.Icons;
import rem.constants.Messages;
import rem.files.FileHandler;
import rem.panels.InfoPanel;
import rem.subwindows.AddEventFrame;
import rem.subwindows.AddTaskFrame;
import rem.subwindows.HelpFrame;
import rem.table.EventTable;
import rem.table.TaskTable;

/**
 * The menu bar of the {@link MainWindow}.
 * @author ovae.
 * @version 20150801.
 *
 */
public class RemMenuBar extends JMenuBar{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// The menus
	private JMenu menuMenus;
	private JMenu menuHelp;

	// The menu items
	private JMenuItem menuItemSave;
	private JMenuItem menuClose;
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
	private JMenuItem menuItemHelp;

	private JTabbedPane tabbedPane;
	private InfoPanel infoPanel;

	// Tables
	private TaskTable taskTable;
	private TaskTable archiveTable;
	private EventTable eventTable;

	// Frames
	private AddTaskFrame addTaskFrame;
	private AddEventFrame addEventFrame;
	private HelpFrame helpFrame;

	// Files
	private File taskFile;
	private File archiveFile;
	private File eventFile;
	private File noteFile;
	private JTextArea noteField;

	/**
	 * Creates a new RemMenuBar.
	 * @param mainwindow the main window.
	 * @param addTaskFrame the frame to add tasks to the tasks table.
	 * @param addEventFrame the frame to add events to the event table.
	 * @param noteField
	 */
	public RemMenuBar(MainWindow mainwindow,AddTaskFrame addTaskFrame,
											AddEventFrame addEventFrame,
											JTextArea noteField){
		this.tabbedPane = mainwindow.getTabbedPane();
		this.infoPanel = mainwindow.getInfoPanel();
		this.taskTable = (TaskTable) mainwindow.getTaskTable();
		this.archiveTable = mainwindow.getArchiveTable();
		this.eventTable = (EventTable) mainwindow.getEventTable();
		this.addTaskFrame = addTaskFrame;
		this.addEventFrame = addEventFrame;
		this.taskFile = mainwindow.getTaskFile();
		this.archiveFile = mainwindow.getArchiveFile();
		this.eventFile = mainwindow.getEventFile();
		this.noteFile = mainwindow.getNoteFile();
		this.noteField = noteField;
		helpFrame = new HelpFrame(mainwindow, noteField);

		menuMenus = new JMenu("\u2630 Menu");
			menuItemNewTask = new JMenuItem("New task");
			menuItemRemoveTask = new JMenuItem("Remove task");
			menuItemChangeTaskStatus = new JMenuItem("Change status");
			menuItemArchive = new JMenuItem("Archive task");

			menuItemRestore = new JMenuItem("Restore task");
			menuItemRemoveArchiveTask = new JMenuItem("Remove");
			menuItemRestoreEvent = new JMenuItem("Restore event");

			menuItemNewEvent = new JMenuItem("New event");
			menuItemRemoveEvent = new JMenuItem("Remove event");
			menuItemChangeEventStatus = new JMenuItem("Change status");
			menuItemArchiveEvent = new JMenuItem("Archive event");

			menuClose = new JMenuItem("Close");
			menuItemSave = new JMenuItem("Save");
			
		menuHelp = new JMenu("Help");
			menuItemHelp = new JMenuItem("Help");

		setUpMenuBar();
	}

	/**
	 * Sets up the menu bar.
	 */
	private void setUpMenuBar(){
		setMenuOptions();
		setMenuActionListener();
		setMenuChangeListener();

		this.add(menuMenus);
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

		this.add(menuHelp);
			menuHelp.add(menuItemHelp);
	}

	/**
	 * Set up the menu options.
	 */
	private void setMenuOptions(){
		//Set icon for the menu items
		menuItemSave.setIcon(Icons.SAVE_ICON.getIcon());
		menuClose.setIcon(Icons.EXIT_ICON.getIcon());
		menuItemHelp.setIcon(Icons.PLACEHOLDER.getIcon());

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
	 * Sets up the menu action listener.
	 */
	private void setMenuActionListener(){
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

		//Help Menu
		menuItemHelp.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				helpFrame.setVisible(true);
			}
		});
	}

	/**
	 * Sets up the menu change listener.
	 */
	private void setMenuChangeListener(){
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
						throw new IllegalStateException("This state is not normal, isn't it!");
				}
			}
		});
	}
}
