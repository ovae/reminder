package rem.calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import rem.constants.Colour;
import rem.table.EventTable;
import rem.table.TaskTable;

/**
 * A calendar component witch can show task and events of the day.
 * The calendar is presented in the gregorian calendar.
 * @author ovae.
 * @version 20150822.
 */
public class CalendarPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel controlPanel;
	private JPanel navigatePanel;
	private JPanel headerPanel;
	private JPanel calendarPanel;
	private ArrayList<CalendarDayPanelComponent> days;
	private int[] daysInMonth;

	//Sizes
	private final int calendarHeight;
	private final int calendarWidth;

	//control buttons
	private JButton statePastButton;
	private JButton stateFutureButton;
	private JButton refreshButton;
	private JButton homeButton;
	private JLabel monthLabel;
	private JLabel yearLabel;

	//States
	private int dayState;
	private int monthState;
	private int yearState;
	private int currentMonth;
	private int currentYear;

	//Table object
	private TaskTable table;

	//Archive table
	private TaskTable archive;

	//Event table
	private EventTable event;

	/**
	 * Creates a new Calendar Panel.
	 * @param table the task table.
	 * @param archive the archive table
	 * @param event the event table.
	 */
	public CalendarPanel(final TaskTable table, final TaskTable archive, final EventTable event){
		this.table = table;
		this.archive = archive;
		this.event = event;
		dayState = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		monthState = Calendar.getInstance().get(Calendar.MONTH);
		yearState = Calendar.getInstance().get(Calendar.YEAR);
		currentMonth = monthState;
		currentYear = yearState;

		statePastButton = new JButton("\u25C4");
		stateFutureButton = new JButton("\u25BA");
		refreshButton = new JButton("\u21BA");
		homeButton = new JButton("\u2302");

		statePastButton.setBackground(Colour.CALENDAR_DAY.getColor());
		stateFutureButton.setBackground(Colour.CALENDAR_DAY.getColor());
		refreshButton.setBackground(Colour.CALENDAR_DAY.getColor());
		homeButton.setBackground(Colour.CALENDAR_DAY.getColor());

		calendarWidth = 7;
		calendarHeight = 6;
		this.setLayout(new BorderLayout());
		controlPanel = new JPanel(new BorderLayout());
		navigatePanel = new JPanel();
		headerPanel = new JPanel(new BorderLayout());
		calendarPanel = new JPanel();
		calendarPanel.setLayout(new GridLayout(calendarHeight,calendarWidth));
		monthLabel = new JLabel(""+(monthState+1));
		yearLabel = new JLabel(""+yearState);
		days = new ArrayList<CalendarDayPanelComponent>();

		daysInMonth = CalendarUtil.getMonthSetting(yearState);

		setUpComponents();
		setUpHeaderPanel();
		setUpCalendarPanel();
		setUpNavigationPanel();
	}

	/**
	 * 
	 */
	private void setUpComponents(){
		controlPanel.add(navigatePanel, BorderLayout.NORTH);
		controlPanel.add(headerPanel, BorderLayout.SOUTH);
		this.add(controlPanel, BorderLayout.NORTH);
		this.add(calendarPanel, BorderLayout.CENTER);
	}

	/**
	 * Sets up the Header panel, it holds the weekdays labels for the calendar.
	 */
	private void setUpHeaderPanel(){
		JPanel weekdaysPanel = new JPanel();
		JPanel[] panelWeekdays = new GradientPanel[7];
		weekdaysPanel.setLayout(new GridLayout(0,7));
		for(int n = 0; n < 7; n++) {
			panelWeekdays[n] = new GradientPanel();
			weekdaysPanel.add(panelWeekdays[n]);
		}

		for(int i = 0; i < 7; i++) {
			panelWeekdays[i].setLayout(new BorderLayout());
			panelWeekdays[i].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
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
	 * Creates all elements of the navigation panel.
	 * It holds all elements to navigate the calendar.
	 */
	private void setUpNavigationPanel(){
		JPanel refreshPanel = new JPanel(new BorderLayout());
		JPanel homePanel = new JPanel(new BorderLayout());
		JPanel bar = new JPanel();
		JPanel controlbar = new JPanel(new BorderLayout());

		refreshPanel.add(refreshButton);
		homePanel.add(homeButton);

		bar.setLayout(new GridLayout(1,7));
		bar.add(new JLabel("Year: "));
		bar.add(yearLabel);
		bar.add(new JLabel("Month: "));
		bar.add(monthLabel);
		bar.add(new JLabel("Day: "+ (dayState)));

		//SetToolTipTexts
		statePastButton.setToolTipText("Previous month");
		stateFutureButton.setToolTipText("Next month");
		refreshButton.setToolTipText("refresh");
		homeButton.setToolTipText("This month");

		statePastButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				monthState--;
				if(monthState <0 ){
					monthState=11;
					yearState--;
				}
				monthLabel.setText(""+(monthState+1)+"");
				yearLabel.setText(""+yearState+"");
				refreshCalendar();
			}
		});

		stateFutureButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				monthState++;
				if(monthState >= 12 ){
					monthState=0;
					yearState+=1;
				}
				monthLabel.setText(""+(monthState+1)+"");
				yearLabel.setText(""+yearState+"");
				refreshCalendar();
			}
		});

		refreshButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshCalendar();
			}
		});

		homeButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				monthState = currentMonth;
				yearState = currentYear;
				monthLabel.setText(""+(currentMonth+1));
				yearLabel.setText(""+currentYear);
				refreshCalendar();
			}
		});

		controlbar.add(statePastButton, BorderLayout.WEST);
		controlbar.add(bar, BorderLayout.CENTER);
		controlbar.add(stateFutureButton, BorderLayout.EAST);

		navigatePanel.add(refreshPanel, BorderLayout.WEST);
		navigatePanel.add(controlbar, BorderLayout.CENTER);
		navigatePanel.add(homePanel, BorderLayout.EAST);
	}

	/**
	 * Refresh's the calendar content.
	 */
	public void refreshCalendar(){
		days.clear();
		calendarPanel.removeAll();
		calendarPanel.revalidate();
		setUpCalendarPanel();
	}

	/**
	 * Creates the Calendar Panel, it holds all components that represent the days of the month.
	 */
	private void setUpCalendarPanel(){
		daysInMonth = CalendarUtil.getMonthSetting(yearState);
		prepaireTheDaysList();
		int gap = getGap();
		int index = 1-gap;
		int dayCounter = 1;
		int weekdaycounter=1;
		for(JPanel panel: days){
			if(index+1 == dayState && currentMonth == monthState && currentYear == yearState){
				((CalendarDayPanelComponent) panel).setActuelDayColour();
			}
			if(dayCounter >= gap && dayCounter <= daysInMonth[monthState]+gap-1){
				if(weekdaycounter==6 || weekdaycounter==7){
					((CalendarDayPanelComponent) panel).setBackgroundColourWeekend();
				}
			}
			if(!(dayCounter >= gap && dayCounter <= daysInMonth[monthState]+gap-1)){
				((CalendarDayPanelComponent) panel).setBackgroundColour();
				
			}
			calendarPanel.add(panel);
			index++;
			dayCounter++;
			if(weekdaycounter == 7){
				weekdaycounter =1;
			}else{
				weekdaycounter++;
			}
		}
	}

	/**
	 * Prepares the days list, witch contains a JPanel for each day of the month.
	 */
	private void prepaireTheDaysList(){
		int gap = getGap();
		int weekday = 1;

		//Add calendarDayPanelComponents before the current month.
		int lose=gap-2;
		for(int i=1;i<gap;i++){
			if(monthState==0){
				days.add(new CalendarDayPanelHolderComponent(daysInMonth[11]-lose));
			}
			else{
				days.add(new CalendarDayPanelHolderComponent(daysInMonth[monthState-1]-lose));
			}
			lose--;
		}

		//Add calendarDayPanelComponents of the current month
		for(int i=0; i<(calendarWidth*calendarHeight); i++){
			if(i<daysInMonth[monthState]){
				days.add(new CalendarDayPanelComponent(weekday));
				weekday++;
			}
		}

		//Add calendarDayPanelComponents after the current month.
		int r = (calendarWidth*calendarHeight)-days.size();
		for(int i=0;i<r;i++){
			days.add(new CalendarDayPanelHolderComponent(i+1));
		}

		//Add the tasks from the taskTable to the calendar.
		ArrayList<String> endList = new ArrayList<>();
		ArrayList<String> aboutList = new ArrayList<>();
		ArrayList<String> topicList = new ArrayList<>();

		//Add the event from the event table to the calendar lists.
		/*for(int i=0; i<event.getRowCount(); i++){
			topicList.add((String) "[E]"+event.getValueAt(i,0));
			aboutList.add((String) event.getValueAt(i,1));
			endList.add((String)event.getValueAt(i,3));
		}*/
		for(int i=0; i<event.getRowCount(); i++){
			int begin = Integer.parseInt((String) event.getValueAt(i, 2));
			int end = Integer.parseInt((String) event.getValueAt(i, 3));
			if(begin != end){
				//Events that take more than one day.
				int beginMonth = CalendarUtil.getMonth(begin);
				int beginDay = CalendarUtil.getDay(begin);
				int endMonth = CalendarUtil.getMonth(end);
				int endDay = CalendarUtil.getDay(end);

				if(beginMonth != endMonth){
					//Events that take place more than one month.
					for(int month=beginMonth;month<endMonth;month++){
						if(month==beginMonth){
							for(int j=beginDay-1;j<daysInMonth[beginMonth-1];j++){
								topicList.add((String) "[E]"+event.getValueAt(i,0));
								aboutList.add((String) event.getValueAt(i,1));
								if(beginMonth < 10){
									endList.add(""+CalendarUtil.getYear(begin)+"0"+beginMonth+""+(j+1));
								}else{
									endList.add(""+CalendarUtil.getYear(begin)+""+beginMonth+""+(j+1));
								}
							}
						}
						if(month>beginMonth && month != endMonth){
							System.out.println("Test C");
						}
					}
					for(int j=0;j<endDay;j++){
						topicList.add((String) "[E]"+event.getValueAt(i,0));
						aboutList.add((String) event.getValueAt(i,1));
						if(j < 10){
							if(endMonth < 10){
								endList.add(""+CalendarUtil.getYear(begin)+"0"+endMonth+"0"+(j+1));
							}else{
								endList.add(""+CalendarUtil.getYear(begin)+""+endMonth+"0"+(j+1));
							}
						}else{
							if(endMonth < 10){
								endList.add(""+CalendarUtil.getYear(begin)+"0"+endMonth+""+(j+1));
							}else{
								endList.add(""+CalendarUtil.getYear(begin)+""+endMonth+""+(j+1));
							}
						}
					}

				}else{
					//Events that take place in only one month.
					for(int j=begin-1;j<end;j++){
						topicList.add((String) "[E]"+event.getValueAt(i,0));
						aboutList.add((String) event.getValueAt(i,1));
						endList.add(""+(j+1));
					}
				}

			}else{
				//Events that take place on only one day.
				topicList.add((String) "[E]"+event.getValueAt(i,0));
				aboutList.add((String) event.getValueAt(i,1));
				endList.add((String)event.getValueAt(i,3));
			}
		}

		//Add the tasks from the tasks table to the calender lists.
		for(int i=0; i<table.getRowCount(); i++){
			topicList.add((String) table.getValueAt(i,0));
			aboutList.add((String) table.getValueAt(i,1));
			endList.add((String) table.getValueAt(i,3));
		}

		//Add the archive from the tasks table to the calender lists.
		for(int i=0; i<archive.getRowCount(); i++){
			topicList.add((String) "[A]"+archive.getValueAt(i,0));
			aboutList.add((String) archive.getValueAt(i,1));
			endList.add((String) archive.getValueAt(i,3));
		}

		//Add tasks of the month before
		if(gap > 0){
			if(monthState==0){
				addElement(daysInMonth[11]-gap+2,monthState,endList,topicList,aboutList);
			}else{
				addElement(daysInMonth[monthState-1]-gap+2,monthState,endList,topicList,aboutList);
			}
		}

		//Add task of the current month
		addElement(2-gap,monthState+1,endList,topicList,aboutList);

		//Add task of after the current month
		addElement(-daysInMonth[monthState]-gap+2,monthState+2,endList,topicList,aboutList);
	}

	/**
	 * Add a new Elements to the Calendar.
	 * @param index
	 * @param monthState
	 * @param endList
	 * @param topicList
	 * @param aboutList
	 */
	private void addElement(int index, int monthState,ArrayList<String> endList,
			ArrayList<String> topicList, ArrayList<String> aboutList){

		for(CalendarDayPanelComponent day : days){
			String vergleich = yearState+""+(monthState)+""+(index);
			if(monthState <=9 && index<=9){
				vergleich = yearState+"0"+(monthState)+"0"+(index);
			}
			if(monthState <=9 && index>9){
				vergleich = yearState+"0"+(monthState)+""+(index);
			}
			if(monthState >9 && index<=9){
				vergleich = yearState+""+(monthState)+"0"+(index);
			}
			if(monthState >9 && index>9){
				vergleich = yearState+""+(monthState)+""+(index);
			}

			int innerDex = 0;
			for(String end: endList){
				if(end.equals(vergleich)){
					day.addElement(topicList.get(innerDex)+": "+aboutList.get(innerDex));
					if(topicList.get(innerDex).startsWith("[E]")){
						day.setBackground(Colour.TABLE_EVENT.getColor());
					}
				}
				innerDex++;
			}
			index++;
		}
	}

	/**
	 * Returns the gap between the first day of the month and the begin of the week.
	 */
	private int getGap(){
		int gap = (new GregorianCalendar(yearState, monthState, 1).get(Calendar.DAY_OF_WEEK)+6);
		if(gap > 7){
			gap -=7;
		}
		return gap--;
	}

}
