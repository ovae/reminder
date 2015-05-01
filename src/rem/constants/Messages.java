package rem.constants;

/**
 * 
 * @author ovae.
 * @version 20150501.
 */
public enum Messages {
	NO_SELECTED_ROW ("You have to select at least one row."),
	UNABLE_TO_LOAD  ("Unable to load"),
	FAILED_TO_SAVE  ("Failed to write to file.");

	private String message;

	Messages(final String message){
		this.message = message;
	}

	public String getMsg(){
		return message;
	}
}
