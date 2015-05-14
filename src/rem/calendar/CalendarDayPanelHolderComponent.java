package rem.calendar;

import javax.swing.BorderFactory;

import rem.constants.Colour;

/**
 * 
 * @author ovae.
 * @version 20150514.
 */
public class CalendarDayPanelHolderComponent extends CalendarDayPanelComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CalendarDayPanelHolderComponent(int dayNumb) {
		super(dayNumb);
		// TODO Auto-generated constructor stub
		taskTable.getTableHeader().setBackground(Colour.CALENDAR_HOLDER_DAY.getColor());
		taskTable.setBorder(BorderFactory.createLineBorder(Colour.CALENDAR_HOLDER_DAY_BORDER.getColor()));
		taskTable.getTableHeader().setBorder(BorderFactory.createLineBorder(Colour.CALENDAR_HOLDER_DAY_BORDER.getColor()));
		taskTable.setGridColor(Colour.CALENDAR_HOLDER_DAY_TASK_BORDER.getColor());
		mainPanel.setBorder(null);
		mainPanel.setBackground(Colour.CALENDAR_HOLDER_DAY.getColor());
	}

}
