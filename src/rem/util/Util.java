package rem.util;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * 
 * @author ovae.
 * @version 20150723.
 */
public class Util {

	/**
	 * Sets the frame parameter centred in the screen
	 * @param frame
	 */
	public final static void centerWindow(JFrame frame) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}
}
