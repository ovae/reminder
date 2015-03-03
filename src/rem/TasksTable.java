package rem;

import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * 
 * @author ovae.
 * @version 20150302
 */
public class TasksTable extends RemTable {

	private String[] columnNames;
	private String[] status;
	private Object[][] tableContent;
	private DefaultTableModel defaultTableModel;
	
	TasksTable(DefaultTableModel defaultTableModel) {
		super(defaultTableModel);
		status = new String[5];
		status[0] = "not_started";
		status[1] = "started";
		status[2] = "half-finished";
		status[3] = "finished";
		status[4] = "delivered";
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
		TableModel tm= this.getModel();
		while(rows.length>0){
			((DefaultTableModel)tm).removeRow(this.convertRowIndexToModel(rows[0]));
			rows = this.getSelectedRows();
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
			JOptionPane.showMessageDialog(null, "You have to select a row.");
		}
	}

	/**
	 * Colours the table rows dependent on the 'end' value
	 */
	public void setTableRowColor(){
		this.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
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
					c.setBackground(new Color(240, 88, 88));//red
				}else if((valueParse-todayParse)<0){
					//the task ist deliverd or you failed the dilevery day.
					c.setBackground(Color.LIGHT_GRAY);
				}else if((valueParse-todayParse)==1){
					//you have one day time to finish your task
					c.setBackground(new Color(255,149,88));//orange
				}else if((valueParse-todayParse)==2){
					c.setBackground(new Color(255,210,120));//yellow
				}else if((valueParse-todayParse)>2){
					//you have more then 2 day time to finish your task
					c.setBackground(new Color(126, 207, 88));//green
				}else{
					c.setBackground(Color.WHITE);
				}
				
				if(tableStatus.equals(status[4]) && (valueParse-todayParse)==0){
					c.setBackground(new Color(162, 104, 104));
				}

				//If you select a row and the row gets blue.
				if(isSelected){
					c.setBackground(new Color(160,166,207));//blue
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
		if(rows.length == 0){
			JOptionPane.showMessageDialog(null, "You have to select a row to archive them");
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

		//add the remove function TODO
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

}