package bin.rem;

import java.util.prefs.Preferences;

public class RemPreference {
	private Preferences prefs;
	private static String userPath = "";
	private static String lookCheckBox = "";

	public void setPreference() {
		// This will define a node in which the preferences can be stored
		prefs = Preferences.userRoot().node(this.getClass().getName());
	}
	
	public void setUserPath(String str){
		prefs.put(userPath, str);
	}
	
	public String getUserPath(){
		return prefs.get(userPath ,"get");
	}
	
	public void setLookCheckBox(String str){
		prefs.put(lookCheckBox ,str);
	}
	
	public String getLookCheckBox(){
		return prefs.get(lookCheckBox, "");
	}
}
