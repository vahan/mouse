package mouse.postgresql;

import mouse.TimeStamp;
import mouse.dbTableModels.Antenna;
import mouse.dbTableModels.AntennaReading;
import mouse.dbTableModels.Box;
import mouse.dbTableModels.DbTableModel;
import mouse.dbTableModels.Transponder;

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
	protected String[] insertValues(DbTableModel model) {
		String[] fields = insertFields();
		String[] values = new String[fields.length];
		
		AntennaReading antReading = (AntennaReading) model;
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
		*/Transponder transponder = antReading.getTransponder();
		String transponderId = transponder.getId();
		Antenna antenna = antReading.getAntena();
		Box box = antenna.getBox();
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
