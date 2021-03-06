package rem.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import rem.InfoPanel;
import rem.constants.Colour;
import rem.constants.Messages;
import rem.popupMenus.TaskTablePopupMenu;

/**
 * 
 * @author ovae.
 * @version 20150514.
 */
public class TasksTable extends RemTable {

	private static final long serialVersionUID = 1L;

	private String[] columnNames;
	private String[] status;
	private Object[][] tableContent;

	public TasksTable(DefaultTableModel defaultTableModel) {
		super(defaultTableModel);
		status = new String[5];
		status[0] = "not_started";
		status[1] = "started";
		status[2] = "half-finished";
		status[3] = "finished";
		status[4] = "delivered";
		this.getTableHeader().setReorderingAllowed(false);
		//TODO
		this.setBackground(Colour.CALENDAR_DAY.getColor());
		eventListeners();
	}

	public void setTableHeader(String[] header){
		columnNames = header;
	}
	
	public void setStates(String[] status){
		this.status = status;
	}

	public void setTableModel(){
		this.setModel(new DefaultTableModel(tableContent, columnNames));
	}

	/**
	 * Function to save new row to the Table.
	 * @param topic, about, begin, end
	 * @param url
	 * "Topic","About","Begin","End", "Status"
	 * This method is only used in the addNewTaskFrame.
	 */
	@Override
	public void addRow(String topic, String about, String begin, String end){
		DefaultTableModel model = (DefaultTableModel) this.getModel();
		model.addRow(new Object[]{topic, about, begin, end, status[0]});
	}

	/**
	 * Function to add a new row to the Table.
	 * @param topic, about, begin, end, status
	 * @param url
	 * "Topic","About","Begin","End", "Status"
	 */
	@Override
	public void addRow(String topic, String about, String begin, String end, String status){
		DefaultTableModel model = (DefaultTableModel) this.getModel();
		model.addRow(new Object[]{topic, about, begin, end, status});
	}

	/**
	 * 
	 * @param tempTable
	 * @param topic
	 * @param about
	 * @param begin
	 * @param end
	 * @param status
	 */
	public void addRow(RemTable tempTable, String topic, String about, String begin, String end, String status){
			DefaultTableModel model = (DefaultTableModel) tempTable.getModel();
			model.addRow(new Object[]{topic, about, begin, end, status});
	}

	/**
	 * 
	 * @param tempTable
	 * @param topic
	 * @param about
	 * @param begin
	 * @param end
	 * @param status
	 */
	public void addRow(String topic, String about, int begin, int end, String status){
		DefaultTableModel model = (DefaultTableModel) this.getModel();
		model.addRow(new Object[]{topic, about, begin, end, status});
	}

	/**
	 * Method to remove selected items from the table.
	 */
	public void removeRow(){
		int[] rows = this.getSelectedRows();
		if(rows.length < 1){
			JOptionPane.showMessageDialog(null, Messages.NO_SELECTED_ROW.getMsg());
			return;
		}
		int p =JOptionPane.showConfirmDialog(null, "Do you want to remove it.","Select an Option",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(p==0){
			TableModel tm= this.getModel();
			while(rows.length>0){
				((DefaultTableModel)tm).removeRow(this.convertRowIndexToModel(rows[0]));
				rows = this.getSelectedRows();
			}
		}
		this.clearSelection();
	}

	/**
	 * removes all elements of the table.
	 */
	@Override
	public void emptyTable(){
		try{
			DefaultTableModel dm = (DefaultTableModel) this.getModel();
			int rowCount = dm.getRowCount();
			//Remove rows one by one from the end of the table
			for (int i = rowCount - 1; i >= 0; i--) {
				dm.removeRow(i);
			}
		}catch(Exception e){}
	}

	/**
	 * Method to add items to the table and update the table.
	 */
	public void updateTableRow(){
		try{
			int[] row = this.getSelectedRows();
			String value= (String) this.getModel().getValueAt(row[0], 4);
			if(value == status[0]){
				this.getModel().setValueAt(status[1], row[0], 4);
			}else if(value == status[1]){
				this.getModel().setValueAt(status[2], row[0], 4);
			}else if(value == status[2]){
				this.getModel().setValueAt(status[3], row[0], 4);
			}else if(value == status[3]){
				this.getModel().setValueAt(status[4], row[0], 4);
			}else{
				this.getModel().setValueAt(status[0], row[0], 4);
			}
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, Messages.NO_SELECTED_ROW.getMsg());
		}
	}

