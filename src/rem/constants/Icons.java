package rem.constants;

import javax.swing.ImageIcon;

/**
 * 
 * @author ovae.
 * @version 20150501.
 */
public enum Icons {
	MAIN_ICON				("/icons/main.png"),
	ABOUT_ICON				("/icons/about.png"),
	SETTINGS_ICON			("/icons/Settings.png"),
	COLOUR_ICON				("/icons/Colour.png"),
	ADD_TASK_ICON			("/icons/add.png"),
	ADD_TASK_HOVER_ICON		("/icons/addHover.png"),
	REMOVE_TASK_ICON		("/icons/remove.png"),
	REMOVE_TASK_HOVER_ICON	("/icons/removeHover.png"),
	DONE_ICON				("/icons/done.png"),
	DONE_HOVER_ICON			("/icons/doneHover.png"),
	SAVE_ICON				("/icons/Save.png"),
	EXIT_ICON				("/icons/Exit.png"),
	ARCHIVE_ICON			("/icons/archive.png"),
	ARCHIVE_HOVER_ICON		("/icons/archiveHover.png"),
	ARCHIVE_REMOVE_ICON		("/icons/archiveRemove.png"),
	ARCHIVE_REMOVE_HOVER_ICON("/icons/archiveRemoveHover.png"),
	RESTORE_ICON			("/icons/restore.png"),
	RESTORE_HOVER_ICON		("/icons/restoreHover.png"),
	ADD_EVENT_ICON			("/icons/addEvent.png"),
	ADD_EVENT_HOVER_ICON	("/icons/addEventHover.png"),
	REMOVE_EVENT_ICON		("/icons/removeEvent.png"),
	REMOVE_EVENT_HOVER_ICON	("/icons/removeEventHover.png"),
	RESTORE_EVENT_ICON		("/icons/restoreEvent.png"),
	RESTORE_EVENT_HOVER_ICON("/icons/restoreEventHover.png"),
	ARCHIVE_EVENT_ICON		("/icons/archiveEvent.png"),
	ARCHIVE_EVENT_HOVER_ICON("/icons/archiveEventHover.png"),
	PLACEHOLDER				("/icons/placeholder.png"),
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
