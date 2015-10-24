package rem.panels;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import rem.MainWindow;
import rem.constants.Colour;
import rem.constants.Messages;
import rem.files.FileHandler;
import rem.table.EventTable;
import rem.table.TaskTable;

/**
 * 
 * @author ovae
 * @version 20150920.
 */
public class BackUpPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MainWindow mainwindow;
	private JTextArea noteField;

	/**
	 * 
	 */
	public BackUpPanel(MainWindow mainwindow, JTextArea noteField){
		if(mainwindow.equals(null)){
			throw new IllegalArgumentException("The window can not be null.");
		}
		this.mainwindow = mainwindow;
		this.noteField = noteField;
		this.setLayout(new BorderLayout());
		setUpContent();
	}

	/**
	 * 
	 */
	private void setUpContent(){
		JButton backUpButton = new JButton("BackUp");

		/**
		 * 
		 */
		backUpButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				save();
			}

		});
		JTextArea area = new JTextArea("Back-Up\n"+mainwindow.getTaskFile().getPath().toString());
		area.setEditable(false);
		area.setMargin(new Insets(10,10,0,10));
		area.setBackground(Colour.TABLE_DEFAULT.getColor());
		this.add(area, BorderLayout.CENTER);
		this.add(backUpButton, BorderLayout.SOUTH);
	}

	/**
	 * 
	 */
	private void save(){
		String timestamp = new Timestamp(System.currentTimeMillis()).toString();
		TaskTable tasktable = (TaskTable)mainwindow.getTaskTable();
		TaskTable archivetable = mainwindow.getArchiveTable();
		EventTable eventtable = (EventTable) mainwindow.getEventTable();

		File taskFile = new File(System.getProperty("user.dir")+"/userfiles/bachup_tasks_"+timestamp+".txt");
		File archiveFile = new File(System.getProperty("user.dir")+"/userfiles/bachup_archive_"+timestamp+".txt");
		File eventFile = new File(System.getProperty("user.dir")+"/userfiles/bachup_event_"+timestamp+".txt");
		File noteFile = new File(System.getProperty("user.dir")+"/userfiles/bachup_note_"+timestamp+".txt");

		try {
			FileHandler.writeFile( tasktable.getTableContent(),		taskFile);
			FileHandler.writeFile( archivetable.getTableContent(),	archiveFile);
			FileHandler.writeFile( eventtable.getTableContent(),	eventFile);
			FileHandler.writeNoteFile(noteFile, noteField.getText());
		} catch (IOException e1) {
			System.err.println(Messages.FAILED_TO_SAVE.getMsg());
		}
	}
}
