package bin.rem;

import java.util.prefs.Preferences;

public class RemPreference {
	private Preferences prefs;
	private static String userPath = "userPath";
	private static String lookCheckBox = "lookCheckBox";
	private static String colorCheckBox = "ColorCheckBox";
	private static String timeFormate = "timeFormate";

	/**
	 * init the preference
	 */
	public void setPreference() {
		// This will define a node in which the preferences can be stored
		prefs = Preferences.userRoot().node(this.getClass().getName());
	}
	
	
	//Userpath*****************************************************************
	/**
	 * Set the user file path.
	 * @param str
	 */
	public void setUserPath(String str){
		prefs.put(userPath, str);
	}
	
	/**
	 * Get the user file path from the preferences.
	 * @return
	 */
	public String getUserPath(){
		return prefs.get(userPath ,"");
	}
	
	//Look*********************************************************************
	/**
	 * set the checkbox to true or false in the preferences.
	 * @param bool
	 */
	public void setLookCheckBox(Boolean bool){
		prefs.putBoolean(lookCheckBox ,bool);
	}
	
	/**
	 * Get a boolean value about the look checkbox.
	 * @return
	 */
	public Boolean getLookCheckBox(){
		return prefs.getBoolean(lookCheckBox, false);
	}
	
	//Colorized****************************************************************
	/**
	 * Set or unset the color checkbox in the preference.
	 * @param bool
	 */
	public void setColorCheckBox(Boolean bool){
		prefs.putBoolean(colorCheckBox ,bool);
	}
	
	/**
	 * get the boolean about the color checkbox out of the preference
	 * @return
	 */
	public Boolean getColorCheckBox(){
		return prefs.getBoolean(colorCheckBox, true);
	}
	//timeFormate***************************************************************
	/**
	 * Set the time format in the preference
	 * @param formate
	 */
	public void setTimeFormate(int formate){
		prefs.putInt(timeFormate, formate);
	}
	
	/**
	 * get the time format out of the preferences
	 * @return
	 */
	public int getTimeFormate(){
		return prefs.getInt(timeFormate, 0);
	}
	
}
