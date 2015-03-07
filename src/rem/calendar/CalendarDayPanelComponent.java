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
	private final int MAX_TASKS = 42;

	public CalendarDayPanelComponent(final int dayNumb){
		this.setLayout(new BorderLayout());

		mainPanel = new JPanel(new BorderLayout());
		this.add(new JScrollPane().add(mainPanel), BorderLayout.CENTER);

		taskTable = new JTable();
		taskTable.setOpaque(false);
		taskTable.setEnabled(false);
		taskTable.setOpaque(false);
		taskTable.getTableHeader().setReorderingAllowed(false);
		
		tableContent = new Object[1][MAX_TASKS];
		String[] header = {""+dayNumb};
		taskTable.setModel(new DefaultTableModel(tableContent, header));
		mainPanel.add(taskTable.getTableHeader(), BorderLayout.PAGE_START);
		mainPanel.add(taskTable, BorderLayout.CENTER);

	}

	public void addTask(final String task){
		DefaultTableModel model = (DefaultTableModel) taskTable.getModel();
		model.addRow(new Object[]{task});
	}

	public void setBackgroundColour(){
		taskTable.setBackground(Color.LIGHT_GRAY);
	}
}
