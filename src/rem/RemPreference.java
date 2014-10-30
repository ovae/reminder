package bin.rem;

import java.util.prefs.Preferences;

public class RemPreference {
	private Preferences prefs;
	private static String userPath = "";
	private static String lookCheckBox = "false";
	private static String colorCheckBox = "true";


	public void setPreference() {
		// This will define a node in which the preferences can be stored
		prefs = Preferences.userRoot().node(this.getClass().getName());
	}
	
	
	//Userpath*****************************************************************
	public void setUserPath(String str){
		prefs.put(userPath, str);
	}
	
	public String getUserPath(){
		return prefs.get(userPath ,"get");
	}
	
	//Look*********************************************************************
	public void setLookCheckBox(Boolean bool){
		prefs.putBoolean(lookCheckBox ,bool);
	}
	
	public Boolean getLookCheckBox(){
		return prefs.getBoolean(lookCheckBox, false);
	}
	
	//Colorized****************************************************************
	public void setColorCheckBox(Boolean bool){
		prefs.putBoolean(colorCheckBox ,bool);
	}
	
	public Boolean getColorCheckBox(){
		return prefs.getBoolean(colorCheckBox, true);
	}
}
