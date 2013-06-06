package mouse.postgresql;


public class Logs extends DbTable {

	public Logs(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
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
	
}
