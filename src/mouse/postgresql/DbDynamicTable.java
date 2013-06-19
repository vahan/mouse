package mouse.postgresql;

import mouse.dbTableModels.DbTableModel;

/**
 * Abstract base class modeling those db tables, that are to be filled after processing the input data
 * @author vahan
 *
 */
public abstract class DbDynamicTable extends DbTable {

	protected DbDynamicTable(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
	}
	

	/**
	 * The underlying table models' array can be change for these tables
	 * @param tableModels
	 */
	public void setTableModels(DbTableModel[] tableModels) {
		this.tableModels = tableModels;
	}

}
