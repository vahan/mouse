package mouse.postgresql;


import javax.swing.DefaultButtonModel;

import mouse.dbTableModels.DbTableModel;

import org.apache.commons.lang3.StringUtils;


public abstract class DbTable {
	
	protected final String tableName;
	
	protected DbTableModel[] tableModels;
	
	protected DBConnector connector;

	protected DbTable(String tableName/*, DBConnector connector*/) {
		this.tableName = tableName;
		//this.connector = connector;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public DbTableModel[] getTableModels() {
		return tableModels;
	}
	
	public DBConnector getConnector() {
		return connector;
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

		String query = "INSERT INTO " + tableName + "(" + fieldsStr + ") VALUES ("
						+ StringUtils.join(valuesStr, "), (") + ")";
		return query;
	}
	protected abstract String createTableQuery();
	
	protected abstract String[] insertFields();
	
	protected abstract String[] insertValues(DbTableModel model);
	


}
