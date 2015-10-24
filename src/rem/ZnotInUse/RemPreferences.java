package rem.ZnotInUse;

import java.util.prefs.Preferences;

/**
 * 
 * @author ovae.
 * @version 20150824.
 */
public class RemPreferences{

	private Preferences preferences;
	private String taskPath;
	private String archivePath;

	/**
	 * 
	 */
	public RemPreferences(){
		// This will define a node in which the preferences can be stored
		this.preferences = Preferences.userRoot().node(this.getClass().getName());
		this.taskPath = "tasksPath";
		this.archivePath = "archivePath";
	}

	/**
	 * 
	 * @param userPath
	 */
	public void storeTasksFilePath(final String userPath){
		if(userPath == null){
			throw new IllegalArgumentException("Userpath can not be null.");
		}
		if(userPath.trim().isEmpty()){
			throw new IllegalArgumentException("Userpath can not be empty.");
		}
		preferences.put(this.taskPath, userPath);
	}

	/**
	 * 
	 * @param userPath
	 */
	public void storeArchiveFilePath(final String userPath){
		if(userPath == null){
			throw new IllegalArgumentException("Userpath can not be null.");
		}
		if(userPath.trim().isEmpty()){
			throw new IllegalArgumentException("Userpath can not be empty.");
		}
		preferences.put(this.archivePath, userPath);
	}

	/**
	 * 
	 * @return the path of the tasks file.
	 */
	public String loadTasksFilePath(){
		return preferences.get(this.taskPath ,"");
	}

	/**
	 * 
	 * @return the path of the archive file.
	 */
	public String loadArchiveFilePath(){
		return preferences.get(this.archivePath ,"");
	}

}
