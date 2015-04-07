package rem.preference;

import java.util.prefs.Preferences;

/**
 * 
 * @author ovae.
 * @version 20150408.
 */
public class RemPreferences{

	private Preferences preferences;
	private String taskPath;
	private String archivePath;

	public RemPreferences(){
		// This will define a node in which the preferences can be stored
		this.preferences = Preferences.userRoot().node(this.getClass().getName());
		this.taskPath = "tasksPath";
		this.archivePath = "archivePath";
	}

	public void storeTasksFilePath(final String userPath){
		if(userPath == null){
			throw new IllegalArgumentException("Userpath can not be null.");
		}
		if(userPath.trim().isEmpty()){
			throw new IllegalArgumentException("Userpath can not be empty.");
		}
		preferences.put(this.taskPath, userPath);
	}

	public void storeArchiveFilePath(final String userPath){
		if(userPath == null){
			throw new IllegalArgumentException("Userpath can not be null.");
		}
		if(userPath.trim().isEmpty()){
			throw new IllegalArgumentException("Userpath can not be empty.");
		}
		preferences.put(this.archivePath, userPath);
	}

	public String loadTasksFilePath(){
		return preferences.get(this.taskPath ,"");
	}

	public String loadArchiveFilePath(){
		return preferences.get(this.archivePath ,"");
	}

}
