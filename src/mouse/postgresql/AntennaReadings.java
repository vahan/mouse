package mouse.postgresql;

import mouse.TimeStamp;
import mouse.dbTableRows.AntennaRow;
import mouse.dbTableRows.AntennaReadingRow;
import mouse.dbTableRows.BoxRow;
import mouse.dbTableRows.DbTableRow;
import mouse.dbTableRows.TransponderRow;

/**
 * Models the antenna_readings table
 * @author vahan
 *
 */
public class AntennaReadings extends DbDynamicTable {
	
	public AntennaReadings(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
		
	}
	
	@Override
	protected String[] insertFields() {
		String[] fields = new String[] {"timestamp", 
				"log_id", 
				"transponder_id", 
				"box_id", 
				"antenna_id", 
				};
		return fields;
	}

	@Override
	protected String[] insertValues(DbTableRow model) {
		String[] fields = insertFields();
		String[] values = new String[fields.length];
		
		AntennaReadingRow antReading = (AntennaReadingRow) model;
		TimeStamp timeStamp = antReading.getTimeStamp();
		String logId = antReading.getLog().getId();
		TransponderRow transponder = antReading.getTransponder();
		String transponderId = transponder.getId();
		AntennaRow antenna = (AntennaRow) antReading.getSource();
		BoxRow box = antenna.getBox();
		String boxId = box.getId();
		String antennaId = antenna.getId();
		values = new String[] {"'" + timeStamp + "'", 
									logId, 
									transponderId, 
									boxId, 
									antennaId, 
									};
		
		return values;
	}


	@Override
	protected void initColumns() {
		columns.put("id", new DbEntry("id", ColumnTypes.serial, "PRIMARY KEY"));
		columns.put("timestamp", new DbEntry("timestamp", ColumnTypes.timestamp, ""));
		columns.put("log_id", new DbEntry("log_id", ColumnTypes.integer, "references logs(id) ON DELETE CASCADE"));
		columns.put("transponder_id", new DbEntry("transponder_id", ColumnTypes.integer, "references transponders(id)"));
		columns.put("box_id", new DbEntry("box_id", ColumnTypes.integer, "references boxes(id)"));
		columns.put("antenna_id", new DbEntry("antenna_id", ColumnTypes.integer, "references antennas(id)"));
	}


}
