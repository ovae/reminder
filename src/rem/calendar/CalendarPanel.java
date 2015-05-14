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
import rem.table.TasksTable;

/**
 * 
 * @author ovae.
 * @version 20150501.
 */
public class CalendarPanel extends JPanel{

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
	private JButton statePast = new JButton("\u25C4");
	private JButton stateFuture = new JButton("\u25BA");
	private JButton refreshButton = new JButton("\u21BA");
	private JButton homeButton = new JButton("\u2302");
	private JLabel monthLabel;
	private JLabel yearLabel;

	//States
	private int dayState;
	private int monthState;
	private int yearState;
	private int currentMonth;
	private int currentYear;

	//Table object
	private TasksTable table;

	//Archive table
	private TasksTable archive;

	//Event table
	private EventTable event;

	/**
	 * 
	 */
	public CalendarPanel(final TasksTable table, final TasksTable archive, final EventTable event){
		this.table = table;
		this.archive = archive;
		this.event = event;
		dayState = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		monthState = Calendar.getInstance().get(Calendar.MONTH);
		yearState = Calendar.getInstance().get(Calendar.YEAR);
		currentMonth = monthState;
		currentYear = yearState;

		calendarWidth = 7;
		calendarHeight = 6;
		this.setOpaque(true);
		this.setLayout(new BorderLayout());
		controlPanel = new JPanel(new BorderLayout());
		navigatePanel = new JPanel();
		headerPanel = new JPanel(new BorderLayout());
		calendarPanel = new JPanel();
		calendarPanel.setLayout(new GridLayout(calendarHeight,calendarWidth));
		monthLabel = new JLabel(""+(monthState+1));
		yearLabel = new JLabel(""+yearState);
		days = new ArrayList<CalendarDayPanelComponent>();

		int[] monthCOnfigurationOne = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30 ,31};
		int[] monthCOnfigurationTwo = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30 ,31};
		if(yearState%4 ==0){
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
		statePast.setToolTipText("Previous month");
		stateFuture.setToolTipText("Next month");
		refreshButton.setToolTipText("refresh");
		homeButton.setToolTipText("This month");

		statePast.addActionListener(new ActionListener(){
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

		stateFuture.addActionListener(new ActionListener(){
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

		controlbar.add(statePast, BorderLayout.WEST);
		controlbar.add(bar, BorderLayout.CENTER);
		controlbar.add(stateFuture, BorderLayout.EAST);

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
	 * 
	 */
	private void setUpCalendarPanel(){
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
	 * @param month
	 * @param year
	 * @return gap
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

		//Add the event from the tasks table to the calender lists.
		for(int i=0; i<event.getRowCount(); i++){
			topicList.add((String) "[E]"+event.getValueAt(i,0));
			aboutList.add((String) event.getValueAt(i,1));
			endList.add((String)event.getValueAt(i,3));
		}

		//Add tasks of the month before
		if(gap > 0){
			if(monthState==0){
				addTasksToDays(daysInMonth[11]-gap+2,monthState,endList,topicList,aboutList);
			}else{
				addTasksToDays(daysInMonth[monthState-1]-gap+2,monthState,endList,topicList,aboutList);
			}
		}

		//Add task of the current month
		addTasksToDays(2-gap,monthState+1,endList,topicList,aboutList);

		//Add task of after the current month
		addTasksToDays(-daysInMonth[monthState]-gap+2,monthState+2,endList,topicList,aboutList);
	}

	/**
	 * 
	 * @param index
	 * @param monthState
	 * @param endList
	 * @param topicList
	 * @param aboutList
	 */
	private void addTasksToDays(int index, int monthState,ArrayList<String> endList,
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
					day.addTask(topicList.get(innerDex)+": "+aboutList.get(innerDex));
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
