package rem.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import rem.RemTable;

public class FileHandler {

	
	private FileHandler(){
		
	}

	public static void loadFile(RemTable table) throws IOException{
		File load = new File("tasks.txt");
		BufferedReader reader = null;
		Pattern pattern = Pattern.compile("(\\[\\s*\\w*\\s*])(\\[\\s*\\w*\\s*\\])(\\[\\s*\\w*\\s*\\])(\\[\\s*\\w*\\s*\\])(\\[\\s*\\w*\\s*\\])");
		try{
			reader = new BufferedReader(new FileReader(load));
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

	public static void writeFile(final ArrayList<String> list) throws IOException{
		File write = new File("tasks.txt");
		PrintWriter writer  =null;
		try{
			writer = new PrintWriter(new BufferedWriter(new FileWriter(write)));
			for(String row: list){
				writer.write(row);
			}
		}finally{
			writer.close();
		}
	}
}
