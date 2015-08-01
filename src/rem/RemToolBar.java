package rem;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import rem.constants.Icons;
import rem.subwindows.AddEventFrame;
import rem.subwindows.AddTaskFrame;
import rem.table.EventTable;
import rem.table.TasksTable;

/**
 * 
 * @author ovae
 * @version 20150801.
 *
 */
public class RemToolBar extends JToolBar{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton newTaskButton;
	private JButton removeButton;
	private JButton doneButton;
	private JButton doneEventButton;
	private JButton archiveButton;
	private JButton restoreTaskButton;
	private JButton removeArchivedTaskButton;
	private JButton newEventButton;
	private JButton removeEventButton;
	private JButton archiveEventButton;
	private JButton restoreEventButton;

	private JTabbedPane tabbedPane;

	private TasksTable taskTable;
	private TasksTable archiveTable;
	private EventTable eventTable;

	private AddTaskFrame addTaskFrame;
	private AddEventFrame addEventFrame;

	/**
	 * 
	 */
	public RemToolBar(JTabbedPane tabbedPane,TasksTable taskTable,TasksTable archiveTable,EventTable eventTable,AddTaskFrame addTaskFrame,AddEventFrame addEventFrame){
		this.tabbedPane = tabbedPane;
		this.taskTable = taskTable;
		this.archiveTable = archiveTable;
		this.eventTable = eventTable;
		this.addTaskFrame = addTaskFrame;
		this.addEventFrame = addEventFrame;

		newTaskButton = new JButton(Icons.ADD_TASK_ICON.getIcon());
		removeButton = new JButton(Icons.REMOVE_TASK_ICON.getIcon());
		doneButton = new JButton(Icons.CHANGE_TASK_STATE_ICON.getIcon());
		archiveButton = new JButton(Icons.ARCHIVE_ICON.getIcon());
		restoreTaskButton = new JButton(Icons.RESTORE_ICON.getIcon());
		removeArchivedTaskButton = new JButton(Icons.ARCHIVE_REMOVE_ICON.getIcon());
		newEventButton = new JButton(Icons.ADD_EVENT_ICON.getIcon());
		removeEventButton = new JButton(Icons.REMOVE_EVENT_ICON.getIcon());
		archiveEventButton = new JButton(Icons.ARCHIVE_EVENT_ICON.getIcon());
		restoreEventButton = new JButton(Icons.RESTORE_EVENT_ICON.getIcon());
		doneEventButton = new JButton(Icons.CHANGE_EVENT_STATE_ICON.getIcon());
		setUpToolBar();
	}

	/**
	 * 
	 */
	private void setUpToolBar(){
		setButtonsOptions();
		setButtonActionListener();
		setButtonChangeListener();

		this.add(newTaskButton);
		this.add(removeButton);
		this.add(doneButton);
		this.add(archiveButton);
		this.addSeparator(new Dimension(3, 10));
		this.add(newEventButton);
		this.add(removeEventButton);
		this.add(doneEventButton);
		this.add(archiveEventButton);
		this.addSeparator(new Dimension(5, 10));
		this.add(restoreTaskButton);
		this.add(removeArchivedTaskButton);
		this.add(restoreEventButton);
		this.setFloatable(false);
	}

	/**
	 * 
	 */
	private void setButtonsOptions(){
		newTaskButton.setRolloverIcon(Icons.ADD_TASK_HOVER_ICON.getIcon());
		removeButton.setRolloverIcon(Icons.REMOVE_TASK_HOVER_ICON.getIcon());
		doneButton.setRolloverIcon(Icons.CHANGE_TASK_STATE_HOVER_ICON.getIcon());
		archiveButton.setRolloverIcon(Icons.ARCHIVE_HOVER_ICON.getIcon());
		restoreTaskButton.setRolloverIcon(Icons.RESTORE_HOVER_ICON.getIcon());
		removeArchivedTaskButton.setRolloverIcon(Icons.ARCHIVE_REMOVE_HOVER_ICON.getIcon());
		newEventButton.setRolloverIcon(Icons.ADD_EVENT_HOVER_ICON.getIcon());
		removeEventButton.setRolloverIcon(Icons.REMOVE_EVENT_HOVER_ICON.getIcon());
		archiveEventButton.setRolloverIcon(Icons.ARCHIVE_EVENT_HOVER_ICON.getIcon());
		restoreEventButton.setRolloverIcon(Icons.RESTORE_EVENT_HOVER_ICON.getIcon());
		doneEventButton.setRolloverIcon(Icons.CHANGE_EVENT_STATE_HOVER_ICON.getIcon());

		newTaskButton.setToolTipText("new task");
		removeButton.setToolTipText("remove");
		doneButton.setToolTipText("change task status");
		archiveButton.setToolTipText("archive");
		restoreTaskButton.setToolTipText("restore task");
		removeArchivedTaskButton.setToolTipText("remove");
		newEventButton.setToolTipText("new event");
		removeEventButton.setToolTipText("remove event");
		archiveEventButton.setToolTipText("archive event");
		restoreEventButton.setToolTipText("restore event");
		doneEventButton.setToolTipText("change event status");

		newTaskButton.setBorder(null);
		removeButton.setBorder(null);
		doneButton.setBorder(null);
		archiveButton.setBorder(null);
		restoreTaskButton.setBorder(null);
		removeArchivedTaskButton.setBorder(null);
		newEventButton.setBorder(null);
		removeEventButton.setBorder(null);
		doneEventButton.setBorder(null);
		archiveEventButton.setBorder(null);
		restoreEventButton.setBorder(null);

		newTaskButton.setEnabled(true);
		removeButton.setEnabled(true);
		doneButton.setEnabled(true);
		archiveButton.setEnabled(true);
		restoreTaskButton.setEnabled(false);
		removeArchivedTaskButton.setEnabled(false);
		newEventButton.setEnabled(true);
		removeEventButton.setEnabled(false);
		archiveEventButton.setEnabled(false);
		restoreEventButton.setEnabled(false);
		doneEventButton.setEnabled(false);
	}

