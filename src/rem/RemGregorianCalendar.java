package rem;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * A GregorianCalendar.
 * @author ovae.
 * @version 20150303.
 */
public class RemGregorianCalendar extends JPanel{

	private Date date = new Date();
	private int currentDay;
	private int currentMonth;
	private int currentYear;
	private int currentDayOfTheMonth;
	private JPanel topPanel = new JPanel();
		private JPanel weekInfoPanel = new JPanel();
			private JLabel monthLabel;
			private JLabel yearLabel;
		private JPanel weekdaysPanel = new JPanel();
	private JPanel cenPanel = new JPanel();
	private JPanel[][] panelHolder = new JPanel[5][7];
	private JPanel[] panelWeekdays = new JPanel[7];
	private JLabel[][] ticketLabel = new JLabel[35][9];
	private int[] daysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30 ,31};

	private JButton statePast = new JButton("\u25C4");
	private JButton stateFuture = new JButton("\u25BA");
	private int monthState;
	private int dayState;
	private int yearState;
	private int currentState;
	private String myDateState;

	@SuppressWarnings("deprecation")
	public RemGregorianCalendar(){
		currentDay = date.getDate();
		currentMonth = date.getMonth();
		currentYear = date.getYear();
		monthState = currentMonth;
		yearState = currentYear+1900;
		monthLabel= new JLabel(""+(currentMonth+1)+"");
		yearLabel = new JLabel(""+(currentYear+1900));
		myDateState = ""+yearLabel+""+monthLabel+""+currentDay;
		createPanel();
		setTopPanel();
		setCenPanelContent();
	}

	private void setUpPanelHolder(){
		for(int m = 0; m < 5; m++) {
			for(int n = 0; n < 7; n++) {
				panelHolder[m][n] = new JPanel();
				cenPanel.add(panelHolder[m][n]);
			}
		}
	}

	private void setTopPanel(){
		topPanel.setLayout(new BorderLayout());
		topPanel.add(weekInfoPanel, BorderLayout.NORTH);
		topPanel.add(weekdaysPanel, BorderLayout.SOUTH);
		setWeekInfoPanel();
		setWeekdaysPanel();
	}

	private void setWeekInfoPanel(){
		JPanel intern = new JPanel();
		intern.setLayout(new FlowLayout());
		weekInfoPanel.setLayout(new BorderLayout());
		intern.add(new JLabel("Year:"));
		intern.add(yearLabel);
		intern.add(new JLabel("   "));
		intern.add(new JLabel("Month:"));
		intern.add(monthLabel);
		intern.add(new JLabel("  "));
		intern.add(new JLabel("Day: "+currentDay));


		weekInfoPanel.add(statePast, BorderLayout.WEST);
		weekInfoPanel.add(stateFuture, BorderLayout.EAST);
		weekInfoPanel.add(intern, BorderLayout.CENTER);

		statePast.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//JOptionPane.showMessageDialog(null, "< Past");
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
				//JOptionPane.showMessageDialog(null, "Future>");
				monthState++;
				if(monthState >= 12 ){
					monthState=0;
					yearState++;
				}
				monthLabel.setText(""+(monthState+1)+"");
				yearLabel.setText(""+yearState+"");
				refreshCalendar();
			}
		});
	}

	private void setWeekdaysPanel(){
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
	}

	private void createPanel(){
		this.setLayout(new BorderLayout());
		this.add(topPanel, BorderLayout.NORTH);
		this.add(cenPanel, BorderLayout.CENTER);
		cenPanel.setLayout(new GridLayout(5,7));
		setUpPanelHolder();
	}

	private void setUpTicketLabel(){
		for(int m = 0; m < 35; m++) {
			for(int n = 0; n < 9; n++) {
				ticketLabel[m][n] = new JLabel("");
				//ticketLabel[m][n].setBorder(BorderFactory.createLineBorder(Color.GRAY));
			}
		}

		for(int m = 1; m < 35; m++) {
			for(int n = 0; n < 9; n++) {
				if(m==3 && n==0){
					ticketLabel[m][n].setText("Theo");
					//panelHolder[n][m-1].setBackground(new Color(106, 181, 203));
				}
			}
		}
	}

	private void setCenPanelContent(){
		setUpTicketLabel();
		int tempDays = 1;
		for(int m = 0; m < 5; m++) {
			for(int n = 0; n < 7; n++) {
				myDateState = ""+yearState+""+monthState+""+tempDays;
				if(tempDays < (daysInMonth[monthState] +1)){
					panelHolder[m][n].setLayout(new GridLayout(3,3));
					panelHolder[m][n].setBorder(BorderFactory.createLineBorder(Color.GRAY));
					if(tempDays == currentDay && monthState == currentMonth && yearState == (currentYear+1900)){
						//panelHolder[m][n].setBackground(new Color(106, 181, 203));
						panelHolder[m][n].setBackground(new Color(25, 179, 131));
						//Red border of the current day
						//panelHolder[m][n].setBorder(BorderFactory.createLineBorder(Color.RED));
					}
					if( n == 5  | n== 6 ){
						if(tempDays == currentDay && monthState == currentMonth && yearState == (currentYear+1900)){
							panelHolder[m][n].setBackground(new Color(25, 179, 131));
						}else{
							panelHolder[m][n].setBackground(Color.LIGHT_GRAY);
						}
					}

					for(int k = 0; k< 9; k++){
						if(k==4){
							panelHolder[m][n].add(new JLabel("  "+tempDays+""));
						}else{
							panelHolder[m][n].add(ticketLabel[tempDays][k]);
						}
					}
					tempDays++;
				}
				if(n==6 && m==4){
					panelHolder[m][n].add(new JLabel("  "+checkWeekday()+""));
				}
			}
		}

	}

	private void loadTickets(){

	}

	private void refreshCalendar(){
		for(int m = 0; m < 5; m++) {
			for(int n = 0; n < 7; n++) {
				cenPanel.remove(panelHolder[m][n]);
			}
		}
		setUpPanelHolder();
		setCenPanelContent();
	}
	
	private int currentDayOfTheYear(){
		int day=0;
		for(int i=0; i<currentMonth; i++){
			day+=daysInMonth[i];
		}
		return day+currentDay;
	}

	public String checkWeekday(){
		Calendar cal = Calendar.getInstance(); 
		return cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);
		//return cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
	}
}
