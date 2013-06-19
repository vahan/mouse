package mouse.postgresql;


import mouse.dbTableModels.DbTableModel;

import org.apache.commons.lang3.StringUtils;

/**
 * Abstract base class for modeling the db tables
 * @author vahan
 *
 */
public abstract class DbTable {
	/**
	 * Name of the table
	 */
	protected final String tableName;
	/**
	 * Array of the rows to be stored
	 */
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
	

	/**
	 * Generates SQL query to insert the generated entries into corresponding tables
	 * @return
	 */
	public String insertQuery(DbTableModel[] models) {
		String[] fields = insertFields();
		String[][] values = new String[models.length][fields.length];
		for (int i = 0; i < models.length; ++i) {
			values[i] = insertValues(models[i]);
		}
		String query = insertQuery(fields, values);
		
		return query;
	}
	
	/**
	 * Generates SQL INSERT query with given fields and values
	 * @param fields
	 * @param values
	 * @return
	 */
	protected String insertQuery(String[] fields, String[][] values) {
		String fieldsStr = StringUtils.join(fields, ", ");
		String[] valuesStr = new String[values.length];
		for (int i = 0; i < values.length; ++i) {
			valuesStr[i] = StringUtils.join(values[i], ", ");
		}

		String query = "INSERT INTO " + tableName + " (" + fieldsStr + ") VALUES ("
						+ StringUtils.join(valuesStr, "), (") + ")";
		return query;
	}
	
	/**
	 * Give a specific query to create the exact table
	 * @return	A 'CREATE TABLE' query
	 */
	protected abstract String createTableQuery();
	
	/**
	 * Gives an array of the column name's to be used for inserting
	 * @return	array of the column name's to be used for inserting
	 */
	protected abstract String[] insertFields();
	
	/**
	 * Gives an array of the values to be used for inserting
	 * @param model	The model to use for getting the appropriate values
	 * @return		array of the values to be used for inserting
	 */
	protected abstract String[] insertValues(DbTableModel model);
	


}
