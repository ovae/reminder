package rem.subwindows;

import rem.MainWindow;
import rem.constants.States;

public class EditTaskFrame extends AddTaskFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EditTaskFrame(MainWindow window) {
		super(window);
	}

	/**
	 * 
	 * @param table
	 * @param topic
	 * @param about
	 * @param begin
	 * @param end
	 * @param status
	 */
	public EditTaskFrame(final MainWindow window,final String topic, final String about, final String begin, final String end,final String status) {
		super(window);
		this.setTitle("Edit Task");
		this.setVisible(false);
		this.setInputFieldAbout(about);
		this.setInputFieldBegin(begin);
		this.setInputFieldEnd(end);
		if(status.equals(States.states[0])){
			this.setStatus(0);
		}else if(status.equals(States.states[1])){
			this.setStatus(1);
		}else if(status.equals(States.states[2])){
			this.setStatus(2);
		}else if(status.equals(States.states[3])){
			this.setStatus(3);
		}else{
			this.setStatus(4);
		}
	}

}
