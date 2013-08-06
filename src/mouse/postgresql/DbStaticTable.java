package mouse.postgresql;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import mouse.TimeStamp;
import mouse.dbTableRows.DbStaticTableRow;
import mouse.dbTableRows.DbTableRow;
import mouse.dbTableRows.TransponderRow;

/**
 * Abstract base class to model those tables that are filled before process the
 * input data
 * 
 * @author vahan
 * 
 */
public abstract class DbStaticTable extends DbTable {

	/**
	 * For example, antenna, box or transponder names
	 */
	protected String[] names;

	protected DbStaticTable(String tableName, String[] names, boolean generate) {
		super(tableName);

		this.names = names;
	}

	/*
	 * @Override public DbStaticTableRow[] getTableModels() { DbStaticTableRow[]
	 * models = new DbStaticTableRow[tableModels.length]; for (int i = 0; i <
	 * tableModels.length; ++i) { models[i] = (DbStaticTableRow) tableModels[i];
	 * } return models; }
	 */

	/*
	 * @Override protected String createTableQuery() { String query =
	 * super.createTableQuery(); query += "CREATE OR REPLACE RULE \"" +
	 * tableName + ON_DUPLICATE_IGNORE_SUFFIX + "\" AS " + "ON INSERT TO \"" +
	 * tableName + "\" " + "WHERE (EXISTS (SELECT 1 FROM " + tableName + " " +
	 * "WHERE " + tableName + "." + uniqueField() + " = " + "NEW." +
	 * uniqueField() + ")) " + "DO INSTEAD NOTHING; "; return query; }
	 */

	@Override
	protected String insertQuery(String[] fields, String[][] values) {
		String fieldsStr = StringUtils.join(fields, ", ");
		String[] valuesStr = new String[values.length];
		for (int i = 0; i < values.length; ++i) {
			valuesStr[i] = StringUtils.join(values[i], ", ");
		}

		String query = "WITH new_values (" + fieldsStr + ") as (" + "values ("
				+ StringUtils.join(valuesStr, "), (") + ")) " + "INSERT INTO "
				+ schemaAndTable + " (" + fieldsStr + ") SELECT " + fieldsStr
				+ " FROM new_values " + "WHERE NOT EXISTS (SELECT 1 FROM "
				+ schemaAndTable + " t WHERE " + "t." + uniqueField()
				+ " = new_values." + uniqueField() + "); ";
		return query;
	}

	/*
	 * @Override public String insertQuery(String[] fields, String[][] values) {
	 * String fieldsStr = StringUtils.join(fields, ", "); String[] valuesStr =
	 * new String[values.length]; for (int i = 0; i < values.length; ++i) {
	 * valuesStr[i] = StringUtils.join(values[i], ", "); } int uniqueFieldInd =
	 * -1; for (int i = 0; i < fields.length; ++i) { if
	 * (fields[i].equals(uniqueField())) { uniqueFieldInd = i; break; } } String
	 * query = "INSERT INTO " + tableName + " (" + fieldsStr + ") " +
	 * "VALUES (SELECT * FROM VALUES (" + StringUtils.join(valuesStr, "), (") +
	 * ") " + "AS tmp (" + fieldsStr + ") " + "WHERE NOT EXISTS (SELECT * FROM "
	 * + tableName + " WHERE "; for (int i = 0; i < values.length; ++i) { query
	 * += uniqueField() + " = " + values[i][uniqueFieldInd]; query += i <
	 * values.length - 1 ? " OR " : ""; } query += "))"; return query; }
	 */

	/**
	 * Each implementation of this abstract method generates the corresponding
	 * entries (possibly used data)
	 */
	protected abstract void generateTables();

	protected abstract String uniqueField();

	public abstract DbTableRow createModel(HashMap<String, String> columnValues);

	public abstract void setTableModels(Object[] array);

	/**
	 * 
	 * @return
	 */
	public String updateLastReadingsQuery(String[] fields,
			DbTableRow[] lastResult, int lastResultIndex, ColumnTypes[] types) {
		String[][] values = new String[tableModels.length][fields.length];
		for (int i = 0; i < tableModels.length; ++i) {
			for (int j = 0; j < fields.length; ++j) {
				if (types[j] == ColumnTypes.timestamp) {
					TimeStamp lastReading = ((DbStaticTableRow) lastResult[i])
							.getLastResults()[lastResultIndex];
					if (lastReading == null) {
						lastReading = new TimeStamp(0); // TODO: This is
														// madness!! It's still
														// a mystery why some
														// lastReadings are null
					}
					values[i][j] = "'" + lastReading + "'::" + types[j];
				} else if (types[j] == ColumnTypes.integer) {
					String lastAntennaId = ((TransponderRow) lastResult[i])
							.getLastAntennaId();
					values[i][j] = lastAntennaId;
				}
			}
		}

		return updateQuery(fields, values);
	}

}
