package rem.calendar;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CalendarDayPanelComponent extends JPanel{

	private JPanel mainPanel;
	private JTable taskTable;
	private Object[][] tableContent;
	public final static int MAX_TASKS = 42;
	private int dayNumb;

	public CalendarDayPanelComponent(final int dayNumb){
		super();
		this.dayNumb = dayNumb;
		this.setLayout(new BorderLayout());
		mainPanel = new JPanel(new BorderLayout());
		this.add(new JScrollPane(mainPanel), BorderLayout.CENTER);
		setUpTable(dayNumb);
	}

	private void setUpTable(int dayNumb){
		taskTable = new JTable(new DefaultTableModel());
		taskTable.setOpaque(false);
		taskTable.setEnabled(false);
		taskTable.getTableHeader().setReorderingAllowed(false);

		//tableContent = new Object[1][MAX_TASKS];
		if(dayNumb == 0){
			String[] header = {""};
			taskTable.setModel(new DefaultTableModel(tableContent, header));
		}else{
			String[] header = {""+dayNumb};
			taskTable.setModel(new DefaultTableModel(tableContent, header));
		}
		if(dayNumb != 0){
			mainPanel.add(taskTable, BorderLayout.CENTER);
			mainPanel.add(taskTable.getTableHeader(), BorderLayout.PAGE_START);
		}
	}

	public void addTask(final String task){
		DefaultTableModel model = (DefaultTableModel) taskTable.getModel();
		model.addRow(new Object[]{task});
	}

	public void setBackgroundColour(){
		taskTable.setBackground(Color.LIGHT_GRAY);
		mainPanel.setBackground(Color.LIGHT_GRAY);
	}

	public void setActuelDayColour(){
		taskTable.getTableHeader().setBackground(new Color(25, 179, 131));
	}
	
	public int getTableHeader(){
		return dayNumb;
	}
}
