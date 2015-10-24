package rem.constants;

import javax.swing.ImageIcon;

/**
 * This enum contains all path of all icons in this programm.
 * @author ovae.
 * @version 20151024.
 */
public enum Icons {
	MAIN_ICON					("/icons/program_main.png"),
	ABOUT_ICON					("/icons/program_about.png"),
	SETTINGS_ICON				("/icons/program_settings.png"),
	COLOUR_ICON					("/icons/program_colour.png"),
	SAVE_ICON					("/icons/program_save.png"),
	EXIT_ICON					("/icons/program_exit.png"),
	PLACEHOLDER					("/icons/placeholder.png"),

	ADD_TASK_ICON				("/icons/addTask.png"),
	ADD_TASK_HOVER_ICON			("/icons/addTaskHover.png"),
	REMOVE_TASK_ICON			("/icons/removeTask.png"),
	REMOVE_TASK_HOVER_ICON		("/icons/removeTaskHover.png"),
	CHANGE_TASK_STATE_ICON		("/icons/changeTaskState.png"),
	CHANGE_TASK_STATE_HOVER_ICON("/icons/changeTaskStateHover.png"),
	ARCHIVE_ICON				("/icons/archiveTask.png"),
	ARCHIVE_HOVER_ICON			("/icons/archiveTaskHover.png"),

	ARCHIVE_REMOVE_ICON			("/icons/archiveRemove.png"),
	ARCHIVE_REMOVE_HOVER_ICON	("/icons/archiveRemoveHover.png"),
	RESTORE_ICON				("/icons/restoreTask.png"),
	RESTORE_HOVER_ICON			("/icons/restoreTaskHover.png"),

	ADD_EVENT_ICON				("/icons/addEvent.png"),
	ADD_EVENT_HOVER_ICON		("/icons/addEventHover.png"),
	REMOVE_EVENT_ICON			("/icons/removeEvent.png"),
	REMOVE_EVENT_HOVER_ICON		("/icons/removeEventHover.png"),
	CHANGE_EVENT_STATE_ICON		("/icons/changeEventState.png"),
	CHANGE_EVENT_STATE_HOVER_ICON("/icons/changeEventStateHover.png"),
	RESTORE_EVENT_ICON			("/icons/restoreEvent.png"),
	RESTORE_EVENT_HOVER_ICON	("/icons/restoreEventHover.png"),
	ARCHIVE_EVENT_ICON			("/icons/archiveEvent.png"),
	ARCHIVE_EVENT_HOVER_ICON	("/icons/archiveEventHover.png"),

	DEV_Mode_ICON				("/icons/devMode.png"),
	DEV_Mode_HOVER_ICON			("/icons/devModeHover.png")
	;

	private final String path;

	Icons(final String path){
		this.path = path;
	}

	public ImageIcon getIcon(){
		return new ImageIcon(getClass().getResource(this.toString()));
	}

	@Override
	public String toString(){
		return path;
	}
}
