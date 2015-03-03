package rem;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import rem.files.FileHandler;
import subWindows.AddTaskFrame;
import subWindows.InfoFrame;

public class MainWindow extends JFrame{

	private int windowHeight;
	private int windowWidth;
	private JPanel mainPanel;

	private JToolBar toolbar;
	private JPanel contentPanel;
	private InfoPanel infoPanel;

	//TabbedPane
	private JTabbedPane tabbedPane;
	private JScrollPane tasksScrollPane;
	private JScrollPane archiveScrollPane;
	private JPanel tasksTab;
	private JPanel archiveTab;
	private TasksTable taskTable;
	private TasksTable archiveTable;
	private RemGregorianCalendar calendarPane;
	private JScrollPane scrollCalendar;

	//Toolbar buttons
	private JButton newTaskButton;
	private JButton removeButton;
	private JButton doneButton;
	private JButton saveButton;
	private JButton archiveButton;
	private JButton settingsButton;
	private JButton infoButton;
	
	//Sub windows
	private AddTaskFrame addTaskFrame;
	private InfoFrame infoFrame;

	//Files
	private File taskFile;
	private File archiveFile;

	/**
	 * 
	 */
	public MainWindow(){
		this.windowHeight = 512;
		this.windowWidth = 800;
		this.mainPanel = new JPanel(new BorderLayout());
		this.toolbar = new JToolBar();
		this.contentPanel = new JPanel(new BorderLayout());
		this.infoPanel = new InfoPanel();

		//JPanels
		this.tabbedPane = new JTabbedPane();
		this.tasksTab = new JPanel(new BorderLayout());
		this.archiveTab = new JPanel(new BorderLayout());
		this.tasksScrollPane = new JScrollPane(tasksTab);
		this.archiveScrollPane = new JScrollPane(archiveTab);
		
		//tabbed elements
		this.taskTable = new TasksTable(new DefaultTableModel());
		this.archiveTable = new TasksTable(new DefaultTableModel());
		calendarPane = new RemGregorianCalendar();
		scrollCalendar = new JScrollPane(calendarPane);

		//toolbar
		this.newTaskButton = new JButton("new");
		this.removeButton = new JButton("remove");
		this.doneButton = new JButton("done");
		this.saveButton = new JButton("save");
		this.archiveButton = new JButton("archive");
		this.settingsButton = new JButton("settings");
		this.infoButton = new JButton("info");

		//Files
		taskFile = new File("userfiles/tasks.txt");
		archiveFile = new File("userfiles/archive.txt");

		//submenus
		this.addTaskFrame = new AddTaskFrame(this);
		this.infoFrame = new InfoFrame();
		centerWindow(infoFrame);
		centerWindow(addTaskFrame);

		windowStructure();
		this.pack();
		this.setVisible(true);
	}

	private void windowSettings(){
		this.setTitle("Reminder");
		this.setSize(new Dimension(windowWidth,windowHeight));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(640, windowHeight));
		this.setLayout(new BorderLayout());
		this.setContentPane(mainPanel);
		centerWindow(this);
	}

	private void windowStructure(){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				windowSettings();
				settingUpTheToolbar();
				settingUpTheContentPanel();
				
				loadTableContent();
			}
		});
	}

	private void settingUpTheToolbar(){
		toolbar.setFloatable(false);

		/* The action listener of the newButton.
		 * If the newButton is pressed:
		 * the required preferences for the AddFrame are loaded,
		 * by default all the inputs are reset.
		 */
		newTaskButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				addTaskFrame.setVisible(true);
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
					taskTable.removeRow();
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
				shiftTableItemsinArchive();
			}
		});

		infoButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				infoFrame.setVisible(true);
			}
		});

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

		toolbar.add(newTaskButton);
		toolbar.add(removeButton);
		toolbar.add(doneButton);
		toolbar.add(saveButton);
		toolbar.add(archiveButton);
		this.toolbar.addSeparator(new Dimension(3, 10));
		toolbar.add(settingsButton);
		toolbar.add(infoButton);
		mainPanel.add(toolbar, BorderLayout.NORTH);
	}

	private void settingUpTheContentPanel(){
		settingUpTaskTable();
		tabbedPane.addTab("Latest", tasksScrollPane);
		tabbedPane.addTab("Archive", archiveScrollPane);
		tabbedPane.addTab("Calendar", scrollCalendar);
		contentPanel.add(tabbedPane, BorderLayout.CENTER);
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		
		mainPanel.add(infoPanel, BorderLayout.SOUTH);
	}

//------------------------------------------------

	private void settingUpTaskTable(){
		//Task tab
		String[] columnNames = {"Topic", "About", "Begin", "End", "Status"};
		taskTable.setTableHeader(columnNames);
		taskTable.setTableModel();
		taskTable.setTableRowColor();

		tasksTab.add(taskTable, BorderLayout.CENTER);
		tasksTab.add(taskTable.getTableHeader(), BorderLayout.NORTH);

		taskTable.checkIfTableHasChanged(infoPanel);

		//Archive tab
		String[] columnNamesArchive = {"Topic", "About", "Begin", "End", "Result"};
		archiveTable.setTableHeader(columnNamesArchive);
		archiveTable.setTableModel();

		archiveTab.add(archiveTable, BorderLayout.CENTER);
		archiveTab.add(archiveTable.getTableHeader(), BorderLayout.NORTH);

	}

	private void loadTableContent(){
		try {
			FileHandler.loadFile(taskTable, taskFile);
			FileHandler.loadFile(archiveTable, archiveFile);
			
		} catch (IOException e) {
			System.err.println("Failed to load a file.");
		}finally{
			infoPanel.setStateSaved();
		}
	}

	/**
	 *Shift the selected rows of the Tasks table in the archive table.
	 */
	private void shiftTableItemsinArchive(){
		int[] rows = taskTable.getSelectedRows();
		TableModel tm= taskTable.getModel();
		for(int i = 0 ; i < rows.length; i++){
			taskTable.addRow(archiveTable, (String) taskTable.getValueAt(i,0),
									(String) taskTable.getValueAt(i,1),
									(String) taskTable.getValueAt(i,2),
									(String) taskTable.getValueAt(i,3),
									(String) taskTable.getValueAt(i,4));
		}
		
		while(rows.length>0){
			((DefaultTableModel)tm).removeRow(taskTable.convertRowIndexToModel(rows[0]));
			rows = taskTable.getSelectedRows();
		}
		
		//add the remove function TODO
		
		taskTable.clearSelection();
	}

	public RemTable getTaskTable(){
		return taskTable;
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
}
