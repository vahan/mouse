package mouse.postgresql;

import mouse.dbTableModels.DbTableModel;

public abstract class DbDynamicTable extends DbTable {

	protected DbDynamicTable(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
	}
	


	public void setTableModels(DbTableModel[] tableModels) {
		this.tableModels = tableModels;
	}

}
