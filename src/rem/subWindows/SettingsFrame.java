package rem.subWindows;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import rem.MainWindow;

/**
 * 
 * @author ovae.
 * @version 20150303
 */
public class SettingsFrame extends JFrame{

	private JPanel mainPanel;
	private JPanel contentPanel;
	private JPanel controlPanel;
	private MainWindow parentFrame;

	private JTextField tasksPathField;
	private JTextField archivePathField;

	private JButton cancelButton;
	private JButton saveButton;
	private JButton changeTaskPathButton;
	private JButton changeArchivePathButton;

	public SettingsFrame(final MainWindow parentFrame){
		super("Settings");
		mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
		windowSettings();

		this.parentFrame = parentFrame;
		this.contentPanel = new JPanel();
		this.controlPanel = new JPanel(new BorderLayout());
		this.cancelButton = new JButton("Cancel");
		this.saveButton = new JButton("Save");

		this.tasksPathField = new JTextField();
		this.archivePathField = new JTextField();
		this.changeTaskPathButton = new JButton("\u21A9");
		this.changeArchivePathButton = new JButton("\u21A9");

		this.setUpAllComponents();
		this.setVisible(false);
	}

	private void windowSettings(){
		this.setLocationRelativeTo(null);
		this.setSize(new Dimension(400,256));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	private void setUpAllComponents(){
		//Set up main panel.
		this.add(contentPanel, BorderLayout.CENTER);
		this.add(controlPanel, BorderLayout.SOUTH);

		//Set up the content panel.
		JLabel taskLabel = new JLabel("task.txt path:");
		JLabel archiveLabel = new JLabel("archive.txt path:");
		contentPanel.setLayout(null);
		contentPanel.setBorder(BorderFactory.createTitledBorder("Filepath"));
		tasksPathField.setEditable(false);
		archivePathField.setEditable(false);

		taskLabel.setBounds(10, 100, 100, 28);
		tasksPathField.setBounds(10, 100, 200, 28);
		changeTaskPathButton.setBounds(0, 28, 28, 28);
		archiveLabel.setBounds(21, 100, 100, 28);
		archivePathField.setBounds(10, 100, 200, 28);
		changeArchivePathButton.setBounds(0, 28, 28, 28);

		taskLabel.setLocation(40, 40);
		tasksPathField.setLocation(140, 40);
		changeTaskPathButton.setLocation(350, 40);
		archiveLabel.setLocation(40, 70);
		archivePathField.setLocation(140, 70);
		changeArchivePathButton.setLocation(350, 70);

		changeTaskPathButton.setMargin(new Insets(0, 0, 0, 0));
		changeArchivePathButton.setMargin(new Insets(0, 0, 0, 0));

		contentPanel.add(taskLabel);
		contentPanel.add(tasksPathField);
		contentPanel.add(changeTaskPathButton);
		contentPanel.add(archiveLabel);
		contentPanel.add(archivePathField);
		contentPanel.add(changeArchivePathButton);

		changeTaskPathButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser("user.dir");
				chooser.showOpenDialog(null);
				tasksPathField.setText(chooser.getSelectedFile().toString());
				//parentFrame.setTaskFilePath(chooser.getSelectedFile());
			}
		});

		changeArchivePathButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser("user.dir");
				chooser.showOpenDialog(null);
				archivePathField.setText(chooser.getSelectedFile().toString());
				//parentFrame.setArchiveFilePath(chooser.getSelectedFile());
			}
		});

		//Set up control panel
		controlPanel.add(saveButton, BorderLayout.WEST);
		controlPanel.add(cancelButton, BorderLayout.CENTER);
	}
}
