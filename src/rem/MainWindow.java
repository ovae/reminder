package rem;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import subWindows.AddTaskFrame;

public class MainWindow extends JFrame{

	private int windowHeight;
	private int windowWidth;
	private JPanel mainPanel;

	private JToolBar toolbar;
	private JPanel contentPanel;
	private JPanel infoPanel;

	private JTabbedPane tabbedPane;
	private JScrollPane tasksScrollPane;
	private JScrollPane archiveScrollPane;
	private JPanel tasksTab;
	private JPanel archiveTab;
	private TasksTable taskTable;
	private JTable archiveTable;

	private JButton newTaskButton;
	private JButton removeButton;
	private JButton doneButton;
	private JButton infoButton;
	
	//Sub windows
	private AddTaskFrame addTaskFrame;

	/**
	 * 
	 */
	public MainWindow(){
		this.windowHeight = 512;
		this.windowWidth = 720;
		this.mainPanel = new JPanel(new BorderLayout());
		this.toolbar = new JToolBar();
		this.contentPanel = new JPanel(new BorderLayout());
		this.infoPanel = new JPanel();

		this.tabbedPane = new JTabbedPane();
		this.tasksTab = new JPanel(new BorderLayout());
		this.archiveTab = new JPanel(new BorderLayout());
		this.tasksScrollPane = new JScrollPane(tasksTab);
		this.archiveScrollPane = new JScrollPane(archiveTab);
		
		this.taskTable = new TasksTable(new DefaultTableModel());
		//this.archiveTable = new RemTable();

		this.newTaskButton = new JButton("new");
		this.removeButton = new JButton("remove");
		this.doneButton = new JButton("done");
		this.infoButton = new JButton("info");

		this.addTaskFrame = new AddTaskFrame(this);
		centerWindow(addTaskFrame);

		windowStructure();
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
				settingUpTheInfoPanel();
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

		toolbar.add(newTaskButton);
		toolbar.add(removeButton);
		toolbar.add(doneButton);
		toolbar.add(infoButton);
		mainPanel.add(toolbar, BorderLayout.NORTH);
	}

	private void settingUpTheContentPanel(){
		settingUpTaskTable();
		tabbedPane.addTab("Latest", tasksScrollPane);
		tabbedPane.addTab("Archive", archiveScrollPane);
		contentPanel.add(tabbedPane, BorderLayout.CENTER);
		mainPanel.add(contentPanel, BorderLayout.CENTER);
	}

	private void settingUpTheInfoPanel(){
		JLabel time = new JLabel("Info Panel");
		infoPanel.add(time);
		mainPanel.add(infoPanel, BorderLayout.SOUTH);
	}

//------------------------------------------------

	private void settingUpTaskTable(){
		String[] columnNames = {"Topic", "About", "Begin", "End", "Status"};
		taskTable.setTableHeader(columnNames);
		taskTable.setTableModel();
		tasksTab.add(taskTable, BorderLayout.CENTER);
		tasksTab.add(taskTable.getTableHeader(), BorderLayout.NORTH);
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
