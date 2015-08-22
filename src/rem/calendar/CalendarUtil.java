package rem.calendar;

/**
 * 
 * @author ovae.
 * @version 20150819.
 *
 */
public class CalendarUtil {

	/**
	 * 
	 * @param year
	 * @return
	 */
	public static int[] getMonthSetting(final int year){
		int[] daysInMonth;
		int[] monthCOnfigurationOne = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30 ,31};
		int[] monthCOnfigurationTwo = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30 ,31};
		if(year%4 ==0){
			daysInMonth = monthCOnfigurationOne;
		}else{
			daysInMonth = monthCOnfigurationTwo;
		}
		return daysInMonth;
	}

	/**
	 * 
	 * @param value has to be in his yyyymmdd form.
	 * @return
	 */
	public static int getMonth(final int value){
		return Integer.parseInt((""+value).substring(4,6));
	}

	/**
	 * 
	 * @param value has to be in his yyyymmdd form.
	 * @return
	 */
	public static int getDay(final int value){
		return Integer.parseInt((""+value).substring(6,8));
	}

	/**
	 * 
	 * @param value has to be in his yyyymmdd form.
	 * @return
	 */
	public static int getYear(final int value){
		return Integer.parseInt((""+value).substring(0,4));
	}

	/**
	 * 
	 * @param date has to be in his yyyymmdd form.
	 * @param value
	 * @return
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
}
