package rem;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

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

	private JButton newTaskButton;
	private JButton removeButton;
	private JButton doneButton;
	private JButton infoButton;
	
	public MainWindow(){
		this.windowHeight = 512;
		this.windowWidth = 720;
		this.mainPanel = new JPanel(new BorderLayout());
		this.toolbar = new JToolBar();
		this.contentPanel = new JPanel(new BorderLayout());
		this.infoPanel = new JPanel();

		this.tabbedPane = new JTabbedPane();
		this.archiveScrollPane = new JScrollPane();
		this.tasksScrollPane = new JScrollPane();
		this.tasksTab = new JPanel();
		this.archiveTab = new JPanel();

		this.newTaskButton = new JButton("new");
		this.removeButton = new JButton("remove");
		this.doneButton = new JButton("done");
		this.infoButton = new JButton("info");

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
		toolbar.add(newTaskButton);
		toolbar.add(removeButton);
		toolbar.add(doneButton);
		toolbar.add(infoButton);
		mainPanel.add(toolbar, BorderLayout.NORTH);
	}

	private void settingUpTheContentPanel(){
		tasksScrollPane.add(tasksTab);
		archiveScrollPane.add(archiveTab);
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
}
