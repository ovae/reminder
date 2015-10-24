package rem.subwindows;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import rem.MainWindow;
import rem.constants.Icons;
import rem.panels.AboutPanel;
import rem.panels.BackUpPanel;
import rem.panels.ColourPanel;
import rem.util.Util;

public class HelpFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel mainPanel;
	private JTabbedPane tabPane;
	private MainWindow mainwindow;
	private JTextArea noteField;

	/**
	 * Creates a new HelpFrame.
	 */
	public HelpFrame(MainWindow mainwindow, JTextArea noteField){
		super("Help");

		if(mainwindow == null){
			throw new IllegalArgumentException("The window can not be null.");
		}
		this.mainwindow = mainwindow;
		this.noteField = noteField;

		this.setVisible(false);
		mainPanel = new JPanel(new BorderLayout());
		tabPane = new JTabbedPane(JTabbedPane.LEFT);
		mainPanel.add(tabPane, BorderLayout.CENTER);

		//set the basic setting for the frame.
		this.setSize(new Dimension(420,280));
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setLayout(new BorderLayout());
		Util.centerWindow(this);

		this.add(mainPanel);
		setUpTabs();
	}

	/**
	 * 
	 */
	private void setUpTabs(){
		tabPane.addTab("Colour",new ColourPanel());
		tabPane.addTab("BackUp",new BackUpPanel(mainwindow, noteField));
		tabPane.addTab("About", new AboutPanel());
		tabPane.setIconAt(0, Icons.COLOUR_ICON.getIcon());
		tabPane.setIconAt(1, Icons.PLACEHOLDER.getIcon());
		tabPane.setIconAt(2, Icons.ABOUT_ICON.getIcon());
	}
}
