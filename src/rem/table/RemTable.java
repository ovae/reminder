package rem.table;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * The RemTable is a specialised JTable, with 5 columns.
 * @author ovae.
 * @version 20150408.
 */
public abstract class RemTable extends JTable{

	private static final long serialVersionUID = 1L;

	private String[] columnNames;
	private Object[][] tableContent;

	RemTable(DefaultTableModel defaultTableModel){
		this.columnNames = new String[5];
		this.columnNames[0] = "colomn0" ;
		this.columnNames[1] = "colomn1" ;
		this.columnNames[2] = "colomn2" ;
		this.columnNames[3] = "colomn3" ;
		this.columnNames[4] = "colomn4" ;
		this.setModel(new DefaultTableModel(tableContent, columnNames));
	}

	/**
	 * 
	 * @param topic
	 * @param about
	 * @param begin
	 * @param end
	 */
	public abstract void addRow(final String topic, final String about, final String begin, final String end);

	/**
	 * 
	 * @param topic
	 * @param about
	 * @param begin
	 * @param end
	 * @param status
	 */
	public abstract void addRow(final String topic, final String about, final String begin, final String end, final String status);

	/**
	 * 
	 * @param tempTable
	 * @param topic
	 * @param about
	 * @param begin
	 * @param end
	 * @param status
	 */
	public abstract void addRow(final RemTable tempTable, final String topic, final String about, final String begin, final String end,final String status);

	/**
	 * Remove a single Row
	 * @param rowNumber
	 */
	public abstract void removeRow(final int rowNumber);

	/**
	 * 
	 */
	public abstract void removeRows();

	/**
	 * Empty the hole Table
	 */
	public abstract void emptyTable();

	/**
	 * Set a new DefaultTableModel;
	 */
	public void setTableModel(){
		this.setModel(new DefaultTableModel(this.tableContent, this.columnNames));
	}

	/**
	 * Set the TableHeader to new values.
	 * @param columnNames
	 */
	public void setTableHeader(final String[] columnNames){
		this.columnNames = columnNames;
	}

	/**
	 * 
	 * @param valueOf
	 * @param valueOf2
	 * @param valueOf3
	 * @param valueOf4
	 * @param valueOf5
	 */
	public void addRow(String valueOf, String valueOf2, int valueOf3, int valueOf4, String valueOf5){}
}