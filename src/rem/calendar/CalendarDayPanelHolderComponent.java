package rem.calendar;

import javax.swing.BorderFactory;

import rem.constants.Colour;

/**
 * This component represents a day in the calendar, that is not in the current viewed month.
 * @author ovae.
 * @version 20150822.
 */
public class CalendarDayPanelHolderComponent extends CalendarDayPanelComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new CalendarDayHolderPanelComponent. The given dayNumb is the number of
	 * the day witch this new components is going to represent.
	 * @param dayNumb the number of the day.
	 */
	public CalendarDayPanelHolderComponent(int dayNumb) {
		super(dayNumb);
		//taskTable.getTableHeader().setBackground(Colour.CALENDAR_HOLDER_DAY.getColor());
		taskTable.setBorder(BorderFactory.createLineBorder(Colour.CALENDAR_HOLDER_DAY_BORDER.getColor()));
		taskTable.getTableHeader().setBorder(BorderFactory.createLineBorder(Colour.CALENDAR_HOLDER_DAY_BORDER.getColor()));
		taskTable.setGridColor(Colour.CALENDAR_HOLDER_DAY_TASK_BORDER.getColor());
		mainPanel.setBorder(null);
		mainPanel.setBackground(Colour.CALENDAR_HOLDER_DAY.getColor());
	}

}
