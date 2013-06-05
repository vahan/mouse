package mouse.postgresql;

public class ScaleReadings extends DbTable {

	@Override
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS scale_readings (" +
				"id serial PRIMARY KEY," +
				"timestamp timestamp," +
				"weight real," +
				"log_id integer references logs(id)," +
				"transponder_id integer references transponders(id)," +
				"scale_id integer references scales(id)" +
			");";
		
		return query;
	}

	@Override
	protected String insertQuery() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
