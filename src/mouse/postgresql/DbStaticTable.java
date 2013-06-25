package mouse.postgresql;

import mouse.TimeStamp;
import mouse.dbTableRows.AntennaRow;
import mouse.dbTableRows.DbStaticTableRow;
import mouse.dbTableRows.DbTableRow;
import mouse.dbTableRows.TransponderRow;

/**
 * Abstract base class to model those tables that are filled before process the input data
 * @author vahan
 *
 */
public abstract class DbStaticTable extends DbTable {

	/**
	 * Entry values to be stored. For example, antenna, box or transponder names
	 */
	protected String[] entries;
	/**
	 * Additional data to be used. Particularly, used by antennas, as corresponding box names
	 */
	protected String[] data;
	
	protected DbStaticTable(String tableName, String[] entries, String[] data) {
		super(tableName);
		// TODO Auto-generated constructor stub

		this.entries = entries;
		this.data = data;
		
		generateTables();
	}
	
	@Override
	public DbStaticTableRow[] getTableModels() {
		DbStaticTableRow[] models = new DbStaticTableRow[tableModels.length];
		for (int i = 0; i < tableModels.length; ++i) {
			models[i] = (DbStaticTableRow) tableModels[i];
		}
		return models;
	}

	/**
	 * Each implementation of this abstract method generates the corresponding entries (possibly used data)
	 */
	protected abstract void generateTables();

	/**
	 * 
	 * @return
	 */
	public String updateLastReadingsQuery(String[] fields, DbStaticTableRow[] lastResult, int lastResultIndex, String[] types) {
		String[][] values = new String[tableModels.length][fields.length];
		for (int i = 0; i < tableModels.length; ++i) {
			for (int j = 0; j < fields.length; ++j) {
				if (types[j] == "timestamp") {
					TimeStamp lastReading = lastResult[i].getLastResults()[lastResultIndex];
					if (lastReading == null) {
						lastReading = new TimeStamp(0); //TODO: This is madness!! It's still a mystery why some lastReadings are null
					}
					values[i][j] = "'" + lastReading + "'::" + types[j];
				} else if (types[j] == "integer") {
					AntennaRow lastAntenna = ((TransponderRow) lastResult[i]).getLastAntenna();
					values[i][j] = lastAntenna.getId();
				}
			}
		}
		
		return updateQuery(fields, values);
	}

	public abstract String[] getColumnNames();

	public abstract DbTableRow createModel(String[] columnValues);

	public abstract void setTableModels(Object[] array);

	
	

}
