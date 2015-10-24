package rem.calendar;

import java.util.Calendar;

/**
 * 
 * @author ovae.
 * @version 20151024.
 *
 */
public class CalendarUtil {

	private static final int[] monthConfigurationOne = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30 ,31};
	private static final int[] monthConfigurationTwo = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30 ,31};

	/**
	 * 
	 * @param year
	 * @return daysInMonth a int array of the days in month.
	 */
	public static int[] getMonthSetting(final int year){
		int[] daysInMonth;
		if(year%4 == 0 || year%400 == 0){
			daysInMonth = monthConfigurationOne;
		}else{
			daysInMonth = monthConfigurationTwo;
		}
		return daysInMonth;
	}

	/**
	 * 
	 * @param value has to be in his yyyymmdd form.
	 * @return the month of the given value.
	 */
	public static int getMonth(final int value){
		return Integer.parseInt((""+value).substring(4,6));
	}

	/**
	 * 
	 * @param value has to be in his yyyymmdd form.
	 * @return the day of the given value.
	 */
	public static int getDay(final int value){
		return Integer.parseInt((""+value).substring(6,8));
	}

	/**
	 * 
	 * @param value has to be in his yyyymmdd form.
	 * @return the year of the given value.
	 */
	public static int getYear(final int value){
		return Integer.parseInt((""+value).substring(0,4));
	}

	/**
	 * 
	 * @param date has to be in his yyyymmdd form.
	 * @param value
	 * @return the corret next date.
	 */
	public static int addDaysToDate(final int date,int value){
		int year = getYear(date);
		int month = getMonth(date);
		int day = getDay(date);
		int[] monthSetting = getMonthSetting(year);
		if((day+value) > monthSetting[month-1]){

			value +=day;
			while(value-monthSetting[month-1] > 0){
				value -= monthSetting[month-1];
				month++;
			}
			day = value;
		}else{
			day+=value;
		}

		String out;
		if(day < 10){
			if(month <10){
				out = year + "0"+ month +"0"+day;
			}else{
				out = year + ""+ month +"0"+day;
			}
		}else{
			if(month <10){
				out = year + "0"+ month +""+day;
			}else{
				out = year + ""+ month +""+day;
			}
		}
		return Integer.parseInt(out);
	}

	/**
	 * Returns the current day  like this yyyymmdd.
	 * @return today
	 */
	public static int getToday(){
		int out=0;
		int dayState = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		int monthState = Calendar.getInstance().get(Calendar.MONTH)+1;
		int yearState = Calendar.getInstance().get(Calendar.YEAR);
		out +=dayState;
		out += (monthState*100);
		out += (yearState*10000);
		return out;
	}
}
