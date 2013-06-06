package mouse.postgresql;


public class DirectionResults extends DbTable {
	
	public DirectionResults(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
				"id serial PRIMARY KEY," +
				"direction text," +
				"rfid text," +
				"transponder_id integer references transponders(id)," +
				"box_id integer references boxes(id)," +
				"stay_id integer references stay_results(id)" +
			");";

		return query;
	}
	
}
