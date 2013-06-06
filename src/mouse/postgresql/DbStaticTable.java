package mouse.postgresql;

import mouse.dbTableModels.DbTableModel;


public abstract class DbStaticTable extends DbTable {

	/**
	 * Entry values to be stored. For example, antenna, box or transponder names
	 */
	protected String[] entries;
	/**
	 * Additional data to be used. Particularly, used by antennas, as corresponding box names
	 */
	protected String[] data;
	
	protected DbStaticTable(String tableName, String[] tableNames, String[] dataNames) {
		super(tableName);
		// TODO Auto-generated constructor stub

		this.entries = tableNames;
		this.data = dataNames;
		
		generateTables();
	}
	

	/**
	 * Each implementation of this abstract method generates the corresponding entries (possibly used data)
	 */
	protected abstract void generateTables();

	/**
	 * Generates SQL query to insert the generated entries into corresponding tables
	 * @return
	 */
	public abstract String insertQuery(DbTableModel model);


}
