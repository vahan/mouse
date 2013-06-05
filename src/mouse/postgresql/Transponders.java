package mouse.postgresql;

public class Transponders extends DbTable {

	@Override
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS transponders (" +
				"id serial PRIMARY KEY," +
				"rfid text," +
				"sex text," +
				"first_reading timestamp," +
				"last_readin timestamp," +
				"first_scale_reading timestamp," +
				"last_scale_reading timestamp," +
				"last_antenna text," +
				"last_box text," +
				"stay_count integer," +
				"meeting_count integer," +
				"balaade_count integer" +
			");";
		
		return query;
	}

	@Override
	protected String insertQuery() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
