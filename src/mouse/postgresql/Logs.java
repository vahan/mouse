package mouse.postgresql;


public class Logs extends DbTable {

	@Override
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS logs (" +
				"id serial PRIMARY KEY," +
				"first_reading timestamp," +
				"last_reading timestamp," +
				"duration real," +
				"imported_at timestamp," +
				"nb_readings integer," +
				"size real" +
			");";

		return query;
	}

	@Override
	protected String insertQuery() {
		// TODO Auto-generated method stub
		return null;
	}
}
