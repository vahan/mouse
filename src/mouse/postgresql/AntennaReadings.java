package mouse.postgresql;

public class AntennaReadings extends DbTable {

	@Override
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS antennaReadings (" +
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
	protected String insertQuery() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
