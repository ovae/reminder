package rem.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import rem.constants.Colour;

/**
 * 
 * @author ovae.
 * @version 20150514.
 */
public class DayTable extends TasksTable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DayTable(DefaultTableModel defaultTableModel) {
		super(defaultTableModel);
	}

	/**
	 * Colours the table rows dependent on the 'end' value
	 */
	@Override
	public void setTableRowColor(){
		this.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){

			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
				final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				String tableTopic = (String)table.getModel().getValueAt(row, 0);

				if(tableTopic.startsWith("[E]")){
					c.setBackground(Colour.TABLE_EVENT.getColor());
				}

				return c;
			}
		});
	}
}
