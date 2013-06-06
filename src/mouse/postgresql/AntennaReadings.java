package mouse.postgresql;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import mouse.dbTableModels.Antenna;
import mouse.dbTableModels.AntennaReading;
import mouse.dbTableModels.Box;
import mouse.dbTableModels.DbTableModel;
import mouse.dbTableModels.Transponder;

public class AntennaReadings extends DbTable {
	
	public AntennaReadings(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
		
	}

	public void setAntennaReadings(AntennaReading[] antennaReadings) {
		this.tableModels = antennaReadings;
	}
	
	public String insertQuery(DbTableModel model) {
		AntennaReading antReading = (AntennaReading) model;
		String[] fields = new String[] {"timestamp", 
										//"log_id", 
										"transponder_id", 
										"box_id", 
										"antenna_id", 
										//"direction_id"
										};
		String timeStampRead = antReading.getTimeStamp();
		Date date;
		try {
			date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSSS", Locale.ENGLISH).parse(timeStampRead);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS", Locale.ENGLISH).format(date);
		Transponder transponder = antReading.getTransponder();
		String transponderId = transponder.getId();
		Antenna antenna = antReading.getAntena();
		Box box = antenna.getBox();
		String boxId = box.getId();
		String antennaId = antenna.getId();
		String[] values = new String[] {"'" + timeStamp + "'", 
										//antReading.getLog().getId(), 
										transponderId, 
										boxId, 
										antennaId, 
										//antReading.getDirectionResult().getId()
										};
		String insertQuery = insertQuery(fields, values);
		return insertQuery;
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


}
