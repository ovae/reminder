package rem.calendar;

import java.awt.Color;

/**
 * 
 * @author ovae.
 * @version 20150501.
 */
public class CalendarDayPanelHolderComponent extends CalendarDayPanelComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CalendarDayPanelHolderComponent(int dayNumb) {
		super(dayNumb);
		// TODO Auto-generated constructor stub
		taskTable.getTableHeader().setBackground(Color.WHITE);
		mainPanel.setBackground(Color.WHITE);
	}

}
