package rem.constants;

import java.awt.Color;

/**
 * 
 * @author ovae.
 * @version 20150514.
 */
public enum Colour {
	CALENDAR_TODAY 					(new Color(25, 179, 131)),
	CALENDAR_WEEKENDDAY				(new Color(220,220,220)),
	CALENDAR_HOLDER_DAY				(new Color(200,200,200)),
	CALENDAR_HOLDER_DAY_BORDER		(new Color(210,210,210)),
	CALENDAR_HOLDER_DAY_TASK		(new Color(220,220,220)),
	CALENDAR_HOLDER_DAY_TASK_BORDER	(new Color(200,200,200)),
	CALENDAR_DAY					(new Color(238,238,238)),
	CALENDAR_DAY_BORDER				(new Color(210,210,210)),
	CALENDAR_DAY_TASK_BORDER		(new Color(210,210,210)),

	TABLE_DELIVERED 			(new Color(220,220,220)),
	TABLE_MORE_THAN_TO_DAYS		(new Color(126, 207, 88)),
	TABLE_TWO_DAYS_LEFT			(new Color(255,210,120)),
	TABLE_ONE_DAY_LEFT			(new Color(255,149,88)),
	TABLE_DELIVERY_DAY			(new Color(240, 88, 88)),
	TABLE_DELIVERY_DAY_DELIVERED(new Color(162, 104, 104)),
	TABLE_SELECTED_ROW			(new Color(160,166,207)),
	TABLE_EVENT					(new Color(255,215,61)),
	TABLE_DEFAULT				(new Color(238,238,238)),
	TABLE_BORDER				(new Color(210,210,210)),
	TABLE_BACKGROUND			(new Color(220,220,220)),
	;

	private final Color colour;

	Colour(final Color colour){
		this.colour = colour;
	}
	
	public Color getColor(){
		return colour;
	}

	/*
	CALENDAR_TODAY 					(new Color(25, 179, 131)),
	CALENDAR_WEEKENDDAY				(Color.LIGHT_GRAY),
	CALENDAR_HOLDER_DAY				(new Color(144,144,144)),
	CALENDAR_HOLDER_DAY_TASK		(new Color(154,154,154)),
	CALENDAR_HOLDER_DAY_TASK_BORDER	(new Color(154,154,154)),
	CALENDAR_HOLDER_DAY_BORDER		(new Color(154,154,154)),
	CALENDAR_DAY					(new Color(238,238,238)),
	CALENDAR_DAY_BORDER				(new Color(210,210,210)),
	CALENDAR_DAY_TASK_BORDER		(new Color(210,210,210)),

	TABLE_DELIVERED 			(new Color(220,220,220)),
	TABLE_MORE_THAN_TO_DAYS		(new Color(126, 207, 88)),
	TABLE_TWO_DAYS_LEFT			(new Color(255,210,120)),
	TABLE_ONE_DAY_LEFT			(new Color(255,149,88)),
	TABLE_DELIVERY_DAY			(new Color(240, 88, 88)),
	TABLE_DELIVERY_DAY_DELIVERED(new Color(162, 104, 104)),
	TABLE_SELECTED_ROW			(new Color(160,166,207)),
	TABLE_EVENT					(new Color(255,215,61)),
	TABLE_DEFAULT				(new Color(238,238,238)),
	 */
}
