package rem.calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * @author ovae.
 * @version 20150308.
 */
public class CalendarPanel extends JPanel{

	private JPanel controlPanel;
	private JPanel navigatePanel;
	private JPanel headerPanel;
	private JPanel calendarPanel;
	private ArrayList<CalendarDayPanelComponent> days;
	private int[] daysInMonth;

	//control buttons
	private JButton statePast = new JButton("\u25C4");
	private JButton stateFuture = new JButton("\u25BA");
	private JLabel monthLabel;
	private JLabel yearLabel;

	/**
	 * 
	 */
	public CalendarPanel(){
		this.setLayout(new BorderLayout());
		controlPanel = new JPanel(new BorderLayout());
		navigatePanel = new JPanel();
		headerPanel = new JPanel(new BorderLayout());
		calendarPanel = new JPanel();
		calendarPanel.setLayout(new GridLayout(6,7));
		monthLabel = new JLabel(""+Calendar.getInstance().get(Calendar.MONTH));
		yearLabel = new JLabel(""+Calendar.getInstance().get(Calendar.YEAR));
		days = new ArrayList<CalendarDayPanelComponent>();

		int[] monthCOnfigurationOne = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30 ,31};
		int[] monthCOnfigurationTwo = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30 ,31};
		if(Calendar.getInstance().get(Calendar.YEAR)%4 ==0){
			daysInMonth = monthCOnfigurationOne;
		}else{
			daysInMonth = monthCOnfigurationTwo;
		}

		setUpomponents();
		setUpControlPanel();
		setUpCalendarPanel();
		setUpNavigationPanel();
	}

	/**
	 * 
	 */
	private void setUpomponents(){
		controlPanel.add(navigatePanel, BorderLayout.NORTH);
		controlPanel.add(headerPanel, BorderLayout.SOUTH);
		this.add(controlPanel, BorderLayout.NORTH);
		this.add(calendarPanel, BorderLayout.CENTER);
	}

	/**
	 * 
	 */
	private void setUpControlPanel(){
		JPanel weekdaysPanel = new JPanel();
		JPanel[] panelWeekdays = new JPanel[7];
		weekdaysPanel.setLayout(new GridLayout(0,7));
		for(int n = 0; n < 7; n++) {
			panelWeekdays[n] = new JPanel();
			weekdaysPanel.add(panelWeekdays[n]);
		}

		for(int i = 0; i < 7; i++) {
			panelWeekdays[i].setLayout(new BorderLayout());
			panelWeekdays[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
		}
		panelWeekdays[0].add(new JLabel("    Monday"), BorderLayout.CENTER);
		panelWeekdays[1].add(new JLabel("    Tuesday"), BorderLayout.CENTER);
		panelWeekdays[2].add(new JLabel("    Wednesday"), BorderLayout.CENTER);
		panelWeekdays[3].add(new JLabel("    Thursday"), BorderLayout.CENTER);
		panelWeekdays[4].add(new JLabel("    Friday"), BorderLayout.CENTER);
		panelWeekdays[5].add(new JLabel("    Saturday"), BorderLayout.CENTER);
		panelWeekdays[6].add(new JLabel("    Sunday"), BorderLayout.CENTER);
		headerPanel.add(weekdaysPanel, BorderLayout.CENTER);
	}

	/**
	 * 
	 */
	private void setUpNavigationPanel(){
		JPanel bar = new JPanel();
		bar.setLayout(new GridLayout(1,5));
		bar.add(new JLabel("Year:"));
		bar.add(yearLabel);
		bar.add(new JLabel("Month:"));
		bar.add(monthLabel);
		bar.add(new JLabel("Day: "+ Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));

		navigatePanel.add(statePast, BorderLayout.WEST);
		navigatePanel.add(bar, BorderLayout.CENTER);
		navigatePanel.add(stateFuture, BorderLayout.EAST);
	}

	/**
	 * 
	 */
	private void setUpCalendarPanel(){
		int selectedMonth = Calendar.MONTH;
		int selectedYear = Calendar.YEAR;
		int gap = prepaireTheDaysList(selectedMonth, selectedYear);
		int index = 2-gap;
		int weekdaycounter=1;
		for(JPanel panel: days){
			//panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
			if(weekdaycounter==6 || weekdaycounter==7){
				((CalendarDayPanelComponent) panel).setBackgroundColour();
			}
			if(index == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
				((CalendarDayPanelComponent) panel).setActuelDayColour();
			}

			calendarPanel.add(panel);
			index++;
			if(weekdaycounter == 7){
				weekdaycounter =1;
			}else{
				weekdaycounter++;
			}
			if(index == selectedMonth){
			}
		}
	}

	/**
	 * 
	 * @param month
	 * @param year
	 * @return gap
	 */
	private int prepaireTheDaysList(final int month, final int year){
		int gap = (new GregorianCalendar(year, month, 1).get(Calendar.DAY_OF_WEEK) +4);
		int weekday = 1;
		for(int i=1;i<gap;i++){
			days.add(new CalendarDayPanelComponent(0));
		}
		for(int i=gap; i<(6*7); i++){
			if(i<daysInMonth[month]+gap){
				days.add(new CalendarDayPanelComponent(weekday));
				weekday++;
			}
		}

		for(CalendarDayPanelComponent day : days){
			if(day.getTableHeader() != 0){
				Random t = new Random();
				for(int i=0; i<t.nextInt(5); i++){
				day.addTask("taskydjyedmyüö "+i);
				}
			}
		}
		return gap;
	}
}
