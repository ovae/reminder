package rem;

import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * @author ovae.
 * @version 20150203
 */
public class InfoPanel extends JPanel{

	private JLabel programState;
	private JLabel dateLabel;
	
	public InfoPanel(){
		programState = new JLabel("");
		dateLabel = new JLabel("");

		Date date = new Date();
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd");
		dateLabel.setText("Date: "+ft.format(date));

		setStateSaved();
		setConfiguration();
		addComponents();
		this.setVisible(true);
	}

	private void setConfiguration() {
		this.setLayout(new BorderLayout());
	}

	private void addComponents() {
		this.add(programState, BorderLayout.EAST);
		this.add(dateLabel, BorderLayout.WEST);
	}
	
	public void setStateSaved(){
		programState.setText("[saved]");
	}
	
	public void setStateChanged(){
		programState.setText("[changed]");
	}
}