package mouse.postgresql;

public class StayResults extends DbTable {

	@Override
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS stay_results (" +
				"id serial PRIMARY KEY," +
				"start timestamp," +
				"stop timestamp," +
				"duration real," +
				"rfid text," +
				"transponder_id integer references transponders(id)," +
				"box_id integer references boxes(id)" +
			");";

		return query;
	}

	@Override
	protected String insertQuery() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
