package mouse.postgresql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import mouse.dataProcessing.DataProcessor;
import mouse.dbTableRows.DbTableRow;

import org.apache.commons.lang3.StringUtils;

/**
 * Abstract base class for modeling the db tables
 * 
 * @author vahan
 * 
 */
public abstract class DbTable {

	/**
	 * Name of the table
	 */
	protected final String tableName;
	/**
	 * schema.table
	 */
	protected final String schemaAndTable;
	/**
	 * Array of the rows to be stored
	 */
	protected DbTableRow[] tableModels;
	/**
	 * Name of the schema where the tables are located
	 */
	public static String SCHEMA = DataProcessor.getInstance().getSettings().getSchema();

	protected final HashMap<String, DbEntry> columns = new HashMap<String, DbEntry>();

	protected DbTable(String tableName) {
		this.tableName = tableName;
		this.schemaAndTable = SCHEMA + "." + tableName;
		initColumns();
	}

	public String getTableName() {
		return schemaAndTable;
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
		String query = "DROP TABLE IF EXISTS " + schemaAndTable + " CASCADE;";
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
		String query = "INSERT INTO " + schemaAndTable + " (" + fieldsStr
				+ ") VALUES (" + StringUtils.join(valuesStr, "), (") + ")";
		return query;
	}

	protected String updateQuery(String[] fields, String[][] values) {
		String query = "UPDATE " + schemaAndTable + " SET ";

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
	 * Give the Postgresql query to create the table
	 * 
	 * @return A 'CREATE TABLE' query
	 */
	protected String createTableQuery() {
		int version = DataProcessor.getInstance().getPsqlManager().getVersion();
		if (version <= 90)
			return createTableQueryVersion90();
		else
			return createTableQueryVersion91();
	}
	
	/**
	 * Give the Postgresql 9.1 or upper query to create the table
	 * 
	 * @return A 'CREATE TABLE IF NOT EXISTS' query
	 */
	protected String createTableQueryVersion91() {
		String query = "CREATE TABLE IF NOT EXISTS " + schemaAndTable + " (";
		query += createTableQueryDefinitions() + "); ";
		return query;
	}

	/**
	 * Postgresql 9.0 and older don't support CREATE TABLE IF NOT EXISTS
	 * This function is a workaround. Source: http://stackoverflow.com/questions/1766046/postgresql-create-table-if-not-exists
	 * 
	 * @return
	 */
	protected String createTableQueryVersion90() {
		String functionName = "create_" + tableName + "_table ()";
		String definitions = createTableQueryDefinitions();
		String query = createTableIfNotExistsFunctionQuery(functionName, definitions);
		query += "SELECT " + functionName + "; ";
		
		return query;
	}
	
	/**
	 * Needed for Postgresql 9.0 and earlier versions
	 * @param definitions
	 * @return
	 */
	protected String createTableIfNotExistsFunctionQuery(String functionName, String definitions) {
		String query = "CREATE OR REPLACE FUNCTION " + functionName + " "
				+ "RETURNS void AS " + "$_$ " + "BEGIN "
				+ "IF EXISTS ( " + "SELECT * FROM pg_catalog.pg_tables "
				+ "WHERE schemaname = '" + SCHEMA + "' AND tablename = '"
				+ tableName + "') THEN RAISE NOTICE 'Table "
				+ schemaAndTable + " already exists.'; "
				+ "ELSE CREATE TABLE " + schemaAndTable + " ("
				+ definitions + "); END IF; END; $_$ LANGUAGE plpgsql; ";
		return query;
	}

	/**
	 * Generates a string of comma-separated definitions for CREATE TABLE query
	 * @return
	 */
	protected String createTableQueryDefinitions() {
		ArrayList<DbEntry> entries = new ArrayList<DbEntry>(columns.values());
		String[] colNames = new String[columns.size()];
		String query = "";
		for (int i = 0; i < colNames.length; ++i) {
			DbEntry col = entries.get(i);
			query += col.getName() + " " + col.getType()
					+ (StringUtils.isEmpty(col.getNote()) ? "" : " " + col.getNote())
					+ (i < colNames.length - 1 ? ", " : "");
			colNames[i] = col.getName();
		}
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
	 * @param model The model to use for getting the appropriate values
	 * @return array of the values to be used for inserting
	 */
	protected abstract String[] insertValues(DbTableRow model);

	protected abstract void initColumns();

}
