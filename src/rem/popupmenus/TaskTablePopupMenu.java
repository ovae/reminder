package rem.popupmenus;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPopupMenu;

import rem.constants.Icons;

/**
 * 
 * @author ovae.
 * @version 20150824.
 */
public class TaskTablePopupMenu extends JPopupMenu{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton closeButton;
	private JButton newTaskButton;

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public TaskTablePopupMenu(int x, int y){
		closeButton = new JButton("Close");
		newTaskButton = new JButton(Icons.ADD_TASK_ICON.getIcon());
		newTaskButton.setText("New Task");
		settingUp(x,y);
	}

	/**
	 * 
	 */
	public void run(){
		this.pack();
		this.setVisible(true);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 */
	private void settingUp(int x, int y){
		this.setVisible(false);
		this.setLocation(x, y);
		this.setLayout(new GridLayout(6,1));
		newTaskButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
		}});

		closeButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				exit();
			}
		});

		this.add(newTaskButton);
		this.add(new JButton("3"));
		this.add(new JButton("4"));
		this.add(new JButton("5"));
		this.add(new JButton("6"));
		this.add(closeButton);
	}

	/**
	 * 
	 */
	private void exit(){
		this.setVisible(false);
	}
}