	/**
	 * Colours the table rows dependent on the 'end' value
	 */
	public void setTableRowColor(){
		this.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){

			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
				final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				String tableValue = (String)table.getModel().getValueAt(row, 3);
				String tableStatus = (String) table.getModel().getValueAt(row, 4);
				//get the date of today
				Date date = new Date();
				SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMdd");
				String today =ft.format(date);
				
				int todayParse = Integer.parseInt(""+today+"");
				int valueParse = Integer.parseInt(tableValue);
				
				if((valueParse-todayParse)==0){
					//the delivery day is today
					c.setBackground(Colour.TABLE_DELIVERY_DAY.getColor());
				}else if((valueParse-todayParse)<0){
					//the task ist deliverd or you failed the dilevery day.
					c.setBackground(Colour.TABLE_DELIVERED.getColor());
				}else if((valueParse-todayParse)==1){
					//you have one day time to finish your task
					c.setBackground(Colour.TABLE_ONE_DAY_LEFT.getColor());
				}else if((valueParse-todayParse)==2){
					c.setBackground(Colour.TABLE_TWO_DAYS_LEFT.getColor());
				}else if((valueParse-todayParse)>2){
					//you have more then 2 day time to finish your task
					c.setBackground(Colour.TABLE_MORE_THAN_TO_DAYS.getColor());
				}else{
					c.setBackground(Colour.TABLE_DEFAULT.getColor());
				}
				
				if(tableStatus.equals(status[4]) && (valueParse-todayParse)==0){
					c.setBackground(Colour.TABLE_DELIVERY_DAY_DELIVERED.getColor());
				}

				//If you select a row and the row gets blue.
				if(isSelected){
					c.setBackground(Colour.TABLE_SELECTED_ROW.getColor());
				}

				return c;
			}
		});
	}

	/**
	 * Resets the coloured table rows.
	 */
	public void setTableRowWhite(){
		this.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){

			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
				final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				c.setBackground(Color.WHITE);
				return c;
			}
		});
	}

	/**
	 *Shift the selected rows of the Tasks table in the archive table.
	 */
	public void shiftTableItemsinOtherTable(RemTable table){
		int[] rows = this.getSelectedRows();
		if(rows.length < 1){
			JOptionPane.showMessageDialog(null, Messages.NO_SELECTED_ROW.getMsg());
			return;
		}
		TableModel tm= this.getModel();
		for(int row: rows){
			this.addRow(table, (String) this.getValueAt(row,0),
					(String) this.getValueAt(row,1),
					(String) this.getValueAt(row,2),
					(String) this.getValueAt(row,3),
					(String) this.getValueAt(row,4));
		}
		while(rows.length>0){
			((DefaultTableModel)tm).removeRow(this.convertRowIndexToModel(rows[0]));
			rows = this.getSelectedRows();
		}

		this.clearSelection();
	}

	/**
	 * Check if any item of the Table has changed.
	 */
	public void checkIfTableHasChanged(final InfoPanel infoPanel){
		this.getModel().addTableModelListener(new TableModelListener(){
			public void tableChanged(TableModelEvent tme){
				infoPanel.setStateChanged();
			}
		});
	}

	public ArrayList<String> getTableContent(){
		ArrayList<String> list = new ArrayList<>();
		for(int i=0; i<this.getRowCount(); i++){
			list.add("["+(String) this.getValueAt(i,0)
					+"]["+(String) this.getValueAt(i,1)
					+"]["+(String) this.getValueAt(i,2)
					+"]["+(String) this.getValueAt(i,3)
					+"]["+(String) this.getValueAt(i,4)+"]\n");
		}
		return list;
	}
	@Override
	public void removeRow(int rowNumber) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeRows() {
		// TODO Auto-generated method stub
	}

	private void eventListeners(){
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				if(SwingUtilities.isRightMouseButton(arg0)){
					TaskTablePopupMenu frame = new TaskTablePopupMenu(arg0.getXOnScreen(), arg0.getYOnScreen());
					//frame.run();
				}
				
			}
		});
	}
}