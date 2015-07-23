package rem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A JPanel with two JLabels, at the left the actual date
 * and at the right an information if the program have changed.
 * @author ovae.
 * @version 20150719.
 */
public class InfoPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	/**
	 * Label witch shows the program state, changed or saved.
	 */
	private JLabel programState;

	/**
	 * Label to show the actual date.
	 */
	private JLabel dateLabel;

	/**
	 * 
	 */
	private JPanel programStateField;

	/**
	 * Creates a new InfoPanel.
	 */
	public InfoPanel(){
		//Initialise the labels.
		programState = new JLabel("");
		dateLabel = new JLabel("");
		programStateField= new JPanel();

		//Set the date label
		Date date = new Date();
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd");
		dateLabel.setText("Date: "+ft.format(date));

		setStateSaved();
		setConfiguration();
		addComponents();
		this.setVisible(true);
	}

	/**
	 * Set all panel configurations
	 */
	private void setConfiguration() {
		this.setLayout(new BorderLayout());
	}

	/**
	 * Add all components to this panel.
	 */
	private void addComponents() {
		programStateField.add(programState);
		programStateField.setBackground(new Color(97,197,84));

		this.add(programStateField, BorderLayout.EAST);
		this.add(dateLabel, BorderLayout.WEST);
	}

	/**
	 * Set the programmState label to saved.
	 */
	public void setStateSaved(){
		programState.setText("saved");
		programStateField.setBackground(new Color(97,197,84));
	}

	/**
	 * Set the programmState label to changed.
	 */
	public void setStateChanged(){
		programState.setText("changed");
		programStateField.setBackground(new Color(255,93,93));
	}
}