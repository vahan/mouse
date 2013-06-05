package mouse.postgresql;

public class Scales extends DbTable {

	@Override
	protected String createTableQuery() {
		String query = "CREATE TABLE IF NOT EXISTS scales (" +
				"id serial PRIMARY KEY," +
				"segment text," +
				"x_pos real," +
				"y_pos real," +
				"last_reading timestamp" +
			");";
		
		return query;
	}

	@Override
	protected String insertQuery() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
