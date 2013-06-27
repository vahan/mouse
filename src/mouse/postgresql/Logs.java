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
		tableModels = new LogRow[1];
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

	@Override
	protected void initColumns() {
		columns.put("id", new DbEntry("id", ColumnTypes.serial, "PRIMARY KEY"));
		columns.put("first_reading", new DbEntry("first_reading", ColumnTypes.timestamp, ""));
		columns.put("last_reading", new DbEntry("last_reading", ColumnTypes.timestamp, ""));
		columns.put("duration", new DbEntry("duration", ColumnTypes.real, ""));
		columns.put("imported_at", new DbEntry("imported_at", ColumnTypes.timestamp, ""));
		columns.put("nb_readings", new DbEntry("nb_readings", ColumnTypes.integer, ""));
		columns.put("size", new DbEntry("size", ColumnTypes.real, ""));
	}
	
}