	/**
	 * 
	 */
	private void setButtonActionListener(){
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

		/*
		 * 
		 */
		restoreTaskButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				archiveTable.shiftTableItemsinOtherTable(taskTable);
			}
		});

		/*
		 * 
		 */
		removeArchivedTaskButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				archiveTable.removeRow();
			}
		});

		/*
		 * 
		 */
		newEventButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				addEventFrame.setVisible(true);
			}
		});

		/*
		 * 
		 */
		removeEventButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				eventTable.removeRow();
			}
		});

		/*
		 * 
		 */
		doneEventButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				eventTable.updateTableRow();
			}
		});

		/*
		 * 
		 */
		archiveEventButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				eventTable.shiftTableItemsinOtherTable(archiveTable);
			}
		});

		/*
		 * 
		 */
		restoreEventButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				archiveTable.shiftTableItemsinOtherTable(eventTable);
			}
		});
	}

	/**
	 * 
	 */
	private void setButtonChangeListener(){
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				switch(tabbedPane.getSelectedIndex()){
					case 0:
						//Latest tab
						newTaskButton.setEnabled(true);
						removeButton.setEnabled(true);
						doneButton.setEnabled(true);
						archiveButton.setEnabled(true);
						restoreTaskButton.setEnabled(false);
						removeArchivedTaskButton.setEnabled(false);
						newEventButton.setEnabled(true);
						removeEventButton.setEnabled(false);
						doneEventButton.setEnabled(false);
						archiveEventButton.setEnabled(false);
						restoreEventButton.setEnabled(false);
						break;
					case 1:
						//Event tab
						newTaskButton.setEnabled(true);
						removeButton.setEnabled(false);
						doneButton.setEnabled(false);
						archiveButton.setEnabled(false);
						restoreTaskButton.setEnabled(false);
						removeArchivedTaskButton.setEnabled(false);
						newEventButton.setEnabled(true);
						removeEventButton.setEnabled(true);
						doneEventButton.setEnabled(true);
						archiveEventButton.setEnabled(true);
						restoreEventButton.setEnabled(false);
						break;
					case 2:
						//Archive tab
						newTaskButton.setEnabled(true);
						removeButton.setEnabled(false);
						doneButton.setEnabled(false);
						archiveButton.setEnabled(false);
						restoreTaskButton.setEnabled(true);
						removeArchivedTaskButton.setEnabled(true);
						newEventButton.setEnabled(true);
						removeEventButton.setEnabled(false);
						doneEventButton.setEnabled(false);
						archiveEventButton.setEnabled(false);
						restoreEventButton.setEnabled(true);
						break;
					case 3:
						//Node tab
						newTaskButton.setEnabled(true);
						removeButton.setEnabled(false);
						doneButton.setEnabled(false);
						archiveButton.setEnabled(false);
						restoreTaskButton.setEnabled(false);
						removeArchivedTaskButton.setEnabled(false);
						newEventButton.setEnabled(true);
						removeEventButton.setEnabled(false);
						doneEventButton.setEnabled(false);
						archiveEventButton.setEnabled(false);
						restoreEventButton.setEnabled(false);
						break;
					case 4:
						//Calendar tab
						newTaskButton.setEnabled(true);
						removeButton.setEnabled(false);
						doneButton.setEnabled(false);
						archiveButton.setEnabled(false);
						restoreTaskButton.setEnabled(false);
						removeArchivedTaskButton.setEnabled(false);
						newEventButton.setEnabled(true);
						removeEventButton.setEnabled(false);
						doneEventButton.setEnabled(false);
						archiveEventButton.setEnabled(false);
						restoreEventButton.setEnabled(false);
						break;
					default:
						//Error
				}
			}
		});
	}

}
