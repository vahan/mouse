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
				//"log_id", 
				"transponder_id", 
				"box_id", 
				"antenna_id", 
				//"direction_id"
				};
		return fields;
	}

	@Override
	protected String[] insertValues(DbTableRow model) {
		String[] fields = insertFields();
		String[] values = new String[fields.length];
		
		AntennaReadingRow antReading = (AntennaReadingRow) model;
		TimeStamp timeStamp = antReading.getTimeStamp();/*
		Date date;
		try {
			date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSSS", Locale.ENGLISH).parse(timeStampRead);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS", Locale.ENGLISH).format(date);
		*/TransponderRow transponder = antReading.getTransponder();
		String transponderId = transponder.getId();
		AntennaRow antenna = antReading.getAntenna();
		BoxRow box = antenna.getBox();
		String boxId = box.getId();
		String antennaId = antenna.getId();
		values = new String[] {"'" + timeStamp + "'", 
									//antReading.getLog().getId(), 
									transponderId, 
									boxId, 
									antennaId, 
									//antReading.getDirectionResult().getId()
									};
		
		return values;
	}


}
