package rem.calendar;

import java.util.Calendar;
import java.util.Date;

import sun.util.calendar.CalendarDate;
import sun.util.calendar.Gregorian;

public class RemDate {

	private int dayOfWeek;
	private int dayOfMonth;
	private int dayOfYear;
	private int month;
	private int year;
	private int weekday;
	private int firstDayOfWeek;
	private Calendar calendar;
	private Gregorian gregorian;

	public RemDate(){
		this.calendar = Calendar.getInstance();
		gregorian = Gregorian.getGregorianCalendar();

		this.dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		this.dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		this.year = calendar.get(Calendar.YEAR) - 1900;
		
	}
}
