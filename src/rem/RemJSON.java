package bin.rem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


public class RemJSON {
	
	//private JSONObject obj = new JSONObject();
	private List taskList = new LinkedList();
	JSONObject Mainobj = new JSONObject();

	/**
	 * Add a task to the JSON object list.
	 * @param tableHead
	 * @param topic
	 * @param about
	 * @param begin
	 * @param end
	 * @param status
	 */
	@SuppressWarnings("unchecked")
	public void setTask(String[] tableHead, String topic, String about, String begin, String end, String status){
		JSONObject obj = new JSONObject();
		obj.put(""+tableHead[0]+"", topic);
		obj.put("About", about);
		obj.put("Begin", begin);
		obj.put("End", end);
		obj.put("Status", status);
		taskList.add(obj);
		Mainobj.put("Tasks", taskList);
	}
	
	/**
	 * Write the JSON object list to a file.
	 * @param path
	 */
	public void writeJsonToFile(File path){
			FileWriter file;
			try {
				file = new FileWriter(path);
				try {
					file.write( JSONValue.toJSONString(Mainobj)+"\n");
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					file.flush();
					file.close();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}
	
	
	/**
	 * 
	 */
	private void updateJsonFile(){
		
	}
	
	/**
	 * 
	 * @param tempFile
	 * @return
	 */
	@SuppressWarnings("null")
	public Object loadJsonFile(File tempFile){
		
		String[][] nullA = null;

		return nullA;
	}
	
	/**
	 * 
	 * @param file
	 */
	private void removeJsonFile(File file){
		
	}
}
