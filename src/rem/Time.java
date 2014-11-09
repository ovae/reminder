package bin.rem;
/**
* Created Sep, 17, 2014
* @author Lennart Ove Weingarten<br />
* @version 0.01
 */
import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {
	private static Date date = new Date();
	
	//Date
	/**
	 * 
	 * @return dd.MM.yyyy
	 */
	public static String getDate(){
		SimpleDateFormat ft = new SimpleDateFormat ("dd.MM.yyyy");
		return ""+ft.format(date)+"";
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String getDate(String str){
		SimpleDateFormat ft = new SimpleDateFormat (str);
		return ""+ft.format(date)+"";
	}
	/**
	 * Get the Week day
	 * example : monday
	 */
	public static String getDayofTheWeek(){
		SimpleDateFormat ft = new SimpleDateFormat ("E");
		return ""+ft.format(date)+"";
	}

	/**
	 * Day of the Year
	 */
	public static String getNumberofDay(){
		SimpleDateFormat ft = new SimpleDateFormat ("D");
		return ""+ft.format(date)+"";
	}

	/**
	 * Week of the year.
	 */
	public static String getNumberofWeek(){
		SimpleDateFormat ft = new SimpleDateFormat ("w");
		return ""+ft.format(date)+"";
	}
	
	/**
	 * get the current month
	 */
	public static String getCurrentMonth(){
		SimpleDateFormat ft = new SimpleDateFormat ("MM");
		return ""+ft.format(date)+"";
	}
	
	/**
	 * get the current year
	 */
	public static String getCurrentYear(){
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy");
		return ""+ft.format(date)+"";
	}
	
	//Time
	/**
	 * @return hh:mm:ss
	 */
	public static String getTime(){
		SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");
		return ""+ft.format(date).toString()+"";
	}

	/**
	 * @return [hh:mm:ss]
	 */
	public static String getTimeDebug(){
		SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");
		return "["+ft.format(date).toString()+"]";
	}
		
}
