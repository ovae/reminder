package rem.preference;
import java.util.prefs.Preferences;

public class RemPreferences {
	private Preferences preferences;
	private String userPath;

	/**
	 * 
	 */
	private RemPreferences(){
		// This will define a node in which the preferences can be stored
		this.preferences = Preferences.userRoot().node(this.getClass().getName());
	}

	/**
	 * 
	 * @param userPath
	 */
	private void storeUserPath(final String userPath){
		if(userPath == null){
			throw new IllegalArgumentException("Userpath can not be null.");
		}
		if(userPath.trim().isEmpty()){
			throw new IllegalArgumentException("Userpath can not be empty.");
		}
		preferences.put(this.userPath, userPath);
	}

	/**
	 * 
	 * @return
	 */
	private String loadUserPath(){
		return preferences.get(this.userPath ,"");
	}

}
