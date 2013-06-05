package mouse.postgresql;

public class DirectionResults extends DbTable {

	@Override
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS direction_results (" +
				"id serial PRIMARY KEY," +
				"direction text," +
				"rfid text," +
				"transponder_id integer references transponders(id)," +
				"box_id integer references boxes(id)," +
				"stay_id integer references stay_results(id)" +
			");";

		return query;
	}

	@Override
	protected String insertQuery() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
