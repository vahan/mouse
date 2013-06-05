package mouse.postgresql;

public class MeetingResults extends DbTable {

	@Override
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS meeting_results (" +
				"id serial PRIMARY KEY," +
				"rfid_from text," +
				"rfid_to text," +
				"start timestamp," +
				"stop timestamp," +
				"duration real," +
				"terminated_by integer," +
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
