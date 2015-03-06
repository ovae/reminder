package rem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class CalendarPanel extends JPanel{

	private JPanel controlPanel;
	private JPanel calendarPanel;
	private ArrayList<JPanel> days;
	private Calendar cal;
	private Date date;
	private int[] daysInMonth;
	
	public CalendarPanel(){
		this.setLayout(new BorderLayout());
		controlPanel = new JPanel();
		calendarPanel = new JPanel();
		calendarPanel.setLayout(new GridLayout(5,7));
		days = new ArrayList<JPanel>();
		cal = Calendar.getInstance();
		date = new Date();
		
		int[] monthCOnfigurationOne = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30 ,31};
		int[] monthCOnfigurationTwo = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30 ,31};
		if(date.getYear()%4 ==0){
			daysInMonth = monthCOnfigurationOne;
		}else{
			daysInMonth = monthCOnfigurationTwo;
		}

		setUpomponents();
		setUpControlPanel();
		setUpCalendarPanel();
	}
	
	private void setUpomponents(){
		this.add(controlPanel, BorderLayout.NORTH);
		this.add(calendarPanel, BorderLayout.CENTER);
	}

	private void setUpControlPanel(){
		controlPanel.add(new JLabel("Control Panel"));
	}

	private void setUpCalendarPanel(){
		int selectedMonth = cal.MONTH;
		int daysOfTheSelectedMonth = daysInMonth[date.getMonth()];
		prepaireTheDaysList();
		int index = 1;
		int weekdaycounter=1;
		for(JPanel panel: days){
			panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
			panel.add(new JLabel(""+index));
			if(weekdaycounter==6 || weekdaycounter==7){
				panel.setBackground(Color.LIGHT_GRAY);
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

	private void prepaireTheDaysList(){
		for(int i=1; i<35; i++){
			days.add(new JPanel());
		}
	}
}
