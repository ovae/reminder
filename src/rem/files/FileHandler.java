package rem.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import rem.RemTable;

/**
 * FileHandler to load and write to a file.
 * @author ovae.
 * @version 20150408.
 */
public class FileHandler {

	/**
	 * An instantiation is disable, because all methods are static.
	 */
	private FileHandler(){}

	/**
	 * Load from file into JTable.
	 * @param table
	 * @param filename
	 * @throws IOException
	 */
	public static void loadFile(final RemTable table, final File filename) throws IOException{
		File load = filename;
		BufferedReader reader = null;
		Pattern pattern = Pattern.compile("(\\[\\s*.*\\s*])(\\[\\s*.*\\s*\\])(\\[\\s*.*\\s*\\])(\\[\\s*.*\\s*\\])(\\[\\s*.*\\s*\\])");
		try{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(load), "UTF-8"));
			String line = null;
			while( (line = reader.readLine()) != null){
				Matcher matcher = pattern.matcher(line);
				if(matcher.matches()){
					table.addRow(String.valueOf(matcher.group(1)).substring(1,matcher.group(1).length()-1),
								 String.valueOf(matcher.group(2)).substring(1,matcher.group(2).length()-1),
								 String.valueOf(matcher.group(3)).substring(1,matcher.group(3).length()-1),
								 String.valueOf(matcher.group(4)).substring(1,matcher.group(4).length()-1),
								 String.valueOf(matcher.group(5)).substring(1,matcher.group(5).length()-1));
				}
			}
		}finally{
			reader.close();
		}
	}

	/**
	 * Write from ArrayList to file.
	 * @param list
	 * @param filename
	 * @throws IOException
	 */
	public static void writeFile(final ArrayList<String> list, final File filename) throws IOException{
		File write = filename;
		PrintWriter writer  =null;
		try{
			writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(write), "UTF-8"));
			if(checkNotUnix()){
				for(String row: list){
					writer.write(row);
					writer.write("\r\n");
				}
			}else{
				for(String row: list){
					writer.write(row);
				}
			}
		}finally{
			writer.close();
		}
	}

	private static boolean checkNotUnix(){
		if( !System.getProperty("os.name").equals("Linux")){
			return true;
		}
		return false;
	}
}
