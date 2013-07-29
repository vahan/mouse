package mouse.postgresql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import mouse.dbTableRows.DbTableRow;

import org.apache.commons.lang3.StringUtils;

/**
 * Abstract base class for modeling the db tables
 * 
 * @author vahan
 * 
 */
public abstract class DbTable {

	protected static String ON_DUPLICATE_IGNORE_SUFFIX = "_on_duplicate_ignore3";

	/**
	 * Name of the table
	 */
	protected final String tableName;
	/**
	 * Array of the rows to be stored
	 */
	protected DbTableRow[] tableModels;

	protected final HashMap<String, DbEntry> columns = new HashMap<String, DbEntry>();

	protected DbTable(String tableName) {
		this.tableName = tableName;

		initColumns();
	}

	public String getTableName() {
		return tableName;
	}

	public DbTableRow[] getTableModels() {
		return tableModels;
	}

	public DbEntry getColumn(String name) {
		return columns.get(name);
	}

	public Set<String> getColumnNames() {
		return columns.keySet();
	}

	public int columnsCount() {
		return columns.size();
	}

	public String dropTableQuery() {
		// String query = "DROP RULE \"" + tableName +
		// ON_DUPLICATE_IGNORE_SUFFIX + "\" ON \"" + tableName + "\";";
		String query = "DROP TABLE IF EXISTS " + tableName + " CASCADE;";
		return query;
	}

	/**
	 * Generates SQL query to insert the generated entries into corresponding
	 * tables
	 * 
	 * @return
	 */
	public String insertQuery(DbTableRow[] models) {
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
	 * 
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

		String query = "INSERT INTO " + tableName + " (" + fieldsStr
				+ ") VALUES (" + StringUtils.join(valuesStr, "), (") + ")";
		return query;
	}

	protected String updateQuery(String[] fields, String[][] values) {
		String query = "UPDATE " + tableName + " SET ";

		for (int i = 0; i < fields.length; ++i) {
			query += fields[i] + " = CASE id";
			for (int j = 0; j < tableModels.length; ++j) {
				query += " WHEN " + tableModels[j].getId() + " THEN "
						+ values[j][i];
			}
			query += " END";
			query += i == fields.length - 1 ? " " : ", ";
		}
		String[] allIds = new String[tableModels.length];
		for (int j = 0; j < tableModels.length; ++j) {
			allIds[j] = tableModels[j].getId();
		}
		query += "WHERE id IN (" + StringUtils.join(allIds, ", ") + " )";

		return query;
	}

	/**
	 * Give the query to create the table
	 * 
	 * @return A 'CREATE TABLE' query
	 */
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (";
		ArrayList<DbEntry> entries = new ArrayList<DbEntry>(columns.values());
		String[] colNames = new String[columns.size()];
		for (int i = 0; i < colNames.length; ++i) {
			DbEntry col = entries.get(i);
			query += col.getName() + " " + col.getType() + " " + col.getNote()
					+ (i < colNames.length - 1 ? ", " : "");
			colNames[i] = col.getName();
		}
		query += "); ";
		return query;
	}

	/**
	 * Gives an array of the column name's to be used for inserting
	 * 
	 * @return array of the column name's to be used for inserting
	 */
	protected abstract String[] insertFields();

	/**
	 * Gives an array of the values to be used for inserting
	 * 
	 * @param model
	 *            The model to use for getting the appropriate values
	 * @return array of the values to be used for inserting
	 */
	protected abstract String[] insertValues(DbTableRow model);

	protected abstract void initColumns();

}
