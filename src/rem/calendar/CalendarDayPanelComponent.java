package rem.calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * This component represents a day in the calendar with a list of tasks.
 * @author ovae.
 * @version 20150408.
 */
public class CalendarDayPanelComponent extends JPanel{

	private static final long serialVersionUID = 1L;

	private JPanel mainPanel;
	private JTable taskTable;
	private Object[][] tableContent;
	public final static int MAX_TASKS = 42;
	private int dayNumb;

	/**
	 * 
	 * @param dayNumb
	 */
	public CalendarDayPanelComponent(final int dayNumb){
		super();
		this.dayNumb = dayNumb;
		this.setLayout(new BorderLayout());
		mainPanel = new JPanel(new BorderLayout());
		this.add(new JScrollPane(mainPanel), BorderLayout.CENTER);
		setUpTable(dayNumb);
		 ClickEvent();
	}

	/**
	 * 
	 * @param dayNumb
	 */
	private void setUpTable(int dayNumb){
		taskTable = new JTable(new DefaultTableModel());
		taskTable.setOpaque(false);
		taskTable.setEnabled(false);
		taskTable.getTableHeader().setReorderingAllowed(false);

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

	/**
	 * 
	 * @param task
	 */
	public void addTask(final String task){
		DefaultTableModel model = (DefaultTableModel) taskTable.getModel();
		model.addRow(new Object[]{task});
	}

	/**
	 * Activates the colour of the JPanel background.
	 */
	public void setBackgroundColour(){
		taskTable.setBackground(Color.LIGHT_GRAY);
		mainPanel.setBackground(Color.LIGHT_GRAY);
	}

	/**
	 * Activates the colour of the table header.
	 */
	public void setActuelDayColour(){
		taskTable.getTableHeader().setBackground(new Color(25, 179, 131));
	}

	/**
	 * Returns the content of the table header.
	 * @return
	 */
	public int getTableHeader(){
		return dayNumb;
	}

	/**
	 * 
	 */
	private void ClickEvent(){
		taskTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e){
				try{
					int row=taskTable.rowAtPoint(e.getPoint());
					int col= taskTable.columnAtPoint(e.getPoint());
					JOptionPane.showMessageDialog(null,""+taskTable.getValueAt(row,col).toString(), "Day: "+dayNumb, JOptionPane.INFORMATION_MESSAGE);
				}catch(ArrayIndexOutOfBoundsException f){
					//This happens if the table has no elements.
				}
			}
		});
	}
}
