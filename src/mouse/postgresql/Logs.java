package mouse.postgresql;

import mouse.dbTableRows.DbTableRow;
import mouse.dbTableRows.LogRow;


/**
 * Models the logs table
 * @author vahan
 *
 */
public class Logs extends DbTable {

	public Logs(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
		tableModels = new LogRow[1];
	}

	@Override
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
				"id serial PRIMARY KEY," +
				"first_reading timestamp," +
				"last_reading timestamp," +
				"duration real," +
				"imported_at timestamp," +
				"nb_readings integer," +
				"size real" +
			");";

		return query;
	}

	@Override
	protected String[] insertFields() {
		String[] fields = new String[] {
				"first_reading",
				"last_reading",
				"duration",
				"imported_at",
				"nb_readings",
				"size"
		};
		return fields;
	}

	@Override
	protected String[] insertValues(DbTableRow model) {
		LogRow log = (LogRow) model;
		if (log == null)
			return null;
		
		String[] values = new String[] {
				"'" + log.getFirstReading() + "'",
				"'" + log.getLastReading() + "'",
				Float.toString(log.getDuration()),
				"'" + log.getImportedAt() + "'",
				Integer.toString(log.getNbReadings()),
				Long.toString(log.getSize())
		};
		
		return values;
	}
	
}
