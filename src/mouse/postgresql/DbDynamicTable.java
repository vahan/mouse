package mouse.postgresql;

import java.util.HashMap;

import mouse.TimeStamp;
import mouse.dbTableRows.DbDynamicTableRow;
import mouse.dbTableRows.DbStaticTableRow;
import mouse.dbTableRows.DbTableRow;

/**
 * Abstract base class modeling those db tables, that are to be filled after processing the input data
 * @author vahan
 *
 */
public abstract class DbDynamicTable extends DbTable {

	protected DbDynamicTable(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
	}
	

	/**
	 * The underlying table models' array can be change for these tables
	 * @param tableModels
	 */
	public void setTableModels(DbTableRow[] tableModels) {
		this.tableModels = tableModels;
	}
	

	/**
	 * 
	 * 
	 * @param dbStaticTableRow	
	 * @return
	 */
	private HashMap<DbStaticTableRow, TimeStamp> lastReadings(int timeStampIndex) {
		HashMap<DbStaticTableRow, TimeStamp> lastReadings = new HashMap<DbStaticTableRow, TimeStamp>();
		for (DbTableRow model : tableModels) {
			DbDynamicTableRow dynamicTableRow = (DbDynamicTableRow) model;
			TimeStamp timeStamp = dynamicTableRow.timeStamp(timeStampIndex);
			DbStaticTableRow dbStaticTableRow = dynamicTableRow.staticTableRow(timeStampIndex);
			if (lastReadings.containsKey(dbStaticTableRow)) {
				TimeStamp lastReading = lastReadings.get(dbStaticTableRow);
				if (timeStamp.after(lastReading)) {
					lastReadings.put(dbStaticTableRow, timeStamp);
				}
			} else {
				lastReadings.put(dbStaticTableRow, timeStamp);
			}
		}
		return lastReadings;
	}
	
	
	public void putLastReadings(int lastResultIndex) {
		HashMap<DbStaticTableRow, TimeStamp> lastReadings = lastReadings(lastResultIndex);
		for (DbStaticTableRow row : lastReadings.keySet()) {
			row.setLastResult(lastReadings.get(row), lastResultIndex);
		}
	}

}
