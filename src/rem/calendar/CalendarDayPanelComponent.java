package rem.calendar;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import rem.constants.Colour;
import rem.table.DayTable;

/**
 * This component represents a day in the calendar with a list of tasks.
 * @author ovae.
 * @version 20150822.
 */
public class CalendarDayPanelComponent extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected JPanel mainPanel;
	private JScrollPane scroll;

	/**
	 * This table hold all task and events of the day.
	 */
	protected JTable taskTable;

	/**
	 * The actual content of the taskTable.
	 */
	private Object[][] tableContent;

	/**
	 * Every day component is inly allowed to hold this amount of elements.
	 */
	public final static int MAX_TASKS = 42;

	/**
	 * The day number of this component.
	 */
	private int dayNumb;

	/**
	 * Creates a new CalendarDayPanelComponent. The given dayNumb is the number of
	 * the day witch this new components is going to represent.
	 * @param dayNumb the number of the day.
	 * @throws IllegalArgumentException if dayNumb < 1.
	 */
	public CalendarDayPanelComponent(final int dayNumb){
		super();
		if(dayNumb < 1){
			throw new IllegalArgumentException("The day number has to be bigger than zero!");
		}
		this.dayNumb = dayNumb;
		this.setLayout(new BorderLayout());
		mainPanel = new JPanel(new BorderLayout());
		scroll = new JScrollPane(mainPanel);
		scroll.setBorder(null);
		this.add(scroll, BorderLayout.CENTER);
		setUpTable(dayNumb);
		ClickEvent();
	}

	/**
	 * Creates the table witch holds the elements of the day component.
	 * @param dayNumb the number of the day.
	 * @throws IllegalArgumentException if dayNumb < 1.
	 */
	private void setUpTable(int dayNumb){
		if(dayNumb < 1){
			throw new IllegalArgumentException("The day number has to be bigger than zero!");
		}
		taskTable = new DayTable(new DefaultTableModel());
		taskTable.setOpaque(false);
		taskTable.setEnabled(false);
		taskTable.getTableHeader().setBorder(BorderFactory.createLineBorder(Colour.CALENDAR_DAY_BORDER.getColor()));
		taskTable.setBorder(BorderFactory.createLineBorder(Colour.CALENDAR_DAY_BORDER.getColor()));
		taskTable.setGridColor(Colour.CALENDAR_DAY_TASK_BORDER.getColor());
		taskTable.setBackground(Colour.CALENDAR_DAY.getColor());
		mainPanel.setBackground(Colour.CALENDAR_DAY.getColor());
		//taskTable.getTableHeader().setBorder(BorderFactory.createRaisedSoftBevelBorder());
		//taskTable.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		taskTable.getTableHeader().setReorderingAllowed(false);
		((DayTable) taskTable).setTableRowColor();

		mainPanel.add(taskTable, BorderLayout.CENTER);
		mainPanel.add(taskTable.getTableHeader(), BorderLayout.PAGE_START);
		String[] header = {""+dayNumb};
		taskTable.setModel(new DefaultTableModel(tableContent, header));
	}

	/**
	 * Adds a new element to this component.
	 * @param element the element that is added to the component.
	 */
	public void addElement(final String element){
		DefaultTableModel model = (DefaultTableModel) taskTable.getModel();
		model.addRow(new Object[]{element});
	}

	/**
	 * Sets the background colour of the component.
	 */
	public void setBackgroundColourWeekend(){
		// Monday to Friday.
		taskTable.setBackground(Colour.CALENDAR_WEEKENDDAY.getColor());
		// Saturday and Sunday. 
		mainPanel.setBackground(Colour.CALENDAR_WEEKENDDAY.getColor());
	}

	/**
	 * Sets the background colour to the colour to {@link Colour#CALENDAR_HOLDER_DAY_TASK}.
	 */
	public void setBackgroundColour(){
		taskTable.setBackground(Colour.CALENDAR_HOLDER_DAY_TASK.getColor());
	}

	/**
	 * Sets the background colour of the table header to {@link Colour#CALENDAR_TODAY}.
	 */
	public void setActuelDayColour(){
		taskTable.getTableHeader().setBackground(Colour.CALENDAR_TODAY.getColor());
	}

	/**
	 * Returns the number of the day.
	 * @return dayNumb the number of the day.
	 */
	public int getTableHeader(){
		return dayNumb;
	}

	/**
	 * This method holds all click events that are used on the taskTable.
	 */
	private void ClickEvent(){
		/**
		 * If the user clicks on an element of the day, a small pop-up window is going
		 * to appear witch hold the information of the clicked element.
		 */
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
