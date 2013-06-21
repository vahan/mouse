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
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
							"id serial PRIMARY KEY," +
							"timestamp timestamp," +
							"log_id integer references logs(id)," +
							"transponder_id integer references transponders(id)," +
							"box_id integer references boxes(id)," +
							"antenna_id integer references antennas(id)," +
							"direction_id integer references direction_results(id)" +
						");";
		
		return query;
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


}
