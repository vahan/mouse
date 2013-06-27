package mouse.postgresql;

import java.util.HashMap;

import dataProcessing.ExtremeReading;

import mouse.TimeStamp;
import mouse.dbTableRows.AntennaRow;
import mouse.dbTableRows.DbDynamicTableRow;
import mouse.dbTableRows.DbStaticTableRow;
import mouse.dbTableRows.DbTableRow;
import mouse.dbTableRows.TransponderRow;

/**
 * Abstract base class modeling those db tables, that are to be filled after processing the input data
 * @author vahan
 *
 */
public abstract class DbDynamicTable extends DbTable {

	protected DbDynamicTable(String tableName) {
		super(tableName);
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
	private HashMap<DbStaticTableRow, ExtremeReading> extremeReadings(int lastResultIndex, int staticTableRowIndex,
			boolean last) {
		HashMap<DbStaticTableRow, ExtremeReading> extremeReadings = new HashMap<DbStaticTableRow, ExtremeReading>();
		for (DbTableRow model : tableModels) {
			DbDynamicTableRow dynamicTableRow = (DbDynamicTableRow) model;
			TimeStamp timeStamp = dynamicTableRow.timeStamp(lastResultIndex);
			TransponderRow transponder = dynamicTableRow.getTransponder();
			DbStaticTableRow source = dynamicTableRow.getSource();
			DbStaticTableRow dbStaticTableRow = dynamicTableRow.staticTableRow(staticTableRowIndex);
			if (extremeReadings.containsKey(dbStaticTableRow)) {
				TimeStamp lastReading = extremeReadings.get(dbStaticTableRow).getTimeStamp();
				if (last && timeStamp.after(lastReading)
						|| !last && timeStamp.before(lastReading)) {
					extremeReadings.put(dbStaticTableRow, new ExtremeReading(timeStamp, transponder, source));
				}
			} else {
				extremeReadings.put(dbStaticTableRow, new ExtremeReading(timeStamp, transponder, source));
			}
		}
		return extremeReadings;
	}
	
	
	public void putExtremeReadings(int extremeResultIndex, int staticTableRowIndex, boolean last) {
		HashMap<DbStaticTableRow, ExtremeReading> lastReadings = extremeReadings(
				extremeResultIndex, staticTableRowIndex, last);
		for (DbStaticTableRow row : lastReadings.keySet()) {
			row.setLastResult(lastReadings.get(row).getTimeStamp(), extremeResultIndex);
			if (row instanceof TransponderRow) {
				DbStaticTableRow source = lastReadings.get(row).getSource();
				if (!(source instanceof AntennaRow))
					continue;
				lastReadings.get(row).getTransponder().setLastAntennaId(((AntennaRow) source).getId());
			}
		}
	}
	
}
