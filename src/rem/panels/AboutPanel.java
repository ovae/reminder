package rem.panels;

import javax.swing.JLabel;
import javax.swing.JPanel;

import rem.MainWindow;

/**
 * 
 * @author ovae.
 * @version 20150822.
 */
public class AboutPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public AboutPanel(){
		this.add(new JLabel("Reminder "+MainWindow.version+""));
	}
}
