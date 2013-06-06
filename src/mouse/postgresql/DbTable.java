package mouse.postgresql;


import mouse.dbTableModels.DbTableModel;

import org.apache.commons.lang3.StringUtils;


public abstract class DbTable {
	
	protected final String tableName;
	
	protected DbTableModel[] tableModels;

	protected DbTable(String tableName) {
		this.tableName = tableName;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public DbTableModel[] getTableModels() {
		return tableModels;
	}

	public String dropTableQuery() {
		String query = "DROP TABLE IF EXISTS " + tableName + " CASCADE";
		return query;
	}
	
	protected abstract String createTableQuery();
	

	/**
	 * Generates SQL query to insert the generated entries into corresponding tables
	 * @param fields
	 * @param values
	 * @return
	 */
	protected String insertQuery(String[] fields, String[] values) {
		String fieldsStr = StringUtils.join(fields, ", ");
		String valuesStr = StringUtils.join(values, ", ");
		String query = "INSERT INTO " + tableName + "(" + fieldsStr + ") VALUES(" + valuesStr + ")";
		
		return query;
	}

}
